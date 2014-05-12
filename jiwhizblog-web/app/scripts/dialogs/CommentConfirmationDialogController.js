'use strict';

angular.module('jiwhizWeb').controller('CommentConfirmationDialogController',
['$scope', '$modalInstance', 'name',
function($scope, $modalInstance, name) {
    $scope.name = name;
    $scope.ok = function() {
        $modalInstance.close();
    };
}
]);