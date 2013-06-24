/**
 * Update blog post content. For blog-update-content.html.
 * 
 * @param $scope
 * @param $location
 * @param $routeParams
 * @param MyBlogPost
 */
function MyBlogUpdateContentController($scope, $location, $routeParams, MyBlogPost) {
    console.log('==>MyBlogUpdateContentController');

    $scope.blogForm = new MyBlogPost();

    $scope.blog = MyBlogPost.get( {blogId : $routeParams.blogId}, function(blogPost) {
        $scope.blogForm.blogId = blogPost.blogId;
        $scope.blogForm.title = blogPost.title;
        $scope.blogForm.content = blogPost.content;
    });

    $scope.submit = function() {
        $scope.blogForm.$updateContent(function() {
            console.log('MyBlogUpdateContentController updated blog.');
            $location.path('/myPost/reviewBlog/' + $scope.blog.blogId);
        });
    };

    $scope.cancel = function() {
        $location.path('/myPost/');
    };

}
