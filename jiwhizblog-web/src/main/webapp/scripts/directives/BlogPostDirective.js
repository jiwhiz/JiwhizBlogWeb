'use strict';

angular.module('jiwhizWeb').directive('blogPost',
['$compile', 
function($compile) {
    return {
        restrict : 'E',
        scope : {
            content : '@'
        },
        replace : true,
        transclude : false,
        link : function(scope, element, attrs) {
            scope.$watch('content', function(value, oldValue) {
                element.html(value);
                $compile(element.contents())(scope);
            });
        }
    };
}
]);