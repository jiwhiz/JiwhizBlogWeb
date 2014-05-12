'use strict';

angular.module('jiwhizWeb').controller('CommentInputDialogController',
['$scope', '$modalInstance', 'content',
function($scope, $modalInstance, content) {
    
    $scope.form = {
        content : content
    };

    $scope.save = function() {
        $modalInstance.close($scope.form);
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
}
]);