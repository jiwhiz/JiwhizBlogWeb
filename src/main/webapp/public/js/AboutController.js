/**
 * Display about content. For about.html.
 * 
 * @param $scope
 */
function AboutController($scope) {
    console.log('==>AboutController');
    $("#headmenu").find("li").removeClass("active");
    $("#aboutmenu").addClass("active");

    $scope.testContent = "public class TestClass {\n private String test;\n}";
}