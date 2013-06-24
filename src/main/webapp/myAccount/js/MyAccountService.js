/**
 * User account service for current logged in user to manage the profile.
 * 
 * See Java RESTful service com.jiwhiz.blog.web.account.MyAccountRestService.
 */
angular.module('MyAccountService', [ 'ngResource' ]).factory('MyAccount', function($resource) {
    return $resource(
        'rest/myAccount/:action', 
        { provider : '@provider' }, 
        {
            overview : { method : 'GET', params : {action : 'overview'} },
            getProfile : { method : 'GET', params : {action : 'profile'} },
            updateProfile : { method : 'PUT', params : {action : 'profile'} },
            useSocialImage : { method : 'PUT', params : {action : 'useSocialImage'} }
        }
    );
});