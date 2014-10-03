var lufibooksApp = angular.module('lufibooks', ['ngRoute']).config(function($routeProvider){
	$routeProvider.when('/main', {
			templateUrl: 'partials/main.html',
			controller: 'MainCtrl'
		}).when('/books', {
			templateUrl: 'partials/books.html',
			controller: 'BooksCtrl'
		}).when('/books/:book_id', {
			templateUrl: 'partials/book.html',
			controller: 'BookCtrl'
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
		$http.post('/vote-ups', proposalData ).success(function() {
			$http.get('/proposals').success(function(data) {
				$scope.proposals = data.proposal;
			});
		});
	};
	$scope.submitIsbn = function(clickEvent, isbnNumber) {
		var isbnData = {"proposal": {"isbn" : isbnNumber}};
		 $http.post('/proposals', isbnData ).success(function(response){
			console.log(response.proposal.key);
			$location.url('/proposals/' + response.proposal.key);
		});
	};
});
lufibooksApp.controller('BookCtrl', function($scope, $http, $routeParams, $location) {
	$http.get('/books').success(function(books) {
		$scope.books = books.book;
	});
	$http.get('/books/' + $routeParams.book_id).success(function(data) {
		$scope.thebook = data.book;
	});
});
lufibooksApp.controller('BooksCtrl', function($scope, $http, $location) {
	$http.get('/books').success(function(data) {
		$scope.books = data.book;
	});
});


lufibooksApp.controller('MainCtrl', function($scope, $http, $location) {
	$http.get('/books').success(function(books) {
		$scope.books = books.book;
	});
	$http.get('/proposals').success(function(proposals) {
			$scope.proposals = proposals.proposal;
	});
	$scope.transitionToBook = function($scope, bookId) {
		$location.url('/books/' + bookId);
	};
});

lufibooksApp.controller('NewProposalCtrl', function($scope, $http, $routeParams, $location) {
	$http.get('/proposals/' + $routeParams.proposal_id).success(function(data) {
		$scope.proposal = data.proposal;
	});
	$scope.addToStock = function(clickEvent, key, stock) {
		var stockData = {"book": {"proposalsKey" : key, "allInStock": stock}};
		$http.post('/books', stockData ).success(function(response){
			$location.url('/books/' + response.book.key);
		});
	};
});

lufibooksApp.directive('bindHtmlUnsafe', function( $compile ) {
    return function( $scope, $element, $attrs ) {

        var compile = function( newHTML ) { // Create re-useable compile function
            newHTML = $compile(newHTML)($scope); // Compile html
            $element.html('').append(newHTML); // Clear and append it
        };

        var htmlName = $attrs.bindHtmlUnsafe; // Get the name of the variable
                                              // Where the HTML is stored

        $scope.$watch(htmlName, function( newHTML ) { // Watch for changes to
                                                      // the HTML
            if(!newHTML) return;
            compile(newHTML);   // Compile it
        });

    };
});

function LoginCtrl($scope, $http) {

}

function BorrowedCtrl($scope, $http) {

}

