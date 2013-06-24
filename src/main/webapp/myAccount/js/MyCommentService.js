/**
 * User comment service for current user managing his/her own comments.
 * 
 * See Java RESTful service com.jiwhiz.blog.web.account.MyCommentRestService.
 */
angular.module('MyCommentService', [ 'ngResource' ]).factory('MyComment', function($resource) {
    return $resource(
        'rest/myAccount/comments/:action:commentId', 
        {}, 
        {
            list   : { method : 'GET', params : {action : 'list'}, isArray : true },
            get    : { method : 'GET' },
            update : { method : 'PUT', params : {commentId : '@commentId'} }
        }
    );
});