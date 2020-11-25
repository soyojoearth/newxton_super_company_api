package com.newxton.nxtframework.process;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newxton.nxtframework.component.NxtUploadImageComponent;
import com.newxton.nxtframework.entity.*;
import com.newxton.nxtframework.exception.NxtException;
import com.newxton.nxtframework.service.*;
import com.newxton.nxtframework.struct.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author soyojo.earth@gmail.com
 * @time 2020/11/24
 * @address Shenzhen, China
 */
@Component
public class NxtProcessOrderFormRefund {

    @Resource
    private NxtProductPictureService nxtProductPictureService;

    @Resource
    private NxtUploadfileService nxtUploadfileService;

    @Resource
    private NxtOrderFormService nxtOrderFormService;

    @Resource
    private NxtOrderFormProductService nxtOrderFormProductService;

    @Resource
    private NxtOrderFormRefundService nxtOrderFormRefundService;

    @Resource
    private NxtOrderFormRefundProductService nxtOrderFormRefundProductService;

    @Resource
    private NxtOrderFormRefundPictureService nxtOrderFormRefundPictureService;

    @Resource
    private NxtOrderFormRefundLogService nxtOrderFormRefundLogService;

    @Resource
    private NxtUploadImageComponent nxtUploadImageComponent;

    /**
     * 创建订单退款申请
     * @param userId
     * @param nxtStructOrderFormRefundCreate
     * @throws NxtException
     */
    @Transactional
    public void create(Long userId, NxtStructOrderFormRefundCreate nxtStructOrderFormRefundCreate) throws NxtException {

        if (nxtStructOrderFormRefundCreate.getId() == null){
            throw new NxtException("缺少订单id");
        }
        if (nxtStructOrderFormRefundCreate.getOrderFormProductList().size() == 0){
            throw new NxtException("缺少选中物品");
        }
        if (nxtStructOrderFormRefundCreate.getReasonType() == null){
            throw new NxtException("请选中退货理由");
        }
        if (nxtStructOrderFormRefundCreate.getReasionDescription() == null){
            throw new NxtException("请填写退货原因");
        }

        NxtOrderForm nxtOrderForm = nxtOrderFormService.queryById(nxtStructOrderFormRefundCreate.getId());

        if (nxtOrderForm == null){
            throw new NxtException("找不到该订单");
        }

        if (!nxtOrderForm.getUserId().equals(userId)){
            throw new NxtException("该订单不属于你");
        }

        if (nxtOrderForm.getStatusPaid() < 1){
            throw new NxtException("该订单没有支付，售后申请无效");
        }



        //先检查退货物品
        List<NxtStructOrderFormRefundProduct> orderFormProductList = nxtStructOrderFormRefundCreate.getOrderFormProductList();
        for (NxtStructOrderFormRefundProduct nxtStructOrderFormRefundProduct :
                orderFormProductList) {
            //检查orderFormProductId归属正确后记录物品
            NxtOrderFormProduct nxtOrderFormProduct = nxtOrderFormProductService.queryById(nxtStructOrderFormRefundProduct.getOrderFormProductId());
            if (!(nxtOrderFormProduct != null && nxtOrderFormProduct.getOrderFormId().equals(nxtOrderForm.getId()))){
                throw new NxtException("有物品不属于该订单");
            }
            //检查申请退货数量，是否超过剩余可退货数量
            Long quantityMax = nxtOrderFormProduct.getQuantity() - nxtOrderFormProduct.getQuantityRefund();
            Long quantityWillRefund = nxtStructOrderFormRefundProduct.getQuantity();
            if (quantityWillRefund > quantityMax){
                throw new NxtException("有物品超过剩余可退货数量");
            }
        }

        //订单标记退货退款
        nxtOrderForm.setStatusRefund(1);
        nxtOrderFormService.update(nxtOrderForm);

        //创建退货记录
        NxtOrderFormRefund nxtOrderFormRefund = new NxtOrderFormRefund();
        nxtOrderFormRefund.setOrderFormId(nxtOrderForm.getId());
        nxtOrderFormRefund.setReasonType(nxtStructOrderFormRefundCreate.getReasonType());
        nxtOrderFormRefund.setReasionDescription(nxtStructOrderFormRefundCreate.getReasionDescription());
        nxtOrderFormRefund.setStatus(0);//状态（-1:拒绝退款 0:已申请 1:完成 2:等用户发货 3:收到货退款 4:收到货有问题，请修改金额）
        nxtOrderFormRefundService.insert(nxtOrderFormRefund);

        //插入退货物品
        for (NxtStructOrderFormRefundProduct nxtStructOrderFormRefundProduct :
                orderFormProductList) {
            //检查orderFormProductId归属正确后记录物品
            NxtOrderFormProduct nxtOrderFormProduct = nxtOrderFormProductService.queryById(nxtStructOrderFormRefundProduct.getOrderFormProductId());
            if (nxtOrderFormProduct != null && nxtOrderFormProduct.getOrderFormId().equals(nxtOrderForm.getId())){
                NxtOrderFormRefundProduct nxtOrderFormRefundProduct = new NxtOrderFormRefundProduct();
                nxtOrderFormRefundProduct.setOrderFormProductId(nxtOrderFormProduct.getId());
                nxtOrderFormRefundProduct.setOrderFormRefundId(nxtOrderFormRefund.getId());
                //检查申请退货数量，是否超过剩余可退货数量
                Long quantityMax = nxtOrderFormProduct.getQuantity() - nxtOrderFormProduct.getQuantityRefund();
                Long quantityWillRefund = nxtStructOrderFormRefundProduct.getQuantity();
                if (quantityWillRefund > quantityMax){
                    quantityWillRefund = quantityMax;
                }
                nxtOrderFormRefundProduct.setQuantity(quantityWillRefund);
                nxtOrderFormRefundProduct.setPriceDeal(nxtOrderFormProduct.getProductPriceDeal());
                //退货总金额：数量x成交单价
                Long amountRefund = quantityWillRefund * nxtOrderFormProduct.getProductPriceDeal();
                nxtOrderFormRefundProduct.setAmountRefund(amountRefund);
                nxtOrderFormRefundProductService.insert(nxtOrderFormRefundProduct);

                //更新已申请退货数量
                nxtOrderFormProduct.setQuantityRefund(quantityWillRefund+nxtOrderFormProduct.getQuantityRefund());
                nxtOrderFormProductService.update(nxtOrderFormProduct);
            }
        }

        //图片
        List<Long> imageIdList = nxtStructOrderFormRefundCreate.getImageIdList();
        for (Long uploadFileid :
                imageIdList) {
            NxtOrderFormRefundPicture nxtOrderFormRefundPicture = new NxtOrderFormRefundPicture();
            nxtOrderFormRefundPicture.setOrderFormRefundId(nxtOrderFormRefund.getId());
            nxtOrderFormRefundPicture.setUploadfileId(uploadFileid);
            nxtOrderFormRefundPictureService.insert(nxtOrderFormRefundPicture);
        }

        //日志
        NxtOrderFormRefundLog nxtOrderFormRefundLog = new NxtOrderFormRefundLog();
        nxtOrderFormRefundLog.setOrderFormRefundId(nxtOrderFormRefund.getId());
        nxtOrderFormRefundLog.setUserId(userId);
        nxtOrderFormRefundLog.setIsAdmin(0);
        nxtOrderFormRefundLog.setDateline(System.currentTimeMillis());
        nxtOrderFormRefundLog.setLogName("订单申请退货");
        nxtOrderFormRefundLog.setLogRemark(nxtOrderFormRefund.getReasionDescription());
        nxtOrderFormRefundLog.setStatusPaid(nxtOrderForm.getStatusPaid());
        nxtOrderFormRefundLog.setStatusDelivery(nxtOrderForm.getStatusDelivery());
        nxtOrderFormRefundLogService.insert(nxtOrderFormRefundLog);

    }

    /**
     * 订单中可退款物品列表
     * @param nxtOrderForm
     */
    public List<NxtStructOrderFormRefundProductAllowItem> productAllowList(NxtOrderForm nxtOrderForm) throws NxtException{

        if (nxtOrderForm == null){
            throw new NxtException("请提供订单");
        }

        Gson gson = new Gson();

        List<NxtStructOrderFormRefundProductAllowItem> nxtStructOrderFormRefundProductAllowItemList = new ArrayList<>();

        Map<Long,Long> orderFormProductIdToProductId = new HashMap();
        //查询订单物品
        List<Long> orderFormIdList = new ArrayList<>();
        orderFormIdList.add(nxtOrderForm.getId());
        List<NxtOrderFormProduct> orderFormProductList = nxtOrderFormProductService.selectAllByOrderFormIdSet(orderFormIdList);
        for (NxtOrderFormProduct nxtOrderFormProduct :
                orderFormProductList) {

            orderFormProductIdToProductId.put(nxtOrderFormProduct.getId(),nxtOrderFormProduct.getProductId());

            NxtStructOrderFormRefundProductAllowItem nxtStructOrderFormRefundProductAllowItem = new NxtStructOrderFormRefundProductAllowItem();
            nxtStructOrderFormRefundProductAllowItem.setOrderFormProductId(nxtOrderFormProduct.getId());
            //检查可退货数量
            Long quantityAllowRefund = nxtOrderFormProduct.getQuantity() - nxtOrderFormProduct.getQuantityRefund();
            nxtStructOrderFormRefundProductAllowItem.setQuantityAllowRefund(quantityAllowRefund);
            nxtStructOrderFormRefundProductAllowItem.setQuantityAll(nxtOrderFormProduct.getQuantity());
            nxtStructOrderFormRefundProductAllowItem.setQuantityIsRefund(nxtOrderFormProduct.getQuantityRefund());
            nxtStructOrderFormRefundProductAllowItem.setAllowRefund(quantityAllowRefund > 0);

            nxtStructOrderFormRefundProductAllowItem.setProductName(nxtOrderFormProduct.getProductName());
            nxtStructOrderFormRefundProductAllowItem.setProductPriceDeal(nxtOrderFormProduct.getProductPriceDeal()/100F);

            try {
                if (nxtOrderFormProduct.getProductSku() != null) {
                    List<NxtStructOrderFormProductSku> skuList = gson.fromJson(nxtOrderFormProduct.getProductSku(), new TypeToken<List<NxtStructOrderFormProductSku>>() {
                    }.getType());
                    nxtStructOrderFormRefundProductAllowItem.setSku(skuList);
                }
            }
            catch (Exception e){
                throw new NxtException("订单物品suk解析错误");
            }

            nxtStructOrderFormRefundProductAllowItemList.add(nxtStructOrderFormRefundProductAllowItem);

        }

        //批量查主图
        List<Long> productIdList = new ArrayList<>();
        productIdList.addAll(orderFormProductIdToProductId.values());
        Map<Long,Long> mapUploadFileIdToProductId = new HashMap<>();
        List<NxtProductPicture> productPictureList =  nxtProductPictureService.selectByProductIdSet(0,Integer.MAX_VALUE,productIdList);
        for (NxtProductPicture item : productPictureList) {
            mapUploadFileIdToProductId.put(item.getUploadfileId(),item.getProductId());
        }
        List<Long> uploadFileId = new ArrayList<>();
        uploadFileId.addAll(mapUploadFileIdToProductId.keySet());
        List<NxtUploadfile> uploadfileList = nxtUploadfileService.selectByIdSet(0,Integer.MAX_VALUE,uploadFileId);

        Map<Long,String> mapProductIdToPicUrl = new HashMap<>();
        for (NxtUploadfile nxtUploadfile : uploadfileList) {
            Long productId = mapUploadFileIdToProductId.get(nxtUploadfile.getId());
            mapProductIdToPicUrl.put(productId,nxtUploadImageComponent.convertImagePathToDomainImagePath(nxtUploadfile.getUrlpath()));
        }

        for (NxtStructOrderFormRefundProductAllowItem nxtStructOrderFormRefundProductAllowItem : nxtStructOrderFormRefundProductAllowItemList){
            Long productId = orderFormProductIdToProductId.get(nxtStructOrderFormRefundProductAllowItem.getOrderFormProductId());
            nxtStructOrderFormRefundProductAllowItem.setPicUrl(mapProductIdToPicUrl.getOrDefault(productId,"/common/images/empty.png"));
        }

        return nxtStructOrderFormRefundProductAllowItemList;

    }

}