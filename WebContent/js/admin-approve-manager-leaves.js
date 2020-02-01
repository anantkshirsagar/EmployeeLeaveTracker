var currentSessionStore = {};

function onLoad(){
	currentSessionStore = getCurrentSession();
	if(currentSessionStore.sessionId == "null" || currentSessionStore.sessionId == "undefined"){
		window.location = "index.html";
	}
	getManagerRecordService();
	
}

function getCurrentSession(){
	return {
		sessionId: sessionStorage.getItem("sessionId"),
		email : sessionStorage.getItem("email")
	};
}

function reload(){
	createTable();
}

var managerRecords = [];

function getManagerRecordService(){
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			if(response == "FAIL"){
				console.log("FAIL");
			} else {
				addManagerRecordIntoArray(JSON.parse(response));
				reload();
				getManagerAllLeaves();
			}
		}
	};
	
	xhttp.open("POST", "AdminServlet", true);
	xhttp.setRequestHeader("userEmailId", currentSessionStore.email);
	xhttp.setRequestHeader("callType", "GET_MANAGER_RECORD");
	xhttp.send();
}

function addManagerRecordIntoArray(manager){
	managerRecords.push(manager);
}

function showDiv(id){
	document.getElementById(id).style.display = "block";
}

function hideDiv(id){
	document.getElementById(id).style.display = "none";
}

function isHidden(element) {
    var style = window.getComputedStyle(element);
    return (style.display === 'none')
}

function goToHomePage(){
	location.href = "index.html";
}

var header = new Array();
header = ['Sr.No', 'Manager Name', 'City', 'Email', 'Contact No'];

function createTable() {
	var table = document.createElement('table');
	table.setAttribute('id', 'table'); // SET THE TABLE ID.
	table.setAttribute('class','table table-striped valign');

	var tr = table.insertRow(-1);

	for (var i = 0; i < header.length; i++) {
		var th = document.createElement('th'); // TABLE HEADER.
		th.innerHTML = header[i];
		tr.appendChild(th);
	}

	
	var div = document.getElementById('printTableDiv');
	div.innerHTML = "";
	div.appendChild(table); // ADD THE TABLE TO YOUR WEB PAGE.
	
	addRow();
}

function addRow() {
	var table = document.getElementById('table');

	var rowCount = table.rows.length; // GET TABLE ROW COUNT.

	for(var i = 0; i < managerRecords.length; ++i){
		//Create new row each time
		
		var tr = table.insertRow(i+1); // TABLE ROW.
		tr = table.insertRow(i+1);
		tr.style.background = "#98D4FF";
		
		var td = document.createElement('td');
		td = tr.insertCell(0);
		var srNoCell = document.createTextNode(i+1);
		td.appendChild(srNoCell);
		
		var nameTD = document.createElement('td');
		nameTD = tr.insertCell(1);
		var nameCell = document.createTextNode(managerRecords[i].name);
		nameTD.appendChild(nameCell);
		
		var cityTD = document.createElement('td');
		cityTD = tr.insertCell(2);
		var cityCell = document.createTextNode(managerRecords[i].city);
		cityTD.appendChild(cityCell);
		
		var emailTD = document.createElement('td');
		emailTD = tr.insertCell(3);
		var emailCell = document.createTextNode(managerRecords[i].email);
		emailTD.appendChild(emailCell);
		
		var contactNoTD = document.createElement('td');
		contactNoTD = tr.insertCell(4);
		var contactNoCell = document.createTextNode(managerRecords[i].contactNo);
		contactNoTD.appendChild(contactNoCell);
	}
}

var leaveRecordHeader = new Array();
leaveRecordHeader = ['Sr.No', 'From', 'To', 'Days', 'Reason', 'Status', 'Approve', 'Cancel'];

var managerLeaveRecord = [];

function addManagerLeaveIntoArray(leaveObject){
	managerLeaveRecord.push(leaveObject);
}

function reloadLeaveRecordTable(){
	createTableForLeavesRecord();
}

function fillLeaveRecordArray(managerLeaveRecord){
	if(managerLeaveRecord){
		for(var i = 0; i < managerLeaveRecord.length; ++i){
			addManagerLeaveIntoArray(managerLeaveRecord[i]);
		}
	}
}

function getManagerAllLeaves(){
	showDiv("leavesRecordDiv");
	var managerRecord = managerRecords[0];
	console.log(managerRecord);
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			console.log("Leave records " +JSON.parse(response));
			if(response == "FAIL"){
				console.log("FAIL");
			} else {
				fillLeaveRecordArray(JSON.parse(response));
				reloadLeaveRecordTable();
			}
		}
	};
	
	xhttp.open("POST", "AdminServlet", true);
	xhttp.setRequestHeader("userEmailId", currentSessionStore.email);
	xhttp.setRequestHeader("callType", "GET_MANAGER_LEAVE_GRID_INFO");
	xhttp.send(JSON.stringify(managerRecord));
	
}

function createTableForLeavesRecord() {
	var table = document.createElement('table');
	table.setAttribute('id', 'leaveTable'); // SET THE TABLE ID.
	table.setAttribute('class','table table-striped valign');

	var tr = table.insertRow(-1);

	for (var i = 0; i < leaveRecordHeader.length; i++) {
		var th = document.createElement('th'); // TABLE HEADER.
		th.innerHTML = leaveRecordHeader[i];
		tr.appendChild(th);
	}

	
	var div = document.getElementById('leavesRecordTable');
	div.innerHTML = "";
	div.appendChild(table); // ADD THE TABLE TO YOUR WEB PAGE.
	
	addRowForLeavesRecord();
}

function addRowForLeavesRecord() {
	var table = document.getElementById('leaveTable');

	var rowCount = table.rows.length; // GET TABLE ROW COUNT.

	for(var i = 0; i < managerLeaveRecord.length; ++i){
		//Create new row each time
		var dateFormat = "DD-MMMM-YYYY";
		
		var tr = table.insertRow(i+1); // TABLE ROW.
		tr = table.insertRow(i+1);
		
		var td = document.createElement('td');
		td = tr.insertCell(0);
		var srNoCell = document.createTextNode(i+1);
		td.appendChild(srNoCell);
		
		var fromDateTD = document.createElement('td');
		fromDateTD = tr.insertCell(1);
		var formattedFromDate = moment(managerLeaveRecord[i].fromDate).format(dateFormat);
		var fromDateCell = document.createTextNode(formattedFromDate);
		fromDateTD.appendChild(fromDateCell);
		
		var toDateTD = document.createElement('td');
		toDateTD = tr.insertCell(2);
		var formattedToDate = moment(managerLeaveRecord[i].toDate).format(dateFormat);
		var toDateCell = document.createTextNode(formattedToDate);
		toDateTD.appendChild(toDateCell);
		
		var daysTD = document.createElement('td');
		daysTD = tr.insertCell(3);
		var daysCell = document.createTextNode(managerLeaveRecord[i].days);
		daysTD.appendChild(daysCell);
		
		var leaveReasonTD = document.createElement('td');
		leaveReasonTD = tr.insertCell(4);
		var leaveReasonCell = document.createTextNode(managerLeaveRecord[i].leaveReason);
		leaveReasonTD.appendChild(leaveReasonCell);
		
		var approvedStatusTD = document.createElement('td');
		approvedStatusTD = tr.insertCell(5);
		var approvedStatusCell = document.createTextNode(managerLeaveRecord[i].approveStatus);
		approvedStatusTD.appendChild(approvedStatusCell);
		
		var currentRecord = JSON.stringify(managerLeaveRecord[i]);
		var approveButtonTD = document.createElement('td');
		approveButtonTD = tr.insertCell(6);
		var approveButton = document.createElement('input');
		approveButton.setAttribute('type', 'button');
		approveButton.setAttribute('value', 'Approve');
		approveButton.setAttribute('name', currentRecord);
		approveButton.setAttribute('onclick', 'approveLeave(this.name)');
		approveButton.setAttribute('class', 'btn btn-default btn-success');
		console.log(managerLeaveRecord[i].approveStatus);
		if(managerLeaveRecord[i].approveStatus == "CANCELLED" || managerLeaveRecord[i].approveStatus == "PENDING"){
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
		if(managerLeaveRecord[i].approveStatus == "APPROVE" || managerLeaveRecord[i].approveStatus == "PENDING"){
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
