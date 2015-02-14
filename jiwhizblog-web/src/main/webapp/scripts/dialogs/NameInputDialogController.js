'use strict';

angular.module('jiwhizWeb').controller('NameInputDialogController',
['$scope', '$modalInstance', 'name', 'title',
function($scope, $modalInstance, name, title) {
    $scope.title = title;

    $scope.nameForm = {
        name : name
    };

    $scope.save = function() {
        console.log('User enter name: ' + $scope.nameForm.name);
        $modalInstance.close($scope.nameForm);
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
}
]);