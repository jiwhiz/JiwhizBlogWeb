angular.module('jiwhizblog', ['UserService', 'PublicBlogService', 'PublicProfileService', 'BlogCommentService',
                              'MyAccountService', 'MyCommentService',
                              'MyBlogPostService', 'MyBlogCommentService',
                              'ManageUserService', 'ManageCommentService',
                              'ui.bootstrap']).
    config(
        [ '$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
            $locationProvider.html5Mode(false);
            $routeProvider.
                when('/', {templateUrl : 'public/home.html', controller : HomeController}).
                when('/about', {templateUrl : 'public/about.html', controller : AboutController}).
                when('/contact', {templateUrl : 'public/contact.html', controller : ContactController}).
                when('/blogs', {templateUrl : 'public/blog-list.html', controller : BlogListController}).
                when('/blog/:year/:month/:path', {templateUrl : 'public/blog-display.html', controller : BlogDisplayController}).
                when('/profile/:userId', {templateUrl : 'public/profile.html', controller : ProfileController}).
                when('/404', {templateUrl : 'public/resourceNotFound.html'}).

                when('/myAccount/', {templateUrl : 'myAccount/account-overview.html', controller : MyAccountController}).
                when('/myAccount/editProfile', {templateUrl : 'myAccount/profile-edit.html'}).
                when('/myAccount/comments', {templateUrl : 'myAccount/comment-list.html', controller : MyCommentListController}).
                when('/myAccount/comments/:commentId', {templateUrl : 'myAccount/comment-edit.html'}).
                when('/myAccount/updateComment/:commentId', {templateUrl : 'myAccount/comment-update.html', controller : MyCommentUpdateController}).

                when('/myPost/', {templateUrl : 'myPost/blog-list.html', controller : MyBlogListController}).
                when('/myPost/reviewBlog/:blogId', {templateUrl : 'myPost/blog-review.html', controller : MyBlogReviewController}).
                when('/myPost/createBlog', {templateUrl : 'myPost/blog-create.html'}).
                when('/myPost/updateBlogContent/:blogId', {templateUrl : 'myPost/blog-update-content.html', controller : MyBlogUpdateContentController}).
                when('/myPost/updateBlogMeta/:blogId', {templateUrl : 'myPost/blog-update-meta.html', controller : MyBlogUpdateMetaController}).
				
                when('/admin/', {templateUrl : 'admin/user-list.html', controller : ManageUserListController}).
                when('/admin/comments', {templateUrl : 'admin/comment-list.html', controller : ManageCommentListController}).
                when('/admin/updateComment/:commentId', {templateUrl : 'admin/comment-update.html', controller : ManageCommentUpdateController}).
	
                otherwise({redirectTo : '/'});
        } ]);

