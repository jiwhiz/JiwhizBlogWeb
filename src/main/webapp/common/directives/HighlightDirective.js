angular.module('jiwhizblog').directive('highlight', function() {
    return {
        restrict : 'EA',
        scope : {
            lang : '@'
        },
        replace : true,
        transclude : false,
        link : function(scope, element, attrs) {
            scope.$watch('lang', function(value, oldValue) {
                var rawContent = element.html();
                var html = hljs.highlight(value, rawContent).value;
                element.html(html);
            });
        }
    };
});