/*
 *	Functions for logging in or signing up
 *	interacts with Servlets
 *
 */

function validateLogin() {
	var emailIn = document.getElementById("emailIn").value;
	var passIn = document.getElementById("passIn").value;

	//Go to servlet to validateLogin()
	$.ajax({
        	type: "POST",
        	url: "ValidateLogin",
        	async: true,
        	data: {
				email: emailIn,
				pass: passIn
        	},
        	success: function(result) {
        		console.log("Result.success = " + result.success)
        		if (result.success == "true") {
        			//document.getElementById("errorMsg").innerHTML = "";
        			//Do session storage for name and email
					sessionStorage.setItem("name", result.data.name);
					console.log("Stored name: " + sessionStorage.getItem("name"));
					sessionStorage.setItem("email", result.data.email);
					console.log("Stored email: " + sessionStorage.getItem("email"));
					sessionStorage.setItem("signedin", true);

					// close the modal window
					$('#signin-modal').modal('hide');

        			onLogIn(emailIn);
        		}
        		else {
        			//var msg = "Sign in error. Please try again"
        			console.log(result.data.errorMsg)
        			//document.getElementById("errorMsg").innerHTML = result.data.errorMsg;
        			$('#signin-error').html(result.data.errorMsg)
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

function onLogIn(email) {
	console.log("In on log in")
	//do stuff on log in
	//display sidebar
	var emailIn = email;
	
	console.log("Getting Files for User: " + emailIn)
	
	logIntoMap();

	// show/hide stuff. Can optimize at later point using single class
	$("#signin-button").toggleClass("d-none");
	$("#signout-button").toggleClass("d-none");
	$("#share-button").toggleClass("d-none");
	$("#sidebar-button-wrapper").toggleClass("d-none");
	$("#notification-button").toggleClass("d-none");
	$("#notification-button-wrapper").toggleClass("d-none");
	//$("#delete-button").toggleClass("d-none");
	$("#title").css("padding-right", "0");
	//console.log("Loading Notifications now")
	loadNotifications();
	
	console.log("Getting Files Now")
    $.ajax({
       type: "POST",
       url: "GetFiles",
       async: true,
       data: {
            email: emailIn
       },
       success: function(result) {
              document.getElementById("sidebar-files").innerHTML = "";
              $("#sidebar-files").append(result);
       }
   });

	//change sign in button to show name and change onclick attribute to call a sign out function
	//display share button
}


function addUser() {
	var emailIn = document.getElementById("emailIn-addUser").value;
	var passIn = document.getElementById("passIn-addUser").value;
	var nameIn = document.getElementById("nameIn-addUser").value;

	console.log('data', emailIn, passIn, nameIn);

	$.ajax({
    	type: "POST",
    	url: "AddUser",
    	async: true,
    	data: {
			email: emailIn,
			pass: passIn,
			name: nameIn
    	},
    	success: function(result) {
    		if (result.success == "true") {
    			console.log("Successfully Added User")
    			sessionStorage.setItem("email", emailIn)
    			sessionStorage.setItem("signedin", true)
    			// close the modal window
    			$('#signup-modal').modal('hide');
    			
    			onLogIn(emailIn);
    		}
    		else {
    			//Update error message html to display error message
    			//document.getElementById("errorMsg").innerHTML = result.data.errorMsg;
    			$('#signup-error').html(result.data.errorMsg);
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
            $('#signin-error').html(msg);
        },
    })
    .then(function(event) {
    	console.log(event)
    })
}
