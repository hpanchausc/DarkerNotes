// handle Notification popover
// toggles show/hide when #notification-button
// hides #notification-button when user clicks outside of notification button
$('#notification-button').click(function() {
	// loadNotifications(); // DO WE NEED THIS HERE?
    $(this).popover('toggle');
}).blur(function() {
    $(this).popover('hide');
});


function loadNotifications() {
	var emailIn = sessionStorage.getItem("email");	
	$.ajax({
	       type: "POST",
	       url: "getNotifications",
	       async: true,
	       data: {
	            email: emailIn
	       },
	       success: function(result) {
	    	   //do anything if need be
	    	   //put result into the notifications table html
	    	   console.log('incoming notification:', result)

	    	   // synchronously save notification content, then insert into dynamically loaded Bootstrap popup
	    	   $.when($('#notification-content').html(result)).then(function() {
	    		   		console.log('now initializing popover')
	    			   $('#notification-button').popover({
			    		   'title': 'Notifications',
			    		   'html': true,
			    		   'trigger': 'manual',
			    		   'content': function() {
			    			   return $('#notification-content').html();
			    		   }
			    	   })
		    	   }
	    	   );
	    	   
	       }
	});
	
}

// animation for wiggling notification
// used when user gets a new notification, in socket.js around line 179
function wiggleNotificationBell() {
	$.when($('#notification-button').addClass('new-notification')).then(window.setTimeout(function () {
	    $('#notification-button').removeClass('new-notification');
	}, 1000));
}


// unfortunately need these to pass data to acceptNotification and declineNotification
var currNotificationRawData = "";
var currNotificationID= "";
var currNotificationFileName = "";
var currNotificationFromName = "";

function handleNotification(id) {
	console.log("handleNotification id ", id);
	currNotificationID = id;
	
	$.ajax({
    	type: "POST",
    	url: "RetrieveNotification",
    	async: true,
    	data: {
			notificationId: id
    	},
    	success: function(result) {
    		if (result.success == "true") {
    			//Use below to set up preview window
    			//result.data.rawData
    			//result.data.fileName
    			//result.data.fromName
    			
    			$('#notification-preview-title').html(result.data.fileName + " from " + result.data.fromName);
    			
    			$.when(document.getElementById('notification-preview-file-content').innerHTML = result.data.rawData)
    			.then(convert(document.getElementById('notification-preview-file-content').innerText, document.getElementById('notification-preview-file-content')));
    			
    			
    			currNotificationRawData = result.data.rawData;
    			currNotificationFileName = result.data.fileName;
    			currNotificationFromName = result.data.fromName;
    			
    			//display preview modal with option to accept/decline file
    			$('#notification-preview-modal').modal('show');
    			
    		} else {
    			console.log("Error in RetrieveNotification")
    		}
    	}
    })
	
}

function acceptNotification(notificationId, fileName, rawData) {
	console.log('accept notificiation id', notificationId)
	
	$.ajax({
    	type: "POST",
    	url: "AcceptNotification",
    	async: true,
    	data: {
			notificationId: notificationId,
			fileName: fileName,
			rawData: rawData
    	},
    	success: function(result) {
    		if (result.success == "true") {
    			console.log('acceptNotification')
				// refresh sidebar file list to include accepted file
    			updateSidebar();
    			// refresh notification list after removing the notification
    			loadNotifications();
    			
    		} else {
    			console.log("Error in acceptNotification")
    			// refresh sidebar file list to include accepted file
    			updateSidebar();
    			// refresh notification list after removing the notification
    			loadNotifications();
    		}
    	}
    })
	
	//accept the file
	//servlet call
	
	//set notification isRead = 1;
	//generate new file in Files
	//generate new fow in Access for specified user
	
	//on success, add to sidebar, update notification table
	
}

function declineNotification(id) {
	console.log('decline notificiation id', id)
	
	//decline the file
	//servlet call
	
	$.ajax({
    	type: "POST",
    	url: "DeclineNotification",
    	async: true,
    	data: {
			notificationId: id
    	},
    	success: function(result) {
    		if (result.success == "true") {
    			console.log('declineNotification')
    			// refresh notification list after removing the notification
    			loadNotifications();	
    		} else {
    			console.log("Error in declineNotification")
    			loadNotifications();
    			updateSidebar();
    		}
    	}
    })
}

// listen for clicks on accept and decline buttons
$('#notification-accept-button').on('click', function() {
	console.log('notification-accept-button CLICKED');
	acceptNotification(currNotificationID, currNotificationFileName, currNotificationRawData);
});
$('#notification-decline-button').on('click', function() {
	console.log('notification-decline-button CLICKED');
	declineNotification(currNotificationID);
});