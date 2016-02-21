// tem is a short for Test Exam Maker.
(function() {
	'use strict';

	angular.module('tem')
		   .controller('TEMController', ['$scope', '$state', '$http', function($scope, $state, $http){
				var uri = "/greet";
			   
				$scope.userInput = {};
				
			    var ifSendOk = function(result) {
					$scope.message = "Greets sent successfully =)";
				};
				var ifFail = function(error) {
					$scope.message = "Something wrong happened: [" + error.message + "]";
				};
			    $scope.sendGreets = function(){
			    	$scope.message = "";
	            	$http.post(uri, $scope.userInput, {header:"application/json"})
	            		 .then(ifSendOk, ifFail);
	            };
	            
	            var ifReceiveOk = function(result) {
					$scope.serverGreet = result.data.greet;
				};
			    $scope.receiveGreets = function(){
			    	$scope.serverGreet = "";
	            	$http.get(uri)
	            		 .then(ifReceiveOk, ifFail);
	            };
		   }]);
}());