var mode = '';
var canEdit = new Object();
var index = 0;

/*
 * $.validator.setDefaults({ ignore: "" });
 */

function getIndex() {
	return ++index;
}
function setStartOnDataBinding() {
	index = (this.dataSource.page() - 1) * this.dataSource.pageSize();
}

function setKeyDown(gridName) {
	$(document.body).keydown(function(e) {
		if (e.altKey && e.keyCode == 87) {
			$("#" + gridName).data("kendoGrid").table.focus();
		}
	});
}
function loadGridChange(gridName, cookieName) {
	var grid = $("#" + gridName).data("kendoGrid");
	var state = $.cookie(cookieName + "-state");
	if(state){
		state = JSON.parse(state);
		if(state.filter){
			parseFilterDates(state.filter, grid.dataSource.options.schema.model.fields);
		}
		grid.dataSource.query(state);
	}
	else{
		grid.dataSource.read();
	}
}


function handleDelete(gridName, event, handler) {
	_handleDelete_(gridName, event, handler)
}
function _handleDelete(gridName, event, handler) {
	_handleDelete_(gridName, event.currentTarget, handler)
}
function _handleDelete_(gridName, event, handler) {
	mode = 'delete';
	var dataitem = {};
	var grid = $("#" + gridName).data('kendoGrid');

	dataitem = grid.dataItem($(event).closest('tr'));
	kendo.showYesNoBox({
		title: dictionary.delete_record,
		message: dictionary.delete_confirm,
		done: function(response) {
			if (response.button == "Yes") {
				grid.dataSource.remove(dataitem);
				grid.dataSource.sync();

				if (grid._data.length == 0 && grid.dataSource.page() > 1)
					setTimeout(function () {

						if (grid._data.length == 0) {
							setTimeout(function () {
								if (grid._data.length == 0) {
								}
								grid.dataSource.read();
							}, 1000);
						}
						if (typeof(handler) != "undefined")
							handler()
					}, 1000);

				else {
					if (typeof(handler) != "undefined")
						handler()
				}
			}
			else {
				dataitem = {};
			}
		}
	});
}
function _handleDelete_id(gridName, id, handler) {
	mode = 'delete';
	var dataitem = {};
	var grid = $("#" + gridName).data('kendoGrid');
	dataitem = grid.dataSource.get(id);
	kendo.showYesNoBox({
		title: dictionary.delete_record,
		message: dictionary.delete_confirm,
		done: function(response) {
			if (response.button == "Yes") {
				grid.dataSource.remove(dataitem);
				grid.dataSource.sync();

				if (grid._data.length == 0 && grid.dataSource.page() > 1)
					setTimeout(function () {

						if (grid._data.length == 0) {
							setTimeout(function () {
								if (grid._data.length == 0) {
								}
								grid.dataSource.read();
							}, 1000);
						}
						if (typeof(handler) != "undefined")
							handler()
					}, 1000);

				else {
					if (typeof(handler) != "undefined")
						handler()
				}
			}
			else {
				dataitem = {};
			}
		}
	});
}

function _setButtonText(e) {
	$(".k-grid-cancel").html($(".k-grid-cancel").find("span")).append(dictionary.Cancel);
	$(".k-grid-update").html($(".k-grid-update").find("span")).append(dictionary.Save);
}

function _openModal(e) {
	mode = 'change';
    _setButtonText(e);

	var title;
	if (e.model.isNew()) {
		// create
		title = dictionary.add;
	} else {
		// edit
		title = dictionary.edit;
	}

	var wnd = e.container.data("kendoWindow");
	wnd.title(title);

	setTimeout(function() {
		$('.k-window input[type=text]:eq(0)').select().focus();
	}, 500);

	if (typeof (e.model.Readonly) != "undefined") {
		isReadonly = e.model.Readonly;
		if (isReadonly) {
			setReadonly();
		}
	}
}

function _dataBound() {
	canEdit = new Object();
}

function _gridDetailInit(e) {
	if (typeof (e.data.Readonly) != "undefined") {
		var editable = (e.data.Readonly == false);
		canEdit[e.data.ID] = editable;
		if (!editable) {
			$(e.detailRow).find('.k-toolbar').remove();
		}
	}

	$(e.detailRow).find('.k-grid-header-wrap').scrollLeft(0);
}

function _cancelModal(e) {
	if (e.model.Readonly == true) {
		window.setTimeout(function() {
			if (e.model.get('Readonly') == false)
				e.model.set('Readonly', true);
		}, 500);
	}
}

function _saveModal(gridName, e) {
	window.setTimeout(function() {
		if (!hasError) {
			var grid = $("#" + gridName).data("kendoGrid");
			grid.dataSource.read();
		}
	}, 1000);
}

var hasError = false;
function error_handler(gridName, e) {
	hasError = false;
	if (e.errors) {
		hasError = true;
        cancelChanges(gridName);

		var message = "";
		$.each(e.errors, function(key, value) {
			if ('errors' in value) {
				$.each(value.errors, function () {
					if (message.length == 0)
						message += this;
					//else
						//message += this + "<br/>";
				});
			}
		});
		kendo.showErrorBox(message);
	} else {
        handleHttpErrorCode(e.xhr, gridName, e);
	}
}
function handleHttpErrorCode(xhr, gridName, e) {
	console.log("asdasdasd")
    switch (xhr.status) {
        case 401:
            kendo.showYesNoBox({
                title : dictionary.SessionExpierd,
                message : dictionary.Unauthorized,
                done : function(response) {
                    if (response.button == "Yes")
                        window.location.reload();
                    else if (typeof(gridName)!= "undefined" && gridName != null)
                        cancelChanges(gridName);
                }
            });
            break;
        case 405:
            kendo.showErrorBox(dictionary.AccessDenied);
            if (typeof(gridName)!= "undefined" && gridName != null) {
                var grid = $("#" + gridName).data("kendoGrid");
                grid.dataSource.read();
            }
            break;
        case 404:
            kendo.showErrorBox(dictionary.ServerNotFound);
            if (typeof(gridName)!= "undefined" && gridName != null)
                cancelChanges(gridName);
            break;
        default:
            kendo.showErrorBox(e.errorThrown);
            if (typeof(gridName)!= "undefined" && gridName != null)
                cancelChanges(gridName);
            break;
    }
}
function cancelChanges(gridName) {
    var grid = $("#" + gridName).data("kendoGrid");
    if (mode == 'delete')
        grid.dataSource.cancelChanges();
    grid.one("dataBinding", function(e) {
        e.preventDefault(); // cancel grid rebind if error occurs
    });
}

function _dataBoundSubentity(e, masterID) {
	if (typeof(canEdit[masterID]) != "undefined" && !canEdit[masterID]) {
		var cols = $('#grid_' + masterID).data('kendoGrid').columns;
		var commandIndex = -1;
		for (i = 0; i < cols.length; i++) {
			if (typeof (cols[i].command) != 'undefined')
				commandIndex = i;
		}

		if (commandIndex > 0) {
			$('#grid_' + masterID + ' .k-grid-header table th:eq(' + commandIndex + ')').remove();
			$('#grid_' + masterID + ' .k-grid-content table tr').each(function () {
				$(this).find('td:eq(' + commandIndex + ')').remove();
			});
		} else if (commandIndex == 0 && cols[0].locked == true) {
			if ($('#grid_' + masterID + ' .k-grid-header .k-grid-header-locked').length > 0) {
				$('#grid_' + masterID + ' .k-grid-header .k-grid-header-locked').remove()
				// var width1 = parseInt($('#grid_' + masterID + ' .k-grid-content-locked').css('width'), 10);
				// var width2 = parseInt($('#grid_' + masterID + ' .k-grid-content').css('width'), 10);
				// $('#grid_' + masterID + ' .k-grid-content').css('width', (width1 + width2) + "px");
				$('#grid_' + masterID + ' .k-grid-content-locked').remove();
			}
		}
	}
}

function onDataBoundCorrectLink(gridName, e) {
	var grid = $('#' + gridName).data('kendoGrid');

	var requestObject = (new kendo.data.transports["aspnetmvc-server"]({prefix: ""}))
		.options.parameterMap({
			page: grid.dataSource.page(),
			sort: grid.dataSource.sort(),
			filter: grid.dataSource.filter()
		});


	$(".export").each(function () {
		// Get the export link as jQuery object
		var $exportLink = $(this);

		// Get its 'href' attribute - the URL where it would navigate to
		var href = $exportLink.attr('href');

		// Update the 'page' parameter with the grid's current page
		//href = href.replace(/page=([^&]*)/, 'page=' + requestObject.page || '~');

		// Update the 'sort' parameter with the grid's current sort descriptor
		href = href.replace(/sort=([^&]*)/, 'sort=' + requestObject.sort || '~');

		// Update the 'pageSize' parameter with the grid's current pageSize
		//href = href.replace(/pageSize=([^&]*)/, 'pageSize=' + grid.dataSource.total());

		//update filter descriptor with the filters applied

		href = href.replace(/filter=([^&]*)/, 'filter=' + (requestObject.filter || '~'));

		// Update the 'href' attribute
		$exportLink.attr('href', href);
	});
}

function GetEnumText(gridName, fieldName, enumValue) {
	var grid = $("#" + gridName).data('kendoGrid');
	for(var i=0;i<grid.columns.length;i++) {
		var column = grid.columns[i];
		if (column.field === fieldName) {
			for(var j=0;j<column.values.length;j++) {
				var item = column.values[j];
				if (item.value == enumValue)
					return item.text;
			}
		}
	}
	return "";
}
function setEnum(gridName, fieldName, enumValues) {
	var grid = $("#" + gridName).data('kendoGrid');
	grid.columns.filter(function (item) {
		return item.field === fieldName;
	}).forEach(function (item) {
		item.values = enumValues;
	});

	grid.refresh();
}


function uploadReset(id) {
	if (id) {
		//if an id is passed as a param, only reset the element's child upload controls (in case many upload widgets exist)
		$("#" + id + " .k-upload-files").remove();
		$("#" + id + " .k-upload-status").remove();
		$("#" + id + " .k-upload.k-header").addClass("k-upload-empty");
		$("#" + id + " .k-upload-button").removeClass("k-state-focused");
	} else {
		//reset all the upload things!
		$(".k-upload-files").remove();
		$(".k-upload-status").remove();
		$(".k-upload.k-header").addClass("k-upload-empty");
		$(".k-upload-button").removeClass("k-state-focused");
	}
}

function textForStatus(gridName, colName, id) {
	var grid = $("#" + gridName).data("kendoGrid");
	var cols = grid.columns;
	var commandIndex = -1;
	for (var i = 0; i < cols.length; i++) {
		if (cols[i].field == colName) {
			commandIndex = i;
			break;
		}
	}

	var values = cols[commandIndex].values;

	var item = $.grep(values, function (item, _) {
		return item.value === id;
	})[0];

	return item ? item.text : "";
}

function saveGridChange(gridName, cookieName, type, e) {
    var grid = $("#" + gridName).data("kendoGrid");
    switch (type) {
        case "dataBound":
            var dataSource = grid.dataSource;

            var state = kendo.stringify({
                page: dataSource.page(),
                pageSize: dataSource.pageSize(),
                sort: dataSource.sort(),
                group: dataSource.group(),
                filter: dataSource.filter()
            });

            $.cookie(cookieName + "-state", state);
            if ($.cookie(cookieName + '-rows')) {
                $.each(JSON.parse($.cookie(cookieName + '-rows')), function () {
                    var item = dataSource.get(this);
                    var row = grid.tbody.find('[data-uid=' + item.uid + ']');
                    row.addClass('k-state-selected');
                })
            }
            break;
        case "change":
            var ids = grid.select().map(function(){
                return grid.dataItem($(this)).Id
            }).toArray();
            $.cookie(cookieName + '-rows',JSON.stringify(ids));
            break;
    }
}
function loadGridChange(gridName, cookieName) {
    var grid = $("#" + gridName).data("kendoGrid");
    var state = $.cookie(cookieName + "-state");
    if(state){
        state = JSON.parse(state);
        if(state.filter){
            parseFilterDates(state.filter, grid.dataSource.options.schema.model.fields);
        }
        grid.dataSource.query(state);
    }
    else{
        grid.dataSource.read();
    }
}
function parseFilterDates(filter, fields) {
    if(filter.filters){
        for(var i = 0; i < filter.filters.length; i++){
            parseFilterDates(filter.filters[i], fields);
        }
    }
    else{
		if (filter.field.indexOf(".")==-1) {
			if (fields[filter.field].type == "date") {
				filter.value = kendo.parseDate(filter.value);
			}
		}
    }
}

// Resize Grid
function calculateGridHeight(gridName, parent, margin) {
	if (parent) {
		if (typeof(margin) == "undefined") {
			margin = 20;
		}
		$('#'+gridName).css({
			height: parseInt($(parent).css('height'),10) - margin
		});
	}
	else {
		$('#'+gridName).css({
			height: parseInt($('body').css('height'),10) - parseInt($('#main-header').css('height'),10) - parseInt($('#mainPanel').css('padding-top'),10) - parseInt($('#mainPanel').css('padding-bottom'),10)
		});
    	resizeWrapper();
	}

	resizeGrid(gridName);
	resize(gridName);
}

function resize(gridName) {
    var gridElement = $("#"+gridName),
        dataArea = gridElement.find(".k-grid-content"),
        gridHeight = gridElement.innerHeight(),
        otherElements = gridElement.children().not(".k-grid-content"),
        otherElementsHeight = 0;
    otherElements.each(function(){
        otherElementsHeight += $(this).outerHeight();
    });
    dataArea.height(gridHeight - otherElementsHeight);
}
function resizeGrid(gridName) {
    var gridElement = $("#"+gridName),
        dataArea = gridElement.find(".k-grid-content"),
        gridHeight = gridElement.innerHeight(),
        otherElements = gridElement.children().not(".k-grid-content"),
        otherElementsHeight = 0;
    otherElements.each(function(){
        otherElementsHeight += $(this).outerHeight();
    });
    dataArea.height(gridHeight - otherElementsHeight);
}

function resizeWrapper() {
    $("#outerWrapper").height($('#body').innerHeight());
}

function hasWarningTab() {
	setTimeout(function () {
		var hasWarn;
		var index  = 0;
		$('.k-window .nav.nav-tabs a').each(function() {
			var panel = $(this).attr('href');
			var tab = $(this);

			hasWarn = false;
			$('' + panel + ' input').each(function () {
				if ($(this).hasClass('k-invalid'))
					hasWarn = true;
			});
			if (hasWarn) {
				tab.addClass('has-warning');
				if (index === 0) {
					tab.trigger('click');
				}
				index++;
			}
			else
				tab.removeClass('has-warning');
		});
	}, 200);
}