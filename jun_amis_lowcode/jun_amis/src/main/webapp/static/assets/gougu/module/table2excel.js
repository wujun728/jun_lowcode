layui.define([], function (exports) {
	var MOD_NAME = 'table2excel';
	var modFile = layui.cache.modules['table2excel'];
	var modPath = modFile.substr(0, modFile.lastIndexOf('.'));
	var plugin_filename = 'table2excel.js'
	var settings = {
		title: '数据表格',
		url: '',
		where:function(){
			return {};
		}
	};
	
	function tableTOexcel(opts){
		let msg;
		let where = opts.where();
		if(!where['limit'] ){
			where['limit'] = 999999;
		}
		$.ajax({
			url:opts.url,
			data:where,
			beforeSend:function(){
				msg = layer.msg('数据导出中...',{
				  time: 20000
				});
				$('.table-excel-out').remove();
			},
			success:function(res){
				if(res.code==0){
					var item=res.data,th=['<tr>'],headArray = opts.cols;
					if(item.length==0){
						layer.msg('查询无数据，无法导出');
						return false;
					}
					for(var a=0;a<headArray.length;a++){
						th.push('<th>'+headArray[a].title+'</th>');
					}
					th.push('</tr>');
					
					for(var m=0;m<item.length;m++){
						th.push('<tr>');
						for(var n=0;n<headArray.length;n++){
							let resetData = headArray[n]['reset'];
							if(resetData && typeof resetData === "function"){
								th.push('<td>'+resetData(item[m])+'</td>');
							}
							else{
								th.push('<td>'+item[m][headArray[n]['field']]+'</td>');
							}
						}
						th.push('</tr>');
					}
					
					var tableId = new Date().getTime();
					$('body').append('<div class="table-excel-out" style="display:none"><table id="tableOut'+tableId+'">'+th.join('')+'</table></div>');
					
					$("#tableOut"+tableId).table2excel({
						name: opts.title,
						filename: opts.title + tableId + ".xls",
						exclude: ".noExl",
						exclude_img: false,
						exclude_links: false,
						exclude_inputs: false
					});
				}
			},
			complete: function () {
				layer.close(msg);
			},
		})
	}
	
	var table2excel = {
		render: function (btnId, options) {
			loadScript();
			var opts = $.extend({}, settings, options);
			$('body').on('click','#'+btnId,function(){
				tableTOexcel(opts);				
			})	
		}
	}	
	
	function loadScript() {
		if (typeof $.fn['table2excel'] == 'undefined') {
			$.ajax({
				url: modPath + '/' + plugin_filename,
				dataType: 'script',
				cache: true,
				async: false,
			});
		}
	}
	exports(MOD_NAME, table2excel);
});
