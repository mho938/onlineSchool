var VLC =
{
    Exist : false,
    Version : -1,
    Name: 'vlc_player',
    Browser: '',
    // blend, bob, discard, linear, mean, x, yadif, yadif2x [supported in vlc version >= 1.1.0]
    DeinterlaceMode : 'yadif2x',
    
    Init : function(name, browser) {
        VLC.Name = name;
        VLC.Browser = browser;
        VLC.Detect();
    },
    
    GetAudioList: function() {
        var anames = new Array();
        
        var vlc = VLC.GetObject();
        if (vlc !== null && vlc.video !== null) {
            var count = vlc.audio.count;
            if (count > 2) {
                for (i = 1; i < count; i++) {
                    aa = vlc.audio.description(i).replace('[', '').replace(']', '').split(' - ');
                    anames.push(Translate.CorrectName(aa[1]));
                }
            }
        }
        
        return anames;
    },
    
    SetAudioTrack: function(audioIndex, index) {
        var vlc = VLC.GetObject();
        if (vlc !== null && vlc.video !== null && typeof(vlc.audio) !== "undefined") {
            parts = VLC.Version.split(',');
            if (parseInt(parts[0],10) === 2 &&
                parseInt(parts[1],10) === 0 &&
                parseInt(parts[2],10) <= 5) {
                vlc.audio.track = index;
            }
            else {
                vlc.audio.track = parseInt(audioIndex,10);
            }
        }
    },
    
    EnableDeinterlace: function() {
        var vlc = VLC.GetObject();
        if (vlc !== null && 
            vlc.video !== null && 
            typeof(vlc.video) !== "undefined") {
            vlc.video.deinterlace.enable(VLC.DeinterlaceMode);
        }
    },
    
    Detect : function() {
        // Object that holds all data on the plugin
        var P = {name:"VLC", status:-1, version:null, minVersion:"1,0,0,0"};

        if (typeof(PluginDetect) === "undefined") {
            return false;
        }
        var $$=PluginDetect;

        P.status = $$.isMinVersion(P.name, P.minVersion);
        P.version = $$.getVersion(P.name);
        VLC.Version = P.version;

        switch (P.status) {
            case 1:     // "installed & enabled, version is >= " + P.minVersion;
            case 0:     // "installed & enabled, version is unknown";
            case -0.1:  // "installed & enabled, version is < " + P.minVersion;
                VLC.Exist = true;
                break;
            default:
            case -0.2:  // "installed but not enabled";
            case -1:    // "not installed or not enabled";
            case -2:    // "please enable ActiveX in Internet Explorer so that we can detect your plugins";
            case -3:    // "error...bad input argument to PluginDetect method";
                VLC.Exist = false;
                break;
        }
		return VLC.Exist;
    },
    
    GetObject: function() {
        if (window.document[VLC.Name])
        {
            return window.document[VLC.Name];
        }
        if (navigator.appName.indexOf("Microsoft Internet")===-1)
        {
            if (document.embeds && document.embeds[VLC.Name])
                return document.embeds[VLC.Name];
        }
        else // if (navigator.appName.indexOf("Microsoft Internet")!=-1)
        {
            return document.getElementById(VLC.Name);
        }
        
        return null;
    },
    
    GetTag: function(chromeless) {
        if (VLC.Exist) {
            if (VLC.Browser === "IE") {
                return '<object name="' + VLC.Name + '" ' +
                               'id="' + VLC.Name + '" ' +
                               'classid="clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921" ' +
                               'style="width:100%; height:100%;display:block;" ' +
                               'events="True">' +
                            '<param name="MRL" value="" />' +
                            (chromeless ? '<param name="ShowDisplay" value="True" />' : "") +
                            '<param name="Loop" value="False" />' +
                            '<param name="AutoPlay" value="True" />' +
                            '<param name="Toolbar" value="False" />' +
                       '</object>';
            }
            else {
                return '<embed type="application/x-vlc-plugin" ' +
                              'name="' + VLC.Name + '" ' +
                              'id="' + VLC.Name + '" ' +
                              (chromeless ? 'controls="false" windowless="true" ' : "") +
                              'autoplay="yes" loop="no" hidden="no" ' +
                              'target="" style="width:100%; height:100%;display:block;" />';
            }
        }
    },
    
    Start: function(address) {
        var vlc = VLC.GetObject();
        if (vlc !== null && 
            vlc.playlist !== null && 
            typeof(vlc.playlist) !== "undefined") {
            var options = [":rtsp-tcp"];
            var itemId = vlc.playlist.add(address,"",options);
            options = [];
            if( itemId !== -1 ) {
                vlc.playlist.playItem(itemId);
                
                clearInterval(VLC._muteTimer);
            }
        }
    },
        
    registerVLCEvent: function(event, handler){
        var vlc = VLC.GetObject();
		if (vlc != null) {
			if(vlc.attachEvent){
			   // Microsoft
			   vlc.attachEvent(event, handler);
			}
			else if(vlc.addEventListener){
			   // Mozilla: DOM level 2
			   vlc.addEventListener(event, handler, false);
			}
			else{
			  // DOM level 0
			  vlc["on" + event] = handler;
			}
		}
    },

    unregisterVLCEvent: function(event, handler) {
        var vlc = VLC.GetObject();
        if (vlc) {
            if (vlc.detachEvent) {
                // Microsoft
                vlc.detachEvent (event, handler);
            } else if (vlc.removeEventListener) {
                // Mozilla: DOM level 2
                vlc.removeEventListener (event, handler, false);
            } else {
                // DOM level 0
                vlc["on" + event] = null;
            }
        }
    },

    HasSignal: function() {
        return VLC.GetObject().input.state !== 3;
    },
        
    Play: function() {
        var vlc = VLC.GetObject();
        if (vlc !== null && 
            vlc.playlist !== null && 
            typeof(vlc.playlist) !== "undefined") {
            vlc.playlist.play();
        }
    },
        
    Pause: function() {
        var vlc = VLC.GetObject();
        if (vlc !== null && 
            vlc.playlist !== null && 
            typeof(vlc.playlist) !== "undefined") {
            vlc.playlist.pause();
        }
    },
        
    Stop: function() {
        var vlc = VLC.GetObject();
        if (vlc !== null && 
            vlc.playlist !== null && 
            typeof(vlc.playlist) !== "undefined") {
            vlc.playlist.stop();
            //vlc.playlist.clear();
        }
    },
    
    SetVolume: function(vol) {
        var vlc = VLC.GetObject();
        if (vlc !== null && 
            vlc.audio !== null && 
            typeof(vlc.audio) !== "undefined") {
            vlc.audio.volume = vol;
        }
    },
    
    SetMute: function(val) {
        clearInterval(VLC._muteTimer);
        var vlc = VLC.GetObject();
        if (vlc !== null && 
            vlc.audio !== null && 
            typeof(vlc.audio) !== "undefined") {
            vlc.audio.mute = val;
        }
    },
    
    SetAspectRatio: function(value) {
        var vlc = VLC.GetObject();
        if (vlc !== null && 
            vlc.video !== null && 
            typeof(vlc.video) !== "undefined") {
            vlc.video.aspectRatio = value;
        }
    },
    
    PlayListItemCount: function() {
        var vlc = VLC.GetObject();
        if (vlc !== null && 
            vlc.playlist !== null && 
            typeof(vlc.playlist) !== "undefined") {
            return vlc.playlist.items.count;
        }
        return 0;
    },
	
	GetPosition: function() {
        var vlc = VLC.GetObject();
        if (vlc !== null && 
            vlc.input !== null && 
            typeof(vlc.input) !== "undefined") {
			return vlc.input.time;
		}
		return 0;
	},
	
	Seek: function(offset) {
        var vlc = VLC.GetObject();
        if (vlc !== null && 
            vlc.input !== null && 
            typeof(vlc.input) !== "undefined") {
			vlc.input.time = offset;
		}
	}
};