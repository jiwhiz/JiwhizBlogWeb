/**
 * Display list of current logged in user's comments. For comment-list.html.
 * 
 */
'use strict';

angular.module('jiwhizWeb').controller('MyCommentListController',
['$log', '$rootScope', '$scope', '$modal', '$q', 'MyAccountService',
function($log, $rootScope, $scope, $modal, $q, MyAccountService) {
    $rootScope.activeMenu = {
        'home' : '',
        'blog' : '',
        'about' : '',
        'contact' : '',
        'admin' : 'active'
    };
    $rootScope.showTitle = true;
    
    $rootScope.page_title = 'My Comments';
    $rootScope.page_description = 'Here is my comments posted here.';

    var setup = function( pageNumber ) {
        MyAccountService.load()
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
                return $q.reject("no comments!");
            })
            .then( function(commentPostList)
            {
                $scope.comments = commentPostList;
                $scope.comments.forEach( function( comment ) {
                    setupCommentStatus(comment);
                    // load blog
                    comment.$get('blog').then(function(blog) {
                        comment.blog = blog;
                    });
                });
            })
            ;
    };
    
    setup(0);

    $scope.selectCommentPage = function(pageNumber) {
        setup(pageNumber-1); //Spring HATEOAS page starts with 0
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
            });
        }, function() {
            $log.info('Comment Input Dialog cancelled.');
        });

    };
    
}
]);
