'use strict';

angular.module('jiwhizWeb', [ 'ngRoute', 'ngResource', 'ngSanitize', 'ui.bootstrap', 'angular-hal'])
.config(
    [ '$routeProvider', '$locationProvider', '$httpProvider', 
    function($routeProvider, $locationProvider, $httpProvider) {
        $locationProvider.html5Mode(false);
        $routeProvider.
            when('/', {templateUrl : 'views/public/home.html', controller : 'HomeController'}).
            when('/about', {templateUrl : 'views/public/about.html', controller : 'AboutController'}).
            when('/contact', {templateUrl : 'views/public/contact.html', controller : 'ContactController'}).
            when('/blogs', {templateUrl : 'views/public/blog-list.html', controller : 'BlogListController'}).
            when('/blogs/:blogId', {templateUrl : 'views/public/blog-display.html', controller : 'BlogDisplayController'}).
            when('/profiles/:userId', {templateUrl : 'views/public/profile.html', controller : 'ProfileController'}).
            when('/404', {templateUrl : 'views/public/resourceNotFound.html', controller : 'ResourceNotFoundController'}).
            when('/403', {templateUrl : 'views/public/accessDenied.html', controller : 'AccessDeniedController'}).

            when('/myAccount/', {templateUrl : 'views/myAccount/account-overview.html', controller : 'MyAccountController'}).
            when('/myAccount/editProfile', {templateUrl : 'views/myAccount/profile-edit.html', controller : 'MyProfileUpdateController'}).
            when('/myAccount/comments', {templateUrl : 'views/myAccount/comment-list.html', controller : 'MyCommentListController'}).
            when('/myAccount/comments/:commentId', {templateUrl : 'views/myAccount/comment-edit.html'}).

            when('/myPost/blogs', {templateUrl : 'views/myPost/blog-list.html', controller : 'MyBlogListController'}).
            when('/myPost/reviewBlog/:blogId', {templateUrl : 'views/myPost/blog-review.html', controller : 'MyBlogReviewController'}).
            when('/myPost/createBlog', {templateUrl : 'views/myPost/blog-create.html'}).
            when('/myPost/updateBlogContent/:blogId', {templateUrl : 'views/myPost/blog-update-content.html', controller : 'MyBlogUpdateContentController'}).
            when('/myPost/updateBlogMeta/:blogId', {templateUrl : 'views/myPost/blog-update-meta.html', controller : 'MyBlogUpdateMetaController'}).
            when('/myPost/slides', {templateUrl : 'views/myPost/slide-list.html', controller : 'MySlideListController'}).
            when('/myPost/createSlide', {templateUrl : 'views/myPost/slide-create.html', controller : 'MySlideCreateController'}).
            when('/myPost/updateSlide/:slideId', {templateUrl : 'views/myPost/slide-create.html', controller : 'MySlideUpdateController'}).
            
            when('/admin/', {templateUrl : 'views/admin/user-list.html', controller : 'ManageUserListController'}).
            when('/admin/comments', {templateUrl : 'views/admin/comment-list.html', controller : 'ManageCommentListController'}).

            otherwise({redirectTo : '/'});
        
        var interceptor = ['$q', '$location', function ($q, $location) {

            function success(response) {
                return response;
            }

            function error(response) {
                if (response.status === 404) {
                    $location.path("/404").replace();
                } else if (response.status === 403) {
                    $location.path("/403").replace();
                } else if (response.status === 401) {
                    $location.path("/401").replace();
                }
                
                return $q.reject(response);
            }

            return function (promise) {
                return promise.then(success, error);
            };
        }];

        //$httpProvider.responseInterceptors.push(interceptor);
    }
])
;

