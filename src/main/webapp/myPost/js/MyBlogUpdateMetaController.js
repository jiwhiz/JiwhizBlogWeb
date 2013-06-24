/**
 * Update blog post meta data. For blog-update-meta.html.
 * 
 * @param $scope
 * @param $location
 * @param $routeParams
 * @param MyBlogPost
 */
function MyBlogUpdateMetaController($scope, $location, $routeParams, MyBlogPost) {
    console.log('==>MyBlogUpdateMetaController');

    $scope.blogForm = new MyBlogPost();

    $scope.blog = MyBlogPost.get( {blogId : $routeParams.blogId}, function(blogPost) {
        $scope.blogForm.blogId = blogPost.blogId;
        $scope.blogForm.title = blogPost.title;
        $scope.blogForm.tagString = blogPost.tagString;
        $scope.blogForm.publishedYear = blogPost.publishedYear;
        $scope.blogForm.publishedMonth = blogPost.publishedMonth;
        $scope.blogForm.publishedPath = blogPost.publishedPath;
    });

    $scope.submit = function() {
        $scope.blogForm.$updateMeta(function() {
            console.log('MyBlogUpdateMetaController updated blog.');
            $location.path('/myPost/reviewBlog/' + $scope.blog.blogId);
        });
    };

    $scope.cancel = function() {
        $location.path('/myPost/');
    };

}
