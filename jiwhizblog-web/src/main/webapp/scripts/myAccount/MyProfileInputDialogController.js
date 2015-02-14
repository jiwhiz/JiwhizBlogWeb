'use strict';

angular.module('jiwhizWeb').controller('MyProfileInputDialogController',
['$scope', '$modalInstance', 'profile',
function($scope, $modalInstance, profile) {
    $scope.profileForm = profile;

    $scope.save = function() {
        $modalInstance.close($scope.profileForm);
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
}
]);