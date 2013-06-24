/**
 * Home page with latest public blog post. For home.html.
 * 
 * @param $scope
 * @param PublicBlog
 */
function HomeController($scope, PublicBlog) {
    console.log('==>HomeController');
    $("#headmenu").find("li").removeClass("active");
    $("#homemenu").addClass("active");
    $scope.hasBlog = false;

    $scope.blog = PublicBlog.latest(function(blog) {
        if (blog.title) {
            console.log('HomeController found lastest blog, title is ' + blog.title);
            $scope.hasBlog = true;

        }
    });
}