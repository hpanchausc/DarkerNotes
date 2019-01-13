if (sessionStorage.getItem("signedin")) {
	$("#emailIn").val(sessionStorage.getItem("email"));
}
console.log(sessionStorage.getItem("signedin"));