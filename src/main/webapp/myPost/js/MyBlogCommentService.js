/**
 * Service for managing blog comments by author.
 * 
 * See Java RESTful service com.jiwhiz.blog.web.post.MyBlogCommentRestService
 */
angular.module('MyBlogCommentService', [ 'ngResource' ]).factory('MyBlogComment', function($resource) {
    return $resource(
        'rest/myPost/blogs/:blogId/comments/:action:commentId/:commentAction', 
        { commentId : '@commentId' }, 
        {
            list       : { method : 'GET',  params : {action : 'list'}, isArray : true },
            get        : { method : 'GET' },
            approve    : { method : 'PUT',  params : {commentAction : 'approve'} },
            disapprove : { method : 'PUT',  params : {commentAction : 'disapprove'} },
            markSpam   : { method : 'PUT',  params : {commentAction : 'markSpam'} },
            unmarkSpam : { method : 'PUT',  params : {commentAction : 'unmarkSpam'} },
        }
    );
});
