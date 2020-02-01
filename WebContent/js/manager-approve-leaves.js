var currentSessionStore = {};

function onLoad(){
	currentSessionStore = getCurrentSession();
	if(currentSessionStore.sessionId == "null" || currentSessionStore.sessionId == "undefined"){
		window.location = "index.html";
	}
	
	getEmployeeWiseLeaveRecord();
	getCurrentEmployeeRecord();
}

function getCurrentSession(){
	return {
		sessionId: sessionStorage.getItem("sessionId"),
		email : sessionStorage.getItem("email")
	};
}

function showDiv(id){
	document.getElementById(id).style.display = "block";
}

function hideDiv(id){
	document.getElementById(id).style.display = "none";
}

var employeeWiseLeaveRequest = [];

function fillEmployeeWiseLeaveRequest(empLeaveReqData){
	if(empLeaveReqData){
		for(var i = 0; i < empLeaveReqData.length; ++i){
			addEmpWiseLeaveRequest(empLeaveReqData[i]);
		}
	}
}

function addEmpWiseLeaveRequest(empLeaveReqObject){
	employeeWiseLeaveRequest.push(empLeaveReqObject);
}

function getEmployeeWiseLeaveRecord() {
	showDiv("showDataTableDiv");
	
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			var employeeWiseLeaveRequest = JSON.parse(response);
			console.log(employeeWiseLeaveRequest);
			fillEmployeeWiseLeaveRequest(employeeWiseLeaveRequest);
			createTableForEmployeeRecord();
		}
	};
	
	xhttp.open("POST", "EmployeeServlet", true);
	xhttp.setRequestHeader("userEmailId", sessionStorage.getItem("email"));
	xhttp.setRequestHeader("callType", "GET_EMPLOYEE_WISE_LEAVE_REQUEST");
	xhttp.send();
	
}

var employeeRecord = new Array();
var employeeRecordHeader = ['Sr.No', 'Name', 'Email', 'Contact No', 'Action'];

function createTableForEmployeeRecord() {
	var table = document.createElement('table');
	table.setAttribute('id', 'printTableDiv'); // SET THE TABLE ID.
	table.setAttribute('class','table table-striped valign');

	var tr = table.insertRow(-1);

	for (var i = 0; i < employeeRecordHeader.length; i++) {
		var th = document.createElement('th'); // TABLE HEADER.
		th.innerHTML = employeeRecordHeader[i];
		tr.appendChild(th);
	}

	
	var div = document.getElementById('showDataTableDiv');
	div.innerHTML = "";
	div.appendChild(table); // ADD THE TABLE TO YOUR WEB PAGE.
	
	addRowForEmployeeRecord();
}

function addRowForEmployeeRecord() {
	var table = document.getElementById('printTableDiv');

	var rowCount = table.rows.length; // GET TABLE ROW COUNT.

	for(var i = 0; i < employeeWiseLeaveRequest.length; ++i){
		//Create new row each time
		
		var tr = table.insertRow(i+1); // TABLE ROW.
		tr = table.insertRow(i+1);
		//tr.style.background = defaultColor;
		
		var td = document.createElement('td');
		td = tr.insertCell(0);
		var srNoCell = document.createTextNode(i+1);
		td.appendChild(srNoCell);
		
		var nameTD = document.createElement('td');
		nameTD = tr.insertCell(1);
		var nameCell = document.createTextNode(employeeWiseLeaveRequest[i].employee.name);
		nameTD.appendChild(nameCell);
		
		var emailTD = document.createElement('td');
		emailTD = tr.insertCell(2);
		var emailCell = document.createTextNode(employeeWiseLeaveRequest[i].employee.email);
		emailTD.appendChild(emailCell);
		
		var contactNoTD = document.createElement('td');
		contactNoTD = tr.insertCell(3);
		var contactNoCell = document.createTextNode(employeeWiseLeaveRequest[i].employee.contactNo);
		contactNoTD.appendChild(contactNoCell);
		
		var currentRecord = JSON.stringify(employeeWiseLeaveRequest[i]);
		
		var leaveRecordButtonTD = document.createElement('td');
		leaveRecordButtonTD = tr.insertCell(4);
		var leaveRecordButton = document.createElement('input');
		leaveRecordButton.setAttribute('type', 'button');
		leaveRecordButton.setAttribute('value', 'Approve Leaves');
		leaveRecordButton.setAttribute('name', currentRecord);
		leaveRecordButton.setAttribute('onclick', 'leaveRecordDialog(this.name)');
		leaveRecordButton.setAttribute('class', 'btn btn-default btn-success');
		leaveRecordButton.setAttribute('aria-label','Left Align');
		leaveRecordButton.setAttribute('data-toggle','modal');
		leaveRecordButton.setAttribute('data-target','#managerLeaveRecordDialog');
		leaveRecordButtonTD.appendChild(leaveRecordButton);
	}
}

var leaveRecordArray = new Array();
var leaveRecordHeader = ['Sr.No', 'From Date', 'To Date', 'Days', 'Reason', 'Status', 'Approve', 'Reject'];

function leaveRecordDialog(leaveRecordObject){
	var empLeaveRecord = JSON.parse(leaveRecordObject);
	leaveRecordArray = new Array();
	fillLeaveRecordArray(empLeaveRecord);
	createTableForLeaveRecord();
	console.log(empLeaveRecord);
}

function fillLeaveRecordArray(empLeaveRecord){
	if(empLeaveRecord){
		for(var i = 0; i < empLeaveRecord.leaveRequests.length; ++i){
			addLeaveRecord(empLeaveRecord.leaveRequests[i]);
		}
	}
}

function addLeaveRecord(leaveRecordObject){
	leaveRecordArray.push(leaveRecordObject);
}

function createTableForLeaveRecord() {
	var table = document.createElement('table');
	table.setAttribute('id', 'employeeLeaveRecordPrintTableDiv'); // SET THE TABLE ID.
	table.setAttribute('class','table table-striped valign');

	var tr = table.insertRow(-1);

	for (var i = 0; i < leaveRecordHeader.length; i++) {
		var th = document.createElement('th'); // TABLE HEADER.
		th.innerHTML = leaveRecordHeader[i];
		tr.appendChild(th);
	}

	
	var div = document.getElementById('showLeaveDataTableDiv');
	div.innerHTML = "";
	div.appendChild(table); // ADD THE TABLE TO YOUR WEB PAGE.
	
	addRowForLeaveRecord();
}

function addRowForLeaveRecord() {
	var table = document.getElementById('employeeLeaveRecordPrintTableDiv');

	var rowCount = table.rows.length; // GET TABLE ROW COUNT.

	for(var i = 0; i < leaveRecordArray.length; ++i){
		//Create new row each time
		
		var tr = table.insertRow(i+1); // TABLE ROW.
		tr = table.insertRow(i+1);
		//tr.style.background = defaultColor;
		
		var td = document.createElement('td');
		td = tr.insertCell(0);
		var srNoCell = document.createTextNode(i+1);
		td.appendChild(srNoCell);
		
		var fromDateTD = document.createElement('td');
		fromDateTD = tr.insertCell(1);
		var fromDateCell = document.createTextNode(leaveRecordArray[i].fromDate);
		fromDateTD.appendChild(fromDateCell);
		
		var toDateTD = document.createElement('td');
		toDateTD = tr.insertCell(2);
		var toDateCell = document.createTextNode(leaveRecordArray[i].toDate);
		toDateTD.appendChild(toDateCell);
		
		var daysTD = document.createElement('td');
		daysTD = tr.insertCell(3);
		var daysCell = document.createTextNode(leaveRecordArray[i].days);
		daysTD.appendChild(daysCell);

		var leaveReasonTD = document.createElement('td');
		leaveReasonTD = tr.insertCell(4);
		var leaveReasonCell = document.createTextNode(leaveRecordArray[i].leaveReason);
		leaveReasonTD.appendChild(leaveReasonCell);

		var approveStatusTD = document.createElement('td');
		approveStatusTD = tr.insertCell(5);
		var approveStatusCell = document.createTextNode(leaveRecordArray[i].approveStatus);
		approveStatusTD.appendChild(approveStatusCell);
		
		var currentRecord = JSON.stringify(leaveRecordArray[i]);
		
		var approveButtonTD = document.createElement('td');
		approveButtonTD = tr.insertCell(6);
		var approveButton = document.createElement('input');
		approveButton.setAttribute('type', 'button');
		approveButton.setAttribute('value', 'Approve');
		approveButton.setAttribute('name', currentRecord);
		approveButton.setAttribute('onclick', 'approveLeave(this.name)');
		approveButton.setAttribute('class', 'btn btn-default btn-success');
		console.log(leaveRecordArray[i].approveStatus);
		if(leaveRecordArray[i].approveStatus == "CANCELLED" || leaveRecordArray[i].approveStatus == "PENDING"){
			console.log("In if");
			approveButton.disabled = false;
		} else {
			console.log("In else");
			approveButton.disabled = true;
		}
		approveButtonTD.appendChild(approveButton);
		
		var rejectButtonTD = document.createElement('td');
		rejectButtonTD = tr.insertCell(7);
		var rejectButton = document.createElement('input');
		rejectButton.setAttribute('type', 'button');
		rejectButton.setAttribute('value', 'Reject');
		rejectButton.setAttribute('name', currentRecord);
		rejectButton.setAttribute('onclick', 'rejectLeave(this.name)');
		rejectButton.setAttribute('class', 'btn btn-default btn-danger');
		if(leaveRecordArray[i].approveStatus == "APPROVE" || leaveRecordArray[i].approveStatus == "PENDING"){
			console.log("In if");
			rejectButton.disabled = false;
		} else {
			console.log("In else");
			rejectButton.disabled = true;
		}
		rejectButtonTD.appendChild(rejectButton);
	}
}

function approveLeave(approveLeaveStr){
	var approveLeaveObject = JSON.parse(approveLeaveStr);
	var fromDate = moment(approveLeaveObject.fromDate).format("MMM DD, YYYY");
	var toDate = moment(approveLeaveObject.toDate).format("MMM DD, YYYY");
	approveLeaveObject.fromDate = new Date(fromDate);
	approveLeaveObject.toDate = new Date(toDate);
	console.log(approveLeaveObject);
	
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			if(response == "FAIL"){
				alert("Internal server error...");
			} else {
				alert("Leave approved!");
				closeDialog("managerLeaveRecordDialog");
				location.reload();
			}
		}
	};
	
	xhttp.open("POST", "EmployeeServlet", true);
	xhttp.setRequestHeader("userEmailId", sessionStorage.getItem("email"));
	xhttp.setRequestHeader("callType", "APPROVE_LEAVE_OF_EMPLOYEE");
	xhttp.setRequestHeader("leaveStatus", "APPROVE");
	xhttp.send(JSON.stringify(approveLeaveObject));
}

function rejectLeave(rejectLeaveStr){
	var rejectLeaveObject = JSON.parse(rejectLeaveStr);
	var fromDate = moment(rejectLeaveObject.fromDate).format("MMM DD, YYYY");
	var toDate = moment(rejectLeaveObject.toDate).format("MMM DD, YYYY");
	rejectLeaveObject.fromDate = new Date(fromDate);
	rejectLeaveObject.toDate = new Date(toDate);
	
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			if(response == "FAIL"){
				alert("Internal server error...");
			} else {
				alert("Leave cancelled!");
				closeDialog("managerLeaveRecordDialog");
				location.reload();
			}
		}
	};
	
	xhttp.open("POST", "EmployeeServlet", true);
	xhttp.setRequestHeader("userEmailId", sessionStorage.getItem("email"));
	xhttp.setRequestHeader("callType", "CANCEL_LEAVE_OF_EMPLOYEE");
	xhttp.setRequestHeader("leaveStatus", "CANCELLED");
	xhttp.send(JSON.stringify(rejectLeaveObject));
}

function getCurrentEmployeeRecord(){
	
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			if(response == "FAIL"){
				console.log("FAIL");
			} else {
				var empRecord = JSON.parse(response);
				document.getElementById("currentLoginEmployeeName").innerHTML = empRecord.name;
			}
		}
	};
	
	xhttp.open("POST", "EmployeeServlet", true);
	xhttp.setRequestHeader("userEmailId", currentSessionStore.email);
	xhttp.setRequestHeader("callType", "GET_CURRENT_LOGIN_EMP_DETAILS");
	xhttp.send();
}

function closeDialog(dialogName){
	$('#'+dialogName).modal('hide');
}
