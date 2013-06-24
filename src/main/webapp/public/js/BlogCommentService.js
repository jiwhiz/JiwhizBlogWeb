/**
 * Service for posting comment by current user.
 * 
 * See Java RESTful service com.jiwhiz.blog.web.account.BlogCommentRestService
 */
angular.module('BlogCommentService', [ 'ngResource' ]).factory('BlogComment', function($resource) {
    return $resource(
        'rest/myAccount/blogs/:blogId/comment', 
        {},
        {
            save : { method : 'POST' }
        }
    );
});