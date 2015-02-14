/**
 * Display list of all comments in the system. For comment-list.html.
 * 
 */
'use strict';

angular.module('jiwhizWeb').controller('ManageCommentListController',
['$log', '$rootScope', '$scope', '$location', '$modal', '$q', 'AdminService',
function($log, $rootScope, $scope, $location, $modal, $q, AdminService) {
    $rootScope.activeMenu = {
        'home' : '',
        'blog' : '',
        'about' : '',
        'contact' : '',
        'admin' : 'active'
    };
    $rootScope.showTitle = true;
    
    $rootScope.page_title = 'Manage Comments';
    $rootScope.page_description = 'Here are all comments in the system.';

    var setup = function( pageNumber ) {
        AdminService.load()
            .then( function(resource) {
                return resource.$get('comments', {'page': pageNumber, 'size':10, 'sort':null});
            })
            .then( function(comments)
            {
                $scope.page = comments.page;
                $scope.page.currentPage = $scope.page.number + 1;
                if (comments.$has('commentPostList')) {
                    return comments.$get('commentPostList');
                }
                $scope.comments = [];
                return $q.reject("no comments!");
            })
            .then( function(commentPostList)
            {
                $scope.comments = commentPostList;
                commentPostList.forEach( function(comment) {
                    setupCommentStatus(comment);
                    setupCommentAdminActions(comment, $scope);

                    comment.$get('author').then(function(author) {
                        comment.author = author;
                    });
                    
                    comment.$get('blog').then(function(blog) {
                        comment.blog = blog;
                    });
               });
            })
            ;
        
    };

    setup(0);
    
    $scope.pageChanged = function() {
        setup($scope.page.currentPage - 1); //Spring HATEOAS page starts with 0
    };

    $scope.approve = function(comment) {
        comment.$patch('self', {}, {"status" : "APPROVED"}).then( function() {
            setup($scope.page.number); //reload page
        });
    };

    $scope.disapprove = function(comment) {
        comment.$patch('self', {}, {"status" : "PENDING"}).then( function() {
            setup($scope.page.number); //reload page
        });
    };

    $scope.markSpam = function(comment) {
        comment.$patch('self', {}, {"status" : "SPAM"}).then( function() {
            setup($scope.page.number); //reload page
        });
    };

    $scope.unmarkSpam = function(comment) {
        comment.$patch('self', {}, {"status" : "PENDING"}).then( function() {
            setup($scope.page.number); //reload page
        });
    };

    $scope.remove = function(comment) {
        if (confirm("Are you sure you want to delete this comment?")) {
            comment.$del('self').then( function() {
                setup($scope.page.number); //reload page
            });            
        }
    };

    $scope.editComment = function(comment) {
        var modalInstance = $modal.open({
            templateUrl : 'views/templates/comment-input-dialog.html',
            controller : 'CommentInputDialogController',
            resolve : {
                'content' : function() {
                    return comment.content;
                }
            }
        });

        modalInstance.result.then(function(form) {
            comment.$patch('self', {}, form).then(function() {
                setup($scope.page.number); //reload page
            }, function(error) {
                $log.info('Update comment!'+error);
            });
        }, function() {
            $log.info('Comment Input Dialog cancelled.');
        });

    };

}
]);