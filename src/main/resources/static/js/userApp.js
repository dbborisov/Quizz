var app = angular.module('angularTable', ['angularUtils.directives.dirPagination']);

// app.controller('listdata',function($scope, $http){
//
// });

app.controller('listdata',function($scope, $http){
	$scope.users = []; //declare an empty array
	$http.get("http://localhost:8080/api/profile/user/all").success(function(response){
		$scope.users = response;  //ajax request to fetch data into $scope.data
	});
    $scope.sort = function(keyname){
        $scope.sortKey = keyname;   //set the sortKey to the param passed
        $scope.reverse = !$scope.reverse; //if true make it false and vice versa
    }
});


// (function() {
// var todos = angular.module('todos', ['ui.bootstrap']);
//
// 	var homeCtrl = function($scope, $http) {
// 		$scope.filteredTodos = []
// 			, $scope.currentPage = 1
// 			, $scope.numPerPage = 10
// 			, $scope.maxSize = 5,
// 		$scope.todos = [];
//
// 		$scope.makeTodos = function () {
//
// 			$http.get("http://localhost:8080/api/users/all")
//
// 				.success(function (data) {
// 					console.log(data);
// 					$scope.todos = data;
// 				})
// 				.error(function (err) {
// 					return err;
// 				});
//
// 		};
//
// 		$scope.makeTodos();
//
// 		var a = $scope.$watch("currentPage + numPerPage", function () {
// 			var begin = (($scope.currentPage - 1) * $scope.numPerPage)
// 				, end = begin + $scope.numPerPage;
// 					console.log($scope.todos.slice(begin, end));
// 			$scope.filteredTodos = $scope.todos.slice(begin, end);
// 			console.log($scope.filteredTodos);
//
// 		});
// console
// 	};
// 	todos.controller("TodoController", ["$scope","$http",homeCtrl]);
// }());



