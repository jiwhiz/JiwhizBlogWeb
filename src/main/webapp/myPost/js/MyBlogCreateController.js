/**
 * Create new blog post for current logged in user.
 * 
 * @param $scope
 * @param $location
 * @param MyBlogPost
 */
function MyBlogCreateController($scope, $location, MyBlogPost) {
    console.log('==>MyBlogCreateController');
    $("#headmenu").find("li").removeClass("active");
    $("#adminmenu").addClass("active");

    // init an empty blog post
    $scope.blogForm = new MyBlogPost({
        title : '',
        content : '',
        tagString : ''
    });

    $scope.submit = function() {
        $scope.blogForm.$create(function(blog) {
            console.log('MyBlogCreateController create new blog, logId is' + blog.blogId);
            // redirect to review blog post page
            $location.path('/myPost/reviewBlog/' + blog.blogId);
        });
    };

    $scope.cancel = function() {
        $location.path('myPost/');
    };

}
