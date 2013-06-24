/**
 * Display logged in user account info. For account-overview.html.
 * 
 * @param $scope
 * @param $routeParams
 * @param NyAccount
 */
function MyAccountController($scope, $routeParams, $http, MyAccount) {
    console.log('==>MyAccountController');
    $("#headmenu").find("li").removeClass("active");
    $("#adminmenu").addClass("active");

    $scope.myAccount = MyAccount.overview({}, function(account) {
        if (account.imageUrl == null) {
            account.imageUrl = "resources/img/gravatar.jpg";
        }
        console.log('MyAccountController: get Account, user name is ' + account.displayName);
    });

    $scope.connectSocial = function(provider) {
        console.log('click connect/' + provider);
        $http.post('connect/' + provider, {}).success(function(data, status) {
            console.log('   connect/' + provider + " success.");
        }).error(function(data, status) {
            console.log('   connect/' + provider + " error!");
        });
    };

    $scope.useSocialImage = function(providerName) {
        console.log('use image from social provider ' + providerName);
        MyAccount.useSocialImage({
            provider : providerName
        }, function(account) {
            $scope.myAccount = account;
        });
    };
}