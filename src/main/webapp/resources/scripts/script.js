$(document).ready(function() {
	$( window ).resize(function() {
		calculateHeight();
	});

	calculateHeight();

	pabId = parseInt($('#pnlTabs li:eq(0)').attr('tabId'),10);
	load(pabId);
});

function load(tabId) {
	$('#mainBody').html('<div id="player"></div>');

	$.ajax({
		url: restUrl + tabId,
		method: 'GET',
		dataType: 'json',
		success: function(data) {
			var index  = 0;
			var list = { channels: data };
			$('#tmpl').html('');
			$("#listTemplate").tmpl(list).prependTo("#tmpl");

			setTimeout(function () {
				$('.list-group-item.active').trigger('click');
			}, 800)
		}
	});
}

function calculateHeight() {
	var h =   parseInt($('body').css('height'), 10)
		- parseInt($('body').css('padding-top'), 10) * 2;

	$('.tab-content').css('height', h - 235 - 23);
	$('#mainBody').css('height', h- 5);
}


function play(current, address, type) {
	$('.list-group-item.active').removeClass('active');
	$(current).addClass('active');

	$('#mainBody').html('<div id="player"></div>');

	if (type === "UDP") {
		$('#player').html(
			'<embed  type="application/x-vlc-plugin" ' +
			'autoplay="yes" loop="no" hidden="no" ' +
			'target="' + address + '" style="width:100%; height:100%;display: block;" />');
	}
	else {
		// RTMP
		jwplayer("player").setup({
			flashplayer: rootUrl + "/resources/scripts/jwplayer.swf",
			file: address,
			height: "100%",
			width: "100%",
			autostart: true,
			controlbar: {"position": "bottom"},
			mute: false,
			volume: 100
		});
	}
}