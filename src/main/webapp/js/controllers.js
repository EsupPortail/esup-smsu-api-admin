
var app = angular.module('myApp');

app.controller('MainCtrl', function($scope, h, $route, $parse, routes, restWsHelpers) {

    $scope.allowLogout = false;

    $scope.$watch('loggedUser', function () {
	$scope.mainVisibleTabs = $.grep(routes.routes, function(e) { 
	    return e.mainText && (!e.show || $parse(e.show)($scope));
	});
    });

    restWsHelpers.initialLogin();

    $scope.$on('$locationChangeSuccess', function(){
	routes.findCurrentTab($scope, $route.current.template);
	$scope.forceSidenav = $scope.currentTab && $scope.currentTab.route === "/welcome";
    });

    $scope.$on('$routeChangeStart', function () {
	$scope.loadInProgress = true;
    });   
    $scope.$on('$routeChangeError', function () {
	$scope.loadInProgress = false;
    });
    $scope.$on('$routeChangeSuccess', function(){
	$scope.loadInProgress = false;
    });
});

