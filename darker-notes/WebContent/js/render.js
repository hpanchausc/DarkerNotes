/*
 * Renders the preview window 
 */

// set up converter on loading script
var converter = new showdown.Converter();
converter.setOption('simplifiedAutoLink', true);
converter.setOption('tables', true);

// called when ready to parse and render an HTML element's text
// input: 	a string of source text to be converted (i.e. the markdown source code)
//			a target HTML element (e.g. the preview-shade html element is the target destination for converted markdown for the preview function)
var convert = function(sourceText, targetElement) {
	var html = converter.makeHtml(sourceText)
	targetElement.innerHTML = html;
	console.log('source text', sourceText)
	console.log('converted html', html)
	console.log('target html', targetElement.innerHTML)
	// render math in the preview-shade with KaTeX 
	renderMathInElement(targetElement, [
		{left: "$$", right: "$$", display: true},
		{left: "\\(", right: "\\)", display: false},
		{left: "\\[", right: "\\]", display: true}
	]);
};



/* Preview button code */
$('#preview-button').click(function() {
		$.when(convert(document.getElementById('text-area').innerText, document.getElementById('preview-shade')))
		.then(console.log('render markdown + LaTeX in preview-shade'));
});

//disable preview button if there is no text in input box
$('#text-area').keyup(function() { togglePreviewButton($('#text-area')) } );

function togglePreviewButton(element) {
	$('#preview-popover').popover('toggleEnabled') // toggle the popover
    if(element.text().length != 0) {
    	// turn on button, disable popover
    	$('#preview-popover').popover('hide') // hide the popover
    	$('#preview-popover').popover('disable') // disable the popover
    	$('#preview-button').removeClass('disabled');
    	$('#preview-button').prop('disabled', false); 
    }
    else {
    	// turn off button, enable popover
    	$('#preview-popover').popover('enable') // enable the popover
    	$('#preview-button').addClass('disabled');
    	$('#preview-button').prop('disabled', true);
    }
}