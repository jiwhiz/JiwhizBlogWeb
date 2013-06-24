/**
 * Display public profile of one user. For profile.html.
 * 
 * @param $scope
 * @param $routeParams
 * @param PublicProfile
 */
function ProfileController($scope, $routeParams, PublicProfile) {
    console.log('==>ProfileController');
    $("#headmenu").find("li").removeClass("active");

    $scope.profile = PublicProfile.get( {userId : $routeParams.userId}, function(profile) {
        if (profile.imageUrl == null) {
            profile.imageUrl = "resources/img/gravatar.jpg";
        }
    });
}