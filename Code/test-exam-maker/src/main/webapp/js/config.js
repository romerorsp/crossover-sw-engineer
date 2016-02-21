// tem is a short for Test Exam Maker.
(function() {
	'use strict';
	angular.module('tem')
	       .config(['$stateProvider','$urlRouterProvider',
	                function ($stateProvider, $urlRouterProvider) {
	    	   			$urlRouterProvider.otherwise( function($injector, $location) {
	    	   				var $state = $injector.get("$state");
	                            $state.go("tem");
	                        }); 
	                        $stateProvider.state('home', {
		                        url: "/tem",
		                        views : { 
	                                main: {
	                                	templateUrl:"views/home.html"
	                                }
		                        },  
		                        data: {
		                        	pageTitle: 'Online Test Exam Maker'
		                        }
	                        }); 
	                        $stateProvider.state('auth', {
		                        url: "/authentication",
		                        views : { 
	                                main: {
	                                	templateUrl:"views/auth/auth.html"
	                                }
		                        },  
		                        data: {
		                        	pageTitle: 'Online Test Exam Maker - User Authentication'
		                        }
	                        });
	                }   
	       ]);
	
	/**
	 * Function to add an intercepter to the navigation.
	 * If there is not a session the user must be redirected
	 * to the login page
	 */
	angular.module('tem')
		   .config(config)
		   .run(['$rootScope', 'sessionService', '$state' , function($rootScope, sessionService, $state) {
			   $rootScope.$state = $state;
			   $rootScope.$on('$stateChangeStart',function(event, toState, toParams, fromState, fromParams){
				   if(!sessionService.isActiveSession()){
					   event.preventDefault();
					   $state.go('auth');
				   }
			   });
	    }]);
	
	/**
	 * Configure the Rest-angular framework connecting to the back-end
	 */
	angular.module('tem')
	       .run(['Restangular', '$state', 'alertService', function(Restangular, $state, alertService) {
	    	   Restangular.setDefaultHeaders({'Accept': 'application/json'});
	    	   Restangular.setDefaultHeaders({'Content-Type': 'application/json'});
	    	   Restangular.setFullResponse(true);

	    	   /* Configure the api host */
	    	   var host = 'http://localhost:8081';
	    	   Restangular.setBaseUrl(host  + '/tem/api');
	    	   
	    	   /* Generic error handler when requesting the back-end */
	    	   Restangular.setErrorInterceptor(function(response, deferred, responseHandler) {
	    		   var errorDetail = response.data? response.data.message: "Could not find the server";
	    		   if(response.status === 0) {
	    			   alertService.showDanger('There is no connection to the server. Please check your internet connection or contact the administrator.', response.status);
	    		   } else if(response.status === 400) {
	    			   alertService.showDanger('Incorrect request. Error detail: ' + errorDetail, response.status);
	    		   } else if(response.status === 401) {
	    			   alertService.showDanger('No session is active, it is possible your authentication credentials have expired or are not valid. Error detail: ' + errorDetail, response.status);
	    			   $state.go('auth');
	    		   } else if(response.status === 403) {
	    			   alertService.showDanger('Access denied. Possible cause: Invalid User or password. Error detail: ' + errorDetail, response.status);
	    		   } else if(response.status === 404) {
	    			   alertService.showDanger('Resource not found. Error detail: ' + errorDetail, response.status);
	    		   } else if(response.status === 500) {
	    			   alertService.showDanger('Internal server error. Please, contact the system administrator. Error detail: ' + errorDetail, response.status);
	    		   }
	    		   return true; // error not handled...
	    	   });
	   }]);
}());