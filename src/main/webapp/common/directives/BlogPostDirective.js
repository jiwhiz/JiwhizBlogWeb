angular.module('jiwhizblog').directive('blogPost', function($compile) {
    return {
        restrict : 'E',
        scope : {
            content : '@'
        },
        replace : true,
        transclude : false,
        link : function(scope, element, attrs) {
            scope.$watch('content', function(value, oldValue) {
                if (value != undefined) {
                    element.html(value);
                    $compile(element.contents())(scope);
                }
            });
        }
    };
});