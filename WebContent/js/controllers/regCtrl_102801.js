appControllers.controller('regCtl', ['$rootScope', '$scope', '$sce', '$location', '$timeout', 'HRService', function($rootScope, $scope, $sce, $location, $timeout, HRService){
	$regForm = $('#hr-reg-form');
	$scope.hideMsgOverlay = true;
	$regButton = $('.hr-reg-buttom');
	
	var locationMap = {
			'DJU': '同济大学',
			'BJU': '北京大学',
			'XAU': '西安电子科技大学',
			'THU': '清华大学',
			'SHJTU': '上海交通大学',
			'TJC': '天津市大学软件学院',
			'WHTU': '武汉理工大学'
		},
	locationVal = location.hash.match(/location=(.*)/)?location.hash.match(/location=(.*)/)[1]:'';
	$scope.titleInfor = locationMap[locationVal] ? (locationMap[locationVal] + '站 - 注册') : 'IBM校园招聘';
	$scope.regFn = function() {
		if (validateForm() || (locationMap[locationVal] == undefined)) {
			$scope.hideMsgOverlay = false;
			if (locationMap[locationVal] == undefined) {
				msg = '无效地点，<a href="">重新选择</a>'
			}
			$scope.overlayMsg = $sce.trustAsHtml('<i class="fa fa-exclamation-triangle"></i>&nbsp;&nbsp;' + msg);
			$timeout(function() {
				$scope.hideMsgOverlay = true;
				$('#hr-reg').css('opacity', 1);
				$regButton.prop('disabled', false)
			}, 2000)
		} else {
			$('#hr-reg').css('opacity', .2);
			$scope.hideMsgOverlay = false;
			$regButton.prop('disabled', true);
			$scope.overlayMsg = $sce.trustAsHtml('<i class="fa fa-refresh fa-spin"></i>&nbsp;&nbsp;注册中...');
			var checkInVal = 'Y';
			try {
				checkInVal = location.hash.match(/checkIn=(.?)/)[1]
			} catch (e) {
				checkInVal = ''
			}
			HRService.Auth.userReg($regForm.serialize() + "&checkIn=" + checkInVal + "&location=" + locationVal).success(function(data) {
				if (data.flag == '0') {
					switch (data.msg) {
					case 'error_tel_existed':
						$scope.overlayMsg = $sce.trustAsHtml('<i class="fa fa-exclamation-triangle"></i>&nbsp;&nbsp;手机已注册');
						break;
					case 'error_code':
						$scope.overlayMsg = $sce.trustAsHtml('<i class="fa fa-exclamation-triangle"></i>&nbsp;&nbsp;验证码错误');
						break;
					case 'error_blank_fields':
						$scope.overlayMsg = $sce.trustAsHtml('<i class="fa fa-exclamation-triangle"></i>&nbsp;&nbsp;请完善信息');
						break;
					case 'error_email':
						$scope.overlayMsg = $sce.trustAsHtml('<i class="fa fa-exclamation-triangle"></i>&nbsp;&nbsp;请填写正确的邮箱');
						break;
					case 'error_tel':
						$scope.overlayMsg = $sce.trustAsHtml('<i class="fa fa-exclamation-triangle"></i>&nbsp;&nbsp;请填写正确的手机号');
						break;
					}
					$timeout(function() {
						$scope.hideMsgOverlay = true;
						$('#hr-reg').css('opacity', 1);
						$regButton.prop('disabled', false)
					}, 2000)
				} else {
					data.tel = $('input[name=tel]').val();
					HRService.Auth.setUser(data);
					
					// Save data to local storage
					if(window.localStorage){
		    			data.expiriation = +new Date() + 1000 * 60 * 60 * 3; // 3 hrs timeout
		    			data.location = locationVal;
		    			localStorage.setItem('loginfo', JSON.stringify(data));
		    		}
					
		    		$location.path('/');
				}
			}).error(function(err) {
				$scope.overlayMsg = $sce.trustAsHtml('<i class="fa fa-exclamation-triangle"></i>&nbsp;&nbsp;注册失败 :(');
				$timeout(function() {
					$scope.hideMsgOverlay = true
				}, 2000)
			}).
			finally(function() {})
		}
	};
	//validateForm function
	function validateForm() {
		var flag = false;
		var regExpTel = /^1\d{10}$/;
		var regExpEmail = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
		//var regExpEmail = /^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$/i;
		$('input', '#hr-reg-form').each(function(i, n) {
			var label = $(n).data().label;
			if (n.value.trim() === '') {
				msg = '请填写' + label;
				$(n)[0].focus();
				flag = true;
				return false
			} else if (label == '手机' && !regExpTel.test(n.value.trim())) {
				msg = '请填写正确的手机号';
				$(n)[0].focus();
				flag = true;
				return false
			} else if (label == '邮箱' && !regExpEmail.test(n.value.trim())) {
				msg = '请填写正确的邮箱';
				$(n)[0].focus();
				flag = true;
				return false
			} else {
				msg = '';
				flag = false;
				return true
			}
		});
		return flag
	}
}]);