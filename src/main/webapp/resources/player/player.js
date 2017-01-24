// jQuery Plugin
// version 0.3, Dec. 20th, 2015
// by Mojtaba Khallash
function initPlugin() {
	(function($) {
		$.player = function(element, options) {
			var defaults = {
				height		: 	'100%',
				width		: 	'100%',
				//data: {
				//	contentType: <live - file>,
				//	type: <rtmp - udp>,
				//	address: "...",
				//	content: {
				//		id: ...,
				//		name: "...",
				//		screenshot: "...",
				//		music: <true - false>,
				//		rateOfUser: ...
				//	},
				//	track: [{
				//		id: ...,
				//		title: "...",
				//		length: ...,
				//		lengthText: "...",
				//		filename: "...",
				//		extension: "...",
				//	}],
				//	artists: [
				//		{
				//			id: ...,
				//			name: "..."
				//		},
				//		...
				//	]
				//}
				data	 	: 	{},
				captions	:	[],
				description	:	true,
				forward		: 	true,
				backward	: 	true,
				next		: 	true,
				prev		: 	true,
				lang		: 	'fa',
				minimizeMOD	:	true,
				stopHandler	: 	function() {},
				nextHandler	: 	function() {},
				prevHandler	: 	function() {},
				chromeless	:	false

			}
			
			var plugin = this;
			plugin.settings = {};
			
			// rtmp - hls - udp
			var sourceType = '';

			
			// Check player existance
			var IE = (Settings.getBrowser() === "IE");
			function checkExistance() {
				if (IE) {
					$('.btnNotIE').hide();
					$('.btnIE').show();
				}
				else {
					$('.btnIE').hide();
					$('.btnNotIE').show();
				}
			}
			var needFlash = (Settings.getOperatingSystem() == 'Windows' && PluginDetect.getVersion('Flash')== null);
			var needVLC = !VLC.Detect();
			var isVlcNeeded = function() {
				return (sourceType == "udp") && needVLC;
			}
			var isFlashNeeded = function() {
				return (sourceType != "udp") && needFlash;
			}

			// Check status
			var isLive = function() {
				var isLive = plugin.settings.data != null && plugin.settings.data.contentType=='live';
				if (sourceType == "udp")
					return isLive && !needVLC;
				else
					return isLive && !needFlash;
			}
			var showPlayerControl = function() {
				return 	plugin.settings.data != null && plugin.settings.data.contentType!='live' && 
						!needFlash && !needVLC && !cannotPlay;
			}

			
			var _init = function() {
				plugin.settings = $.extend({}, defaults, options);
				plugin.element = element;
				
				plugin.element.load( playerRoot + "/player/player.html", function() { 
					Dictionary.apply(plugin.settings.lang);
					
					$('.btnMX').attr('href',download.mxPlayer);
					$('.btnNotIE').attr('href',download.flash.ie);
					$('.btnIE').attr('href',download.flash.other);
					$('.btnVLC').attr('href',download.vlc);
					
					$('.btnFullscreen .fa-compress').hide();
					$('.btnFullscreen .fa-expand').show();
					
					checkExistance();
					
					setupHeight();
					
					createPlayer();
				});
			}
			
			var loading = true;
			function setLoading(status) {
				loading = status;
				if (loading == true) {
					$('.hide-on-loading').hide();
					$('.show-on-loading').show();
					
					$('.global-info').css('display','none');
					$('.loading-info').css('display','table-cell');
				}
				else {
					$('.show-on-loading').hide();
					$('.hide-on-loading').show();
					
					$('.loading-info').css('display','none');
					$('.global-info').css('display','table-cell');
				}
			}
			
			var playing = false;
			function setPlaying(status) {
				playing = status;
				if (playing == true) {
					$('.pnlLive .btnPlay').parent().hide();
					$('.btnPlay').hide();
					$('.btnPause').show();
				}
				else {
					$('.btnPause').hide();
					$('.btnPlay').show();
				}
				
				if (plugin.settings.chromeless == false)
					showDescription();
			}
			
			function _formatData() {
				if (typeof(plugin.settings.data) != "undefined" &&
					typeof(plugin.settings.data.address) != "undefined") {
						
					//
					// content
					//
					if (typeof(plugin.settings.data.content)=="undefined" || plugin.settings.data.content == null) {
						plugin.settings.data.content = { 
							name : "",
							screenshot : null,
							music : false,
							rateOfUser : null
						};
					}
					else {
						// Music
						if (typeof(plugin.settings.data.content.music)=="undefined")
							plugin.settings.data.content.music = false;
						
						// Screenshot
						if (typeof(plugin.settings.data.content.screenshot) == "undefined")
							plugin.settings.data.content.screenshot = null;
						
						// Title
						if (typeof(plugin.settings.data.content.name)=="undefined" || plugin.settings.data.content.name == null)
							plugin.settings.data.content.name = "";
						
						// rateOfUser
						if (typeof(plugin.settings.data.content.rateOfUser) == "undefined")
							plugin.settings.data.content.rateOfUser = null;
					}
					
					//
					// track
					//
					if (typeof(plugin.settings.data.track)=="undefined" || plugin.settings.data.track == null) {
						plugin.settings.data.track = {
							title : ""
						};
					}
					else {
						if (typeof(plugin.settings.data.track.title)=="undefined" || plugin.settings.data.track.title == null)
							plugin.settings.data.track.title = "";
					}
							
					//
					// artists
					//
					if (typeof(plugin.settings.data.artists)=="undefined" && plugin.settings.data.artists == null) {
						plugin.settings.data.artists = [];
					}
				}
				else
					plugin.settings.data = null;
			}
			
			var metadata = null;
			var mnuAudios = false;
			var mnuQualities = false;
			var mnuCaptions  = false;
			var cannotPlay = false;
			var hasOutput = false;
			var fullscreen = false;
			var id = "_player_";
			var showingTimer = 0;
			var createPlayer = function() {
				metadata = null;
				mnuAudios = false;
				mnuQualities = false;
				mnuCaptions = false;
				id = 'random_player_' + Math.floor((Math.random() * 999999999) + 1);
				// JWPlayer place
				$('.jwplayer-wrapper').html('<div id="rtmp-' + id + '"></div>').attr(id,'jw-'+id).hide();
				// VLC Player Place
				$('.vlc-player-wrapper').html('').attr('id','vlc-'+id).hide();
				

				var os = Settings.getOperatingSystem();
				cannotPlay = true;
				clearTimeout(showingTimer);
				_formatData();
				if (plugin.settings.data != null) {
					
					// Description
					if (plugin.settings.chromeless == false) {
						$('.description-wrapper').show();
						showDescription();
					}
					
					if (plugin.settings.forward)
						$('.btnForward').show();
					else
						$('.btnForward').hide();
					if (plugin.settings.backward)
						$('.btnBackward').show();
					else
						$('.btnBackward').hide();
					if (plugin.settings.next)
						$('.btnNext').show();
					else
						$('.btnNext').hide();
					if (plugin.settings.prev)
						$('.btnPrev').show();
					else
						$('.btnPrev').hide();

					hasOutput = !plugin.settings.data.content.music;
					var pw = $('.player-wrapper')
					if (hasOutput) {
						if (plugin.settings.data.content.music) {
							pw.removeClass('video-player');
							pw.addClass('music-player');
						}
						else {
							pw.removeClass('music-player');
							pw.addClass('video-player');
						}
						pw.css('visibility', 'visible');
					}
					else {
						pw.css('visibility', 'hidden');
					}
					
					cannotPlay = (plugin.settings.data.contentType == "file" && plugin.settings.data.content.music && os=="Android");
					
					if (cannotPlay || needFlash)
						$('.pnl-play-timer').hide();
					else
						$('.pnl-play-timer').show();
					
					if (plugin.settings.data.contentType!='live')
						$('.pnl-play-timer > div').show();
					else
						$('.pnl-play-timer > div').hide();
					
					// rtmp - hls - udp
					sourceType = plugin.settings.data.type;
					switch (sourceType) {
						case 'rtmp':
						case 'hls':
							$('.need-vlc').hide();
							if (isFlashNeeded()) {
								$('.pnl-play-timer').hide();
								$('.need-flash').show();
							}
							else {
								$('.jwplayer-wrapper').show();
								$('jw-'+id).show();
								
								$('.need-flash').hide();
							}
							break;
						case 'udp':
							$('.need-flash').hide();
							if (isVlcNeeded()) {
								$('.pnl-play-timer').hide();
								$('.need-vlc').show();
							}
							else {
								$('.vlc-player-wrapper').show();
								$('vlc-'+id).show();
								
								$('.need-vlc').hide();
							}
							break;
					}
					
					// Album Name
					var dict = Dictionary.getDict(plugin.settings.lang);
					
					if (plugin.settings.data.content.name.length > 0) {
							
						switch (plugin.settings.data.contentType) {
							case 'file':
								if (plugin.settings.data.content.music==true)
									$('.lblTitleText').text(dict.Album);
								else
									$('.lblTitleText').text(dict.Film);
								break;
							case 'live':
								$('.lblTitleText').text(dict.Channel);
								break;
						}
						$('.lblTitleValue').text(plugin.settings.data.content.name);
						$('.pnlTitle').show();
					}
					else
						$('.pnlTitle').hide();
					
					if (plugin.settings.data.contentType=='file') {
						// Track Name
						if (plugin.settings.data.track.title.length > 0)
							$('.lblTrack').text(plugin.settings.data.track.title).show();
						else
							$('.lblTrack').hide();
						
						// Artist
						if (plugin.settings.data.artists.length > 0) {
								
							$('lblArtistText').text(plugin.settings.data.content.music ? dict.Singer : dict.Director);

							var artists = "";
							for (var i=0; i<plugin.settings.data.artists.length; i++) {
								var artist = plugin.settings.data.artists[i];
								if (artist && artist.length > 0) {
									if (artists.length > 0)
										artists += " - ";
									artists += plugin.settings.data.artists[i].name;
								}
							}
							$('.lblArtistValue').text(artists);
							$('.pnlArtist').show();
						}
						else
							$('.pnlArtist').hide();
					}
					else {
						$('.lblTrack, .pnlArtist').hide();
					}
					
					// Poster
					if (plugin.settings.data.content.screenshot != null)
						$('.imgPoster').attr('src', plugin.settings.data.content.screenshot).show();
					else
						$('.imgPoster').hide();
					
					// Star
					if (plugin.settings.data.contentType=='file' &&
						plugin.settings.data.content.rateOfUser != null) {
						for (var i=1; i<6; i++) {
							if (plugin.settings.data.content.rateOfUser>=20*i)
								$('.star'+i).attr('class', 'fa fa-star');
							else
								$('.star'+i).attr('class', 'fa fa-star-o');
						}
						$('.star-wrapper').show();
					}
					else
						$('.star-wrapper').hide();
					
					// Player Controller
					if (plugin.settings.data.contentType!='live' && !needFlash && !cannotPlay)
						$('.global-control').show();
					else
						$('.global-control').hide();
					
					if (plugin.settings.data.contentType!='live' && !needFlash && cannotPlay)
						$('.android-only').show();
					else
						$('.android-only').hide();
					
					if (isLive())
						$('.pnlLive').show();
					else
						$('.pnlLive').hide();
					
				}
				else {
					hasOutput = false;
					$('.description-wrapper').hide();
					setupHeight();
					$('.loading-info').css('display', 'none');
					var dict = Dictionary.getDict(plugin.settings.lang);
					$('.global-info').text(dict.NoSource).css('display', 'table-cell');
					if (plugin.settings.chromeless == false)
						$(".controls-container, .metadata-panel").show();
					$('.pnlLive').hide();
					$(".show-on-loading").show();
					$('.player').css("background-image", "none")

					return;
				}
					
                $('.pnl-toggle-controls').off('mousemove')
                $('.pnl-toggle-controls').on('mousemove', function( e ){
                    clearTimeout(showingTimer);
                    if (!$(".controls-container").is(':visible'))
                        $(".controls-container, .metadata-panel").fadeIn('fast');

                    showingTimer = setTimeout(function() {
                        $(".controls-container, .metadata-panel").fadeOut('slow');
                    }, 3000);
                });
					
				updateStyle();
				
				setLoading(true);
				if (sourceType == "udp") {
					if (needVLC) {
						setLoading(false);
						$('#player_control').css("visibility", "visible");
						return;
					}
					else
						setupUDP();
				}
				else {
					if (needFlash) {
						setLoading(false);
						$('#player_control').css("visibility", "visible");
						return;
					}
					else
						setupRTMP();
				}
			}
			
			function updateStyle() {
				setupHeight();
				
				if (player != null) player.pause();
				if (slider != null) slider.setValue(0);
				
				clearTimeout(showingTimer);
				if (plugin.settings.chromeless == false)
					$(".controls-container, .metadata-panel").show();
			}
			
			function setupHeight() {
				var w = parseInt($('.player').css('width'),10);
				var h = parseInt((9 * w)/16,10);
				if (h < 100)
					h = plugin.settings.height;
				if (plugin.settings.minimizeMOD&&plugin.settings.data!=null&&plugin.settings.data.content!=null&&plugin.settings.data.content.music)
					h = 100;
				
				$('.player').css({
					"min-width" : plugin.settings.width,
					"min-height" : h,
					"background-image" : "url('" + playerRoot + "/player/pics/" + (hasOutput ? "vod" : "mod") + ".png')",
					"background-color" : "black",
					"background-size" : "cover",
					"background-position" : "0px center" 
				});
				
				plugin.element.css({
					width : plugin.settings.width,
					height : h,
					display: 'block'
				});
			}
			
			function showDescription() {
				var pd = $('.playerDescription');
				if ( (plugin.settings.description) &&
					 !plugin.settings.chromeless &&
					 ( ( plugin.settings.data.content.music && fullscreen) ||
					   (!plugin.settings.data.content.music && playing==false)))
					pd.show();
				else
					pd.hide();
				
				// Description Style
				pd.css({
					background: plugin.settings.data.content.music ? 'transparent' : 'rgba(128, 128, 128, 0.3)'
				});
			}

			
			//================================ API ================================//
			plugin.play = function() { handlePlay(); }
			plugin.pause = function() { handlePause(); }
			plugin.stop = function() { handleStop(); }
			plugin.next = function() { handleNext(); }
			plugin.prev = function() { handlePrev(); }
			plugin.forward = function() { handleForward(); }
			plugin.backward = function() { handleBackward(); }
			plugin.fullscreen = function() { toggleFullscreen(); }
			plugin.getCurrentTime = function() { return currentTime; }
			plugin.setChromeless = function(chromeless) { 
				plugin.settings.chromeless = chromeless; 
				if (player != null)
					player.setControls(chromeless);
				if (chromeless)
					$('.pnl-toggle-controls, .controls-container, .metadata-panel,.playerDescription').hide();
				else
					$('.pnl-toggle-controls').show();
			}
			plugin.getChromeless = function(chromeless) { return plugin.settings.chromeless; }
			plugin.changeSource = function(data, captions) { 
				plugin.settings.data = data;
				if (typeof(captions) != "undefined")
					plugin.settings.captions = captions;
				else
					plugin.settings.captions = [];
				createPlayer();
			}
			
			
			
			
			
			
			//================================ RTMP ================================//
			function setupRTMP() {
				// convert RTMP to HLS if possible
				var address = "";
				if (plugin.settings.data != null) {
					address = plugin.settings.data.address;
					
					if (plugin.settings.data.contentType == "live") {
						/*var index = address.indexOf("?");
						if (index > -1) {
							address = 
								address.substring(0, index)
									.replace("rtmp", "http")
									.replace("livepkgr/", "hls-live/livepkgr/_definst_/liveevent/") + ".m3u8";
						}*/
					}
				}
				$('.btnPlayUrl').attr('href', address);

				var setup = {
					width: '100%',
					height: '100%',
					androidhls : true,
					file : address,
					aspectratio : '16:9',
					primary : 'flash',
					controls : (plugin.settings.chromeless == true),
					autostart : true,
					events : {
						onReady: ready,
						onComplete: complete,
						onPlay: play,
						onPause: pause,
						onIdle: stop,
						onMeta: meta
					}
				}
				if (plugin.settings.chromeless == true) {
					setup.image = plugin.settings.data.content.screenshot;
					if (plugin.settings.data.content.music)
						setup.wmode = 'transparent';
				}
				if (jwVersion == 6) {
					setup.skin = playerRoot + "/player/jwplayer-6/skin/defaultSkin.xml";
				}
				if (plugin.settings.captions != null) {
					var tracks = [];
					for (var i = 0; i < plugin.settings.captions.length; i++) {
						tracks.push({
							kind:'captions',
							file:plugin.settings.captions[i].file,
							label:plugin.settings.captions[i].title
						});
					}
					
					setup.tracks = tracks;
				}
				
				tryPlay(setup);
			}
			function tryPlay(setup) {
				setTimeout(function() {
					try {
						player = jwplayer('rtmp-'+id).setup(setup);
					}
					catch (e) {
						tryPlay(setup);
					}
				}, 30);
			}
			
			
			
			
			
			//================================ UDP ================================//
			function setupUDP() {
				VLC.Init('vlc_player'+id, Settings.getBrowser());
				$('#vlc-'+id).html(VLC.GetTag(!plugin.settings.chromeless));
				setTimeout(function() {
					if (IE) {
						$('#vlc_player'+id).css({
							'width': $('#vlc-'+id).css('width'),
							'height': $('#vlc-'+id).css('height')
						});
					}
					VLC.Start(plugin.settings.data.address);
					VLC.EnableDeinterlace();
					VLC.registerVLCEvent("MediaPlayerBuffering", function(val){
						if (val == 100)
							ready();
					});
					VLC.registerVLCEvent("MediaPlayerEndReached", complete);
					VLC.registerVLCEvent("MediaPlayerPlaying", play);
					VLC.registerVLCEvent("MediaPlayerPaused", pause);
					VLC.registerVLCEvent("MediaPlayerStopped", stop);
				}, 1000);
			}
			
			
			
			
			
			
			//================================ Player Controls ================================//
			var length = -1;
			var timer = 0;
			var showingTimer = 0;
			var player = null;
			var slider = null;
			var status = '';
			function ready() {
				setLoading(false);
				
				if (plugin.settings.data != null) {
					if (plugin.settings.data.contentType == "live")
						length = 0;
					else
						length = plugin.settings.data.track.length / 1000;
				}
					
				if (length > -1) {
					createSlider(length);
				}
				else {
					pooling = setInterval(function() {
						length = player.getDuration();
						if (length > -1) {
							clearInterval(pooling);
							createSlider(length);
						}
					}, 300);
				}
				status = 'ready';
			}
			
			function complete() {
				setPlaying(false);
				clearInterval(timer);
				status = 'end';
				plugin.settings.stopHandler({state:status});
			}
			
			function play() {
				setPlaying(true);
				
				if (plugin.settings.chromeless == false)
					$('.description-wrapper').show();
				
				if (timer != null)
					clearInterval(timer);
				timer = setInterval(function() {
					if (metadata == null)
						getMetadata();
					
					if (slider == null) {
						clearInterval(timer);
						if (plugin.settings.data!=null && plugin.settings.data.contentType == "live") {
							if (timer != null)
								clearInterval(timer);
						}
						else
							stop();
						return;
					}
					
					if (player != null)
						slider.setValue(player.getPosition());
					else
						slider.setValue(VLC.GetPosition());
				}, 1000);

				clearTimeout(showingTimer);
				if (plugin.settings.data!=null && (!plugin.settings.data.content.music || fullscreen)) {
					showingTimer = setTimeout(function() {
						$(".controls-container, .metadata-panel").fadeOut('slow');
					}, 3000);
				}
				status = 'playing';
			}
			
			function pause() {
				setPlaying(false);
				
				if (timer != null)
					clearInterval(timer);
				status = 'pause';
			}
			
			function stop() {
				setPlaying(false);
				
				if (timer != null)
					clearInterval(timer);
					
				if (slider != null) {
					slider = $("#play_timer").data('slider');
					slider.setValue(0);
				}
				
				currentTime = -1;
				status = 'stop';
				plugin.settings.stopHandler({state:status});
			}
			
			currentLevel = -1;
			currentQualityState = -1;
			function meta(event) {
				if (metadata != null && metadata.current.quality == 0 && metadata.qualities[0].label == "Auto" && event.metadata.bandwidth) {
					currentLevel = Number(event.metadata.currentLevel.substr(0,1));
					
					var ind = event.metadata.currentLevel.indexOf('(');
					currentQualityState = event.metadata.currentLevel.substr(ind-1,event.metadata.currentLevel.length);
				}
			}
			
			var currentTime = -1;
			function createSlider(length) {
				slider = $("#play_timer").slider({
					tooltip: 'always',
					min: 0,
					max: length,
					value:0,
					formatter: function(value) {
						currentTime = time = value;
						var s = parseInt(time % 60, 10);
						if (String(s).length == 1)
							s = "0" + s;
						time = (time - s) / 60;
						var m = parseInt(time % 60, 10);
						if (String(m).length == 1)
							m = "0" + m;
						var h = parseInt((time - m) / 60, 10);
						if (String(h).length == 1)
							h = "0" + h;

						return (h != "00" ? (h+":") : "") + m + ":" + s;
					}
				})
				.on("slideStop", function(slideEvt) {
					var offset = slideEvt.value;
					if (player != null)
						player.seek(offset);
					else
						VLC.Seek(offset);
						
				})
				.data('slider');
				
				$('#player_control').css("visibility", "visible");
			}
			
			function getMetadata() {
				currentLevel = -1;
				currentQualityState = "";
				if (player != null) {
					metadata = {
						audios : player.getAudioTracks(),
						qualities : player.getQualityLevels(),
						captions : player.getCaptionsList(),
						
						current : {
							audio : player.getCurrentAudioTrack(),
							quality : player.getCurrentQuality(),
							caption : player.getCurrentCaptions()
						}
					};
				}
				else {
					metadata = {
						audios : [],
						qualities : [],
						captions : [],
						
						current : {
							audio : -1,
							quality : -1,
							caption : -1
						}
					}
				}
				
				// Audio
				if (metadata.audios != null && metadata.audios.length > 1) {
					$('.audios-submenu').html('');
					for (var i = 0; i < metadata.audios.length; i++) {
						$('.audios-submenu').append('<li>' + 
														'<a class="audio" style="color:white;direction:ltr;font-size:16px;" data-index="' + i + '">' + 
															(i == metadata.current.audio
																? '<i class="fa fa-check active-check" style="color:white"></i> '
																: '<i class="fa fa-check" style="color:transparent"></i> ') +
															metadata.audios[i].name +
														'</a>' +
													 '</li>');
					}
					$( 'div' ).off('click', '.audio');
					$( 'div' ).on('click', '.audio', function(){
						$('.audios-submenu i.active-check').removeClass('active-check').attr('style','color:transparent');
						$(this).find('i').addClass('active-check').attr('style','color:white');
						setAudioTrack($(this).data('index'));
						$('.btnAudios').removeClass('open');
					});
					$('.btnAudios').show();
				}
				else
					$('.btnAudios').hide();
				
				// Quality
				if (metadata.qualities != null && metadata.qualities.length > 1) {
					$('.qualities-submenu').html('');
					for (var i = 0; i < metadata.qualities.length; i++) {
						$('.qualities-submenu').append('<li>' + 
														'<a class="quality" style="color:white;direction:ltr;font-size:16px;" data-index="' + i + '">' + 
															(i == metadata.current.quality
																? '<i class="fa fa-check active-check" style="color:white"></i> '
																: '<i class="fa fa-check" style="color:transparent"></i> ') +
															metadata.qualities[i].label +
														'</a>' +
													 '</li>');
					}
					$( 'div' ).off('click', '.quality');
					$( 'div' ).on('click', '.quality', function(){
						$('.qualities-submenu i.active-check').removeClass('active-check').attr('style','color:transparent');
						$(this).find('i').addClass('active-check').attr('style','color:white');
						setQuality($(this).data('index'));
						$('.btnQualities').removeClass('open');
					});
					$('.btnQualities').show();
				}
				else
					$('.btnQualities').hide();
				
				// Caption
				if (metadata.captions != null && metadata.captions.length > 1) {
					$('.captions-submenu').html('');
					for (var i = 0; i < metadata.captions.length; i++) {
						$('.captions-submenu').append('<li>' + 
														'<a class="caption" style="color:white;direction:ltr;font-size:16px;" data-index="' + i + '">' + 
															(i == metadata.current.caption
																? '<i class="fa fa-check active-check" style="color:white"></i> '
																: '<i class="fa fa-check" style="color:transparent"></i> ') +
															metadata.captions[i].label +
														'</a>' +
													 '</li>');
					}
					$( 'div' ).off('click', '.caption');
					$( 'div' ).on('click', '.caption', function(){
						$('.captions-submenu i.active-check').removeClass('active-check').attr('style','color:transparent');
						$(this).find('i').addClass('active-check').attr('style','color:white');
						setCaption($(this).data('index'));
						$('.btnCaptions').removeClass('open');
					});
					$('.btnCaptions').show();
				}
				else
					$('.btnCaptions').hide();
				
				$('.dropdown-toggle').dropdown();
			}
			
			
			
			
			//================================ Event Handler ================================//
			$( document ).off('click', '.btnPause');
			$( document ).on('click', '.btnPause', function(){
				handlePause();
			});
			function handlePause() {	
				if (player != null)
					player.pause();
				else
					VLC.Pause();
			}
			
			$( document ).off('click', '.btnPlay');
			$( document ).on('click', '.btnPlay', function(){
				handlePlay();
			});
			function handlePlay() {	
				if (player != null)
					player.play();
				else
					VLC.Play();
			}
			
			$( document ).off('click', '.btnStop');
			$( document ).on('click', '.btnStop', function(){
				handleStop();
			});
			function handleStop() {	
				plugin.settings.stopHandler({state:'stoping'});
				
				if (player != null)
					player.stop();
				else
					VLC.Stop();
					
				if (fullscreen)
					toggleFullscreen();
			}
			
			$( document ).off('click', '.btnNext');
			$( document ).on('click', '.btnNext', function(){
				handleNext();
			});
			function handleNext() {	
				plugin.settings.nextHandler();
			}
			
			$( document ).off('click', '.btnPrev');
			$( document ).on('click', '.btnPrev', function(){
				handlePrev();
			});
			function handlePrev() {	
				plugin.settings.prevHandler();
			}
			
			$( document ).off('click', '.btnForward');
			$( document ).on('click', '.btnForward', function(){
				handleForward();
			});
			function handleForward() {	
				var interval = 5 * (length) / 100;
				
				var position = 0;
				if (player != null)
					position = player.getPosition();
				else
					position = VLC.GetPosition();
				position += interval;
				
				if (player != null)
					player.seek(position);
				else
					VLC.Seek(position);
			}
			
			$( document ).off('click', '.btnBackward');
			$( document ).on('click', '.btnBackward', function(){
				handleBackward();
			});
			function handleBackward() {	
				var interval = 5 * (length) / 100;
								
				var position = 0;
				if (player != null)
					position = player.getPosition();
				else
					position = VLC.GetPosition();
				position -= interval;

				if (player != null)
					player.seek(position);
				else
					VLC.Seek(position);
			}

			function getFullscreenState() {
				if (document.fullscreenElement)
					return document.fullscreenElement;
				else if (document.msFullscreenElement)
					return document.msFullscreenElement;
				else if (document.mozFullScreen)
					return document.mozFullScreen;
				else if (document.webkitIsFullScreen)
					return document.webkitIsFullScreen;
				else return false;
			}
			function addFullscreenEvent() {
				document.addEventListener("fullscreenchange", function () {
					fullscreen = document.fullscreenElement;
					handleFullscreen();
				}, false);

				document.addEventListener("msfullscreenchange", function () {
					fullscreen = document.msFullscreenElement;
					handleFullscreen();
				}, false);

				document.addEventListener("mozfullscreenchange", function () {
					fullscreen = document.mozFullScreen;
					handleFullscreen();
				}, false);
				document.addEventListener("webkitfullscreenchange", function () {
					fullscreen = document.webkitIsFullScreen;
					handleFullscreen();
				}, false);
			}
			addFullscreenEvent();


			$( document ).off('click', '.btnFullscreen');
			$( document ).on('click', '.btnFullscreen', function(){
				toggleFullscreen();
			});
			function toggleFullscreen() {
				$('.player').addClass('isInFullScreen').fullScreen(!getFullscreenState());
			}
			function handleFullscreen() {
				if (fullscreen) {
					$('.btnFullscreen .fa-expand').hide();
					$('.btnFullscreen .fa-compress').show();
				}
				else {
					$('.btnFullscreen .fa-compress').hide();
					$('.btnFullscreen .fa-expand').show();
				}
				setupHeight();
			}
			
			$( document ).off('click', '.pnl-toggle-controls');
			$( document ).on('click', '.pnl-toggle-controls', function(){
				closeAllSubmenu();
				clearTimeout(showingTimer);
				if (plugin.settings.data!=null && (!plugin.settings.data.content.music || fullscreen)) {
					if ($(".controls-container").is(':visible')) {
						$(".controls-container, .metadata-panel").fadeOut('fast');
					}
					else {
						$(".controls-container, .metadata-panel").fadeIn('fast');
					}
				}
				else {
					$(".controls-container, .metadata-panel").show();
				}
			});
			
			$('.btnAudios > a').on('click', function(){ toggleAudioTrack(); });
			$('.btnQualities > a').on('click', function(){ toggleQualities(); });
			$('.btnCaptions > a').on('click', function(){ toggleCaptions(); });

			
			
			
			
			
			
			
			
			mnuAudios = false;
			toggleAudioTrack = function() {
				clearTimeout(showingTimer);
				if (!mnuAudios) {
					mnuQualities = false;
					mnuCaptions = false;
				}
				mnuAudios = !mnuAudios;
			}
			setAudioTrack = function(index) {
				metadata.current.audio = index;
				if (player != null)
					player.setCurrentAudioTrack(index);
				mnuAudios = false;
			}
			
			mnuQualities = false;
			toggleQualities = function() {
				clearTimeout(showingTimer);
				if (!mnuQualities) {
					mnuAudios = false;
					mnuCaptions = false;
				}
				mnuQualities = !mnuQualities;
			}
			setQuality = function(index) {
				metadata.current.quality = index;
				if (player != null)
					player.setCurrentQuality(index);
				mnuQualities = false;
				
				currentLevel = -1;
				currentQualityState = -1;
			}
			
			mnuCaptions = false;
			toggleCaptions = function() {
				clearTimeout(showingTimer);
				if (!mnuCaptions) {
					mnuAudios = false;
					mnuQualities = false;
				}
				mnuCaptions = !mnuCaptions;
			}
			setCaption = function(index) {
				metadata.current.caption = index;
				if (player != null)
					player.setCurrentCaptions(index);
				mnuCaptions = false;
			}

			function closeAllSubmenu() {
				mnuCaptions = false;
				mnuAudios = false;
				mnuQualities = false;
			}

			plugin.setData = function(data) {
				if (data == null)
					stop();
				if (data == null && fullscreen)
					toggleFullscreen();

				createPlayer();
			}

			window.addEventListener("resize", function () {
				setupHeight();
			});
			
			_init();
		}
	})(jQuery);


	var protocol = window.location.protocol;
	var host = window.location.host;
	// Global Variable
	var download = {
		mxPlayer : protocol + "//" + host + "/downloads/MX_Player_1.7.32_APKField.apk",
		flash : {
			ie : protocol + "//" + host + "/downloads/Adobe.Flash.Player.v11.0.1/for.Internet.Explorer.AOL/Setup.exe",
			other : protocol + "//" + host + "/downloads/Adobe.Flash.Player.v11.0.1/for.Firefox.Netscape.Safari.Opera/Setup.exe"
		},
		vlc: protocol + "//" + host + "/downloads/vlc-2.2.1-win32.exe"
	};



	// Settings
	var Settings = {
		getOperatingSystem : function() {
			var OSName="Unknown OS";
			if (navigator.appVersion.indexOf("Win")!=-1) OSName="Windows";
			if (navigator.appVersion.indexOf("Mac")!=-1) OSName="MacOS";
			if (navigator.appVersion.indexOf("X11")!=-1) OSName="UNIX";
			if (navigator.appVersion.indexOf("Android")!=-1) OSName="Android";
			else if (navigator.appVersion.indexOf("Linux")!=-1) OSName="Linux";

			return OSName;
		},
			
		getBrowser : function() {
			navigator.sayswho = (function(){
				var ua= navigator.userAgent, tem, 
				M= ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
				if(/trident/i.test(M[1])){
					tem=  /\brv[ :]+(\d+)/g.exec(ua) || [];
					return 'IE';//+(tem[1] || '');
				}
				if(M[1]=== 'Chrome'){
					tem= ua.match(/\bOPR\/(\d+)/)
					if(tem!= null) {
						return 'Opera';
					}
				}
				M= M[2]? [M[1], M[2]]: [navigator.appName, navigator.appVersion, '-?'];
				if((tem= ua.match(/version\/(\d+)/i))!= null) M.splice(1, 1, tem[1]);
				if(M[0]=== 'Chrome') {
					version = parseInt(M[1]);
					return 'Chrome';
				}
				if(M[0]=== 'Firefox') {
					version = parseInt(M[1]);
					return 'Firefox';
				}
				if(M[0]=== 'Safari') {
					version = parseInt(M[1]);
					return 'Safari';
				}
				return M.join(' ');
			})();
			
			return navigator.sayswho;
		}
	}



	// Dictionary
	var Dictionary = {
		fa : {
			LoadingData : "در حال دریافت اطلاعات از سرور",
			Prev: "قبلی",
			Backward: "رو به عقب",
			Play: "پخش",
			Pause: "توقف",
			Stop: "قطع پخش",
			Forward: "رو به جلو",
			Next: "بعدی",
			NeedFlash : "پخش کننده فلش بر روی مرورگر شما نصب نیست یا غیر فعال است.",
			NeedVLC : "پخش کننده VLC بر روی مرورگر شما نصب نیست یا غیر فعال است.",
			DownloadFlash : "دانلود پخش کننده فلش",
			DownloadVLC : "دانلود پخش کننده VLC",
			DownloadMX: "دانلود پخش کننده MXPlayer",

			Channel : "کانال",
			Film : "فیلم",
			Album : "آلبوم",
			Singer : "خواننده",
			Director : "کارگردان",
			
			NoSource : "منبع ورودی یافت نشد"
		},
		en : {
			LoadingData : "Receiving data from server",
			Prev: "Previous",
			Backward: "Backward",
			Play: "Play",
			Pause: "Pause",
			Stop: "Stop",
			Forward: "Forward",
			Next: "Next",
			NeedFlash : "Flashplayer plugin not installed or inactive.",
			NeedVLC : "VLC plugin not installed or inactive.",
			DownloadFlash : "Download Flash Player",
			DownloadVLC : "Download VLC",
			DownloadMX: "Download MXPlayer",
			
			Channel : "Channel",
			Film : "Film",
			Album : "Album",
			Singer : "Singer",
			Director : "Director",
			
			NoSource : "No Input Source Found"
		},
		getDict: function(lang) {
			if (lang == 'fa')
				return Dictionary.fa;
			else
				return Dictionary.en;
		},
		apply : function(lang) {
			var dict = Dictionary.getDict(lang);
			
			$('.btnPrev').attr('title', dict.Prev);
			$('.btnBackward').attr('title', dict.Backward);
			$('.btnPause').attr('title', dict.Pause);
			$('.btnPlay').attr('title', dict.Play);
			$('.btnStop').attr('title', dict.Stop);
			$('.btnForward').attr('title', dict.Forward);
			$('.btnNext').attr('title', dict.Next);
			
			$('.loading-data').text(dict.LoadingData);
			$('.btn-play').text(dict.Play);
			
			$('.btnMX').text(dict.DownloadMX);
			$('.need-flash a').text(dict.DownloadFlash);
			$('.need-flash span').text(dict.NeedFlash);
			$('.need-vlc a').text(dict.DownloadVLC);
			$('.need-vlc span').text(dict.NeedVLC);
		}
	}
	
	$.holdReady( false );
}

$.holdReady( true );
//var jwVersion = 6;
var jwVersion = 7;
var _scripts = [
	'/player/plugin/PluginDetect_AllPlugins.js',
	'/player/vlc/vlc.js',
	'/player/jwplayer-' + jwVersion + '/jwplayer.js'
];

function _loadScript(_i) {
	if (_i < _scripts.length) {
		var _js = _scripts[_i];
		_addScript(playerRoot + _js, function () {
			_i++;
			_loadScript(_i);
		});
	}
	else {
		initPlugin();
	}
}
function _addScript(path, loadHandler) {
	var _script = document.createElement('script');
	_script.setAttribute('src', path);
	_script.setAttribute('type', "text/javascript");
	_script.onload = loadHandler;
	document.head.appendChild(_script);
}
setTimeout("_loadScript(0);",200);
