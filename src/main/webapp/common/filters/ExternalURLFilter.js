angular.module('jiwhizblog').
	filter('ExternalURL', function() {
		return function(url) {
			if (typeof url == 'undefined') {
				return '';
			}
			if (url == null) {
				return '';
			}
			if (url.substring(0, 7) === 'http://' || url.substring(0, 8) === 'https://') {
				return url;
			}
			return 'http://' + url;
		};
	});
