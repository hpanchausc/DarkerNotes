/*
 * Logout user function
 */

function logout() {
	console.log("logging out " + sessionStorage.getItem("name"))
	
	logOutMap();
	
	// clear sessionStorage
	sessionStorage.clear();
	sessionStorage.setItem("currentFileID", -1);
	sessionStorage.setItem("fileName", "Default File");
	sessionStorage.setItem("signedin", false);
	
	// hide elements, put back into guest mode
	$("title").css("padding-right", "8.5vw");
	$("#signin-button").toggleClass("d-none");
	$("#signout-button").toggleClass("d-none");
	$("#share-button").toggleClass("d-none");
	$("#sidebarCollapse").toggleClass("d-none");
	$("#notification-button").toggleClass("d-none");
	$("#notification-button-wrapper").toggleClass("d-none");
	
	// refresh the webpage
	location.reload();
}