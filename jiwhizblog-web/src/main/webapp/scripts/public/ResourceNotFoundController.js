/**
 * Display 404 error message.
 * 
 */
'use strict';

angular.module('jiwhizWeb').controller('ResourceNotFoundController',
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
    
    $rootScope.page_title = 'Oops! 404 Not Found';
    $rootScope.page_description = 'Cannot found what you are looking for.';
}
]);