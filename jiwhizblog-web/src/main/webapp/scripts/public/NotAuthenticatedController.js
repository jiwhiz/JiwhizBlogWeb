/**
 * Display 401 error message.
 * 
 */
'use strict';

angular.module('jiwhizWeb').controller('NotAuthenticatedController',
['$rootScope', '$scope', 
function($rootScope, $scope) {
    $rootScope.activeMenu = {
        'home' : '',
        'blog' : '',
        'about' : '',
        'contact' : '',
        'admin' : ''
    };
    $rootScope.showTitle = true;
    
    $rootScope.page_title = '401 Unauthorized';
    $rootScope.page_description = 'It is member only resource.';
}
]);