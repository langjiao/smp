$(function () {
    layui.use('laydate', function(){
        var laydate = layui.laydate;
        var vm  = this;
        laydate.render({
            elem: '#login-time',
            range: true,
            trigger: 'click',
            done: function(value) {
                // vm.loginTime = value;
            }
        });
    });
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/login_log/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', width: 100, key: true, hidden:true },
			{ label: '登录用户', name: 'username', width: 100 },
			{ label: 'IP地址', name: 'ip', width: 100 },
			{ label: '登录地点', name: 'location', width: 100 },
			{ label: '登录时间', name: 'loginTime', width: 100 },
            { label: '登录系统', name: 'system', width: 100 },
			{ label: '浏览器', name: 'browser', width: 100 }
        ],
		viewrecords: true,
        height: 420,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
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
            loginUser: null,
            loginTime: null
		},
        showList: true
	},
	methods: {
		query: function () {
		    var loginTime = $("#login-time").val();
            vm.q.loginTime =loginTime;
            if (loginTime) {
                vm.startLoginTime = loginTime.split(' - ')[0];
                vm.endloginTime = loginTime.split(' - ')[1];
            }
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
                    url: baseURL + "sys/login_log/delete",
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
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{'loginUser': vm.q.loginUser,'loginTime':vm.q.loginTime,'startLoginTime':vm.startLoginTime,'endloginTime':vm.endloginTime},
                page:page
            }).trigger("reloadGrid");
		}
	}
});