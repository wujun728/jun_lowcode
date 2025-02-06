layui.define([], function (exports) {
	var MOD_NAME = 'editormd';
	var modFile = layui.cache.modules['editormd'];
	var modPath = modFile.substr(0, modFile.lastIndexOf('.'));
	var plugin_filename = 'editormd.min.js'//插件路径
	var settings = {
		markdown: '',
		path: modPath + '/lib/',
		placeholder: "请输入内容...",
		height: window.innerHeight / 2,
		htmlDecode: "style,script,iframe",
		imageUpload: true,
		imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
		imageUploadURL: "/admin/api/upload/sourse/editormd",
		saveHTMLToTextarea: true,//保存html到textarea
		toolbarIcons: function () {
			return [
				"undo", "redo", "|",
				"bold", "del", "italic", "quote", "ucwords", "uppercase", "lowercase", "|",
				"h1", "h2", "h3", "h4", "h5", "h6", "|",
				"list-ul", "list-ol", "hr", "|",
				"link", "reference-link", "image", "code", "preformatted-text", "code-block", "table", "datetime", "html-entities", "pagebreak", "|",
				"goto-line", "watch", "preview", "fullscreen", "clear", "search", "|",
				"help"
			];
		},
		lang: {
			dialog: {
				preformattedText: { placeholder: "此处编写代码..." },
				codeBlock: { placeholder: "此处编写代码...." }
			}
		},
		onfullscreen: function () {
			this.editor.css("z-index", 120);
		},
		onfullscreenExit: function () {
			this.editor.css({
				zIndex: 10,
				border: "none",
			});
			this.resize();
		},
		onload: function () {
			initPasteDragImg(this); //必须
		}
	};
	var editor = {
		render: function (editorId, options) {
			loadScript();
			var opts = $.extend({}, settings, options);
			return editormd(editorId, opts);
		}
	}
	function loadScript() {
		if (typeof editormd == 'undefined') {
			$.ajax({ //获取插件
				url: modPath + '/' + plugin_filename,
				dataType: 'script',
				cache: true,
				async: false,
			});
			$.ajax({ //获取插件
				url: modPath + '/paste.upload.img.js',
				dataType: 'script',
				cache: true,
				async: false,
			});
			layui.link(modPath + '/css/editormd.min.css');
			layui.link(modPath + '/css/editormd.preview.min.css');
		}
	}
	exports(MOD_NAME, editor);
});
