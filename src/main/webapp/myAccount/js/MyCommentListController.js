/**
 * Display list of current logged in user's comments. For comment-list.html.
 * 
 * @param $scope
 * @param MyComment
 */
function MyCommentListController($scope, MyComment) {
    console.log('==>MyCommentListController');
    $("#headmenu").find("li").removeClass("active");
    $("#adminmenu").addClass("active");

    $scope.comments = MyComment.list(function(comments) {
        for ( var index = 0; index < comments.length; index++) {
            var comment = comments[index];
            setupCommentStatus(comment);
        }
    });

}
