/**
 * Display list of all users in the system. For user-list.html.
 * 
 * @param $scope
 * @param ManageUser
 */
function ManageUserListController($scope, ManageUser) {
    console.log('==>ManageUserListController');
    $("#headmenu").find("li").removeClass("active");
    $("#adminmenu").addClass("active");

    $scope.users = ManageUser.list();

    $scope.lock = function(user) {
        console.log('ManageUserListController.lock()');
        user.$lock(function(returnUser) {
            console.log('ManageUserListController lock user returned.');
        });
    };

    $scope.unlock = function(user) {
        console.log('ManageUserListController.unlock()');
        user.$unlock(function(returnUser) {
            console.log('ManageUserListController unlock user returned.');
        });
    };

    $scope.trust = function(user) {
        console.log('ManageUserListController.trust()');
        user.$trust(function(returnUser) {
            console.log('ManageUserListController trust user returned.');
        });
    };

    $scope.untrust = function(user) {
        console.log('ManageUserListController.untrust()');
        user.$untrust(function(returnUser) {
            console.log('ManageUserListController untrust user returned.');
        });
    };

}
