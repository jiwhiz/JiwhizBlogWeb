/**
 * Display about content. For about.html.
 * 
 */
'use strict';

angular.module('jiwhizWeb').controller('AboutController',
['$rootScope', '$scope', 
function($rootScope, $scope) {
    $rootScope.activeMenu = {
        'home' : '',
        'blog' : '',
        'about' : 'active',
        'contact' : '',
        'admin' : ''
    };
    $rootScope.showTitle = true;
    
    $rootScope.page_title = 'About Me';
    $rootScope.page_description = 'Here is my story.';
}
]);