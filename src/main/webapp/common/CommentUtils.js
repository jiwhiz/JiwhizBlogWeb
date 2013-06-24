var setupCommentStatus = function(comment) {
	if (comment.statusString == 'APPROVED') {
		comment.status = {
			name : ' Approved',
			textClass : 'text-success',
			iconClass : 'icon-ok',
			title : 'Published'
		};
	}
	if (comment.statusString == 'PENDING') {
		comment.status = {
			name : ' Pending',
			textClass : 'text-warning',
			iconClass : 'icon-spinner icon-spin',
			title : 'Waiting for approval'
		};
	}
	if (comment.statusString == 'SPAM') {
		comment.status = {
			name : ' Spam',
			textClass : 'text-error',
			iconClass : 'icon-ban-circle',
			title : 'Mark as Spam'
		};
	}
};

var setupCommentAuthorActions = function(comment, $scope) {
	comment.actions = [];
	var actionIndex = 0;
	if (comment.statusString == 'APPROVED') {
		comment.actions[actionIndex++] = {
			actionFunction : $scope.disapprove,
			name : ' Disapprove',
			title : 'Disapprove Comment',
			iconClass : 'icon-thumbs-down'
		};
	}
	if (comment.statusString == 'PENDING') {
		//console.log('PENDING status, can be approved or marked as spam');
		//console.log('$scope.approve is ' + $scope.approve);
		comment.actions[actionIndex++] = {
			actionFunction : $scope.approve,
			name : ' Approve',
			title : 'Approve Comment',
			iconClass : 'icon-thumbs-up'
		};
		comment.actions[actionIndex++] = {
			actionFunction : $scope.markSpam,
			name : ' Spam',
			title : 'Mark as Spam',
			iconClass : 'icon-ban-circle'
		};
	}
	if (comment.statusString == 'SPAM') {
		comment.actions[actionIndex++] = {
			actionFunction : $scope.unmarkSpam,
			name : ' Unspam',
			title : 'Not a Spam',
			iconClass : 'icon-ok-circle'
		};
	}
};

var setupCommentAdminActions = function(comment, $scope) {
	setupCommentAuthorActions(comment, $scope);
	var actionIndex = comment.actions.length;
	if (comment.statusString == 'SPAM') {
		comment.actions[actionIndex++] = {
			actionFunction : $scope.remove,
			name : ' Delete',
			title : 'Delete',
			iconClass : 'icon-trash'
		};
	}
};
