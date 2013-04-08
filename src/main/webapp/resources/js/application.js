//Tag Cloud setup
$.fn.tagcloud.defaults = {
	size: {start:10, end: 20, unit: 'pt'},
	color: {start: '#00CC66', end: '#006600'}
};

$(function () {
	$('#tagcloud a').tagcloud();
});
