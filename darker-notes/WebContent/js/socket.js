/* 
 * for client side websockets implementation
 * needed for autosave
 */
// wait until page is loaded

//setup before functions
var typingTimer;                //timer identifier
var typingTimer2;
var doneTypingInterval = 500;  //time in ms (2 seconds)
var socket;

//on keyup, start the countdown
$('#text-area').keyup(function(){
    clearTimeout(typingTimer);
    if (document.getElementById("text-area").innerHTML.length > 0) {
        typingTimer = setTimeout(doneTyping, doneTypingInterval);
    }
});

$('#text-title').keyup(function(){
    clearTimeout(typingTimer2);
    if (document.getElementById("text-title").innerHTML.length > 0) {
        typingTimer2 = setTimeout(doneTyping, doneTypingInterval);
    }
});

//user is "finished typing," do something
function doneTyping () {
    //Get Current FileID
	var currFileID = sessionStorage.getItem("currentFileID");
	//Get rawData
	var rawFileData = document.getElementById("text-area").innerHTML;
	//Get email
	var emailIn = sessionStorage.getItem("email");
	//Get Filename
	var currFileName = "";
	if (document.getElementById("text-title").innerHTML.length > 0) {
		currFileName = document.getElementById("text-title").innerText;
	} else {
		currFileName = "New File";
	}
	
	console.log("File name: " + currFileName)
	
	if (sessionStorage.getItem("signedin") == "true") {
		console.log("sending a message to server now")
		
		$.ajax({
	    	type: "POST",
	    	url: "autoSave",
	    	async: true,
	    	data: {
				email: emailIn,
				fileID: currFileID,
				fileName: currFileName,
				fileContent: rawFileData 
	    	},
	    	success: function(result) {
	    		sessionStorage.setItem("currentFileID", result)
	    		console.log("Returned from autoSave, updating sidebar")
	    		updateSidebar();
	    		
	    	},
	    	error: function(result) {
	    		console.log("Error from autoSave, updating sidebar")
	    		updateSidebar();
	    	}
		})
	}
	
	//How Do we want to handle the case where a user is making a new file???
	//How do we want to handle the case where a user has not specified a filename
}

function logIntoMap() {
	socket.send(JSON.stringify({
		action: "addUtoMap",
		email: sessionStorage.getItem("email"),
		emailTo: "",
		fileID: "",
		rawData: ""
	}));
}

function logOutMap() {
	socket.send(JSON.stringify({
		action: "removeUFromMap",
		email: sessionStorage.getItem("email"),
		emailTo: "",
		fileID: "",
		rawData: ""
	}));
}

function sendFile() {
	console.log('sendFile() called')
	
	var emailToUser = document.getElementById("shareEmail").value;
	var rawFileData = document.getElementById("text-area").innerHTML;
	var currFileID = sessionStorage.getItem("currentFileID");
	//Get Filename
	var currFileName = "";
	if (document.getElementById("text-title").innerHTML.length > 0) {
		currFileName = document.getElementById("text-title").innerText;
	} else {
		currFileName = "New File";
	}
	
	if ("-1" != currFileID) {
		$.ajax({
	    	type: "POST",
	    	url: "ShareFile",
	    	async: true,
	    	data: {
	    		email: sessionStorage.getItem("email"),
	    		emailTo: emailToUser,
	    		rawData: rawFileData,
	    		fileName: currFileName
	    	},
	    	success: function(result) {
	    		if (result.success == "true") {
	    			console.log(result)
	    			// send file if the destination email exists in the database
	    			socket.send(JSON.stringify({
	    				action: "SendFile",
	    				email: sessionStorage.getItem("email"),
	    				emailTo: emailToUser,
	    				fileID: currFileID,
	    				rawData: rawFileData
	    			}));
	    			$('#share-error').html(result.data.errorMsg)
					// close the modal window
					$('#share-modal').modal('hide');
        		}
        		else {
        			console.log(result.data.errorMsg);
        			$('#share-error').html(result.data.errorMsg)
        		}
	    	},
	    	error: function(result) {
	    		console.log("Error from sendFile")
	    	}
		});
	}
	else {
		$('#share-error').html("Cannot send an empty file.")
	}
}

//Setting up the WebSocket connection for client side
$(document).ready(function () {
	socket = new WebSocket("ws://localhost:8080/darker-notes/ws");
	
	sessionStorage.setItem("signedin", false);
	
	sessionStorage.setItem("currentFileID", -1);
	
	//sessionStorage.setItem("currentFileID", "1");
	sessionStorage.setItem("fileName", "New File");
	//sessionStorage.setItem("fileName", "TestFileConnor");
	
	socket.onopen = function(event) {
		console.log("Connected in socket.js")
	}
	socket.onmessage = function(event) {
		console.log(event.data)
		
		//A message sent from server to client
		
		//Display a new notification to the user with message "event.data"
		
		//{
		// Display a new notification to the user
		//}
		
		// LOAD THE NEW NOTIFICATION AS IT COMES IN
		loadNotifications()
		
		// Notify the user with an alert popup and wiggle the notification bell
		wiggleNotificationBell(); // from notification.js
		newPopupMsg($('#new-notification-popup'), event.data); // from app.js
	}
	socket.onclose = function(event) {
		console.log("Disconnected in socket.js")
	}
	
	
});