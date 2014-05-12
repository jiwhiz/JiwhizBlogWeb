/**
 * Update blog post content. For blog-update-content.html.
 * 
 */
'use strict';

angular.module('jiwhizWeb').controller('MyBlogUpdateContentController',
['$log', '$rootScope', '$scope', '$location', '$routeParams', 'AuthorService',
function($log, $rootScope, $scope, $location, $routeParams, AuthorService) {
    $rootScope.page_title = 'Update Blog';
    $rootScope.page_description = 'Update my blog content here.';

    $scope.blogForm = {};
    var blogId = $routeParams.blogId;

    AuthorService.load()
        .then( function(resource) {
            return resource.$get('blog', {'blogId': blogId});
        })
        .then( function(blogPost) {
            $scope.blog = blogPost;
            $scope.blogForm.content = blogPost.content;
        });

    $scope.submit = function() {
        $scope.blog.$patch('self', {}, $scope.blogForm).then( function(blogPost) {
            console.log('MyBlogUpdateContentController updated blog content returned.');
            $location.path('/myPost/reviewBlog/' + blogId);
        });
    };

    $scope.cancel = function() {
        $location.path('/myPost/blogs');
    };

}
]);
