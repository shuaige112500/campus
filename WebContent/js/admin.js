var alltopics;
var pathstr = "../uploads/";
var localhost = "http://localhost:9080";
var host;
if(window.location.hostname == 'localhost' || window.location.hostname == '9.115.26.86'){
	host = 'http://9.115.26.86:9080';
}else if(window.location.hostname == '170.225.225.31'){
	host = 'http://170.225.225.31:9080';
}
Array.prototype.unique=function(){
    var newArr=[],obj={};
    for(var i=0,len=this.length;i<len;i++){
        if(!obj[this[i]]){ 
            newArr.push(this[i]);
            obj[this[i]]=true;
        }
    }
    return newArr;
}

$(function() {
	$('a#downloadReportDJU').attr('href', host + '/campus/Download?location=DJU');
	$('a#downloadReportBJU').attr('href', host + '/campus/Download?location=BJU');
	$('a#downloadReportXAU').attr('href', host + '/campus/Download?location=XAU');
	$('a#downloadReportTHU').attr('href', host + '/campus/Download?location=THU');
	$('a#downloadReportSHJTU').attr('href', host + '/campus/Download?location=SHJTU');
	$('a#downloadReportTJC').attr('href', host + '/campus/Download?location=TJC');
	$('a#downloadReportWHTU').attr('href', host + '/campus/Download?location=WHTU');
	$('#commentsTable,#submitButton').hide();
	loadComments();
});

// load all comments and make checked when choosen
function loadComments() {
	var locationInfor = location.href.split('location=')[1];
	if(!(locationInfor == undefined)){
		$.ajax({ 
			 url: host+"/campus/GetComment" + "?location=" + locationInfor,
			 dataType: "jsonp",
			 success:function(allData) {
				console.log("Success");
				var authorSelect = [];
				//var authorSelectFilterData = {}; authorSelectFilterData.Comments = [];
				allData.Comments.forEach(function(item,index){
					authorSelect.push(item.speakerName);
				});
				authorSelect = authorSelect.unique();
				initDataTable(allData);


				var authorSelectObj = $('#authorSelect');
				authorSelectObj.html("");
				authorSelectObj[0].options.add(new Option("All","all"));
				authorSelect.forEach(function(item,index){
					authorSelectObj[0].options.add(new Option(item,index));
				});	

				authorSelectObj.change(function(){
					var selectedOptionVal = $(this).children('option:selected').text();
					if(selectedOptionVal == 'All'){
						$.ajax({ 
							url: host+"/campus/GetComment" + "?location=" + locationInfor,
							dataType: "jsonp",
							success:function(newData) {
								initDataTable(newData);
							},
							error:function(){
								console.log("Error");
							}
						});
					}else{
						$.ajax({ 
							url: host+"/campus/GetCommentsBySpeaker" + "?location=" + locationInfor + "&speaker=" + selectedOptionVal,
							dataType: "jsonp",
							success:function(authorSelectFilterData) {
								initDataTable(authorSelectFilterData);
							},
							error:function(){
								console.log("Error");
							}
						});
					}
				});
			 },
			 error:function(){
				console.log("Error");
			 },
			 complete:function(){
			 	$('#wrapper').css({"height":"auto"});
			 	$('#commentsTable,#submitButton').show();
			 }
		});
	}
}
// select authorSelect function

// init data table
function initDataTable(filter){
	$("tbody").html("");
	for(var i=0; i<filter.Comments.length; i++) {
		var tr = $("<tr></tr>");
		var td1 = $("<td><div class=\"checkbox\"><label><input type=\"checkbox\" value=\"" + 
						filter.Comments[i].choosen + "\" id=\"" + filter.Comments[i].cid + "\"></label></div></td>");
		var td2 = $("<td style='word-break:break-all;'>"+filter.Comments[i].comments+"</td>");
		var td3 = $("<td>"+filter.Comments[i].userName+"</td>");
		var td4 = $("<td>"+filter.Comments[i].topicName+"</td>");
		var td5 = $("<td>"+filter.Comments[i].speakerName+"</td>");
		tr.append(td1);
		tr.append(td2);
		tr.append(td3);
		tr.append(td4);	
		tr.append(td5);	
		$("tbody").append(tr);
		// adjust checkbox status
		cboxRefresh();
	}
}

// checkbox checked when choosen
function cboxRefresh() {
	var cbox = $("input[type=checkbox]");
	for(var i=0; i<cbox.length; i++) {
		if(cbox[i].value == "1") {
			$(cbox[i]).attr("checked", true);
		} else {
			$(cbox[i]).attr("checked", false);
		}
	}
}

// update comments to choosen
function saveQuestion() {
	console.log(location.hash);
	var locationInfor = "?location=" + location.hash.split('#')[1];
	var cbox = $("input[type=checkbox]");
	var jsonstr = "[";
	for(var i=0; i<cbox.length; i++) {
		jsonstr += "{\"cid\":\""+cbox[i].id+"\", \"choosen\": \"";
		if($(cbox[i]).is(":checked")) {
			jsonstr += "1\"},";
		} else {
			jsonstr += "0\"},";
		}
	}
	var last = jsonstr.lastIndexOf(",");
	jsonstr = jsonstr.substring(0,last);
	jsonstr += "]";

	$.ajax({ 
		 url: host+"/campus/SaveQuestion" + locationInfor,
		 dataType: "jsonp",
		 data: "question="+jsonstr,
		 success:function() {
			console.log("Question saved.");
		 },
		 error:function(){
			console.log("Error");
		 }
	 });
}