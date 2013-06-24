angular.module('UserService', [ 'ngResource' ]).factory('User', function($resource) {
    return $resource('rest/public/currentUser');
});