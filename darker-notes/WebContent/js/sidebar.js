/** For the sidebar animations **/
// for the button menu -> x
$(document).ready(function () {
    $('#sidebarCollapse').on('click', function () {
    	$(this).toggleClass('collapsed');
    });
});

// for the slidein/slideout functionality
$("#sidebarCollapse").click(function(e) {
    e.preventDefault();
    $("#wrapper").toggleClass("toggled");
    
    // listen for delete button events
    spawnDeleteButtonListener();
    
    // initialize popovers for delete buttons
    initDeletePopovers();
});


//initialize popovers for delete buttons
// NOTE: popovers must be mapped to parents if they are not to be dismissed
// 		'container' is parent element, if parent element is not the table row, spawnDeleteButtonListener() 
// 		will make button (and the popover) disappear when we try to hover over the popover. this is because
//		it considers us hovering over another element (whatever the 'container' is set to).
function initDeletePopovers() {
	
    var deleteButtons = $('.delete-button');
    var tableRows = $('.file-table-row');
    for (var i = 0; i < deleteButtons.length; i++) {
    	$(deleteButtons[i]).popover({
		   'title': 'Delete this file?',
		   'html': true,
		   'container': tableRows[i],
		   'placement': 'bottom',
		   'trigger': 'manual',
		   'content': function() {
			   // want to get fileID from onclick (ex: onclick="handleDeletePopover(this, 23 )")
			   //var fileID = ($($('.delete-button')[i]).attr('onclick')).split(' ')[1]; 
			   var yesHtml = '<button class="btn btn-light" onclick="deleteFile(' + deleteFileID + ')">Yes</button>';
			   var noHtml = '<button class="btn btn-light" onclick="$(\'.delete-button\').addClass(\'d-none\');">No</button>';
			   return yesHtml + ' ' + noHtml;
		   }
	   })
    }
}

// listen for when mouse is hovering over file in sidebar to spawn a delete button
function spawnDeleteButtonListener() {
	$('.file-table-row').hover(function() {
		$(this).find('.delete-button').removeClass('d-none');
	}, function() {
		$(this).find('.delete-button').addClass('d-none');
	});
}

var deleteFileID;
// hides #delete-popover when user clicks on or outside of delete button
function handleDeletePopover(element, fileID) {
	console.log('this: ', element)
	console.log('fileID: ', fileID)
	deleteFileID = fileID
	
	$(element).popover('toggle');
	$(element).blur(function() {
	    $(this).popover('hide');
	});
	
	// add listeners to delete yes/no buttons to delete this specific file
	$()
}
