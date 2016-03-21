appServices.factory('HRService', function($http, $q, $cookies){
	/**
	 * @file The global server of GCG Campus application, more API function refer right navigation bar.
	 * @author XIAO YU WANG <wxyu@cn.ibm.com>
	 * @author MING YUAN DU <dumingy@cn.ibm.com>
	 * @version 1.0
	 * */
	var host,
		userInfo;
	if(window.location.hostname == 'localhost' || window.location.hostname == '9.115.26.86'){
		host = 'http://9.115.26.86:9080';
	}else if(window.location.hostname == '170.225.225.31'){
		host = 'http://170.225.225.31:9080';
	}
	
	return {
		/**
		 * Returns $http x-domain service - get lists of topic by location
		 * @author XIAO YU WANG <wxyu@cn.ibm.com>
		 * @param {String} locationInfor The code of each school: e.g. 'BJU'
		 * @example <caption>1. Use the hardcode:</caption>
		 * // Get the topic list of BJU
		 * HRService.loadTopicList('BJU');
		 * @example <caption>2. Get location from cookie:</caption>
		 * // Location from $cookie
		 * HRService.loadTopicList($cookies.locationCookie);
		 * @returns {Object}
		 */
		loadTopicList: function(locationInfor){
			return $http.jsonp(host + '/campus/GetTopicList?location='+ locationInfor +'&callback=JSON_CALLBACK');
		},

		/**
		 * Returns $http x-domain service - get topic details by id and location
		 * @author XIAO YU WANG <wxyu@cn.ibm.com>
		 * @param {Number} tid topic ID
		 * @param {String} locationInfor The code of each school: e.g. 'BJU'
		 * @example
		 * // Get #43 and "BJU" location topic detail
		 * HRService.loadTopicList(43, 'BJU');
		 * @returns {Object}
		 */
		loadDetail: function(tid,locationInfor){
			return $http.jsonp(host + '/campus/TopicDetail?topicId=' + tid + '&location=' + locationInfor + '&callback=JSON_CALLBACK');
		},
		
		/**
		 * Returns $http x-domain service - post comments
		 * @author XIAO YU WANG <wxyu@cn.ibm.com>
		 * @param {Object} postData Post data include topic id and comments content.
		 * @example
		 * // Post comemnt "You and me!" of topid id 43
		 * HRService.postComment({topicId: 43, comments: 'You and me!'});
		 * @returns {Object}
		 */
		postComment: function(postData){
			var paramStr = $.param(postData);
			return $http.jsonp(host + '/campus/AddComment?' + paramStr + '&callback=JSON_CALLBACK');
		},
		
		/**
		 * Returns $http x-domain service - post rating
		 * @author XIAO YU WANG <wxyu@cn.ibm.com>
		 * @param {Object} postData Post data include topic id and checked value(Y/N), Y: checked, N: un-checked.
		 * @example
		 * // Rate topid id 43
		 * HRService.rate({topicId: 43, checked: 'Y'});
		 * @returns {Object}
		 */
		rate: function(postData){
			var paramStr = $.param(postData);
			return $http.jsonp(host + '/campus/HandleRating?' + paramStr + '&callback=JSON_CALLBACK');
		},
		
		/**
		 * Returns $http x-domain service - post code to the phone
		 * @author XIAO YU WANG <wxyu@cn.ibm.com>
		 * @deprecated OMG...
		 * @returns {Object}
		 */
		postCode: function(tel){
			return $http.jsonp(host + '/campus/PostCode?tel=' + tel + '&callback=JSON_CALLBACK');
		},
		
		Auth: {
			appStatus: '',
			
			userReg: function(paramStr){
				return $http.jsonp(host + '/campus/Register?' + paramStr + '&callback=JSON_CALLBACK');
			},
			
			userLogin: function(postData){
				var paramStr = $.param(postData);
				return $http.jsonp(host + '/campus/Login?' + paramStr + '&callback=JSON_CALLBACK');
			},
			
			setUser: function(uArr){
				userInfo = uArr;
			},
			
			getUser: function(){
				return userInfo;
			},
			
			isLoggedIn: function(){
				var deferred = $q.defer(),
					loginfoStorageObj = null,
					now = +new Date(),
					expiriation = 0;
					
				if(window.localStorage && window.localStorage.loginfo){
					loginfoStorageObj = JSON.parse(localStorage.loginfo);
					expiriation = loginfoStorageObj.expiriation;
					storageLocation = loginfoStorageObj.location;
				}
				
				// Get user info from global or local storage data
				if(!!this.getUser() || (loginfoStorageObj && (now < expiriation) && (storageLocation == $cookies.locationCookie))){
					if(!this.getUser()){
						// Set user info from local storage data
						this.setUser(loginfoStorageObj);
					}
					
					this.appStatus = 'LOGGED';
					deferred.resolve('LOGGED');
			    }else{
			    	this.appStatus = 'UNLOGGED';
			    	deferred.reject('UNLOGGED');
			    }
				
				return deferred.promise;
			}
		}
	};
});