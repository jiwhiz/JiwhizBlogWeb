var setupCommentStatus = function(comment) {
	if (comment.status == 'APPROVED') {
		comment.statusObj = {
			name : ' Approved',
			textClass : 'text-success',
			iconClass : 'fa-ok',
			title : 'Published'
		};
	}
	if (comment.status == 'PENDING') {
		comment.statusObj = {
			name : ' Pending',
			textClass : 'text-warning',
			iconClass : 'fa-spinner fa-spin',
			title : 'Waiting for approval'
		};
	}
	if (comment.status == 'SPAM') {
		comment.statusObj = {
			name : ' Spam',
			textClass : 'text-danger',
			iconClass : 'fa-ban',
			title : 'Mark as Spam'
		};
	}
};

var getFirstSection = function(content) {
    if (content.length < 50) {
        return content;
    }
    var start = content.indexOf("<p>");
    var end = content.indexOf("</p>");
    if (start < 0){
        if (content.length > 50) {
            return content.substring(0, 50);
        }
        return content; 
    }
    
    if (end < 50){
        return content.substring(0, start);
    }
    
    if (end < 0) { //no </p>
        end = getContent().length();
        if (end > 50) {
            end = 50;
        }
    }
    
    return content.substring(0, end);
};

var setupCommentAuthorActions = function(comment, $scope) {
	comment.actions = [];
	var actionIndex = 0;
	if (comment.status == 'APPROVED') {
		comment.actions[actionIndex++] = {
			actionFunction : $scope.disapprove,
			name : ' Disapprove',
			title : 'Disapprove Comment',
			iconClass : 'fa-thumbs-down'
		};
	}
	if (comment.status == 'PENDING') {
		comment.actions[actionIndex++] = {
			actionFunction : $scope.approve,
			name : ' Approve',
			title : 'Approve Comment',
			iconClass : 'fa-thumbs-up'
		};
		comment.actions[actionIndex++] = {
			actionFunction : $scope.markSpam,
			name : ' Spam',
			title : 'Mark as Spam',
			iconClass : 'fa-ban'
		};
	}
	if (comment.status == 'SPAM') {
		comment.actions[actionIndex++] = {
			actionFunction : $scope.unmarkSpam,
			name : ' Unspam',
			title : 'Not a Spam',
			iconClass : 'fa-check-circle'
		};
	}
};

var setupCommentAdminActions = function(comment, $scope) {
	setupCommentAuthorActions(comment, $scope);
	var actionIndex = comment.actions.length;
	if (comment.status == 'SPAM') {
		comment.actions[actionIndex++] = {
			actionFunction : $scope.remove,
			name : ' Delete',
			title : 'Delete',
			iconClass : 'fa-trash-o'
		};
	}
};
