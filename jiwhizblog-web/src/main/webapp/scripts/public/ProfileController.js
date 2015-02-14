/**
 * Display public profile of one user. For profile.html.
 * 
 */
'use strict';

angular.module('jiwhizWeb').controller('ProfileController',
['$rootScope', '$scope', '$routeParams', 'WebsiteService',
function($rootScope, $scope, $routeParams, WebsiteService) {
    $rootScope.htmlTitle = 'Profile - ';
    $rootScope.activeMenu = {
        'home' : '',
        'blog' : '',
        'about' : '',
        'contact' : '',
        'admin' : ''
    };
    $rootScope.showTitle = true;
    
    $rootScope.page_title = 'User Profile';
    $rootScope.page_description = 'See who has come to my website and left comments.';

    WebsiteService.load()
        .then( function( websiteResource ) {
            return websiteResource.$get('profile', {'userId': $routeParams.userId});
        })
        .then( function(profile) {
            if (profile.imageUrl == null) {
                profile.imageUrl = "images/gravatar.jpg";
            }
            $scope.profile = profile;
            
            setupComment(0);

        });
    
    var setupComment = function( pageNumber ) {
        $scope.profile.$get('comments', {'page': pageNumber, 'size':10, 'sort':null})
            .then( function( resource )
            {
                $scope.page = resource.page;
                $scope.page.currentPage = $scope.page.number + 1;
                return resource.$get('commentPostList');
            })
            .then( function( commentPostList )
            {
                $scope.profile.comments = commentPostList;
                $scope.profile.comments.forEach( function (comment) {
                    comment.$get("blog").then( function(blog) {
                        comment.blog = blog;
                    });
                });
            })
            ;
        
    };

    $scope.selectCommentPage = function(pageNumber) {
        setupComment(pageNumber-1); //Spring HATEOAS page starts with 0
    };

}
]);