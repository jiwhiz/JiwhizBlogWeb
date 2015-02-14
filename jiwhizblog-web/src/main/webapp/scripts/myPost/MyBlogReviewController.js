/**
 * Display blog post for review. For blog-review.html.
 * 
 */
'use strict';

angular.module('jiwhizWeb').controller('MyBlogReviewController',
['$log', '$rootScope', '$scope', '$location', '$routeParams', 'AuthorService',
function($log, $rootScope, $scope, $location, $routeParams, AuthorService) {
    $rootScope.page_title = 'Review Blog';
    $rootScope.page_description = 'See what your blog post looks like.';

    $scope.blogForm = {};
    var blogId = $routeParams.blogId;

    AuthorService
        .load()
        .then( function(resource) {
            return resource.$get('blog', {'blogId': blogId});
        })
        .then( function(blogPost) {
            loadBlogPost(blogPost);
        });
    
    function loadBlogPost(blogPost) {
        $scope.blog = blogPost;
    }
    
    $scope.publish = function() {
        console.log('MyBlogReviewController.publish()');
        $scope.blogForm.published = 'true';
        $scope.blog.$patch('self', {}, $scope.blogForm).then( function(blogPost) {
            console.log('MyBlogReviewController publish blog returned.');
            $location.path('/blogs/' + blogPost.id);
        });
    };

    $scope.unpublish = function() {
        console.log('MyBlogReviewController.unpublish()');
        $scope.blogForm.published = 'false';
        $scope.blog.$patch('self', {}, $scope.blogForm).then( function(blogPost) {
            console.log('MyBlogReviewController unpublish blog returned.');
            loadBlogPost(blogPost);
        });
    };
}
]);
