/**
 * Update comment content. For comment-update.html.
 * 
 * @param $scope
 * @param $location
 * @param $routeParams
 * @param MyBlogPost
 */
function ManageCommentUpdateController($scope, $location, $routeParams, ManageComment) {
    console.log('==>ManageCommentUpdateController');

    $scope.commentForm = new ManageComment();

    $scope.comment = ManageComment.get( {commentId : $routeParams.commentId}, function(comment) {
        console.log('ManageCommentUpdateController get comment, id = ' + comment.commentId);
        $scope.commentForm.commentId = comment.commentId;
        $scope.commentForm.content = comment.content;
    });

    $scope.submit = function() {
        $scope.commentForm.$update(function() {
            console.log('ManageCommentUpdateController updated comment.');
            $location.path('/admin/comments');
        });
    };

    $scope.cancel = function() {
        $location.path('/admin/comments');
    };

}
