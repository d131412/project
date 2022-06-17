/**
 * @打开评级试算页
 * @returns
 */
var rating=""; //上一步下一步的Id传递
var statusAll="";
var ratingIDGlobal="";
var ratingStep = "";
function showProgress(ratingId,cus,tem,status){
	rating=ratingId;
	ratingIDGlobal=rating;
	custNo=cus;
	tempType=tem;
	statusAll=status;
	//initSteps(rating);
	//$('.chosen-select').chosen({width: "100%"});
	if((rating)&&(tem)&&tem!="null"&&rating!="null"){
		 initSteps(rating);    	 
	}else{
		swal({
	        title: "",
	        text: "<div class='row'><div class='col-lg-12'><form class='form-horizontal' id='modelChange'><div class='form-group'><label class='col-sm-4 control-label' style='padding: 0;'>主体评级模版</label><div class='col-sm-6'><select class='chosen-select' id='' name='modelSteps' style='width: 100%;'><option value='UNIVERSAL_S_SODEL2'>生产型</option><option value='UNIVERSAL_S_SODEL'>服务型</option><option value='NEW_BUILD'>新建型</option></select></div></div></form></div></div>",
	        html: true,
	        //type: "prompt",
	        showCancelButton: true,
	        cancelButtonColor: "#ed5565",
	        confirmButtonColor: "#1c84c6",
	        cancelButtonText: "取消",
	        confirmButtonText: "确定",
	        closeOnConfirm: false
	    }, function () {
	  	var obj = $("#modelChange").serializeObject();
	  	if(obj.modelSteps){
	  	  swal.close();
	  		tempType=obj.modelSteps;
		  	  initSteps(rating);
	  	}else{
	  		 swal.close();
	  	}
});
		
	}
}

/**
 * @初始化评级步骤菜单
 * @returns
 */
function initSteps(rating){
	$.ajax({
		url : "/irs/mainRating/getRatingById",
		data : {
			ratingId : rating
		},
		async : false,
		success : function(result) {
			if(!result){
				$("#myModal").modal("hide");
				$.ajax({
					url : "/irs/ratingMainStep/createRating",
					data : {
						bpCode : custNo,
						tempType:tempType
					},
					async : false,
					success : function(ratingIdRe) {
						ratingIDGlobal=ratingIdRe;
						initSteps(ratingIdRe);
					}})
			}else{
				$("#myModal").modal();
				$("#dingliang").show();
				$("#dingxing").hide();
				$("#report").hide();
				$("#footer1").show();
				$("#footer2").hide();
				$("#footer3").hide();
				$("#scoreInfo").hide();
				if(statusAll!='000'&&statusAll!='010'&&statusAll!='020'){
					$("#btn_next2").attr("disabled", true);
				}
				$("#dlTxt").css({"background-color":"rgb(21, 127, 204)","color":"#fff"});
				$("#dxTxt").css({"background-color":"#dadada","color":"#9e9e9e"});
				$("#reportTxt").css({"background-color":"#dadada","color":"#9e9e9e"});
				quantitative(result.steps[0].id);
				qualitative(result.steps[1].id);
				ratingStep = result.steps[1].id;
			}
		}
	})
}
/**
 * @加载定量列表页
 * @returns
 */
function quantitative(steps){
	$.ajax({
		url : "/irs/ratingIndex/getRatingIndexsByStepId",
		data : {
			stepId : steps
		},
		async : false,
		success : function(result) {
			$("#dlListof").html("");
			var html="<tr class='table-head'><td width='50%'>科目名称</td><td width='50%'>值</td></tr>";
				html+="<tr align='left' class='parent' id='row1'><td colspan='3' class='index-name'><img src='/image/group_opened.png' />"+result.response.data[0].indexCategory+"</td></tr>";
				$.each(result.response.data,function(i,row){
					var valueIndex = "";
					if(row.indexValue!=null){
						valueIndex=row.indexValue;
					}
				html+="<tr class='child_row1'><td>"+row.indexName+"</td><td><input type='text' name='"+row.indexCode+"' value='"+valueIndex+"' /></td></tr>";
				})
			$("#dlListof").html(html);
			
		}
	})
	
}

function openOrClose(indexCategory,parentId){
	var display = $("#"+indexCategory+"div").css("display");
	if(display=="none"){
		$("#"+parentId).find("img").attr("src","/image/group_opened.png");
		$("#"+indexCategory+"div").css("display","block");
	}else{
		$("#"+parentId).find("img").attr("src","/image/group_closed.png");
		$("#"+indexCategory+"div").css("display","none");
	}
}

/**
 * @加载定量列表页
 * @returns
 */
function qualitative(steps){
	$.ajax({
		url : "/irs/ratingIndex/getRatingIndexsByStepId",
		data : {
			stepId : steps
		},
		async : false,
		success : function(result) {
			$("#dxAnalysis").html("");
			$("#dxAnalysis").append("定性分析");
			var html="";
			var lastParentId="";
			var sn = 1;
				$.each(result.response.data,function(i,row){
					if(lastParentId==row.parentId){
						html+="<div class='child_row1'><div id='"+row.indexName+"Div' class='options-directory'>"+sn+"、"+row.indexName+"</div>";
						var dxText=JSON.parse(row.dxText);
						for (var key in dxText){
							if(row.indexValue==key){
								html+="<label><input type='radio' name='"+row.indexCode+"' value='"+key+"' checked/>"+dxText[key]+"</label>";
							}else{
								html+="<label><input type='radio' name='"+row.indexCode+"' value='"+key+"'/>"+dxText[key]+"</label>";
							}
					    }
					}else{
						if(html!=''){
							html += "</div>";
							$("#dxAnalysis").append(html);
							html = '';
							sn = 1;
						}
						html+="<div class='parent index-name' onclick='openOrClose(\""+row.indexCategory+"\",\""+row.parentId+"\")' id='"+row.parentId+"'><img src='/image/group_opened.png' />"+row.indexCategory+"</div>";
						html+="<div id='"+row.indexCategory+"div'><div class='child_row1'><div id='"+row.indexName+"Div' class='options-directory' >"+sn+"、"+row.indexName+"</div>";
						var dxText=JSON.parse(row.dxText);
						for (var key in dxText){
							if(row.indexValue==key){
								html+="<label><input type='radio' name='"+row.indexCode+"' value='"+key+"' checked/>"+dxText[key]+"</label>";
							}else{
								html+="<label><input type='radio' name='"+row.indexCode+"' value='"+key+"' />"+dxText[key]+"</label>";
							}
					    }
					}
					
					if(i==result.response.data.length-1){
						$("#dxAnalysis").append(html);
						html = '';
						sn = 1;
					}
					lastParentId=row.parentId;
					sn+=1;
				})
		}
	})
}

/**
 * @评级试算按钮
 * @returns
 */
$("#btn_calculate").click(function(){
	var calculateFlag = true;
	
	$.ajax({
		url : "/irs/ratingIndex/getRatingIndexsByStepId",
		data : {
			stepId : ratingStep
		},
		async : false,
		success : function(result) {
			$.each(result.response.data,function(i,row){
				var value = $("#dxAnalysis").find('input:radio[name='+row.indexCode+']:checked').val();
				
				if(value==null){
					if($("#"+row.indexName+"Div").find("span").length==0){
						$("#"+row.indexName+"Div").append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style='color:red' class='glyphicon glyphicon-exclamation-sign'>请选择"+row.indexName+"</span>");
					}
					if(calculateFlag){
						calculateFlag = false;
					}
				}else{
					$("#"+row.indexName+"Div").find("span").remove();
				}
			})
		}
	})
	if(!calculateFlag){
		swal({
            title: "提示",
            text: "计算参数异常！",
            type: "info"
        });
		return false;
	}
	var map = {};
	map=$("#dxDl").serializeObject();
	$.ajax({
		url : "/irs/mainRating/startTrial?type="+tempType+"&mainId="+ratingIDGlobal,
		type:'GET',
		data:{"map":JSON.stringify(map)},
		dataType:'json',
		contentType:"application/json",
		async : false,
		success : function(result) {
			debugger
			if(result){
				$("#scoreInfo").css("display","inline-block");
				$("#btn_next2").attr("disabled", false);
				
				var dlScore=result.response.data[0].quanSco*1.0;
				dlScore=dlScore.toFixed(2); 
				$("#dlScore").text(dlScore);
				
				var dxScore=result.response.data[0].qualSco*1.0;
				dxScore=dxScore.toFixed(2);
				$("#dxScore").text(dxScore);
				
				var sumScore=result.response.data[0].sco*1.0;
				sumScore=sumScore.toFixed(2);
				$("#sumScore").text(sumScore);
				$("#tab_sheet_1").attr("src", "/irs/DebtRatingResultsController/debtRatingReport?custNo="+ratingIDGlobal);
			    $("#tab_sheet_2").attr("src", "/irs/DebtRatingResultsController/debtRatingDetail?custNo="+ratingIDGlobal);
				//$("#tab_sheet_3").attr("src", "/irs/mainRating/scoringDetails?custNo="+result.response.data[0].id)
			}else{
				swal({
	                title: "提示",
	                text: "计算参数异常！",
	                type: "info"
	            });
			}
		},error:function(){
			swal({
                title: "提示",
                text: "计算失败，程序发生异常！",
                type: "info"
            });
		}
	})
	
})
/**
 * @将form变单转化为json对象
 */
$.fn.serializeObject = function() {  
    var o = {};  
    var a = this.serializeArray();  
    $.each(a, function() {  
        if (o[this.name]) {  
            if (!o[this.name].push) {  
                o[this.name] = [ o[this.name] ];  
            }  
            o[this.name].push(this.value || '');  
        } else {  
            o[this.name] = this.value || '';  
        }  
    });  
    return o;  
}




$("#btn_next2").click(function(){
	if(statusAll!='000'&&statusAll!='010'&&statusAll!='020'){
		swal({
	        title: "",
	        text: "定量、定性指标是否复核完成？",
	        type: "warning",
	        showCancelButton: true,
	        cancelButtonColor: "#ed5565",
	        confirmButtonColor: "#1c84c6",
	        cancelButtonText: "否",
	        confirmButtonText: "是",
	        closeOnConfirm: false
	    }, function () {
	  	  swal.close();
	  	  $("#dingxing").hide();
	  	  $("#dingliang").hide();
	  	  $("#report").show();
	  	  $("#footer1").hide();
	  	  $("#footer2").hide();
	  	  $("#footer3").show();
	  	  $("#dlTxt").css({"background-color":"#dadada","color":"#9e9e9e"});
	  	  $("#dxTxt").css({"background-color":"#dadada","color":"#9e9e9e"});
	  	  $("#reportTxt").css({"background-color":"rgb(21, 127, 204)","color":"#fff"});    	  
	    });
	}else{
		$("#dingxing").hide();
	  	  $("#dingliang").hide();
	  	  $("#report").show();
	  	  $("#footer1").hide();
	  	  $("#footer2").hide();
	  	  $("#footer3").show();
	  	  $("#dlTxt").css({"background-color":"#dadada","color":"#9e9e9e"});
	  	  $("#dxTxt").css({"background-color":"#dadada","color":"#9e9e9e"});
	  	  $("#reportTxt").css({"background-color":"rgb(21, 127, 204)","color":"#fff"});
		$("#tab_sheet_1").attr("src", "/irs/DebtRatingResultsController/debtRatingReport?custNo="+ratingIDGlobal);
	    $("#tab_sheet_2").attr("src", "/irs/DebtRatingResultsController/debtRatingDetail?custNo="+ratingIDGlobal);
		//$("#tab_sheet_3").attr("src", "/irs/mainRating/scoringDetails?custNo="+rating)
	}
	  
})
