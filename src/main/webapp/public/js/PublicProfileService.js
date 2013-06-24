/**
 * Service for retrieving user's public profile.
 * 
 * See Java RESTful service com.jiwhiz.blog.web.site.PublicSiteRestService
 */
angular.module('PublicProfileService', [ 'ngResource' ]).factory('PublicProfile', function($resource) {
    return $resource('rest/public/profile/:userId', {}, {});
});