/**
 * Setup admin menu item based on logged in user roles. 
 * If user is not logged in, display "Sign In" menu.
 *  
 * @param $scope
 * @param $routeParams
 * @param User
 */
function AdminMenuController($scope, $routeParams, User) {
	console.log('==>AdminMenuController');

	$scope.adminMenu = {};
	$scope.adminMenu.items = [];
	
	$scope.currentUser = User.get( function(user) {
		console.log('==>AdminMenuCtrl: get Current User, user is '+user.displayName);
		if (user.authenticated) {
			console.log('  user authenticated, is '+(user.admin? '' : 'NOT ')+ 'admin, is '+(user.author? '' : 'NOT ')+ 'author');
			$scope.adminMenu.iconClass = 'icon-user';
			$scope.adminMenu.title = ' ' + user.displayName;
			var index = 0;

			//myAccount menu
			$scope.adminMenu.items[index++] = {menu : true, url : '#/myAccount/', title : ' My Account', iconClass : "icon-wrench"};
			$scope.adminMenu.items[index++] = {menu : true, url : '#/myAccount/comments', title : ' My Comments', iconClass : "icon-comments"};
			
			if (user.author) {
				$scope.adminMenu.items[index++] = {menu : false, dividerClass : 'divider'};
				$scope.adminMenu.items[index++] = {menu : true, url : '#/myPost/', title : ' My Posts', iconClass : "icon-folder-open-alt"};
				$scope.adminMenu.items[index++] = {menu : true, url : '#/myPost/createBlog', title : ' Create Post', iconClass : "icon-edit"};
								
			}
			
			if (user.admin) {
				$scope.adminMenu.items[index++] = {menu : false, dividerClass : 'divider'};
				$scope.adminMenu.items[index++] = {menu : true, url : '#/admin/', title : ' All Users', iconClass : "icon-group"};
				$scope.adminMenu.items[index++] = {menu : true, url : '#/admin/comments', title : ' All Comments', iconClass : "icon-comments-alt"};
			}
			
			$scope.adminMenu.items[index++] = {menu : false, dividerClass : 'divider'};
			//Log out
			$scope.adminMenu.items[index++] = {menu : true, url : 'signout', title : ' Sign Out', iconClass : "icon-signout"};
		} else {
			console.log('  user is not authenticated, add social signin menu.');
			$scope.adminMenu.iconClass = 'icon-signin';
			$scope.adminMenu.title = ' Sign In';
			var index = 0;
			//social sign in
			$scope.adminMenu.items[index++] = {menu : true, url : 'signin/google', title : ' Google Sign In', iconClass : "icon-google-plus"};
			$scope.adminMenu.items[index++] = {menu : true, url : 'signin/facebook', title : ' Facebook Sign In', iconClass : "icon-facebook"};
			$scope.adminMenu.items[index++] = {menu : true, url : 'signin/twitter', title : ' Twitter Sign In', iconClass : "icon-twitter"};
		}
	});
	
	
}