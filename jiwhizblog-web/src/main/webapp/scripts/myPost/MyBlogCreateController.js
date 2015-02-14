/**
 * Create new blog post for current logged in user.
 * 
 */
'use strict';

angular.module('jiwhizWeb').controller('MyBlogCreateController',
['$log', '$rootScope', '$scope', '$location', 'AuthorService',
function($log, $rootScope, $scope, $location, AuthorService) {
    $rootScope.activeMenu = {
            'home' : '',
            'blog' : '',
            'about' : '',
            'contact' : '',
            'admin' : 'active'
        };
    $rootScope.showTitle = true;
    
    $rootScope.page_title = 'Create Blog';
    $rootScope.page_description = 'Create a new blog post here.';

    // init an empty blog post
    $scope.blogForm = {
        title : '',
        content : '',
        tagString : ''
    };
    
    $scope.submit = function() {
        AuthorService
            .load()
            .then( function(resource) {
                return resource.$post('blogs', {}, $scope.blogForm);
            })
            .then( function(location) {
                $log.debug('Create Blog returned with location='+location);
                var blogId = location.substring(location.lastIndexOf('/') + 1);
                $location.path('/myPost/reviewBlog/'+blogId);
            });
    };

    $scope.cancel = function() {
        $location.path('myPost/blogs');
    };

}
]);
