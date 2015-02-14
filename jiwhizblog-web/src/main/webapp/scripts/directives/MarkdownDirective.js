angular.module('jiwhizWeb').directive('markdown', 
function() {
    return {
        restrict : 'E',
        scope : {
            content : '@'
        },
        transclude : false,
        link : function(scope, element, attrs) {
            scope.$watch('content', function(value, oldValue) {
                var html = md.toHtml(value);
                element.html(html);
            });
        }
    };
});

var md = function() {
    marked.setOptions({
        gfm : true,
        pedantic : false,
        sanitize : true,
        // callback for code highlighter
        highlight : function(code, lang) {
            hljs.tabReplace = '  ';
            if (lang != undefined)
                return hljs.highlight(lang, code).value;

            return hljs.highlightAuto(code).value;
        }
    });

    var toHtml = function(markdown) {
        if (markdown == undefined)
            return '';

        return marked(markdown);
    };

    hljs.tabReplace = '  ';

    return {
        toHtml : toHtml
    };
}();