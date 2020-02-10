const pathName = document.location.pathname;
const index = pathName.substr(1).indexOf("/");
const result = pathName.substr(0,index+1);
const uploadPath = result + "/demo/tinyFileUpload";


tinymce.init({
    selector: '#tinyCustomer',
    language:'zh_CN',
    plugins: 'preview fullpage paste importcss searchreplace autolink directionality visualblocks visualchars image link table charmap hr pagebreak nonbreaking anchor toc insertdatetime advlist lists textpattern noneditable charmap quickbars emoticons',
    imagetools_cors_hosts: ['picsum.photos'],
    menubar: 'file edit view insert format table help',
    removed_menuitems:'media,template,codesample,formats,fontformats',
    toolbar: 'preview | undo redo | bold italic underline strikethrough | fontsizeselect formatselect | alignleft aligncenter alignright alignjustify | outdent indent |  numlist bullist | image | forecolor backcolor removeformat | pagebreak | charmap emoticons',
    toolbar_sticky: true,
    images_upload_url: uploadPath,
    template_cdate_format: '[Date Created (CDATE): %m/%d/%Y : %H:%M:%S]',
    template_mdate_format: '[Date Modified (MDATE): %m/%d/%Y : %H:%M:%S]',
    height: 600,
    image_caption: true,
    quickbars_selection_toolbar: 'bold italic | quicklink h2 h3 blockquote quickimage quicktable',
    noneditable_noneditable_class: "mceNonEditable",
    toolbar_drawer: 'sliding',
    contextmenu: "link image table",
    // images_upload_handler: function (blobInfo, succFun, failFun) {
    //     alert(blobInfo.base64());
    //     succFun("data:image/png;base64,"+blobInfo.base64());
    // }
});