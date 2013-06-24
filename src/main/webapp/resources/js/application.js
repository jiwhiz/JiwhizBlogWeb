//Tag Cloud setup
$.fn.tagcloud.defaults = {
	size: {start:10, end: 20, unit: 'pt'},
	color: {start: '#0099FF', end: '#0033FF'}
};

$(function () {
	$('#tagcloud a').tagcloud();
});
