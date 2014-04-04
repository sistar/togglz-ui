/*
 * Author: Sari Haj Hussein
 */
var app = angular.module("app", ["ngRoute","ngResource"])
	.constant("apiUrl", "http://localhost:9000/api") // to tell AngularJS that 9000 is not a dynamic parameter
	.config(["$routeProvider", function($routeProvider) {
		return $routeProvider.when("/", {
			templateUrl: "/views/main",
			controller: "ListCtrl"
		}).when("/create", {
			templateUrl: "/views/detail",
			controller: "CreateCtrl"
	    }).when("/edit/:id", {
			templateUrl: "/views/detail",
			controller: "EditCtrl"
	    }).otherwise({
			redirectTo: "/"
		});
	}
	]).config([
	"$locationProvider", function($locationProvider) {
		return $locationProvider.html5Mode(true).hashPrefix("!"); // enable the new HTML5 routing and histoty API
	}
]);

app.controller("AppCtrl", ["$scope", "$location", function($scope, $location) {
	$scope.go = function (path) {
		$location.path(path);
	};
}]);

app.controller("ListCtrl", ["$scope", "$resource", "apiUrl", function($scope, $resource, apiUrl) {
	var FeatureStates = $resource(apiUrl + "/featureStates"); // a RESTful-capable resource object
	$scope.featureStates = FeatureStates.query(); // for the list of featureStates in public/html/main.html
}]);

app.controller("CreateCtrl", ["$scope", "$resource", "$timeout", "apiUrl", function($scope, $resource, $timeout, apiUrl) {
	// to save a featureState
	$scope.save = function() {
		var CreateFeatureState = $resource(apiUrl + "/featureStates/new"); // a RESTful-capable resource object
		CreateFeatureState.save($scope.featureState); // $scope.featureState comes from the detailForm in public/html/detail.html
		$timeout(function() { $scope.go('/'); }); // go back to public/html/main.html
	};
}]);

app.controller("EditCtrl", ["$scope", "$resource", "$routeParams", "$timeout", "apiUrl", function($scope, $resource, $routeParams, $timeout, apiUrl) {
	var ShowFeatureState = $resource(apiUrl + "/featureStates/:id", {id:"@id"});
	if ($routeParams.id) {
		// retrieve the corresponding featureState from the database
		// $scope.featureState.id.$oid is now populated so the Delete button will appear in the detailForm in public/html/detail.html
		$scope.featureState = ShowFeatureState.get({id: $routeParams.id});
		$scope.dbContent = ShowFeatureState.get({id: $routeParams.id}); // this is used in the noChange function
	}
	
	// decide whether to enable or not the button Save in the detailForm in public/html/detail.html 
	$scope.noChange = function() {
		return angular.equals($scope.featureState, $scope.dbContent);
	};

	// to update a featureState
	$scope.save = function() {
		var UpdateFeatureState = $resource(apiUrl + "/featureStates/" + $routeParams.id); // a RESTful-capable resource object
		UpdateFeatureState.save($scope.featureState); // $scope.featureState comes from the detailForm in public/html/detail.html
		$timeout(function() { $scope.go('/'); }); // go back to public/html/main.html
	};
	
	// to delete a featureState
	$scope.delete = function() {
		var DeleteFeatureState = $resource(apiUrl + "/featureStates/" + $routeParams.id); // a RESTful-capable resource object
		DeleteFeatureState.delete(); // $scope.featureState comes from the detailForm in public/html/detail.html
		$timeout(function() { $scope.go('/'); }); // go back to public/html/main.html
	};
}]);