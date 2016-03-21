appControllers.controller('homeCtl', ['$rootScope','$scope','$sce','$timeout','$cookies','HRService',function($rootScope, $scope, $sce, $timeout, $cookies, HRService){
	var scrollItems = [];
	// Show bottom navigation
	$('.navbar-absolute-bottom').show();
	
	$scope.hideMsgOverlay = false;
	$scope.overlayMsg = $sce.trustAsHtml('<i class="fa fa-refresh fa-spin"></i>&nbsp;&nbsp;数据加载中');
	$scope.speakerMap = {
		'Xiao Wei Shen': 'http://gcghrcampus.b0.upaiyun.com/static/images/XiaoWeiShen.jpg',
		'Jessie Hu': 'http://gcghrcampus.b0.upaiyun.com/static/images/JessieHu.jpg',
		'Kim Xu': 'http://gcghrcampus.b0.upaiyun.com/static/images/KimXu.jpg',
		'陆晔': 'http://gcghrcampus.b0.upaiyun.com/static/images/LUYE.jpg',
		'王岗': 'http://gcghrcampus.b0.upaiyun.com/static/images/WangGang.jpg',
		'Mary Tang': 'http://gcghrcampus.b0.upaiyun.com/static/images/Mary Tang.jpg',
		'Hai Ju': 'http://gcghrcampus.b0.upaiyun.com/static/images/Hai Ju.jpg',
		'Jun Zheng': 'http://gcghrcampus.b0.upaiyun.com/static/images/James Zheng.jpg',
		'Jason FAN': 'http://gcghrcampus.b0.upaiyun.com/static/images/Jason FAN.jpg',
		'Lisa ZHAI': 'http://gcghrcampus.b0.upaiyun.com/static/images/Lisa ZHAI.jpg',
		'Gang LIN': 'http://gcghrcampus.b0.upaiyun.com/static/images/Gang LIN.jpg',
		'IBMer': 'http://gcghrcampus.b0.upaiyun.com/static/images/IBMer.jpg',
		'曾斌': 'http://gcghrcampus.b0.upaiyun.com/static/images/zengbin.jpg',
		'李秀慧': 'http://gcghrcampus.b0.upaiyun.com/static/images/xiuhuili.jpg'
	}
	var dateList = {
		'DJU': {'date':'9月22日','tips':'同济大学分享会'},
		'BJU': {'date':'9月23日','tips':'北京宣讲会'},
		'XAU': {'date':'9月24日','tips':'西安电子科技大学分享会'},
		'THU': {'date':'10月14日','tips':'宣讲会'},
		'SHJTU': {'date':'10月15日','tips':'宣讲会'},
		'TJC':{'date':'10月26日','tips':'宣讲会'},
		'WHTU':{'date':'10月29日','tips':'宣讲会'}
	};
	HRService.loadTopicList($cookies.locationCookie).success(function(data){
		$scope.hideMsgOverlay = true;
		$scope.meetingDate = dateList[$cookies.locationCookie].date;
		$scope.tips = dateList[$cookies.locationCookie].tips;
		$scope.scrollItems = data.topics;		
		$scope.rate = function(e,i){
			e.stopPropagation();
   			e.preventDefault();
   			if(i.item.isRating == "1"){
				i.item.isRating = "0";
				i.item.rating -= 1;
			}else{
				i.item.isRating = "1";				
				i.item.rating += 1;
			}
			HRService.rate({
				topicID: i.item.topicID, 
				checked: (i.item.isRating == "1") ? 'Y' : 'N'
			}).success(function(){}).error(function(){});
		};
	}).error(function(error){
		console.log(error);
	}).finally(function(){
		if($cookies.locationCookie == 'BJU' || $cookies.locationCookie == 'SHJTU')
		{
			$scope.BJUdisplay = false;
			}
		else{
			$scope.BJUdisplay = true;
			}
		$timeout(function(){
			$scope.hideMsgOverlay = true;
		}, 1000);
	});
}]);