/*MessageBox*/
(function(kendo, $) {
    var ExtDialog = kendo.ui.Window
        .extend({
            _buttonTemplate : kendo
                .template('<div class="k-ext-dialog-buttons" style="text-align:center;clear:both;padding-top:10px;"><div style="display:inline-block"># $.each (buttons, function (idx, button) { # <button class="k-button" style="margin-right:5px; width:100px;"><span class="k-icon #= button.cls #"></span> #= button.title #</button> # }) # </div></div>'),
            _contentTemplate : kendo
                .template('<div class="k-ext-dialog-content k-rtl" style="overflow:auto;">'),

            init : function(element, options) {

                var that = this;

                options.visible = options.visible || false;

                kendo.ui.Window.fn.init.call(that, element, options);
                $(element).data("kendoWindow", that);

                var html = $(element).html();
                $(element).html(that._contentTemplate(options));
                $(element).find("div.k-ext-dialog-content").append(html);

                $(element).find("div.k-ext-dialog-content").append(
                    that._buttonTemplate(options));
                // $(element).after(that._buttonTemplate(options));

                $.each(
                    options.buttons,
                    function(idx, button) {
                        if (button.click) {
                            $(
                                $(element)
                                    .parent()
                                    .find(".k-ext-dialog-buttons .k-button")[idx])
                                .on("click", {
                                    handler : button.click
                                }, function(e) {
                                    e.data.handler({
                                        button : this,
                                        dialog : that
                                    });
                                });
                        }
                    });

                that.bind("resize", function(e) {
                    that.resizeDialog();
                });
            },

            resizeDialog : function() {
                var that = this;
                var $dialog = $(that.element);
                var width = $dialog.width();
                var height = $dialog.height();
                $dialog.parent().find(".k-ext-dialog-buttons").width(width);
            },

            options : {
                name : "ExtDialog"
            }
        });
    kendo.ui.plugin(ExtDialog);

    kendo.ui.ExtMessageBox = {
        show : function(options) {
            return new $.Deferred(
                function(deferred) {
                    if ($("#extMessageBox").length > 0) {
                        $("#extMessageBox").parent().remove();
                    }

                    if (options.icon == "Information")
                        options.iconClass = "fa fa-info-circle k-ext-information";
                    else if (options.icon == "Question")
                        options.iconClass = "fa fa-question-circle k-ext-question";
                    else if (options.icon == "Error")
                        options.iconClass = "fa fa-times-circle k-ext-error";
                    else if (options.icon == "Warning")
                        options.iconClass = "fa fa-warning k-ext-warning";

                    if (("buttons" in options) == false) {
                        if (options.type == "OK")
                            options.buttons = [ {
                                name : "OK"
                            } ];
                        else if (options.type == "OKCancel")
                            options.buttons = [ {
                                name : "Cancel"
                            }, {
                                name : "OK"
                            } ];
                        else if (options.type == "YesNo")
                            options.buttons = [ {
                                name : "Yes"
                            }, {
                                name : "No"
                            } ];
                        else if (options.type == "YesNoCancel")
                            options.buttons = [ {
                                name : "Cancel"
                            }, {
                                name : "No"
                            }, {
                                name : "Yes"
                            } ];
                    }

                    $.each(options.buttons, function(index, value) {
                        value.click = function(e) {
                            $("#extMessageBox").data("kendoExtDialog")
                                .close();
                            deferred.resolve({
                                button : value.name ,
                                inputMessage :$("#inputMessage").val(),
                                checkbox :$('input[name=optradio]:checked').val()
                            });
                        }
                        if (("title" in value) == false) {
                            $.each([ {
                                name : "OK",
                                title : dictionary.Ok,
                                cls : 'k-update'
                            }, {
                                name : "Cancel",
                                title : dictionary.Cancel,
                                cls : 'k-cancel'
                            }, {
                                name : "Yes",
                                title : dictionary.Yes,
                                cls : 'k-update'
                            }, {
                                name : "No",
                                title : dictionary.No,
                                cls : 'k-cancel'
                            } ], function(i, v) {
                                if (v.name == value.name) {
                                    value.title = v.title;
                                    value.cls = v.cls;
                                }
                            });
                        }
                    });

                    var inputVisible = false;
                    var checkboxVisible = false;
                    switch(options.inputType) {
                        case "text":
                        case "password":
                            inputVisible = true;
                            break;
                        case "checkbox":
                            checkboxVisible = true;
                            break;

                        case "textCheckbox":
                            inputVisible = true;
                            checkboxVisible = true;
                            options.inputType = "text";
                            break;
                        case "passwordCheckbox":
                            inputVisible = true;
                            checkboxVisible = true;
                            options.inputType = "password";
                            break;
                    }

                    options = $.extend({
                        width : "400px",
                        // height: "120px",
                        modal : true,
                        visible : false,
                        inputVisible : inputVisible,
                        checkboxVisible : checkboxVisible,
                        message : "",
                        icon : "k-ext-information"
                    }, options);

                    $(document.body)
                        .append(
                        kendo.format(
                            "<div id='extMessageBox' style='position:relative;'><div class='{0}'></div><div class='k-ext-text'>{1}</div><div style='padding-top:50px;padding-bottom: 20px;display:{4}'><div style='padding-bottom:20px;text-align:center;clear:both;display:{5}'><label class='radio-inline'><input type='radio' name='optradio' value='YES'>{7}</label><label class='radio-inline'><input type='radio' name='optradio' value='NO'>{8}</label></div><input type='{2}' id='inputMessage' placeholder='{3}' class='k-input k-textbox' style='direction:rtl;width:100%;display:{6}'></div></div>",
                            options.iconClass,
                            options.message,
                            options.inputType,
                            options.inputPlaceholder,
                            options.visible,
                            options.checkboxVisible==false?'none':'block',
                            options.inputVisible==false?'none':'block',
                            dictionary.Yes,
                            dictionary.No));

                    $("#extMessageBox").kendoExtDialog(options);
                    $("#extMessageBox").parent().find(
                        "div.k-window-titlebar div.k-window-actions")
                        .empty();
                    $("#extMessageBox").parent().find(
                        "div.k-window-titlebar span.k-window-title");
                    $("#extMessageBox").data("kendoExtDialog").center()
                        .open();
                });
        }
    };
})(window.kendo, window.kendo.jQuery);

(function(window, undefined) {

    /* Error MessageBox */
    kendo.showErrorBox = function(msg) {
        if (msg.responseText) {
            msg = $.parseJSON(msg.responseText).message;
        }

        $.when(kendo.ui.ExtMessageBox.show({
            title : dictionary.Error,
            message : msg,
            type : "OK",
            icon : "Error",
            inputType :"hidden",
            visible: "none"
        }));
    }

    /* Information MessageBox */
    kendo.showInformationBox = function(msg) {
        $.when(kendo.ui.ExtMessageBox.show({
            title : "",
            message : msg,
            type : "OK",
            icon : "Information",
            inputType :"hidden",
            visible: "none"
        }));
    }

    /* Warning MessageBox */
    kendo.showWarningBox = function(msg) {
        $.when(kendo.ui.ExtMessageBox.show({
            title : "",
            message : msg,
            type : "OK",
            icon : "Warning",
            inputType :"hidden",
            visible: "none"

        }));
    }

    /* YesNo Box */
    kendo.showYesNoBox = function(obj) {
        $.when(kendo.ui.ExtMessageBox.show({
            title : obj.title,
            message : obj.message,
            type : "YesNo",
            icon : "Question",
            inputType :"hidden",
            visible: "none"
        })).done(obj.done);
    }

    /* YesNoCancel Box */
    kendo.showYesNoCancelBox = function(obj) {
        $.when(kendo.ui.ExtMessageBox.show({
            title : obj.title,
            message : obj.message,
            type : "YesNoCancel",
            icon : "Question",
            inputType :"hidden",
            visible: "none"
        })).done(obj.done);
    }

    /* OkCancel Box */
    kendo.showOkCancelBox = function(obj) {
        $.when(kendo.ui.ExtMessageBox.show({
            title : obj.title,
            message : obj.message,
            type : "OKCancel",
            icon : "Question",
            inputType :"hidden",
            visible: "none"
        })).done(obj.done);
    }

    /* Input Message Box */
    kendo.showInputMessageBox = function(obj) {
        $.when(kendo.ui.ExtMessageBox.show({
            title : obj.title,
            message : obj.message,
            inputPlaceholder : obj.inputPlaceholder,
            type : obj.type,
            icon : "Question",
            visible:"block",
            inputType :obj.inputType
        })).done(obj.done);
    }
})(this);