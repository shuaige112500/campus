var host;
if(window.location.hostname == 'localhost' || window.location.hostname == '9.115.26.86'){
	host = 'http://9.115.26.86:9080';
}else if(window.location.hostname == '170.225.225.31'){
	host = 'http://170.225.225.31:9080';
}
$(function() {
	loadAdminComments(location.href.split('location=')[1]);
	$('a#downloadReportDJU').attr('href', host + '/campus/Download?location=DJU');
	$('a#downloadReportBJU').attr('href', host + '/campus/Download?location=BJU');
	$('a#downloadReportXAU').attr('href', host + '/campus/Download?location=XAU');
	$('a#downloadReportTHU').attr('href', host + '/campus/Download?location=THU');
	$('a#downloadReportSHJTU').attr('href', host + '/campus/Download?location=SHJTU');
	$('a#downloadReportTJC').attr('href', host + '/campus/Download?location=TJC');
	$('a#downloadReportWHTU').attr('href', host + '/campus/Download?location=WHTU');
});
function loadAdminComments(locationInfor) {
	$.ajax({ 
		 url: host+"/campus/GetAdminComments" + "?location=" + locationInfor,
		 dataType: "jsonp",
		 success:function(data) {
			console.log("Success");
			data.Comments.forEach(function(item,index){
		    	if(item.choosen == 1){
		    		$('#adminContentQuestion' + index + ' input')[0].checked = true;
		    	}else if(item.choosen == 0){
		    		$('#adminContentQuestion' + index + ' input')[0].checked = false;
		    	}
		    	$('#adminContentQuestion' + index + ' input')[1].value = item.userName;
		    	$('#adminContentQuestion' + index + ' input')[2].value = item.comments;
		    });
		 },
		 error:function(){
			console.log("Error");
		 },
		 complete:function(){
		 	//$('#wrapper').css({"height":"auto"});
		 }
	});
}
function saveAdminComments() {
	var adminComments = "",checkedValue = "";
	for (var i = 0; i < 10; i++) {
		if($('#adminContentQuestion' + i + ' input')[0].checked){
			checkedValue = "1";
		}else{
			checkedValue = "0";
		}
		adminComments += "{'comments':'" + $('#adminContentQuestion' + i + ' input')[2].value + "','from':'" + $('#adminContentQuestion' + i + ' input')[1].value + "','checked':'" + checkedValue + "' }, "
	};
	adminComments = '[' + adminComments.substring(0,adminComments.lastIndexOf(',')) + ']';
	$.ajax({ 
		 url: host+"/campus/SaveAdminComments" + "?location=" + location.href.split('location=')[1],		
		 data: "data=" + adminComments,
		 dataType: "jsonp",		 
		 success:function(json) {
			console.log("Success");
			console.log(json);
		 },
		 error:function(){
			console.log("Error");
		 },
		 complete:function(){
		 }
	});
}