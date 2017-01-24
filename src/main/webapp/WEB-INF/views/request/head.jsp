<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<title>management</title>
<script id="fileTemplate" type="text/x-kendo-template">
    <span class='k-progress'></span>
    <div class='file-wrapper'>
        <span class='file-icon #=addExtensionClass(files[0].extension)#'></span>
        <h4 class='file-heading file-name-heading'>نام :#=name#</h4>
        <h4 class='file-heading file-size-heading'>انداره :#=size# bytes</h4>
        <button type='button' class='k-upload-action'></button>
    </div>
</script>
<script type="text/javascript">
    function addExtensionClass(extension) {
        switch (extension) {
            case '.jpg':
            case '.img':
            case '.png':
            case '.gif':
                return "img-file";
            case '.doc':
            case '.docx':
                return "doc-file";
            case '.xls':
            case '.xlsx':
                return "xls-file";
            case '.pdf':
                return "pdf-file";
            case '.zip':
            case '.rar':
                return "zip-file";
            default:
                return "default-file";
        }
    }
    function initializeOnEdit(e) {
        $("#files").kendoUpload({
            multiple: false,
            async: {
                "saveUrl": "http://172.17.242.98:8081/online-school/rest/upload?type=logo",
                "removeUrl": "http://localhost:8081/online-school/rest/removeFile",
                "autoUpload": false
            },
            /*"localization": {
                "cancel": "انصراف",
                "remove": "پاک کردن",
                "retry": "تلاش مجدد",
                "select": "انتخاب عکس",
                "statusFailed": "خطلا",
                "statusUploaded": "آپلود با موفقیت انجام شد",
                "statusUploading": "در حال آپلود",
                "uploadSelectedFiles": "آپلود فایل",
                "headerStatusUploading": "آپلود",
                "headerStatusUploaded": "انجام شد"
            },*/
            "success": onSuccessUpload,

            template: kendo.template($('#fileTemplate').html())
        });
        function onSuccessUpload(e1) {
            e.model.set("course.imagePath", e1.response.logoPath);
        }

    }
    function onSuccessUpload(e) {
        currentModel.set("logo", e.response.logoPath);
    }
    function onEdit(e) {

        $('#validate').change(function() {
            if($(this).is(":checked")) {
                e.model.set("course.validate",true);
            }
            else{
                e.model.set("course.validate",false);
            }
        });



        $("#weekcount").kendoNumericTextBox({
            value:0
        });
        var datePicker=$("#date").kendoDatePicker({
            change: function(e1){
                e.model.set("course.createDate",datePicker.value().getTime());
            }
        }).data("kendoDatePicker");
        if (e.model.isNew()){
            datePicker.value(new Date());
            var x= {};
            e.model.set("course",x);
            e.model.set("id",0);
            e.model.set("course.id",0);
            e.model.set("course.imagePath","");
            e.model.set("course.name","");
            e.model.set("course.validate",true);
            e.model.set("course.description","");
            e.model.set("course.weekCount",0);
            e.model.set("course.balance",54);
            $('#validate').prop('checked', true);
        }
        else{
            datePicker.value(new Date(e.model.course.createDate));
            if (e.model.course.validate)
                $('#validate').prop('checked', true);
        }
        $("#teacher").kendoDropDownList({
            dataTextField: "name",
            dataValueField: "id",
            valueTemplate: '<span class="selected-value" >#:name# #:family#</span><span>',
            template: '<span class="selected-value" >#:name# #:family#</span><span>',
            select: function (e1) {
                var dataItem = this.dataItem(e1.item);
                var membership={
                    id:dataItem.id,
                    username:dataItem.username,
                    password:dataItem.password,
                    name:dataItem.name,
                    family:dataItem.family,
                    role:dataItem.role
                };
                e.model.set("applicant",membership);
            },dataBound: function (e1) {
                var dataItem = this.dataItem(e1.item);
                var membership={
                    id:dataItem.id,
                    username:dataItem.username,
                    password:dataItem.password,
                    name:dataItem.name,
                    family:dataItem.family,
                    role:dataItem.role
                };
                e.model.set("applicant",membership);

            },
            dataSource:{
                transport: {
                    read: {
                        url: "<s:url value="../membership/teachers" />",
                        contentType: "application/json; charset=utf-8",
                        type: "GET"
                    }
                }
            }
        });
        $("#field").kendoDropDownList({
            dataTextField: "value",
            dataValueField: "id",
            select: function(e1){
                var dataItem = this.dataItem(e1.item);
                e.model.set("course.field",dataItem.id);
            },dataBound: function (e1) {
                var dataItem = this.dataItem(e1.item);
                e.model.set("course.field",dataItem.id);
            },
            dataSource:{
                transport: {
                    read: {
                        url: "<s:url value="../course/fields" />",
                        contentType: "application/json; charset=utf-8",
                        type: "GET"
                    }
                }
            }
        });
        $("#grade").kendoDropDownList({
            dataTextField: "value",
            dataValueField: "id",
            select: function(e1){
                var dataItem = this.dataItem(e1.item);
                e.model.set("course.grade",dataItem.id);
            },
            dataBound: function (e1) {
                var dataItem = this.dataItem(e1.item);
                e.model.set("course.grade",dataItem.id);
            },
            dataSource:{
                transport: {
                    read: {
                        url: "<s:url value="../course/grades" />",
                        contentType: "application/json; charset=utf-8",
                        type: "GET"
                    }
                }
            }
        });

    }
</script>

<style>
    .demo-section {
        min-height: 280px;
    }


    #fieldlist {
        margin: 0;
        padding: 0;
    }

    #fieldlist li {
        list-style: none;
        padding-bottom: .7em;
        text-align: right;
    }

    #fieldlist label {
        display: block;
        padding-bottom: .3em;
        font-weight: bold;
        text-transform: uppercase;
        font-size: 12px;
        color: #444;
    }

    #fieldlist li.status {
        text-align: center;
    }

    #fieldlist li .k-widget:not(.k-tooltip),
    #fieldlist li .k-textbox {
        margin: 0 5px 5px 0;
    }

    .confirm {
        padding-top: 1em;
    }

    .valid {
        color: green;
    }

    .invalid {
        color: red;
    }

    #fieldlist li input[type="checkbox"] {
        margin: 0 5px 0 0;
    }

    span.k-widget.k-tooltip-validation {
        display; inline-block;
        width: 160px;
        text-align: right;
        border: 0;
        padding: 0;
        margin: 0;
        background: none;
        box-shadow: none;
        color: red;
    }

    .k-tooltip-validation .k-warning {
        display: none;
    }
</style>

<style>
    .file-icon {
        display: inline-block;
        float: right;
        width: 48px;
        height: 48px;
        margin-right: 10px;
        margin-top: 13.5px;
    }


    #example .file-heading {
        font-family: Arial;
        font-size: 1.1em;
        display: inline-block;
        float: right;
        width: 60%;
        margin: 0 0 0 20px;
        height: 25px;
        -ms-text-overflow: ellipsis;
        -o-text-overflow: ellipsis;
        text-overflow: ellipsis;
        overflow:hidden;
        white-space:nowrap;
    }

    #example .file-name-heading {
        font-weight: bold;
        margin-top: 20px;
    }

    #example .file-size-heading {
        font-weight: normal;
        font-style: italic;
    }

    li.k-file .file-wrapper .k-upload-action {
        position: absolute;
        top: 0;
        right: 0;
    }

    li.k-file div.file-wrapper {
        position: relative;
        height: 75px;
    }
</style>