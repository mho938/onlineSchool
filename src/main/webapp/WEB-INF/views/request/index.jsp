<%@page import="java.util.HashMap"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="kendo" uri="http://www.kendoui.com/jsp/tags"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="stu" uri="http://samim.tv/tools/functions/utils" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<s:url value="/request/read" var="readUrl" />
<c:url value="/request/read" var="w" />
<c:url value="/request/create" var="updateUrl" />

<c:import url="/WEB-INF/views/request/request.edit.jsp" charEncoding="UTF-8" />
<kendo:grid
        name="grid"
        pageable="true"
        style="height:500px;"
        resizable="true"
        reorderable="true"
        navigatable="true"
        editable="popup"
        edit="function(e) {onEdit(e); initializeOnEdit(e); _openModal(e); }"
        >
    <kendo:grid-editable
            confirmation="false"
            confirmDelete="Delete"
            template="#= getTemplate(data) #"
            cancelDelete="Cancel"
            mode="popup"
            window=""
            createAt="top"
            destroy="true">
    </kendo:grid-editable>
    <kendo:grid-toolbar>
        <kendo:grid-toolbarItem name="create" text="create" />
    </kendo:grid-toolbar>
    <kendo:grid-pageable refresh="true"
                         pageSizes="true"
                         buttonCount="5"/>
    <kendo:grid-sortable allowUnsort="true"
                         mode="multiple"/>
    <kendo:grid-columns>
        <kendo:grid-column title="name"
                           field="course.name" >
            <kendo:grid-column-template>
                <script>
                    function def_course_name (model){

                        if (model.course!= null || model.course!=undefined)
                        {
                            if (model.course.name!=undefined || model.course.name!=null)
                                return "<span>"+model.course.name+"</span>";
                            else
                                return "<span></span>";
                        }
                        else
                            return "<span></span>";
                    }
                </script>
            </kendo:grid-column-template>
        </kendo:grid-column>
        <kendo:grid-column title="teacher"
                           field="applicant" >
            <kendo:grid-column-template>
                <script>
                    function def_teacher_name (model){

                        if (model.applicant!= null || model.applicant!=undefined)
                        {
                            if (model.applicant.name!=undefined || model.applicant.name!=null)
                                return "<span>"+model.applicant.name+"  "+model.applicant.family+"</span>";
                            else
                                return "<span></span>";

                        }
                        else
                            return "<span></span>";
                    }
                </script>
            </kendo:grid-column-template>
        </kendo:grid-column>
        <kendo:grid-column title="validate" field="course.validate">
            <kendo:grid-column-template>
                <script>
                    function def_course_validate (model){
                        if (model.course!= null || model.course!=undefined)
                        {
                            if (model.course.validate) {
                                return "<div style='text-align:center;'><i class='fa fa-check fa-lg' style='color:green'></i></div>";
                            }
                            else
                                return "<div style='text-align:center;'><i class='fa fa-times fa-lg' style='color:red'></i></div>";
                        }
                        else
                            return "<div style='text-align:center;'><i class='fa fa-times fa-lg' style='color:red'></i></div>";
                    }
                </script>
            </kendo:grid-column-template>
        </kendo:grid-column>

        <kendo:grid-column title="&nbsp;">
            <kendo:grid-column-command>
                <kendo:grid-column-commandItem name="edit"
                                               className="k-icon-edit k-mini"
                                               text=" " />
                <kendo:grid-column-commandItem name="Delete" text=" "
                                               className="k-icon-delete k-mini"
                                               click="function(e) { _handleDelete('grid', e); }" />
            </kendo:grid-column-command>
        </kendo:grid-column>
    </kendo:grid-columns>
    <kendo:dataSource pageSize="20"
                      error="function(e) { error_handler('grid', e); }"
                      data="data">
        <kendo:dataSource-transport>
            <kendo:dataSource-transport-read url="read" type="POST"
                                             contentType="application/json" />
            <kendo:dataSource-transport-update url="create" dataType="json"
                                               type="POST" contentType="application/json" />
            <kendo:dataSource-transport-create url="create" dataType="json"
                                               type="POST" contentType="application/json" />
            <kendo:dataSource-transport-destroy url="destroy" dataType="json"
                                               type="POST" contentType="application/json" />
            <kendo:dataSource-transport-parameterMap>
                <script type="text/javascript">
                    function parameterMap(options, type) {
                        return JSON.stringify(options);
                    }
                </script>
            </kendo:dataSource-transport-parameterMap>
        </kendo:dataSource-transport>
        <kendo:dataSource-schema data="data" total="total" errors="errors">
            <kendo:dataSource-schema-model id="id">
                <kendo:dataSource-schema-model-fields>
                    <kendo:dataSource-schema-model-field name="course" type="data" />
                    <kendo:dataSource-schema-model-field name="applicant" type="data"  />
                </kendo:dataSource-schema-model-fields>
            </kendo:dataSource-schema-model>
        </kendo:dataSource-schema>
    </kendo:dataSource>


</kendo:grid>

<script type="text/javascript">
    $(document).ready(function () {
        $(window).resize(function () {
            calculateGridHeight("grid");
        });
        calculateGridHeight("grid");
    });
</script>
<style scoped="scoped">
    .k-grid-DeleteEvents span {
        display: inline-block;
        font-family: FontAwesome;
        font-style: normal;
        font-weight: 400;
        height: 16px;
        line-height: 0.7;
        overflow: hidden;
        text-align: center;
        width: 16px;
        vertical-align: middle;
        opacity: 0.8;
    }

    .k-grid-DeleteEvents span:before {
        content: "\f00d";
    }
</style>