/**
 * List all public blog posts for blog-list.html.
 * @param $scope
 * @param PublicBlog
 */
function BlogListController($scope, PublicBlog) {
	console.log('==>BlogListController');
	$("#headmenu").find("li").removeClass("active");
	$("#blogmenu").addClass("active");

	$scope.blogs = PublicBlog.list();
	
	
}