var appControllers = angular.module('appControllers', []);
var appServices = angular.module('appServices', []);
var msg = '';
var app = angular.module('HR', ['appControllers', 'appServices']);
appControllers.controller('preregCtrl', ['$rootScope', '$scope', '$sce', '$location', '$timeout', '$location', 'HRService', function($rootScope, $scope, $sce, $location, $timeout, $location, HRService) {
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
	locationVal = location.search.split('=')[1];
	$scope.titleInfor = locationMap[locationVal] ? ('IBM校园招聘-' + locationMap[locationVal] + '站') : 'IBM校园招聘';
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
			var checkInVal = '';
			try {
				checkInVal = location.search.match(/checkIn=(.?)/)[1]
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
					$('#hr-msg-overlay').css({
						'maxWidth': '80%',
						'top': '30%'
					});
					var _roomAndDate = {
						'DJU': {'date':'活动时间：2015年9月22日','room':'活动地点：同济大学 (嘉定校区济事楼434)','title':'IBM 学长学姐分享会'},
						'BJU': {'date':'活动时间：2015年9月23日，18:30-20:30','room':'活动地点：北京大学英杰交流中心阳光厅','title':'IBM 校园宣讲会'},
						'XAU': {'date':'活动时间：2015年9月24日','room':'活动地点：西安电子科技大学','title':'IBM 学长学姐分享会'},
						'THU': {'date':'活动时间：2015年10月14日','room':'活动地点：清华大学','title':'IBM学长学姐分享会'},
						'SHJTU': {'date':'活动时间：2015年10月15日，19:00-21:00','room':'活动地点：光彪楼一楼多功能厅(闵行校区)','title':'IBM 校园宣讲会'},
						'WHTU': {'date':'活动时间：2015年10月29日，18:30-20:30','room':'活动地点：武汉市洪山区珞狮路165就业大厦二楼2号厅','title':'IBM 校园宣讲会'}
					};
					$scope.overlayMsg = $sce.trustAsHtml('注册成功啦！<br /><br /><strong style="color: yellow;">欢迎您届时参加 '+ _roomAndDate[locationVal].title +'<br />'+ _roomAndDate[locationVal].date  +'<br />'+ _roomAndDate[locationVal].room +'</strong><br /><br />我们等你来！<br /><br /><button style="color:#00a6c8; font-size:14px;" onclick="window.location.href=\'navigation.html\'">点击参与更多活动!</button>');
					$scope.hideMsgOverlay = false;
					$('#hr-reg').css('opacity', .2)
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

	function validateForm() {
		var flag = false;
		var regExpTel = /^1\d{10}$/;
		var regExpEmail = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
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