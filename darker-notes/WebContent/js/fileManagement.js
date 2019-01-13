function loadFile(fileID) {
	//var emailIn = document.getElementById("emailIn").value;
	$.ajax({
        	type: "POST",
        	url: "GetFileContent",
        	async: true,
        	data: {
				file: fileID
        	},
        	success: function(result) {
        		if (result.success == "true") {
        			//replace what's in the editor with data returned from the servlet
        			document.getElementById("text-area").innerHTML=result.data.rawData;
        			document.getElementById("text-title").innerHTML=result.data.fileName;
        			sessionStorage.setItem("currentFileID", fileID);
        			
        			// enable preview button if the file is not empty
        			togglePreviewButton($('#text-area'));
        			
        		}
        		else {
        			//Update error message html to display error message
        			//document.getElementById("errorMsg").innerHTML = result.data.errorMsg;
        			console.log("Failure in GetFileContent + " + result.data.errorMsg)
        		}
        	},
            error: function (jqXHR, exception) {
                var msg = '';
                if (jqXHR.status === 0) {
                    msg = 'Not connect.\n Verify Network.';
                } else if (jqXHR.status == 404) {
                    msg = 'Requested page not found. [404]';
                } else if (jqXHR.status == 500) {
                    msg = 'Internal Server Error [500].';
                } else if (exception === 'parsererror') {
                    msg = 'Requested JSON parse failed.';
                } else if (exception === 'timeout') {
                    msg = 'Time out error.';
                } else if (exception === 'abort') {
                    msg = 'Ajax request aborted.';
                } else {
                    msg = 'Uncaught Error.\n' + jqXHR.responseText;
                }
                console.log(msg)
                $('#signin-error').html(msg);
            },
        })
}

function addFile() {
	var emailIn = sessionStorage.getItem("email");
	
	$.ajax({
    	type: "POST",
    	url: "AddFile",
    	async: true,
    	data: {
			email: emailIn,
			file: "New File",
			data: ""
    	},
    	success: function(result) {
    		if (result.success == "true") {
    			//sessionStorage.setItem("currentFileID", fileID);
    			document.getElementById("text-area").innerHTML="";
    			document.getElementById("text-title").innerHTML=result.data.fileName;
    			sessionStorage.setItem("currentFileID", result.data.newFileId);
    			updateSidebar();
    		}
    		else {
    			//Update error message html to display error message
    			//document.getElementById("errorMsg").innerHTML = result.data.errorMsg;

    		}
    	}
    })
	
}

function copyFile() {
	var emailIn = sessionStorage.getItem("email");
	var fileName = "";
	if (document.getElementById("text-title").innerHTML.length > 0) {
		fileName = "Copy of " + document.getElementById("text-title").innerText;
	} else {
		fileName = "New File";
	}
	
	var rawData = "";
	
	if (sessionStorage.getItem("currentFileID") != "-1") {
		rawData = document.getElementById("text-area").innerHTML;
	}
	
	
	
	$.ajax({
    	type: "POST",
    	url: "AddFile",
    	async: true,
    	data: {
			email: emailIn,
			file: fileName,
			data: rawData
    	},
    	success: function(result) {
    		if (result.success == "true") {
    			
    			document.getElementById("text-title").innerHTML=result.data.fileName;
    			sessionStorage.setItem("currentFileID", result.data.newFileId);
    			updateSidebar();
    		}
    		else {
    			//Update error message html to display error message
    			//document.getElementById("errorMsg").innerHTML = result.data.errorMsg;

    		}
    	}
    })
}

function updateSidebar() {
	var emailIn = sessionStorage.getItem("email");
	
    $.ajax({
        type: "POST",
        url: "GetFiles",
        async: true,
        data: {
             email: emailIn
        },
        success: function(result) {
               console.log(result)
               document.getElementById("sidebar-files").innerHTML = "";
               $("#sidebar-files").append(result);
               
               // listen for delete button events
               spawnDeleteButtonListener();
               // initialize popovers for delete buttons
               initDeletePopovers();
        }
    });
}

function deleteFile(fileId) {
	var currFileID = fileId;
	var email = sessionStorage.getItem("email");
	
	if (currFileID != "-1") {
		//delete currFileid for user email
		$.ajax({
	    	type: "POST",
	    	url: "DeleteFile",
	    	async: true,
	    	data: {
				email: email,
				fileId: currFileID
	    	},
	    	success: function(result) {
	    		if (result.success == "true") {
	    			//sessionStorage.setItem("currentFileID", fileID);
	    			document.getElementById("text-area").innerHTML="";
	    			document.getElementById("text-title").innerHTML="New File";
	    			sessionStorage.setItem("currentFileID", "-1");
	    			updateSidebar();
	    		}
	    	}
	    })
		
	}
}