(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-584b7a58"],{8256:function(e,t,n){"use strict";var i=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"tinymce-container",class:{fullscreen:e.fullscreen},style:{width:e.containerWidth}},[n("textarea",{staticClass:"tinymce-textarea",attrs:{id:e.tinymceId}}),e._v(" "),n("div",{staticClass:"editor-custom-btn-container"})])},a=[],o=(n("ac6a"),n("c5f6"),["advlist anchor autolink autosave code codesample colorpicker colorpicker contextmenu directionality emoticons fullscreen hr image imagetools insertdatetime link lists media nonbreaking noneditable pagebreak paste preview print save searchreplace spellchecker tabfocus table template textcolor textpattern visualblocks visualchars wordcount"]),r=o,l=["searchreplace bold italic underline strikethrough alignleft aligncenter alignright outdent indent  blockquote undo redo removeformat subscript superscript code codesample","hr bullist numlist link image charmap preview anchor pagebreak insertdatetime media table emoticons forecolor backcolor fullscreen"],c=l,s=n("2d63"),d=[];function u(){return window.tinymce}var m=function(e,t){var n=document.getElementById(e),i=t||function(){};if(!n){var a=document.createElement("script");a.src=e,a.id=e,document.body.appendChild(a),d.push(i);var o="onload"in a?r:l;o(a)}function r(t){t.onload=function(){this.onerror=this.onload=null;var e,n=Object(s["a"])(d);try{for(n.s();!(e=n.n()).done;){var i=e.value;i(null,t)}}catch(a){n.e(a)}finally{n.f()}d=null},t.onerror=function(){this.onerror=this.onload=null,i(new Error("Failed to load "+e),t)}}function l(e){e.onreadystatechange=function(){if("complete"===this.readyState||"loaded"===this.readyState){this.onreadystatechange=null;var t,n=Object(s["a"])(d);try{for(n.s();!(t=n.n()).done;){var i=t.value;i(null,e)}}catch(a){n.e(a)}finally{n.f()}d=null}}}n&&i&&(u()?i(null,n):d.push(i))},f=m,h="https://cdn.jsdelivr.net/npm/tinymce-all-in-one@4.9.3/tinymce.min.js",g={name:"Tinymce",props:{id:{type:String,default:function(){return"vue-tinymce-"+ +new Date+(1e3*Math.random()).toFixed(0)}},value:{type:String,default:""},toolbar:{type:Array,required:!1,default:function(){return[]}},menubar:{type:String,default:"file edit insert view format table"},height:{type:[Number,String],required:!1,default:360},width:{type:[Number,String],required:!1,default:"auto"}},data:function(){return{hasChange:!1,hasInit:!1,tinymceId:this.id,fullscreen:!1,languageTypeList:{en:"en",zh:"zh_CN",es:"es_MX",ja:"ja"}}},computed:{containerWidth:function(){var e=this.width;return/^[\d]+(\.[\d]+)?$/.test(e)?"".concat(e,"px"):e}},watch:{value:function(e){var t=this;!this.hasChange&&this.hasInit&&this.$nextTick((function(){return window.tinymce.get(t.tinymceId).setContent(e||"")}))}},mounted:function(){this.init()},activated:function(){window.tinymce&&this.initTinymce()},deactivated:function(){this.destroyTinymce()},destroyed:function(){this.destroyTinymce()},methods:{init:function(){var e=this;f(h,(function(t){t?e.$message.error(t.message):e.initTinymce()}))},initTinymce:function(){var e=this,t=this;window.tinymce.init({selector:"#".concat(this.tinymceId),language:this.languageTypeList["en"],height:this.height,body_class:"panel-body ",object_resizing:!1,toolbar:this.toolbar.length>0?this.toolbar:c,menubar:this.menubar,plugins:r,end_container_on_empty_block:!0,powerpaste_word_import:"clean",code_dialog_height:450,code_dialog_width:1e3,advlist_bullet_styles:"square",advlist_number_styles:"default",imagetools_cors_hosts:["www.tinymce.com","codepen.io"],default_link_target:"_blank",link_title:!1,nonbreaking_force_tab:!0,init_instance_callback:function(n){t.value&&n.setContent(t.value),t.hasInit=!0,n.on("NodeChange Change KeyUp SetContent",(function(){e.hasChange=!0,e.$emit("input",n.getContent())}))},setup:function(e){e.on("FullscreenStateChanged",(function(e){t.fullscreen=e.state}))},convert_urls:!1})},destroyTinymce:function(){var e=window.tinymce.get(this.tinymceId);this.fullscreen&&e.execCommand("mceFullScreen"),e&&e.destroy()},setContent:function(e){window.tinymce.get(this.tinymceId).setContent(e)},getContent:function(){window.tinymce.get(this.tinymceId).getContent()},imageSuccessCBK:function(e){var t=this;e.forEach((function(e){window.tinymce.get(t.tinymceId).insertContent('<img class="wscnph" src="'.concat(e.url,'" >'))}))}}},p=g,b=(n("b19f"),n("2877")),y=Object(b["a"])(p,i,a,!1,null,"3a4cd6c3",null);t["a"]=y.exports},"8d3e":function(e,t,n){"use strict";var i=n("b78e"),a=n.n(i);a.a},b19f:function(e,t,n){"use strict";var i=n("f218"),a=n.n(i);a.a},b78e:function(e,t,n){},f218:function(e,t,n){},fe60:function(e,t,n){"use strict";n.r(t);var i=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"app-container"},[n("el-card",{staticStyle:{"margin-top":"10px"}},[n("el-form",{attrs:{"label-position":"top","label-width":"80px",model:e.formLabelAlign}},[n("el-form-item",{attrs:{label:"标题"}},[n("el-input",{model:{value:e.formLabelAlign.title,callback:function(t){e.$set(e.formLabelAlign,"title",t)},expression:"formLabelAlign.title"}})],1),e._v(" "),n("el-form-item",[n("div",{staticStyle:{"margin-top":"20px"}},[n("el-select",{attrs:{placeholder:"请选择"},model:{value:e.formLabelAlign.category_id,callback:function(t){e.$set(e.formLabelAlign,"category_id",t)},expression:"formLabelAlign.category_id"}},e._l(e.category_list,(function(e){return n("el-option",{key:e.category_id,attrs:{label:e.category_name_display,value:e.category_id}})})),1),e._v(" "),n("router-link",{attrs:{to:"/content/kind"}},[n("el-button",[e._v("类别管理")])],1)],1)]),e._v(" "),n("el-form-item",{staticStyle:{"margin-bottom":"30px"}},[n("Tinymce",{ref:"editor",attrs:{height:400},model:{value:e.formLabelAlign.detail,callback:function(t){e.$set(e.formLabelAlign,"detail",t)},expression:"formLabelAlign.detail"}})],1),e._v(" "),n("el-form-item",[n("span",[e._v("上传图片")]),e._v(" "),n("el-upload",{staticClass:"avatar-uploader",attrs:{action:"/api/admin/upload/public_pic","list-type":"picture-card",headers:e.headers,"on-success":e.UploadOnSuccess,"before-upload":e.beforeAvatarUpload}},[n("i",{staticClass:"el-icon-plus"})]),e._v(" "),n("el-dialog",{attrs:{visible:e.dialogVisible},on:{"update:visible":function(t){e.dialogVisible=t}}},[n("img",{attrs:{width:"100%",src:e.imageUrl,alt:""}})])],1),e._v(" "),n("el-form-item",[n("el-checkbox",{model:{value:e.formLabelAlign.is_recommend,callback:function(t){e.$set(e.formLabelAlign,"is_recommend",t)},expression:"formLabelAlign.is_recommend"}},[e._v("推荐")])],1),e._v(" "),n("el-form-item",[n("el-button",{attrs:{type:"primary"},on:{click:e.handleCreate}},[e._v("发布")])],1)],1)],1)],1)},a=[],o=n("db72"),r=n("8256"),l=n("2f62"),c=n("5f87"),s={components:{Tinymce:r["a"]},data:function(){return{radio2:"1",checked:!1,formLabelAlign:{title:"",detail:"",category_id:"",is_recommend:!1},headers:{token:Object(c["a"])(),user_id:Object(c["b"])()},imageUrl:"",dialogVisible:!1}},computed:Object(o["a"])({},Object(l["c"])({category_list:function(e){return e.new.CategoryListData}})),created:function(){this.$store.dispatch("new/getCategory")},methods:{handleAvatarSuccess:function(e,t){this.imageUrl=t.url,console.log(this.imageUrl)},beforeAvatarUpload:function(e){var t="image/jpeg"===e.type,n=e.size/1024/1024<2;return t||this.$message.error("上传图片只能是 JPG 格式!"),n||this.$message.error("上传图片大小不能超过 2MB!"),t&&n},UploadOnSuccess:function(e,t,n){this.$refs.editor.imageSuccessCBK([{url:e.url}]),this.$forceUpdate()},handleCreate:function(){var e=this,t=this;this.formLabelAlign.is_recommend=this.formLabelAlign.is_recommend?1:0,this.$store.dispatch("new/createNew",this.formLabelAlign).then((function(n){e.$message({message:"创建成功！",type:"success"}),t.$router.back()}))}}},d=s,u=(n("8d3e"),n("2877")),m=Object(u["a"])(d,i,a,!1,null,null,null);t["default"]=m.exports}}]);