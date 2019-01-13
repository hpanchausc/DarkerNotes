$(document).ready(function () {
	
	// enable bootstrap tooltips on buttons
	$('[data-toggle="tooltip"]').tooltip();
	// enable bootstrap popovers
	//$('[data-toggle="popover"]').popover()
	
	console.log('hello world!');
	
});

// pops up tooltip that gives the user a custom message for 2 seconds
// input:	Bootstrap tooltip element
//			The message you want to display in the tooltip
function newPopupMsg(element, msg) {
	$.when($(element).attr('data-original-title', msg))
		.then($(element).tooltip('show'))
		.then(window.setTimeout(function () {
			$(element).tooltip('hide');
		}, 2000));
}