// 
// Here is how to define your module 
// has dependent on mobile-angular-ui
// 

var appControllers = angular.module('appControllers', ['ngCookies']);
var appServices = angular.module('appServices', []);

var app = angular.module('HR', [
  'ngRoute',
  'ngAnimate',
  'mobile-angular-ui',
  
  // touch/drag feature: this is from 'mobile-angular-ui.gestures.js'
  // it is at a very beginning stage, so please be careful if you like to use
  // in production. This is intended to provide a flexible, integrated and and 
  // easy to use alternative to other 3rd party libs like hammer.js, with the
  // final pourpose to integrate gestures into default ui interactions like 
  // opening sidebars, turning switches on/off ..
  'mobile-angular-ui.gestures',
  'appControllers',
  'appServices'
]);

app.run(['$transform', '$rootScope', '$location', 'HRService', function($transform, $rootScope, $location, HRService) {
	window.$transform = $transform;
	HRService.Auth.isLoggedIn();
	$rootScope.$on('$routeChangeError', function(event, current, previous, rejection){
		console.log(rejection);
		if (rejection == 'UNLOGGED'){
            $location.path('/login');
			//location.href='/campus/#login?checkIn=Y&location=' + locationMap[locationVal].school;
        }else{
        	
        }
	});
	
	var preActiveLocationsArr = ['DJU', 'XAU', 'THU'];
	try{
		if(preActiveLocationsArr.indexOf($location.search().location) > -1){
			$('.navbar-absolute-bottom a:last-child').remove();
		}
	}catch(e){
	}
}]);

// 
// You can configure ngRoute as always, but to take advantage of SharedState location
// feature (i.e. close sidebar on backbutton) you should setup 'reloadOnSearch: false' 
// in order to avoid unwanted routing.
// 
app.config(function($routeProvider) {
	$routeProvider
		.when('/',            {templateUrl: 'fragments/home.html', controller: 'homeCtl', resolve: {access: ["HRService", function(HRService) {return HRService.Auth.isLoggedIn();}]}})	
		.when('/lucky',       {templateUrl: 'fragments/lucky.html', controller: 'luckyCtl', resolve: {access: ["HRService", function(HRService) {return HRService.Auth.isLoggedIn();}]}})
		.when('/login',       {templateUrl: 'fragments/login.html', controller: 'loginCtl'})
		.when('/reg',         {templateUrl: 'fragments/reg.html', controller: 'regCtl'})
		.when('/detail/:id',  {templateUrl: 'fragments/detail.html', controller: 'detailCtl', resolve: {access: ["HRService", function(HRService) {return HRService.Auth.isLoggedIn();}]}})
		.otherwise({redirectTo: '/'});
});

/* ------------- GLOBAL CONFIGURATION START ------------- */
var config = {
	"STATIC_DOMAIN": 'http://gcghrcampus.b0.upaiyun.com/' // Use CDN to store static files, e.g. (.js, .css, .jpg)
};
/* ------------- GLOBAL CONFIGURATION END ------------- */