/**
 * Setup admin menu item based on logged in user roles. 
 * If user is not logged in, display "Sign In" menu.
 *  
 */
'use strict';

angular.module('jiwhizWeb').controller('AdminMenuController',
['$scope', '$routeParams', 'WebsiteService',
function($scope, $routeParams, WebsiteService) {
	$scope.adminMenu = {};
	$scope.adminMenu.items = [];
	
    WebsiteService.load()
        .then( function( websiteResource ) {
            return websiteResource.$get('currentUser');
        })
        .then( function(user) {
    	    
    	    if (user) {
    	        processUser(user);
    	        $scope.adminMenu.iconClass = 'fa-user';
    	        $scope.adminMenu.title = ' ' + user.displayName;
    	        var index = 0;
    
                //myAccount menu
                $scope.adminMenu.items[index++] = {menu : true, url : '#/myAccount/', title : ' My Account', iconClass : "fa-wrench"};
                $scope.adminMenu.items[index++] = {menu : true, url : '#/myAccount/comments', title : ' My Comments', iconClass : "fa-comments"};
                
                if (user.isAuthor()) {
                    $scope.adminMenu.items[index++] = {menu : false, dividerClass : 'divider'};
                    $scope.adminMenu.items[index++] = {menu : true, url : '#/myPost/blogs', title : ' My Blogs', iconClass : "fa-folder-open"};
                    $scope.adminMenu.items[index++] = {menu : true, url : '#/myPost/createBlog', title : ' Create Blog', iconClass : "fa-edit"};                                
                }
                
                if (user.isAdmin()) {
                    $scope.adminMenu.items[index++] = {menu : false, dividerClass : 'divider'};
                    $scope.adminMenu.items[index++] = {menu : true, url : '#/admin/', title : ' All Users', iconClass : "fa-group"};
                    $scope.adminMenu.items[index++] = {menu : true, url : '#/admin/comments', title : ' All Comments', iconClass : "fa-comments"};
                }
                
                $scope.adminMenu.items[index++] = {menu : false, dividerClass : 'divider'};
                //Log out
                $scope.adminMenu.items[index++] = {menu : true, url : 'signout', title : ' Sign Out', iconClass : "fa-sign-out"};
    	    } else {
    	        $scope.adminMenu.iconClass = 'fa-sign-in';
    	        $scope.adminMenu.title = ' Sign In';
    	        var index = 0;
    	        //social sign in
    	        $scope.adminMenu.items[index++] = {
    	                menu : true, 
    	                url : 'signin/google?scope=openid%20profile', 
    	                title : ' Google Sign In', 
    	                iconClass : "fa-google-plus"};
    	        $scope.adminMenu.items[index++] = {
    	                menu : true, 
    	                url : 'signin/facebook', 
    	                title : ' Facebook Sign In', 
    	                iconClass : "fa-facebook"};
    	        $scope.adminMenu.items[index++] = {
    	                menu : true, 
    	                url : 'signin/twitter', 
    	                title : ' Twitter Sign In', 
    	                iconClass : "fa-twitter"};
    
    	    }
    	},
    	function(error){
    	});
	
}

]);