/**
 * Display logged in user account info. For account-overview.html.
 * 
 */
'use strict';

angular.module('jiwhizWeb').controller('MyAccountController',
['$log', '$rootScope', '$scope', '$routeParams', '$http', '$modal', 'MyAccountService',
function($log, $rootScope, $scope, $routeParams, $http, $modal, MyAccountService) {
    $rootScope.activeMenu = {
        'home' : '',
        'blog' : '',
        'about' : '',
        'contact' : '',
        'admin' : 'active'
    };
    $rootScope.showTitle = true;
    
    $rootScope.page_title = 'My Account';
    $rootScope.page_description = 'Here is my account overview.';

    var loadAccount = function () {
        MyAccountService
            .load()
            .then( function(resource) {
                $scope.myAccount = resource;
            })
            ;
    };
    
    loadAccount();

    $scope.connectSocial = function(provider) {
        console.log('click connect/' + provider);
        $http.post('connect/' + provider, {}).success(function(data, status) {
        	$log.debug('   connect/' + provider + " success.");
        }).error(function(data, status) {
        	$log.debug('   connect/' + provider + " error!");
        });
    };

    $scope.useSocialImage = function(providerName) {
    	$log.debug('use image from social provider ' + providerName);
        var imageUrl = $scope.myAccount.socialConnections[providerName].imageUrl;
        if (imageUrl !== null){
            $scope.myAccount.$patch('profile', {}, {'imageUrl' : imageUrl})
                .then( function(){
                    loadAccount();
                })
                ;
        }
    };
    
    $scope.editProfile = function() {
        $log.debug('==>MyAccountController.editProfile()');
        var modalInstance = $modal.open({
            templateUrl : 'views/myAccount/profile-input-dialog.html',
            controller : 'MyProfileInputDialogController',
            resolve : {
                'profile' : function() {
                    return {
                        'displayName' : $scope.myAccount.displayName,
                        'email' : $scope.myAccount.email,
                        'webSite' : $scope.myAccount.webSite
                    };
                }
            }
        });

        modalInstance.result.then(function(form) {
            $scope.myAccount.$patch('profile', {}, form)
                .then( function(){
                    $log.debug('Updated profile: ' + form.displayName);
                    loadAccount();
                })
                ;
        }, function() {
            $log.info('Profile Input Dialog cancelled.');
        });

    };
}
]);