function showDiv(id){
	document.getElementById(id).style.display = "block";
}

function hideDiv(id){
	document.getElementById(id).style.display = "none";
}

function loginService(){
	var email = document.getElementById("email").value;	
	var password = document.getElementById("password").value;

	
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			console.log(response);
			if(response == "VALID_USER"){
				sessionStorage.setItem("email", email);
				var sessionId = sessionIdGenerator(); 
				sessionStorage.setItem("sessionId", sessionId);
				window.location = "admin-add-holidays.html";
			} else {
				window.location = "error-page.html";
			}
		}
	};
	
	xhttp.open("POST", "LoginServlet", true);
	xhttp.setRequestHeader("userEmailId", sessionStorage.getItem("email"));
	xhttp.setRequestHeader("callType", "ADMIN_LOGIN_SERVICE");
	xhttp.setRequestHeader("adminEmail", email);
	xhttp.setRequestHeader("adminPassword", password);
	xhttp.send();
}


function isHidden(element) {
    var style = window.getComputedStyle(element);
    return (style.display === 'none')
}

function goToHomePage(){
	location.href = "index.html";
}

var sessionIdGenerator = function () {
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
		return v.toString(16);
	});
};
