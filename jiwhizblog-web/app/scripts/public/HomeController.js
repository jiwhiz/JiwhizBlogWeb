/**
 * Home page with latest public blog post. For home.html.
 * 
 */
'use strict';

angular.module('jiwhizWeb').controller('HomeController',
['$rootScope', '$scope', 'WebsiteService', 
function($rootScope, $scope, WebsiteService) {
    $rootScope.activeMenu = {
        'home' : 'active',
        'blog' : '',
        'about' : '',
        'contact' : '',
        'admin' : ''
    };
    $rootScope.showTitle = false;
    
    $scope.hasBlog = false;
    $scope.noBlog = false;
    
    WebsiteService
        .load()
        .then( function( websiteResource ) {
            return websiteResource.$get('latestBlog');
        })
        .then( function( latestBlog ) {
            if (latestBlog) {
                $scope.hasBlog = true;
                $scope.blog = latestBlog;
                $scope.blog.$get('author').then( function(profile) {
                    $scope.blog.author = profile;
                });
                
            } else {
                $scope.noBlog = true;
            }
        })
        ;
    
}
]);