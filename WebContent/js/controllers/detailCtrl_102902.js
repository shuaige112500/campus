appControllers.controller('detailCtl', ['$rootScope', '$scope', '$sce', '$timeout', '$routeParams','$cookies', 'HRService', function($rootScope, $scope, $sce, $timeout, $routeParams,$cookies, HRService){
	var topicId = $routeParams.id,
		userInfo = HRService.Auth.getUser();
	
	
	// Show bottom navigation
	$('.navbar-absolute-bottom').show();
	$('#hr-detail, #hr-date').css('opacity', .2);
	$scope.hideMsgOverlay = false;
	$scope.overlayMsg = $sce.trustAsHtml('<i class="fa fa-refresh fa-spin"></i>&nbsp;&nbsp;初始数据中...');
	$scope.submitText = '提交';
	$scope.commentsMsg = '';
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
	};
	var dateList = {
			'DJU': {'date':'9月22日','tips':'同济大学分享会'},
			'BJU': {'date':'9月23日','tips':'北京宣讲会'},
			'XAU': {'date':'9月24日','tips':'西安电子科技大学分享会'},
			'THU': {'date':'10月14日','tips':'宣讲会'},
			'SHJTU': {'date':'10月15日','tips':'宣讲会'},
			'TJC':{'date':'10月26日','tips':'宣讲会'},
			'WHTU':{'date':'10月29日','tips':'宣讲会'}
		};
	$scope.meetingDate = dateList[$cookies.locationCookie].date;
	
	// Escapte HTML tags
	var escapeHTML = function(str){
		var htmlEscapes = {
		  '&': '&amp;',
		  '<': '&lt;',
		  '>': '&gt;',
		  '"': '&quot;',
		  "'": '&#x27;',
		  '/': '&#x2F;',
		  '\n': '<br />',
		  '\r': '<br />'
		};
		
		var htmlEscaper = /[&<>"'\/\n\r]/g;
		
		return str.replace(htmlEscaper, function(match) {
		    return htmlEscapes[match];
		});
	};
	
	$.extend({
		popProfile: function(){
			$('#hr-speaker-info-overlay').show();
		},
		
		hideProfile: function(){
			$('#hr-speaker-info-overlay').hide();
		}
	});
	
	//console.info('UserInfo: ', userInfo);
	HRService.loadDetail(topicId,$cookies.locationCookie).success(function(data){
		var topicObj = data.result.topic,
			commentsObj = data.result.comments;
		
		$scope.hideBand = true;
		$('.hr-comments-item').remove();
		
		$scope.topic = {
			pageTitle: topicObj.topicName,
			speaker: topicObj.speakerName,
			topicDesc: topicObj.topicDesc,
			rating: topicObj.rating,
			hadRated: topicObj.isRating,
			speakerDesc: topicObj.speakerDesc,
			ID: '',
			userName: '',
			userAvatar: '',
			postComment: function(){
				$scope.commentsMsg = '';
				if(valideForm('hr-detail-comments-area')){
					$scope.submitText = '提交中';
					var commentsNode = $('#hr-detail-comments-area'),
						submitBtnNode = $('.hr-detail-buttom');
					submitBtnNode.prop('disabled', true);
					HRService.postComment({topicId: topicId, comments: commentsNode.val()}).success(function(){
						$('.hr-color-band').after('<article class="hr-comments-item"><p><span class="hr-profile">'+ (userInfo.uname || '未知') +'</span><span style="word-wrap: break-word;">'+ escapeHTML(commentsNode.val()) +'</span></p></article>');
						
						commentsNode.val('');
						$scope.submitText = '提交';
						$scope.commentsCount += 1;
						$scope.commentsMsg = $sce.trustAsHtml('<span style="color: green;"><i class="fa fa-check"></i> 感谢您的有力评论 :)</span>');
					}).error(function(){
						$scope.commentsMsg = $sce.trustAsHtml('<span style="color: darkorange;"><i class="fa fa-exclamation-triangle"></i> 评论失败 :(</span>');
					}).finally(function(){
						submitBtnNode.prop('disabled', false);
					});
				}
				
				return false;
			},
			rate: function(){
				if($scope.topic.hadRated == '1'){
					$scope.topic.hadRated = '0';
					$scope.topic.rating -= 1;
				}else{
					$scope.topic.hadRated = '1';
					$scope.topic.rating += 1;
				}
				
				HRService.rate({topicID: topicId, checked: ($scope.topic.hadRated == '1') ? 'Y' : 'N'}).success(function(){
					
				}).error(function(){
					
				});
			}
		};
		
		$scope.$watch('commentsArea', function(newVal, oldVal){
			if(newVal && newVal.length > 140){
				$('#hr-detail-comments-area').css('borderColor', 'red');
			}else{
				$('#hr-detail-comments-area').css('borderColor', '#C7C7C7');
			}
		});
		
		// load comments
		var commentsHTML = '';
		$scope.commentsCount = commentsObj.length;
		if(commentsObj.length > 0){
			$scope.hideBand = false;
			$.each(commentsObj, function(i, comment){
				var name = comment.userName;
				
				commentsHTML += '<article class="hr-comments-item"><p><span class="hr-profile">'+ name +'</span><span style="word-wrap: break-word;">'+ escapeHTML(comment.comments) +'</span></p></article>';
			});
			$('.hr-color-band').after(commentsHTML);
		}
		$('#hr-detail, #hr-date').css('opacity', 1);
		
		// validate from comment value
		function valideForm(id){
			var commentsVal = $('#' + id).val();
			
			if(commentsVal.length == 0 || commentsVal.length > 140){
				
				if(commentsVal.length > 140){
					$scope.commentsMsg = $sce.trustAsHtml('<span style="color: darkorange;"><i class="fa fa-exclamation-triangle"></i> 控制140字 :(</span>');
				}else{
					$scope.commentsMsg = $sce.trustAsHtml('<span style="color: darkorange;"><i class="fa fa-exclamation-triangle"></i> 请写入您的评论 :(</span>');
				}
				
				return false;
			}else{
				return true;
			};
		}
		
		$scope.hideMsgOverlay = true;
	}).error(function(err){
		$scope.overlayMsg = $sce.trustAsHtml('<span style="color: #EFFF78;"><i class="fa fa-exclamation-triangle"></i>&nbsp;&nbsp;加载出错！</span>');
		$scope.hideMsgOverlay = false;
	});
}]);