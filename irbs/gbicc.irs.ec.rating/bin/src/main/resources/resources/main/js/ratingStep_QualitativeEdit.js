//评级步骤 ——定性选择页面

//字段定义
var RatingLogFields ={
	grid: [
			{name : "id",					primaryKey:true,		canEdit : false,hidden:true},
			{name : "indexCategory",		title : "类别",			canEdit : false,hidden:true}, 
			{name : "indexName",			title : "定性指标名称",	width : '*',canEdit : false,canSave : false}, 
			{
				name : "paramValue_title",
				title : "选项"
			}, 
			{name : "indexWeight",			title : "权重",			width : 100,hidden:true,canEdit : false,canSave : false}
	]
};

//存储定性指标结果Map 
var noFinParamMap = new Map();

isc.FwRestDataSource.create({
	ID: 'ratingLogByNoFinDS',
	fields: RatingLogFields.grid,
	dataURL: FrameworkUiInterface.webContextPath + '/irs/ratingLog/isc',  	   //默认增删改查的方法
    fetchDataURL: FrameworkUiInterface.webContextPath + '/irs/ratingIndex/getRatingIndexsByStepId'  //用于指定自定义控制器方法属性
});

var paramValueForm;
	
isc.FwListGrid.create({
		ID : "nonFinGrid",
		width : "100%",
		height : "100%",
		border:0,
		groupStartOpen:"all",
	    groupByField: 'indexCategory',
	    autoFetchData : false,
		showHeader:false,
		canPageable:false,
		showRollOver:false,
		canSelectText :false,
		showSelectionCanvas :false,
		dataSource : ratingLogByNoFinDS,
		fields: RatingLogFields.grid,
		initialCriteria : {
            "stepId" : rateWindow.RatingObject.currentStep.id
        },
		getExpansionComponent : function (record) {
			paramValueForm = isc.DynamicForm.create({
					    name: "paramValueForm",
					    width:"100%",
					    titleWidth:200,
					    fields: [
							        {
							        	showTitle:false,
							        	name:record.indexCode,
							        	type:"radioGroup",
							        	width:500,
							        	required:true,
							        	canEdit:rateWindow.editable,
							        	valueMap:eval("("+record.dxText+")"),
							        	value:record.indexValue,
							        	changed:function(form, item, value){
							        		noFinParamMap.set(item.name,[record.paramName ,value]);
							        	}
							        }
					    ]
					});
			return paramValueForm;
	    }
});

//加载数据，展开行，存储定性选项
nonFinGrid.fetchData(nonFinGrid.getCriteria(),function(optional){
	for(var i = 0 ; i<optional.data.length ; i++){
		noFinParamMap.set(optional.data[i].indexCode,[optional.data[i].indexName,optional.data[i].indexValue]);
	}
	nonFinGrid.expandRecords(optional.data);
});

isc.IButton.create({
	ID:'ratingEstimateBtn',
	title: '<b>评级测算</b>',
	autoDraw : false,
	width: 80,
	click:function(){
		debugger
		var requestMapz = rateWindow.requestMap;
    	//检查定性必填项
    	for (var [key, value] of noFinParamMap) {
    		if(!value[1]){
    			isc.say("请选择定性指标："+value[0]);
    			return;
    		}else{
    			if(key.indexOf("TY")>-1){
    				requestMapz[key] = value[1];
    			}
    		}
    	}
    	// 开始测算
    	isc.FwRPCManager.post({
	       	url:FrameworkUiInterface.webContextPath + "/irs/mainRating/startTrial",
	       	urlParameters:{stepId:rateWindow.RatingObject.currentStep.id,mainId:rateWindow.RatingObject.id}, //传入当前步骤ID和定性选择项
	        data:requestMapz,
	        callback: function(response,rawData,request){
	        	trialResultForm.show();
	        	if(!rawData.response.data[0]){
	        		isc.say("已经超过试算次数，不允许继续发起试算");
	        		return;
	        	}else{
	        		rateWindow.trialObject = rawData.response.data[0];
		        	trialResultForm.show();
		        	trialResultForm.setValues(rateWindow.trialObject);
		        	isc.say("试算成功！");
	        	}
	        }
    	});
	}
});

//展示试算结果
isc.FwReadOnlyDynamicForm.create({
	ID : "trialResultForm",
	numCols : 8,
	width : "100%",
	height:30,
	titleAlign:"left",
	fields : [ 
		{
			name : "quanSco",
			title : "试算定量得分",
			type : "float",
			format:"0.00"
		},
		{
			name : "qualSco",
			title : "试算定性得分",
			type : "float",
			format:"0.00"
		},
		{
			name : "sco",
			title : "试算综合得分",
			type : "float",
			format:"0.00"
		},{
			name : "level",
			title : "试算等级",
			type : "text"
		}
	]
});

// 初始化隐藏、点击试算展示
trialResultForm.hide();
// “试算没有开启”或者当前流程状态是“退回重审”或者 “大于试算次数”则隐藏试算按钮

isc.IButton.create({
	ID:'preBtn_NoFin',
	title: '<b>上一步</b>',
	autoDraw : false,
	width: 80,
	click:function(){
		//执行上一步模型计算
		isc.FwRPCManager.get({
	       	url:FrameworkUiInterface.webContextPath + "/irs/ratingMainStep/lastStep",
	       	urlParameters:{stepId:rateWindow.RatingObject.currentStep.id}, //传入当前步骤ID
	        callback: function(response,rawData,request){
	        	rateWindow.RatingObject = rawData.response.data[0];
				rateMenu.refreshStep();
				rateFrameHL.changePage(rateWindow.RatingObject.currentStep.stepResources);
				rightPanel.setTitle(rateWindow.RatingObject.currentStep.stepName);
	        }
    	});
	}
});

isc.IButton.create({
    ID : 'nextBtn_NoFin',
    title: '<b>下一步</b>',
    autoDraw : false,
    width: 80,
    click : function(){
    	var requestMap =rateWindow.requestMap;
    	//检查定性必填项
    	for (var [key, value] of noFinParamMap) {
    		if(!value[1]){
    			isc.say("请选择定性指标："+value[0]);
    			return;
    		}else{
    			requestMap[key] = value[1];
    		}
    	}
    			//执行下一步模型计算
    			isc.FwRPCManager.get({
    		       	url:FrameworkUiInterface.webContextPath + "/irs/ratingMainStep/nextStep",
    		       	urlParameters:{stepId:rateWindow.RatingObject.currentStep.id}, //传入当前步骤ID和定性选择项
    		        callback: function(response,rawData,request){
    		        	rateWindow.RatingObject = rawData.response.data[0];
    		        	rateWindow.requestMap=requestMap;
    					rateMenu.refreshStep();
    					rateFrameHL.changePage(rateWindow.RatingObject.currentStep.stepResources);
    					rightPanel.setTitle(rateWindow.RatingObject.currentStep.stepName);
    		        }
    	    	});
    }
});



isc.HLayout.create({
        ID : "btnHL",
        autoDraw : false,
        width:"100%",
        height:40,
        membersMargin: 10,
        align: "center",
        members:[ratingEstimateBtn,preBtn_NoFin,nextBtn_NoFin]
});
  
if(!rateWindow.editable){
	btnHL.hide();
}

isc.VLayout.create({
    width:"100%",
    autoDraw : false,
    membersMargin: 20,
    members:[
    	nonFinGrid,
    	isc.HLayout.create({
			width : "100%",
			height:'10%',
			members:[
				trialResultForm
			]
		}),
        btnHL
    ]
});
