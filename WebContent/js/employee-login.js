function showDiv(id){
	document.getElementById(id).style.display = "block";
}

function hideDiv(id){
	document.getElementById(id).style.display = "none";
}

function register(){
	var empRegisterDivId = document.getElementById("empRegisterDiv");
	if(isHidden(empRegisterDivId)){
		console.log("Hidden");
		showDiv("empRegisterDiv");
	} else {
		console.log("Not hidden");
		hideDiv("empRegisterDiv");
	}
}

function registerService(){
	var empName = document.getElementById("empName").value;	
	var empCity = document.getElementById("empCity").value;	
	var empEmail = document.getElementById("empEmail").value;	
	var empPassword = document.getElementById("empPassword").value;	
	var empContactNo = document.getElementById("empContactNo").value;
	
	var employee = {
		name: empName,
		city: empCity,
		email: empEmail,
		password: empPassword,
		contactNo: empContactNo
	};
	
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			if(response == "FAIL"){
				alert("Error while registering the employee");
			} else {
				location.reload();
			}
		}
	};
	
	xhttp.open("POST", "EmployeeServlet", true);
	xhttp.setRequestHeader("callType", "EMPLOYEE_REGISTER_SERVICE");
	xhttp.send(JSON.stringify(employee));
}

function setSessionParameters(){
}

function loginService(){
	var email = document.getElementById("email").value;	
	var password = document.getElementById("password").value;
	
	var employee = {
		email: email,
		password: password
	};
	
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			if(response == "MANAGER"){
				sessionStorage.setItem("email", email);
				var sessionId = sessionIdGenerator(); 
				sessionStorage.setItem("sessionId", sessionId);
				window.location = "manager-leave-page.html";
				
			} else if (response == "VALID_USER") {
				sessionStorage.setItem("email", email);
				var sessionId = sessionIdGenerator(); 
				sessionStorage.setItem("sessionId", sessionId);
				window.location = "employee-leave-page.html";
				
			} else if(response == "INVALID_USER") {
				window.location = "error-page.html";				
			}
		}
	};
	
	xhttp.open("POST", "EmployeeServlet", true);
	xhttp.setRequestHeader("callType", "VALIDATE_EMPLOYEE");
	xhttp.send(JSON.stringify(employee));
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