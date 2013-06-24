/**
 * Display blog post for review. For blog-review.html.
 * 
 * @param $scope
 * @param $location
 * @param $routeParams
 * @param MyBlogPost
 */
function MyBlogReviewController($scope, $location, $routeParams, MyBlogPost) {
    console.log('==>MyBlogReviewController');

    $scope.blogForm = new MyBlogPost;

    $scope.blog = MyBlogPost.get({
        blogId : $routeParams.blogId
    }, function(blogPost) {
        $scope.blogForm.blogId = blogPost.blogId;

        if (blogPost.publishedPath) { // publishedPath is set
            $scope.blogForm.publishedYear = blogPost.publishedYear;
            $scope.blogForm.publishedMonth = blogPost.publishedMonth;
            $scope.blogForm.publishedPath = blogPost.publishedPath;
        } else {
            $scope.blogForm.publishedYear = new Date().getFullYear();
            $scope.blogForm.publishedMonth = new Date().getMonth() + 1;
            $scope.blogForm.publishedPath = blogPost.title.replace(/[^a-zA-Z0-9]/g, '_');
        }
    });

    $scope.publish = function() {
        console.log('MyBlogReviewController.publish()');
        $scope.blogForm.$publish( function(blogPost) {
            console.log('MyBlogReviewController publish blog returned.');
            $location.path('/blog/' + blogPost.fullPublishedPath);
        });
    };

    $scope.unpublish = function() {
        console.log('MyBlogReviewController.unpublish()');
        $scope.blogForm.$unpublish( function(blogPost) {
            console.log('MyBlogReviewController unpublish blog returned.');
            $scope.blog = blogPost;
        });
    };
}
