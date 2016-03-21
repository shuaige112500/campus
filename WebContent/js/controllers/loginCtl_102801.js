appControllers.controller('loginCtl', ['$rootScope', '$scope', '$sce', '$location', '$timeout', '$cookies', 'HRService', function($rootScope, $scope, $sce, $location, $timeout, $cookies, HRService){
	$scope.hideMsgOverlay = true;
	$loginButton = $('.hr-login-buttom');
	
	var telNumber = '',
		loginfoStorageObj = null;
	
	if(window.localStorage && window.localStorage.loginfo){
		loginfoStorageObj = JSON.parse(localStorage.loginfo);
		$scope.tel = loginfoStorageObj.tel;
	}
	
	$scope.$watch('tel', function(newVal, oldVal){
		telNumber = newVal;
	});
	
	if(location.href.indexOf('checkIn=Y&location=') != -1){
		var locationMap = {
				'DJU': {'school':'DJU'},
				'BJU': {'school':'BJU'},
				'XAU': {'school':'XAU'},
				'THU': {'school':'THU'},
				'SHJTU': {'school':'SHJTU'},
				'TJC': {'school':'TJC'},
				'WHTU': {'school':'WHTU'}
			},
		locationVal = location.hash.match(/location=(.*)/)?location.hash.match(/location=(.*)/)[1]:{};
		if(locationMap[locationVal] != undefined) {
			$cookies.locationCookie = locationMap[locationVal].school;
			var regUrl = '#/reg?checkIn=Y&location=' + locationMap[locationVal].school;
			$('#hr-login p a').attr('href',regUrl);
		}else{
			$scope.hideMsgOverlay = false;
			$scope.overlayMsg = $sce.trustAsHtml('<i class="fa fa-exclamation-triangle"></i>&nbsp;&nbsp;活动尚未开始<br/>或重新扫描二维码登录！');
		}
	}else{
		location.hash ="#/login?checkIn=Y&location=" + $cookies.locationCookie;
	}
	$scope.login = function(){
		$('#hr-login').css('opacity', .2);
		$scope.hideMsgOverlay = false;
		$loginButton.prop('disabled', true);
		$scope.overlayMsg = $sce.trustAsHtml('<i class="fa fa-refresh fa-spin"></i>&nbsp;&nbsp;验证中...');
		
		valudateTel(telNumber,locationMap[locationVal]);
	};
	
	function valudateTel(tel,locationVal){
		var regExp = /^1\d{10}$/;
		var checkInVal = '';
		try{
			checkInVal = location.hash.match(/checkIn=(.?)/)[1];
		}catch(e){
			checkInVal = '';
		}
		
	    if(regExp.test(tel) && locationVal != undefined){
	    	HRService.Auth.userLogin({tel: tel, checkIn: checkInVal, location: locationVal.school}).success(function(data){
	    		if(data.flag == '0'){
	    			switch(data.msg){
						case 'error_tel_not_existed':
							$scope.overlayMsg = $sce.trustAsHtml('<i class="fa fa-exclamation-triangle"></i>&nbsp;&nbsp;手机尚未注册');
						break;
					}
	    			$timeout(function(){
	    				$scope.hideMsgOverlay = true;
	    			}, 1000);
	    		}else{
	    			data.tel = tel;
		    		HRService.Auth.setUser(data);
		    		
		    		// Save data to local storage
		    		if(window.localStorage){
		    			data.expiriation = +new Date() + 1000 * 60 * 60 * 3; // 3 hrs timeout
		    			data.location = locationVal.school;
		    			localStorage.setItem('loginfo', JSON.stringify(data));
		    		}
		    		
		    		$location.path('/');
	    		}
	    	}).error(function(err){
	    		HRService.Auth.setUser('');
	    		$scope.overlayMsg = $sce.trustAsHtml('<i class="fa fa-exclamation-triangle"></i>&nbsp;&nbsp;网络错误 :(');
	    	}).finally(function(){
	    		$loginButton.prop('disabled', false);
				$('#hr-login').css('opacity', 1);
			});
	    }else{
	    	$loginButton.prop('disabled', false);
	    	$scope.overlayMsg = $sce.trustAsHtml('<i class="fa fa-exclamation-triangle"></i>&nbsp;&nbsp;格式有误 :(');
	    	setTimeout(function(){
	    		$scope.hideMsgOverlay = true;
	    		$('#hr-login').css('opacity', 1);
	    	}, 1000);
	    }
	}
}]);