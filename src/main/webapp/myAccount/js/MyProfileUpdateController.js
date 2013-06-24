/**
 * Edit current logged in user profile. For profile-edit.html.
 * 
 * @param $scope
 * @param $routeParams
 * @param $location
 * @param MyProfile
 */
function MyProfileUpdateController($scope, $routeParams, $location, MyAccount) {
    console.log('==>MyProfileUpdateController');
    $("#headmenu").find("li").removeClass("active");
    $("#adminmenu").addClass("active");

    $scope.profileForm = MyAccount.getProfile();

    $scope.submit = function() {
        $scope.profileForm.$updateProfile(function() {
            console.log('MyProfileUpdateController saved profile');
            $location.path('/myAccount/'); // redirect to my account page after update returned.
        });

    };

    $scope.cancel = function() {
        $location.path('/myAccount/');
    };
}