/**
 * Service for current logged in user managing his/her own blog posts.
 * 
 * See Java RESTful service com.jiwhiz.blog.web.post.MyBlogPostRestService
 * 
 */
angular.module('MyBlogPostService', [ 'ngResource' ]).factory('MyBlogPost', function($resource) {
    return $resource(
        'rest/myPost/blogs/:action:blogId/:blogAction', 
        { blogId : '@blogId' }, 
        {
            list          : { method : 'GET',  params : {action : 'list'}, isArray : true },
            get           : { method : 'GET' },
            create        : { method : 'POST', params : {action : 'create'} },
            updateContent : { method : 'PUT',  params : {blogAction : 'updateContent'}},
            updateMeta    : { method : 'PUT',  params : {blogAction : 'updateMeta'}},
            publish       : { method : 'PUT',  params : {blogAction : 'publish'} },
            unpublish     : { method : 'PUT',  params : {blogAction : 'unpublish'} }
        }
    );
});
