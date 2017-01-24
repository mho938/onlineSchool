!function(a){function b(d){if(c[d])return c[d].exports;var e=c[d]={exports:{},id:d,loaded:!1};return a[d].call(e.exports,e,e.exports,b),e.loaded=!0,e.exports}var c={};return b.m=a,b.c=c,b.p="",b(0)}([function(a,b,c){var d,e;d=[c(2),c(3),c(5),c(4),c(6)],e=function(a,b,c,d){var e=function(a,e,f){function g(){setTimeout(function(){y("setup",{})},0),"hide"!==e.oncomplete&&e.oncomplete!==!1&&a.getConfig().repeat!==!0&&a.on("playlistComplete",k),"autoplay"===e.oncomplete&&(e.onclick="play",M=new c(e),M.on("autoplayTrigger",w),M.on("countdownUpdate",u)),K=B.isString(e.heading)?e.heading:U,h(),z.addClass(f,"jw-plugin-related"),N.resize(a.getWidth(),a.getHeight())}function h(){D=z.createElement('<div class="jw-related-container jw-reset"></div>'),f.appendChild(D);var a=z.createElement(W);F=a.getElementsByClassName("jw-related-grid")[0];for(var c=0;12>c;c++){var d=new b(e);d.on("activate",w),O.push(d),F.appendChild(d.el)}D.appendChild(a);var g=z.createElement(V),h=g.getElementsByClassName("jw-related-close-icon")[0];E=g.getElementsByClassName("jw-related-heading-text")[0],new A(h).on("click tap",l),D.appendChild(g)}function i(b){l(b);var c=a.getPlaylistItem();C=j(e.file,c.mediaid),q(),I=K}function j(a,b){return a&&a.indexOf("MEDIAID")>0&&b?a.replace("MEDIAID",b):a&&-1===a.indexOf("MEDIAID")?a:null}function k(b){R||S||!P.length||("playing"===a.getState()&&a.pause(),S=!0,J=b?"click"===b.type||"tap"===b.type?"interaction":"complete":"api",u(K),p(),M&&"complete"===J&&M.startAutoplay(O[0],P[0]),v(J))}function l(b){if(S){var c="play";b?("click"===b.type||"tap"===b.type)&&(c="interaction"):c="api","paused"===a.getState()&&a.play(),M&&M.clearAutoplay(),S=!1,v(c)}}function m(b){b===C?z.log("RELATED: Failed to load "+b):z.log("RELATED: "+b),x(null),a.removeButton("related"),u("No related videos found")}function n(){C?(u(""),z.ajax(C,o,m,!0)):m("No related videos file found")}function o(b){var c;if(b.responseText!==G){if(c="["===b.responseText.trim().substring(0,1)?z.tryCatch(function(){return JSON.parse(b.responseText)}):z.tryCatch(function(){return z.rssparser.parse(b.responseXML.firstChild)}),!B.isArray(c))return void m("This feed is not valid XML, JSON, and/or RSS feed.");G=b.responseText,P=B.filter(c,function(a){var b=B.isArray(a.sources)&&a.sources[0]?a.sources[0]:{};return a.image&&a.title&&("play"===e.onclick&&(b.file||a.file)||"play"!==e.onclick&&a.link)?!0:void 0}),P.length||m("Related feed entries do not contain the necessary data.")}P.length&&(a.addButton(void 0,I,k,"related",T),x(c),p(),u(K),M&&"complete"===J&&M.startAutoplay(O[0],P[0]))}function p(a){var b=L;if(L=B.isUndefined(a)?L:a,F&&L){L!==b&&(b&&z.removeClass(F,b.classname),z.addClass(F,L.classname)),z.toggleClass(F,"jw-related-grid-center-content",P.length<L.numcols),z.toggleClass(D,"jw-related-large-layout",X.indexOf(L)>=2);for(var c=0;c<O.length;c++)O[c].updateThumb(P[c],c<L.maxthumbs)}}function q(){e.recommendations?(H&&(H.onload=null,H.onreadystatechange=null,H.onerror=null,H.abort&&H.abort()),Q={},H=z.ajax(j(e.recommendations,a.getPlaylistItem().mediaid)||e.recommendations,r,s,!0)):n()}function r(a){H=null;try{a=JSON.parse(a.responseText)}catch(b){return void z.log("RELATED: Error parsing recommended feed: "+b)}C=a.file||C,Q=a.params||Q,n()}function s(a){H=null,z.log("RELATED: Error loading recommended feed: "+a),n()}function t(b){b.active?(R=!0,a.removeButton("related")):(R=!1,P&&P.length&&a.addButton(void 0,I,k,"related",T))}function u(a){E.textContent=a}function v(b){var c={visible:S,method:b,relatedCount:Math.min(P.length,L.maxthumbs)};c.visible&&(c.feed=C,c.items=P.slice(0,L.maxthumbs),c.autoplay="autoplay"===e.oncomplete),J=void 0,z.toggleClass(f,"jw-show",S),z.toggleClass(a.getContainer(),"jw-flag-overlay-open",S),a.setControls(!S),y(S?"open":"close",c)}function w(b,c){var d="play"===e.onclick;"auto"!==c&&(c=d?"manual":"link"),y("play",{position:P.indexOf(b),relatedCount:Math.min(P.length,L.maxthumbs),method:c,item:b}),d||"auto"===c?(a.load(b),a.play()):(l({event:"link"}),window.top.location=b.link),"auto"===c&&P.push(P.shift())}function x(a){y("playlist",{playlist:a})}function y(a,b){C&&(b.relatedFile=z.getAbsolutePath(C)),B.extend(b,Q),b.onclick="play"===e.onclick?"play":"link",N.trigger(a,b)}var z=a.utils,A=z.UI,B=a._;B.extend(this,a.Events),d._||(d._=B,d.utils=z,d.UI=A,d.Events=a.Events);var C,D,E,F,G,H,I,J,K,L,M,N=this,O=[],P=[],Q={},R=!1,S=!1,T="jw-related-dock-btn",U="Related Videos",V='<div class="jw-related-header jw-reset"><span class="jw-related-heading-text jw-text jw-reset"></span><span class="jw-icon jw-icon-close jw-related-close-icon jw-reset"></span></div>',W='<div class="jw-related-grid-container jw-reset"><div class="jw-related-grid"></div></div>',X=[{classname:"jw-related-grid-2-1",numcols:2,numrows:1,maxthumbs:2,maxwidth:480,maxheight:270},{classname:"jw-related-grid-3-2",numcols:3,numrows:2,maxthumbs:6,maxwidth:640,maxheight:360},{classname:"jw-related-grid-4-3",numcols:4,numrows:3,maxthumbs:12,maxwidth:Number.POSITIVE_INFINITY,maxheight:Number.POSITIVE_INFINITY}];this.resize=function(a,b){var c=B.find(X,function(c){return a<c.maxwidth||b<c.maxheight});c!==L&&p(c)},a.on("ready",g),a.on("cast",t),a.on("playlistItem",i),this.open=function(a){k(a)},this.close=function(a){l(a)}};e.version=a.version,jwplayer().registerPlugin("related",a.minPlayerVersion,e)}.apply(b,d),!(void 0!==e&&(a.exports=e))},,function(a,b){a.exports={version:"2.0.0",minPlayerVersion:"7.0.0"}},function(a,b,c){var d,e;d=[c(4)],e=function(a){function b(){var b=a.utils,d=a.UI,e=a._;e.extend(c.prototype,a.Events,{setup:function(a){this.isActive=!1,this.action=a&&"play"===a.onclick?"play":"link",this.container=b.createElement('<div class="jw-related-thumb jw-reset"><div class="jw-related-thumb-cover jw-reset"></div><span class="jw-related-thumb-title jw-text jw-reset"></span></div>'),this.item=void 0,this.title=this.container.getElementsByClassName("jw-related-thumb-title")[0],this.ui=new d(this.container).on("click tap",this.activated,this),this.el=this.container},updateThumb:function(a,c){this.isActive=c,this.item=a,b.toggleClass(this.container,"jw-related-thumb-inactive",!this.item||!this.isActive),this.isActive&&this.item&&(this.title.innerHTML=a.title,this.container.style.backgroundImage='url("'+a.image+'")')},activated:function(){this.trigger("activate",this.item)}})}var c=function(a){c.prototype.trigger||b(),this.setup(a)};return c}.apply(b,d),!(void 0!==e&&(a.exports=e))},function(a,b){var c={};a.exports=c},function(a,b,c){var d,e;d=[c(4)],e=function(a){function b(){var b=a.utils,d=a._,e='<svg xmlns="http://www.w3.org/2000/svg" class="jw-related-autoplay-svg"><rect class="jw-related-autoplay-frame" width="100%" height="100%"/><rect class="jw-related-autoplay-frame-progress" width="100%" height="100%"/></svg>',f="__TITLE__ will play in XX seconds",g=10;d.extend(c.prototype,a.Events,{setup:function(a){this.options=a,this.autoplayMessage=a.autoplaymessage?a.autoplaymessage:f,this.autoplayTime=d.isNumber(a.autoplaytimer)?a.autoplaytimer:g},clearAutoplay:function(){this.autoplayTextInterval&&(window.clearInterval(this.autoplayTextInterval),window.clearTimeout(this.autoplayTimeout),this.autoplayThumb.el.removeChild(this.autoplayFrame),b.removeClass(this.autoplayThumb.el,"jw-related-autoplay-target"),this.autoplayTimeout=this.autoplayTextInterval=void 0)},startAutoplay:function(a,c){if(a){if(this.autoplayItem=c,this.autoplayThumb=a,b.addClass(this.autoplayThumb.el,"jw-related-autoplay-target"),this.autoplayFrame)this.autoplayThumb.el.appendChild(this.autoplayFrame);else{this.autoplayFrame=b.createElement(e),this.autoplayThumb.el.appendChild(this.autoplayFrame);var d=this.autoplayThumb.el.getElementsByClassName("jw-related-autoplay-frame-progress")[0];d.style.animationDuration=d.style.webkitAnimationDuration=this.autoplayTime+"s"}this.autoplayTimeout=window.setTimeout(this.triggerAutoplay.bind(this),1e3*this.autoplayTime),this.autoplayStartTime=Date.now(),this.updateAutostartText(),this.autoplayTextInterval=window.setInterval(this.updateAutostartText.bind(this),500)}},triggerAutoplay:function(){this.trigger("autoplayTrigger",this.autoplayItem,"auto")},updateAutostartText:function(){var a=Math.max(0,Math.ceil(this.autoplayTime-(Date.now()-this.autoplayStartTime)/1e3)),b=this.autoplayMessage;b=b.replace(/xx/gi,a),b=b.replace(/__TITLE__/gi,this.autoplayItem.title),this.trigger("countdownUpdate",b)}})}var c=function(a){c.prototype.trigger||b(),this.setup(a)};return c}.apply(b,d),!(void 0!==e&&(a.exports=e))},function(a,b,c){var d=c(7);"string"==typeof d&&(d=[[a.id,d,""]]),c(9)(d,{}),d.locals&&(a.exports=d.locals)},function(a,b,c){b=a.exports=c(8)(),b.push([a.id,".jw-related-dock-btn .jw-dock-image,.jw-related-icon-related{background-image:url('data:image/svg+xml;charset=US-ASCII,%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20width%3D%2218%22%20height%3D%2212%22%20viewBox%3D%22-390%20295%2018%2012%22%3E%3Cpath%20fill%3D%22%23FFF%22%20d%3D%22M-378%20295c-1.1%200-2.1.3-3%20.8-.9-.5-1.9-.8-3-.8-3.3%200-6%202.7-6%206s2.7%206%206%206c1.1%200%202.1-.3%203-.8.9.5%201.9.8%203%20.8%203.3%200%206-2.7%206-6s-2.7-6-6-6zm-6%2011c-2.8%200-5-2.2-5-5s2.2-5%205-5c.7%200%201.4.2%202.1.5-1.3%201.1-2.1%202.7-2.1%204.5s.8%203.4%202.1%204.5c-.7.3-1.4.5-2.1.5zm6%200c-.7%200-1.4-.2-2.1-.5%201.3-1.1%202.1-2.7%202.1-4.5s-.8-3.4-2.1-4.5c.7-.3%201.4-.5%202.1-.5%202.8%200%205%202.2%205%205s-2.2%205-5%205z%22%2F%3E%3C%2Fsvg%3E');background-repeat:no-repeat}.jw-plugin-related{width:100%;height:100%;visibility:hidden;opacity:0;bottom:0}.jw-plugin-related.jw-show{visibility:visible;opacity:1}.jw-plugin-related .jw-related-container{position:absolute;top:0;left:0;height:100%;width:100%;background-color:rgba(0,0,0,.75)}.jw-plugin-related .jw-related-container .jw-text{color:#fff}.jw-plugin-related .jw-related-grid-container{position:absolute;width:100%;top:0;bottom:0;margin-top:2.5em;padding:0 1.25em 1.25em;line-height:0;display:-webkit-box;display:-webkit-flex;display:flex;-webkit-box-orient:horizontal;-webkit-box-direction:normal;-webkit-flex-direction:row;flex-direction:row;-webkit-flex-wrap:wrap;flex-wrap:wrap;-webkit-align-content:center;align-content:center;-webkit-box-pack:center;-webkit-justify-content:center;justify-content:center;-webkit-box-align:center;-webkit-align-items:center;align-items:center;text-align:center}.jw-plugin-related .jw-related-header{text-align:left;margin:.75em 1.25em}.jw-plugin-related .jw-related-header .jw-related-close-icon{width:15%;text-align:right;display:inline-block;font-size:1.1em;color:#fff;vertical-align:middle;cursor:pointer}.jw-plugin-related .jw-related-header .jw-related-heading-text{width:85%;display:inline-block;font-size:.65em;letter-spacing:.05em;text-align:left;text-overflow:ellipsis;overflow:hidden;white-space:nowrap;vertical-align:middle}.jw-plugin-related .jw-related-large-layout .jw-related-heading-text{font-size:1em}.jw-plugin-related .jw-related-thumb{cursor:pointer;background-color:#000;display:inline-block;position:relative;background-size:cover;background-position:50% 50%}.jw-plugin-related .jw-related-thumb:after{display:block;padding-bottom:56.25%;content:''}.jw-plugin-related .jw-related-thumb .jw-related-thumb-cover,.jw-plugin-related .jw-related-thumb .jw-related-thumb-title{display:none;position:absolute;top:0;left:0;width:100%;height:100%;pointer-events:none}.jw-plugin-related .jw-related-thumb .jw-related-thumb-title{text-align:left;padding:1em;text-overflow:ellipsis;white-space:nowrap;overflow:hidden}.jw-plugin-related .jw-related-thumb.jw-related-autoplay-target .jw-related-thumb-cover{display:block;background-color:rgba(0,0,0,.25)}.jw-flag-touch .jw-plugin-related .jw-related-thumb .jw-related-thumb-title,.jw-plugin-related .jw-related-thumb:hover .jw-related-thumb-title{display:block}.jw-flag-touch .jw-plugin-related .jw-related-thumb .jw-related-thumb-cover,.jw-plugin-related .jw-related-thumb:hover .jw-related-thumb-cover{display:block;background-color:rgba(0,0,0,.5)}.jw-plugin-related .jw-related-thumb.jw-related-thumb-inactive{display:none}.jw-plugin-related .jw-related-grid{text-align:left}.jw-plugin-related .jw-related-grid.jw-related-grid-center-content{text-align:center}.jw-plugin-related .jw-related-grid-2-1{width:292px;display:inline-block}.jw-plugin-related .jw-related-grid-2-1 .jw-related-thumb{width:146px;max-width:50%}.jw-plugin-related .jw-related-grid-2-1 .jw-related-thumb:nth-child(1n+3){display:none}.jw-plugin-related .jw-related-grid-3-2{width:438px;display:inline-block}.jw-plugin-related .jw-related-grid-3-2 .jw-related-thumb{width:146px;max-width:33.3%;max-height:50%}.jw-plugin-related .jw-related-grid-3-2 .jw-related-thumb:nth-child(1n+7){display:none}.jw-plugin-related .jw-related-grid-4-3{width:100%}.jw-plugin-related .jw-related-grid-4-3 .jw-related-thumb{width:25%;max-height:33.3%}.jw-plugin-related .jw-related-autoplay-svg{position:absolute;width:100%;height:100%}.jw-plugin-related .jw-related-autoplay-frame{fill:none;stroke:#666f82;stroke-width:4%}.jw-plugin-related .jw-related-autoplay-frame-progress{fill:none;stroke:#fff;stroke-width:4%;stroke-dasharray:385%;stroke-dashoffset:385%;-webkit-animation:dash 10s linear forwards;animation:dash 10s linear forwards}@keyframes dash{to{stroke-dashoffset:-.1%}}",""])},function(a,b){a.exports=function(){var a=[];return a.toString=function(){for(var a=[],b=0;b<this.length;b++){var c=this[b];c[2]?a.push("@media "+c[2]+"{"+c[1]+"}"):a.push(c[1])}return a.join("")},a.i=function(b,c){"string"==typeof b&&(b=[[null,b,""]]);for(var d={},e=0;e<this.length;e++){var f=this[e][0];"number"==typeof f&&(d[f]=!0)}for(e=0;e<b.length;e++){var g=b[e];"number"==typeof g[0]&&d[g[0]]||(c&&!g[2]?g[2]=c:c&&(g[2]="("+g[2]+") and ("+c+")"),a.push(g))}},a}},function(a,b,c){function d(a,b){for(var c=0;c<a.length;c++){var d=a[c],e=l[d.id];if(e){e.refs++;for(var f=0;f<e.parts.length;f++)e.parts[f](d.parts[f]);for(;f<d.parts.length;f++)e.parts.push(h(d.parts[f],b))}else{for(var g=[],f=0;f<d.parts.length;f++)g.push(h(d.parts[f],b));l[d.id]={id:d.id,refs:1,parts:g}}}}function e(a){for(var b=[],c={},d=0;d<a.length;d++){var e=a[d],f=e[0],g=e[1],h=e[2],i=e[3],j={css:g,media:h,sourceMap:i};c[f]?c[f].parts.push(j):b.push(c[f]={id:f,parts:[j]})}return b}function f(){var a=document.createElement("style"),b=o();return a.type="text/css",b.appendChild(a),a}function g(){var a=document.createElement("link"),b=o();return a.rel="stylesheet",b.appendChild(a),a}function h(a,b){var c,d,e;if(b.singleton){var h=q++;c=p||(p=f()),d=i.bind(null,c,h,!1),e=i.bind(null,c,h,!0)}else a.sourceMap&&"function"==typeof URL&&"function"==typeof URL.createObjectURL&&"function"==typeof URL.revokeObjectURL&&"function"==typeof Blob&&"function"==typeof btoa?(c=g(),d=k.bind(null,c),e=function(){c.parentNode.removeChild(c),c.href&&URL.revokeObjectURL(c.href)}):(c=f(),d=j.bind(null,c),e=function(){c.parentNode.removeChild(c)});return d(a),function(b){if(b){if(b.css===a.css&&b.media===a.media&&b.sourceMap===a.sourceMap)return;d(a=b)}else e()}}function i(a,b,c,d){var e=c?"":d.css;if(a.styleSheet)a.styleSheet.cssText=r(b,e);else{var f=document.createTextNode(e),g=a.childNodes;g[b]&&a.removeChild(g[b]),g.length?a.insertBefore(f,g[b]):a.appendChild(f)}}function j(a,b){var c=b.css,d=b.media;if(b.sourceMap,d&&a.setAttribute("media",d),a.styleSheet)a.styleSheet.cssText=c;else{for(;a.firstChild;)a.removeChild(a.firstChild);a.appendChild(document.createTextNode(c))}}function k(a,b){var c=b.css,d=(b.media,b.sourceMap);d&&(c+="\n/*# sourceMappingURL=data:application/json;base64,"+btoa(unescape(encodeURIComponent(JSON.stringify(d))))+" */");var e=new Blob([c],{type:"text/css"}),f=a.href;a.href=URL.createObjectURL(e),f&&URL.revokeObjectURL(f)}var l={},m=function(a){var b;return function(){return"undefined"==typeof b&&(b=a.apply(this,arguments)),b}},n=m(function(){return/msie [6-9]\b/.test(window.navigator.userAgent.toLowerCase())}),o=m(function(){return document.head||document.getElementsByTagName("head")[0]}),p=null,q=0;a.exports=function(a,b){b=b||{},"undefined"==typeof b.singleton&&(b.singleton=n());var c=e(a);return d(c,b),function(a){for(var f=[],g=0;g<c.length;g++){var h=c[g],i=l[h.id];i.refs--,f.push(i)}if(a){var j=e(a);d(j,b)}for(var g=0;g<f.length;g++){var i=f[g];if(0===i.refs){for(var k=0;k<i.parts.length;k++)i.parts[k]();delete l[i.id]}}}};var r=function(){var a=[];return function(b,c){return a[b]=c,a.filter(Boolean).join("\n")}}()}]);