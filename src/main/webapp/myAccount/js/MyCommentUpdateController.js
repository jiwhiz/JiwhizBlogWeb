/**
 * Update comment content. For comment-update.html.
 * 
 * @param $scope
 * @param $location
 * @param $routeParams
 * @param MyBlogPost
 */
function MyCommentUpdateController($scope, $location, $routeParams, MyComment) {
    console.log('==>MyCommentUpdateController');

    $scope.commentForm = new MyComment();

    MyComment.get( {commentId : $routeParams.commentId}, function(comment) {
        console.log('MyCommentUpdateController get comment, commentId = ' + comment.commentId);
        $scope.commentForm.commentId = comment.commentId;
        $scope.commentForm.content = comment.content;
    });

    $scope.submit = function() {
        $scope.commentForm.$update(function() {
            console.log('MyCommentUpdateController updated comment.');
            $location.path('/myAccount/comments');
        });
    };

    $scope.cancel = function() {
        $location.path('/myAccount/comments');
    };

}
