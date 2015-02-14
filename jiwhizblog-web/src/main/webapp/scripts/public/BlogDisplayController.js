/**
 * Display one public blog post for blog-display.html
 * 
 */
'use strict';

angular.module('jiwhizWeb').controller('BlogDisplayController',
['$rootScope', '$scope', '$routeParams', '$location', '$modal', '$q', 'WebsiteService', 
function($rootScope, $scope, $routeParams, $location, $modal, $q, WebsiteService) {
    $rootScope.activeMenu = {
        'home' : '',
        'blog' : 'active',
        'about' : '',
        'contact' : '',
        'admin' : ''
    };
    
    $rootScope.showTitle = true;
    
    $rootScope.page_title = 'My Personal Blog';
    $rootScope.page_description = 'Some of my thoughts and experiences.';

    WebsiteService.load()
        .then( function( websiteResource ) {
            $scope.authenticated = websiteResource.authenticated;
            
            if ($scope.authenticated) {
                websiteResource.$get('currentUser').then( function( user ) {
                    $scope.currentUser = user;
                });
            }

            return websiteResource.$get('blog', {'blogId': $routeParams.blogId});
        })
        .then( function( blog ) {
            $scope.blog = blog;
            $rootScope.htmlTitle = blog.title + " - ";
            
            hljs.initHighlightingOnLoad();
            
            //load author
            blog.$get('author').then( function(profile) {
                $scope.blog.author = profile;
            });
            
            if (blog.approvedCommentCount == 0){
                $scope.blog.comments = [];
                return $q.reject("no comment!");
            }
            
            setupComment(0);
        }, function(error){})
        ;

    var setupComment = function( pageNumber ) {
        $scope.blog.$get('comments', {'page': pageNumber, 'size':10, 'sort':null})
            .then( function( resource )
            {
                $scope.page = resource.page;
                $scope.page.currentPage = $scope.page.number + 1;
                return resource.$get('commentPostList');
            })
            .then( function( commentPostList )
            {
                $scope.blog.comments = commentPostList;
                //load user for each comment
                $scope.blog.comments.forEach( function (comment) {
                    //load comment author profile
                    comment.$get('author').then( function(profile) {
                        comment.author = profile;
                    });
                });
            })
            ;
        
    };

    $scope.pageChanged = function() {
        setupComment($scope.page.currentPage - 1); //Spring HATEOAS page starts with 0
    };
    
    $scope.commentForm = { content : ''};

    /*
     * Post comment to the blog.
     */
    $scope.postComment = function() {
        WebsiteService.load()
            .then( function( websiteResource ) {
                return websiteResource.$post('postComment', {'blogId':$scope.blog.id}, $scope.commentForm);
            })
            .then( function(comment) {
                if (comment.status == 'APPROVED') {
                    console.log('posted comment approved immediately, display at the end of comment list.');
                    comment.author = $scope.currentUser;
                    $scope.blog.comments.push(comment);
                } else {
                    $modal.open({
                        templateUrl: 'views/public/comment-confirmation.html',
                        controller: 'CommentConfirmationDialogController',
                        resolve: {
                            name: function () {
                                return $scope.currentUser.displayName;
                            }
                        }
                    });
                    
                }
                $scope.commentForm.content = '';
            })
            ;
    };
    
}

]);