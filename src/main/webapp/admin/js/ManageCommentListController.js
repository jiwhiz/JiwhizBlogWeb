/**
 * Display list of all comments in the system. For comment-list.html.
 * 
 * @param $scope
 * @param ManageComment
 */
function ManageCommentListController($scope, $location, ManageComment) {
    console.log('==>ManageCommentListController');
    $("#headmenu").find("li").removeClass("active");
    $("#adminmenu").addClass("active");

    var loadComments = function() {
        var comments = ManageComment.list(function(comments) {
            for ( var index = 0; index < comments.length; index++) {
                var comment = comments[index];
                setupCommentStatus(comment);
                setupCommentAdminActions(comment, $scope);
            }
        });
        return comments;
    };

    $scope.comments = loadComments();

    $scope.approve = function(comment) {
        console.log('ManageCommentListController.approve()');
        comment.$approve(function(comment) {
            console.log('ManageCommentListController: approve comment returned.');
            setupCommentStatus(comment);
            setupCommentAdminActions(comment, $scope);
        });
    };

    $scope.disapprove = function(comment) {
        console.log('ManageCommentListController.disapprove()');
        comment.$disapprove(function(comment) {
            console.log('ManageCommentListController disapprove comment returned.');
            setupCommentStatus(comment);
            setupCommentAdminActions(comment, $scope);
        });
    };

    $scope.markSpam = function(comment) {
        console.log('ManageCommentListController.markSpam()');
        comment.$markSpam(function(comment) {
            console.log('ManageCommentListController mark comment as spam returned.');
            setupCommentStatus(comment);
            setupCommentAdminActions(comment, $scope);
        });
    };

    $scope.unmarkSpam = function(comment) {
        console.log('ManageCommentListController.unmarkSpam()');
        comment.$unmarkSpam(function(comment) {
            console.log('ManageCommentListController mark comment as not spam returned.');
            setupCommentStatus(comment);
            setupCommentAdminActions(comment, $scope);
        });
    };

    $scope.remove = function(comment) {
        console.log('ManageCommentListController.remove(), commentId = ' + comment.commentId);

        // TODO alert first
        comment.$remove(function() {
            console.log('ManageCommentListController delete comment returned.');
            $scope.comments = loadComments();
        });
    };

}
