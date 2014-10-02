var lufibooksApp = angular.module('lufibooks', ['ngRoute']).config(function($routeProvider){
	$routeProvider.when('/main', {
			templateUrl: 'partials/main.html',
			controller: 'MainCtrl'
		}).when('/books', {
			templateUrl: 'partials/books.html',
			controller: 'BooksCtrl'
		}).when('/proposals', {
			templateUrl: 'partials/proposals.html',
			controller: 'ProposalsCtrl'
		}).when('/proposals/:proposal_id', {
			templateUrl: 'partials/proposal.html',
			controller: 'NewProposalCtrl'
		}).when('/login', {
			templateUrl: 'partials/login.html',
			controller: 'LoginCtrl'
		}).when('/borrowed:books_id', {
			templateUrl: 'partials/borrowed.html',
			controller: 'BorrowedCtrl'
		}).otherwise({
			redirectTo: '/main'
		});
});

lufibooksApp.controller('ProposalsCtrl', function($scope, $http, $location) {
	$http.get('/proposals').success(function(data) {
		$scope.proposals = data.proposal;
	});

	$scope.VoteUp = function(clickEvent, key) {
		var proposalData = {"proposal-key" : key}
		$http.post('/vote-ups', proposalData )
	};
	$scope.submitIsbn = function(clickEvent, isbnNumber) {
		var isbnData = {"proposal": {"isbn" : isbnNumber}};
		 $http.post('/proposals', isbnData ).success(function(response){
			console.log(response.proposal.key);
			$location.url('/proposals/' + response.proposal.key);
		});
	};
});

function NewBooksCtrl($scope, $http) {
	$http.get('/books').success(function(data) {
		$scope.proposals = data;
	});
}

function MainCtrl($scope, $http) {
	$http.get('/books').success(function(data) {
		$scope.proposals = data;
	});
}

lufibooksApp.controller('NewProposalCtrl', function($scope, $http, $routeParams, $location) {
	$http.get('/proposals/' + $routeParams.proposal_id).success(function(data) {
		$scope.proposal = data.proposal;
	});
});

function LoginCtrl($scope, $http) {

}

function BorrowedCtrl($scope, $http) {

}

