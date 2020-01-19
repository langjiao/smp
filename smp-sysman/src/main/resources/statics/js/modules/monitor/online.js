$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'monitor/online/list',
        datatype: "json",
        colModel: [			
			{ label: 'SessionID', name: 'id', width: 100, key: true, hidden:true},
			{ label: '用户名', name: 'username', width: 100 },
			{ label: '登录时间', name: 'startTime', width: 100 },
			{ label: '最后访问时间 ', name: 'endTime', width: 100 },
			{ label: 'IP地址', name: 'host', width: 100 },
			{ label: '地理位置', name: 'address', width: 100 },
			{ label: '状态', name: 'status', width: 100, formatter: function(value, options, row){
				return value == 0 ?
					'<span class="label label-danger">超时</span>' :
					'<span class="label label-success">在线</span>';
			}}
        ],
		viewrecords: true,
        height: 385,//表格高度，可自行调节
        rowNum: 2, //默认一页显示多少条数据，可自行调节
		rowList : [10,20,30,40,50],//翻页控制条中，每页显示记录数可选集合
		styleUI: 'Bootstrap',//主题，这里选用的是 Bootstrap 主题
		loadtext: '信息读取中...',//数据加载时显示的提示信息
        rownumbers: true, //是否显示行号，默认值是 false，不显示
        rownumWidth: 25, //行号列的宽度
        autowidth:true,//宽度是否自适应
        multiselect: true,//是否可以多选
        pager: "#jqGridPager",
        jsonReader : {//读取服务器返回的json数据并解析
            root: "page.list",//返回数组集合
            // page: "page.currPage",//当前页数
            // total: "page.totalPage",//总页数
            // records: "page.totalRecords"//总行数
			page: "page.pageNum",//当前页数
			total: "page.pages",//总页数
			records: "page.total"//总行数
        },
		// page和limit分别代表当前页数和每页数据量，这里是1
        prmNames : {//设置jqGrid将要向Server传递的参数名称
            page:"page",   //表示请求页码的参数名称
            rows:"limit",  //表示请求行数的参数名称
            order: "order" // 表示采用的排序方式的参数名称
        },
		// 数据加载完成并且 DOM 创建完毕之后的回调函数
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});
var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			address: null
		},
		showList: true,
		title: null,
		schedule: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "monitor/online/delete",
                    contentType: "application/json",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 1){
							alert('操作成功', function(index){
								vm.reload();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'beanName': vm.q.address},
                page:page 
            }).trigger("reloadGrid");
		}
	}
});