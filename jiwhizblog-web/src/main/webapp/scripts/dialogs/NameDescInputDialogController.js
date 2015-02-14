'use strict';

angular.module('jiwhizWeb').controller('NameDescInputDialogController',
['$scope', '$modalInstance', 'name', 'description', 'title',
function($scope, $modalInstance, name, description, title) {
    $scope.title = title;

    $scope.form = {
        name : name,
        description : description
    };

    $scope.save = function() {
        $modalInstance.close($scope.form);
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
}
]);