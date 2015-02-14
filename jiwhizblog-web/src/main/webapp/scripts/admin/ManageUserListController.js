/**
 * Display list of all users in the system. For user-list.html.
 * 
 */
'use strict';

angular.module('jiwhizWeb').controller('ManageUserListController',
['$log', '$rootScope', '$scope', 'AdminService',
function($log, $rootScope, $scope, AdminService) {
    $rootScope.activeMenu = {
        'home' : '',
        'blog' : '',
        'about' : '',
        'contact' : '',
        'admin' : 'active'
    };
    $rootScope.showTitle = true;
    
    $rootScope.page_title = 'Manage Users';
    $rootScope.page_description = 'Here are all users in the system.';

    var setup = function( pageNumber ) {
        AdminService.load()
            .then( function(resource) {
                return resource.$get('users', {'page': pageNumber, 'size':10, 'sort':null});
            })
            .then( function(users)
            {
                $scope.page = users.page;
                $scope.page.currentPage = $scope.page.number + 1;
                return users.$get('userAccountList');
            })
            .then( function(userAccountList)
            {
                $scope.users = userAccountList;
                userAccountList.forEach( function(user) {
                    processUser(user);

                    // load social connections
                    user.$get('socialConnections').then(function(connections) {
                        user.socialConnections = connections;
                    });
               });
            })
            ;
        
    };

    setup(0);
    
    $scope.pageChanged = function() {
        setup($scope.page.currentPage - 1); //Spring HATEOAS page starts with 0
    };

    $scope.lock = function(user) {
        user.$patch('self', {}, {'command' : 'lock'}).then( function() {
            console.log('ManageUserListController lock user returned.');
            setup($scope.page.number); //reload page
        });
    };

    $scope.unlock = function(user) {
        user.$patch('self', {}, {'command' : 'unlock'}).then( function() {
            console.log('ManageUserListController unlock user returned.');
            setup($scope.page.number); //reload page
        });
    };

    $scope.trust = function(user) {
        console.log('ManageUserListController.trust()');
        user.$patch('self', {}, {'command' : 'trust'}).then( function() {
            console.log('ManageUserListController trust user returned.');
            setup($scope.page.number); //reload page
        });
    };

    $scope.untrust = function(user) {
        console.log('ManageUserListController.untrust()');
        user.$patch('self', {}, {'command' : 'untrust'}).then( function() {
            console.log('ManageUserListController untrust user returned.');
            setup($scope.page.number); //reload page
        });
    };

    $scope.addAuthorRole = function(user) {
        console.log('ManageUserListController.addAuthorRole()');
        user.$patch('self', {}, {'command' : 'addAuthorRole'}).then( function() {
            setup($scope.page.number); //reload page
        });
    };

    $scope.removeAuthorRole = function(user) {
        console.log('ManageUserListController.removeAuthorRole()');
        user.$patch('self', {}, {'command' : 'removeAuthorRole'}).then( function() {
            setup($scope.page.number); //reload page
        });
    };
}
]);