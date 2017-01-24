<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="stu" uri="http://samim.tv/tools/functions/utils"%>

<script type="text/javascript">
	function getTemplate(data) {
		return $("#editTemplate").html();
	}
</script>


<script type="text/x-kendo-template" id="editTemplate">

    <ul role="tablist" class="nav nav-tabs"
        style="clear: both; width: 100%; padding: -1px 70px">
        <li class="active">
            <a data-toggle="tab"
               role="tab"
               href="#info">
                اطلاعات کلی
            </a>
        </li>
        <li>
            <a data-toggle="tab"
               role="tab"
               id="tabdetail"
               href="#detail">
                جزئیات
            </a>
        </li>
        <li>
            <a data-toggle="tab"
               role="tab"
               id="tabfile"
               href="#logo">
                لوگو
            </a>
        </li>
    </ul>


    <div class="tab-content">
        <div id="info"
             class="tab-pane active"
             style="max-height: 350px; overflow-y: auto;">
            <br>

            <div class="k-edit-label">
                <label for="name"> نام درس</label>
                <i class="fa fa-asterisk" style="color: red; font-size: 0.5em;"></i>
                </div>
            <div class="k-edit-field"
                 data-container-for="name">
                <input style="direction: rtl;"
                       data-bind="value:course.name"
                       data-val-required="نام الزامی است"
                       type="text"
                       name="name"
                       class="k-input k-textbox"
                       id="name">
            </div>
            <div class="k-edit-label">
                <label for="tagTextArea">توضیح درس</label>
            </div>
            <div class="k-edit-field"
                 data-container-for="chDescrip">
			<textarea id="tagTextArea"
                      rows="3"
                      style="height:50px;   width:83%;max-width:83%;min-width:83%;"
                      name="defaultDescription"
                      class="k-input k-textbox"
                      data-bind="value:course.description"/>
            </div>

            <div class="k-edit-label">
                <label for="teacher"> استاد</label>
                <i class="fa fa-asterisk" style="color: red; font-size: 0.5em;"></i>
            </div>
            <div class="k-edit-field"
                 data-container-for="teacher">
                <select id="teacher"
                        data-bind="value:applicant"
                        name="teacher"
                        required
                        data-bind="value:applicant"
                        style="width: 200px"/>
            </div>


            <div class="k-edit-label">
                <label for="validate">معتبر</label>
                <i class="fa fa-asterisk" style="color: red; font-size: 0.5em;"></i>
            </div>
            <div class="k-edit-field"
                 data-container-for="validate">
                <input style="direction: rtl;"
                       data-bind="value:course.validate"
                       type="checkbox"
                       class="k-checkbox"
                       name="validate"
                       id="validate">
            </div>


        </div>



        <div id="detail"
             class="tab-pane "
             style="max-height: 350px; overflow-y: auto;">
            <br>


            <div class="k-edit-label">
                <label for="date">تاریخ</label>
                <i class="fa fa-asterisk" style="color: red; font-size: 0.5em;"></i>
            </div>
            <div class="k-edit-field"
                 data-container-for="date">
                <input id="date" value="10/10/2000" style="float:none" />
            </div>


            <div class="k-edit-label">
                <label for="field"> رشته</label>
                <i class="fa fa-asterisk" style="color: red; font-size: 0.5em;"></i>
            </div>
            <div class="k-edit-field"
                 data-container-for="field">
                <select id="field"
                        data-bind="value:course.field"
                        name="field"
                        required
                        data-bind="value:field"
                        style="width: 200px"/>
            </div>
            <div class="k-edit-label">
                <label for="grade"> مقطع</label>
                <i class="fa fa-asterisk" style="color: red; font-size: 0.5em;"></i>
            </div>
            <div class="k-edit-field"
                 data-container-for="grade">
                <select id="grade"
                        data-bind="value:course.grade"
                        name="grade"
                        required
                        data-bind="value:grade"
                        style="width: 200px"/>
            </div>

            <div class="k-edit-label">
                <label for="weekcount">تعداد هفته</label>
                <i class="fa fa-asterisk" style="color: red; font-size: 0.5em;"></i>
            </div>
            <div class="k-edit-field"
                 data-container-for="weekcount">
                <input style="direction: rtl;"
                       style="height:50px;   width:83%;max-width:83%;min-width:83%;"
                       data-bind="value:course.weekCount"
                       type="number"
                       value="0"
                       name="weekcount"
                       id="weekcount">
            </div>
        </div>
        <div id="logo"
             class="tab-pane "
             style="max-height: 350px; overflow-y: auto;">
            <br>

            <div class="k-edit-label">
                <label for="example"
                       style="padding-top: 20px; padding-bottom: 20px;">logo
                </label>
            </div>
            <div class="k-edit-field"
                 data-container-for="uploadLogo"
                 style="padding-top: 15px; padding-bottom: 15px;">
                <div id="example">
                    <div class="k-content">
                        <input type="file" name="files" id="files" />
                    </div>
            </div>
        </div>


    </div>
</div>
</script>