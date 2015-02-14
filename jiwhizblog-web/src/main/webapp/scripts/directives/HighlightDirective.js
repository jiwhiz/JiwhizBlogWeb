angular.module('jiwhizWeb').directive('highlight', 
function() {
    return {
        restrict : 'EA',
        scope : {
            lang : '@',
            size: '@'
        },
        replace : true,
        transclude : false,
        link : function(scope, element, attrs) {
            scope.$watch('lang', function(value, oldValue) {
                var rawContent = element.html();
                rawContent = rawContent.replace(/\{lt\}/g, '<');
                rawContent = rawContent.replace(/\{gt\}/g, '>');
                rawContent = rawContent.replace(/\t/g, '    ');
                var html = hljs.highlight(value, hljs.fixMarkup(rawContent), true).value;
                element.html(html);
                element.addClass(scope.size);
            });
        }
    };
});