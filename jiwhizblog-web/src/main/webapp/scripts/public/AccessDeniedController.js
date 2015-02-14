/**
 * Display 403 error message.
 * 
 */
'use strict';

angular.module('jiwhizWeb').controller('AccessDeniedController',
['$rootScope', '$scope', 'WebsiteService',
function($rootScope, $scope, WebsiteService) {
    $rootScope.activeMenu = {
        'home' : '',
        'blog' : '',
        'about' : '',
        'contact' : '',
        'admin' : ''
    };
    $rootScope.showSlider = false;
    $rootScope.showTitle = true;
    
    $rootScope.page_title = '403 Forbidden';
    $rootScope.page_description = 'Cannot access what you are looking for.';
    
    WebsiteService
        .load()
        .then( function( website ) {
            $scope.authenticated = website.authenticated;
        
            if ($scope.authenticated) {
                website.$get('currentUser').then( function( user ) {
                    $scope.currentUser = user;
                });
            }
        })
        ;

}
]);