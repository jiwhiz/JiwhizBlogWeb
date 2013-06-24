/**
 * Service for retrieving public blogs.
 * 
 * See Java RESTful service com.jiwhiz.blog.web.site.PublicBlogRestService
 */
angular.module('PublicBlogService', [ 'ngResource' ]).factory('PublicBlog', function($resource) {
    return $resource(
        'rest/public/blogs/:action:year/:month/:path', 
        {}, 
        {
            get : { method : 'GET' },
            list : { method : 'GET', params : {action : 'list'}, isArray : true },
            latest : { method : 'GET', params : {action : 'latest'} }
        }
    );
});