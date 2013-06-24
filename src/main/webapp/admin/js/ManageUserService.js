/**
 * Service for managing all users by admin.
 * 
 * See Java RESTful service com.jiwhiz.blog.web.admin.ManageUserRestService
 * 
 */
angular.module('ManageUserService', [ 'ngResource' ]).factory('ManageUser', function($resource) {
    return $resource(
        'rest/admin/users/:action:userId/:userAction', 
        { userId : '@userId' }, 
        {
            list             : { method : 'GET',  params : {action : 'list'}, isArray : true },
            get              : { method : 'GET' },
            lock             : { method : 'PUT',  params : {userAction : 'lock'} },
            unlock           : { method : 'PUT',  params : {userAction : 'unlock'} },
            trust            : { method : 'PUT',  params : {userAction : 'trust'} },
            untrust          : { method : 'PUT',  params : {userAction : 'untrust'} },
            addAuthorRole    : { method : 'PUT',  params : {userAction : 'addAuthorRole'} },
            removeAuthorRole : { method : 'PUT',  params : {userAction : 'removeAuthorRole'} }
        }
    );
});
