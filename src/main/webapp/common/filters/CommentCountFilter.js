angular.module('jiwhizblog').
	filter('CommentCount', function() {
		return function(count) {
        if (typeof count == "undefined"){
				return '';
			}
			var out = count + ' ';
		
			if (count > 1) {
				out = out + 'Comments';
			}
			else {
				out = out + 'Comment';
			}
			return out;
		};
	});
