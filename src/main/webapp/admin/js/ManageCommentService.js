/**
 * Service for managing all comments by admin.
 * 
 * See Java RESTful service com.jiwhiz.blog.web.admin.ManageCommentRestService
 */
angular.module('ManageCommentService', [ 'ngResource' ]).factory('ManageComment', function($resource) {
    return $resource(
        'rest/admin/comments/:action:commentId/:commentAction', 
        { commentId : '@commentId' }, 
        {
            list       : { method : 'GET',  params : {action : 'list'}, isArray : true },
            get        : { method : 'GET' },
            update     : { method : 'PUT' },
            approve    : { method : 'PUT',  params : {commentAction : 'approve'} },
            disapprove : { method : 'PUT',  params : {commentAction : 'disapprove'} },
            markSpam   : { method : 'PUT',  params : {commentAction : 'markSpam'} },
            unmarkSpam : { method : 'PUT',  params : {commentAction : 'unmarkSpam'} },
        }
    );
});
