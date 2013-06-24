/**
 * Display list of all blog posts of current logged in user.
 * 
 * @param $scope
 * @param MyBlogPost
 * @param MyBlogComment
 */
function MyBlogListController($scope, MyBlogPost, MyBlogComment) {
    console.log('==>MyBlogListController');
    $("#headmenu").find("li").removeClass("active");
    $("#adminmenu").addClass("active");

    $scope.blogs = MyBlogPost.list(function(blogs) {
        for ( var index = 0; index < blogs.length; index++) {
            blogs[index].commentsCollapsed = true;
        }
    });

    /**
     * When user expands the comment list, load comments of the blog.
     */
    $scope.triggerCommentsCollapse = function(blog) {
        blog.commentsCollapsed = !blog.commentsCollapsed;
        console.log('blog.totalCommentCount = ' + blog.totalCommentCount);
        console.log('blog.comments = ' + blog.comments);
        if (blog.comments == null) {
            // load comments
            blog.comments = MyBlogComment.list( {blogId : blog.blogId}, function(comments) {
                console.log(' MyBlogListController load comments for blog ' + blog.blogId + ', return '
                        + comments.length + ' comments.');
                for ( var cindex = 0; cindex < comments.length; cindex++) {
                    setupCommentStatus(comments[cindex]);
                    setupCommentAuthorActions(comments[cindex], $scope);
                }
            });
        }
    };

    $scope.approve = function(blog, comment) {
        console.log('MyBlogListController.approve()');
        comment.$approve( {blogId : blog.blogId}, function(comment) {
            console.log('MyBlogListController: approve comment returned.');
            setupCommentStatus(comment);
            setupCommentAuthorActions(comment, $scope);
        });
    };

    $scope.disapprove = function(blog, comment) {
        console.log('MyBlogListController.disapprove()');
        comment.$disapprove( {blogId : blog.blogId}, function(comment) {
            console.log('MyBlogListController disapprove comment returned.');
            setupCommentStatus(comment);
            setupCommentAuthorActions(comment, $scope);
        });
    };

    $scope.markSpam = function(blog, comment) {
        console.log('MyBlogListController.markSpam()');
        comment.$markSpam( {blogId : blog.blogId}, function(comment) {
            console.log('MyBlogListController mark comment as spam returned.');
            setupCommentStatus(comment);
            setupCommentAuthorActions(comment, $scope);
        });
    };

    $scope.unmarkSpam = function(blog, comment) {
        console.log('MyBlogListController.unmarkSpam()');
        comment.$unmarkSpam( {blogId : blog.blogId}, function(comment) {
            console.log('MyBlogListController mark comment as not spam returned.');
            setupCommentStatus(comment);
            setupCommentAuthorActions(comment, $scope);
        });
    };
}
