var lufibooksApp = angular.module('lufibooks', ['ngRoute']).config(function($routeProvider){
	$routeProvider.when('/main', {
			templateUrl: 'partials/main.html',
			controller: 'newBooksCtrl'
		}).when('/books', {
			templateUrl: 'partials/books.html',
			controller: 'booksCtrl'
		}).when('/proposals', {
			templateUrl: 'partials/proposals.html',
			controller: 'proposalsCtrl'
		}).when('/proposals/:proposal_id', {
			templateUrl: 'partials/proposal-detail.html',
			controller: 'newProposalCtrl'
		}).when('/login', {
			templateUrl: 'partials/login.html',
			controller: 'loginCtrl'
		}).when('/borrowed:books_id', {
			templateUrl: 'partials/borrowed.html',
			controller: 'borrowedCtrl'
		}).otherwise({
			redirectTo: '/main'
		});
});

function proposalsCtrl($scope, $http) {
	$http.get('/proposals').success(function(data) {
		$scope.proposals = data;
	});
}



