/**
 * Display one public blog post for blog-display.html
 * 
 * @param $scope
 * @param $routeParams
 * @param PublicBlog
 *            PublicBlogService object
 */
function BlogDisplayController($rootScope, $scope, $routeParams, $location, PublicBlog, User, BlogComment) {
    console.log('==>BlogDisplayController');
    $("#headmenu").find("li").removeClass("active");
    $("#blogmenu").addClass("active");

    $scope.blog = PublicBlog.get({
        year : $routeParams.year,
        month : $routeParams.month,
        path : $routeParams.path
    }, function(blog) {

    });

    $scope.currentUser = User.get(); // get current logged in user into scope. Any better way?

    $scope.commentForm = new BlogComment();
    $scope.commentForm.content = '';

    /*
     * Post comment to the blog. Call RESTful service POST 'rest/myAccount/blogs/:blogId/comment'. Return CommentResult.
     */
    $scope.postComment = function() {
        $scope.commentForm.$save({
            blogId : $scope.blog.blogId
        }, function(comment) {
            $scope.commentForm = new BlogComment();
            $scope.commentForm.content = '';

            if (comment.statusString == 'APPROVED') {
                console.log('posted comment approved immediately, display at the end of comment list.');
                $scope.blog.comments.push(comment);
                $scope.blog.approvedCommentCount = $scope.blog.approvedCommentCount + 1;
            } else {
                $scope.message = 'Thank you for the feedback. Your comment will be published after approval.';
                $('#messageModel').modal('show');
            }
        });
    };

}