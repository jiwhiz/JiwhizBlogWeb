angular.module('jiwhizWeb').
    directive('memberInfo', function() {
        return {
            restrict: 'EA',
            templateUrl: 'views/templates/member-info-template.html',
            scope: {
                member: '='
            },
            replace: true,
            transclude: false,
            link: function(scope, element, attrs) {
	        }
    };
});