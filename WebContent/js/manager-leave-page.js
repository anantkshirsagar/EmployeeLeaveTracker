var currentSessionStore = {};

function onLoad(){
	currentSessionStore = getCurrentSession();
	if(currentSessionStore.sessionId == "null" || currentSessionStore.sessionId == "undefined"){
		window.location = "index.html";
	}
	getAllLeavesService();
	getAllLeavesOfEmployee();
	getAllLeavesCount();
	getCurrentEmployeeRecord();
}

function getCurrentSession(){
	return {
		sessionId: sessionStorage.getItem("sessionId"),
		email : sessionStorage.getItem("email")
	};
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

function getAllLeavesCount(){
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			if(response == "FAIL"){
				console.log("FAIL");
			} else {
				var approvedLeavesCount = xhttp.getResponseHeader("approvedLeavesCount");
				var pendingLeavesCount = xhttp.getResponseHeader("pendingLeavesCount");
				var remainingLeavesCount = xhttp.getResponseHeader("remainingLeavesCount");
				document.getElementById("approvedLeavesDiv").innerHTML = approvedLeavesCount;
				document.getElementById("pendingLeavesDiv").innerHTML= pendingLeavesCount;
				document.getElementById("remainingLeavesDiv").innerHTML = remainingLeavesCount;
			}
		}
	};
	
	xhttp.open("POST", "EmployeeServlet", true);
	xhttp.setRequestHeader("userEmailId", currentSessionStore.email);
	xhttp.setRequestHeader("callType", "GET_ALL_LEAVE_COUNTS");
	xhttp.send();
}

function isHidden(element) {
    var style = window.getComputedStyle(element);
    return (style.display === 'none')
}

var holidayData = [];

function addHolidayRecordIntoArray(holidayObject){
	holidayData.push(holidayObject);
}

function getAllLeavesService(){
	var dateFormat = "DD-MMMM-YYYY";
	
	var xhttp = new XMLHttpRequest();

	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			if(response == "FAIL"){
				console.log("FAIL");
			} else {
				var dataArray = JSON.parse(response);
				for(var i = 0; i < dataArray.length; ++i){
					dataArray[i].holidayDate = moment(dataArray[i]).format(dateFormat);
					addHolidayRecordIntoArray(dataArray[i]);
				}
				displayHolidayLeaveList();
			}
		}
	};

	xhttp.open("POST", "EmployeeServlet", true);
	xhttp.setRequestHeader("userEmailId", currentSessionStore.email);
	xhttp.setRequestHeader("callType", "GET_HOLIDAY_RECORD_GRID_INFO");
	xhttp.send();
	displayHolidayLeaveList();
}

function displayHolidayLeaveList(){
	var holidayListDiv = document.getElementById("holidayListDiv");
	if(holidayData){
		for(var i = 0; i < holidayData.length; ++i){
			var divElement = document.createElement('div');
			divElement.setAttribute("class", "panel-body");
			var leaveText = document.createTextNode(holidayData[i].holidayName +" "+ holidayData[i].holidayDate);
			divElement.appendChild(leaveText);
			holidayListDiv.appendChild(divElement);
		}
	}
}

var leaveRecordHeader = new Array();
leaveRecordHeader = ['Sr.No', 'From', 'To', 'Days', 'Reason', 'Status', 'Action'];

var employeeLeaveRecord = [];

function addEmployeeLeaveRecordIntoArray(leaveRecord){
	employeeLeaveRecord.push(leaveRecord);
}

function getAllLeavesOfEmployee(){
	showDiv("showDataTableDiv");

	var xhttp = new XMLHttpRequest();

	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			if(response == "FAIL"){
				console.log("FAIL");
			} else {
				var dataArray = JSON.parse(response);
				for(var i = 0; i < dataArray.length; ++i){
					addEmployeeLeaveRecordIntoArray(dataArray[i]);
				}
				createTableForLeavesRecord();
			}
		}
	};

	xhttp.open("POST", "EmployeeServlet", true);
	xhttp.setRequestHeader("userEmailId", currentSessionStore.email);
	xhttp.setRequestHeader("callType", "GET_EMPLOYEE_LEAVE_GRID_INFO");
	xhttp.send();
}

function createTableForLeavesRecord() {
	var table = document.createElement('table');
	table.setAttribute('id', 'printTableDiv'); // SET THE TABLE ID.
	table.setAttribute('class','table table-striped valign');

	var tr = table.insertRow(-1);

	for (var i = 0; i < leaveRecordHeader.length; i++) {
		var th = document.createElement('th'); // TABLE HEADER.
		th.innerHTML = leaveRecordHeader[i];
		tr.appendChild(th);
	}

	
	var div = document.getElementById('showDataTableDiv');
	div.innerHTML = "";
	div.appendChild(table); // ADD THE TABLE TO YOUR WEB PAGE.
	
	addRowForLeavesRecord();
}

function addRowForLeavesRecord() {
	var table = document.getElementById('printTableDiv');

	var rowCount = table.rows.length; // GET TABLE ROW COUNT.

	for(var i = 0; i < employeeLeaveRecord.length; ++i){
		//Create new row each time
		
		var dateFormat = "DD-MMM-YYYY";
		var defaultColor = "#98D4FF";
		if(employeeLeaveRecord[i].approveStatus == 'PENDING'){
			defaultColor = "#FFA7A7";
		} else if(employeeLeaveRecord[i].approveStatus == 'CANCELLED'){
			defaultColor = "#F0F0F0";
		}
		
		var tr = table.insertRow(i+1); // TABLE ROW.
		tr = table.insertRow(i+1);
		tr.style.background = defaultColor;
		
		var td = document.createElement('td');
		td = tr.insertCell(0);
		var srNoCell = document.createTextNode(i+1);
		td.appendChild(srNoCell);
		
		var fromDateTD = document.createElement('td');
		fromDateTD = tr.insertCell(1);
		var formattedFromDate = moment(employeeLeaveRecord[i].fromDate).format(dateFormat);
		var fromDateCell = document.createTextNode(formattedFromDate);
		fromDateTD.appendChild(fromDateCell);
		
		var toDateTD = document.createElement('td');
		toDateTD = tr.insertCell(2);
		var formattedToDate = moment(employeeLeaveRecord[i].toDate).format(dateFormat);
		var toDateCell = document.createTextNode(formattedToDate);
		toDateTD.appendChild(toDateCell);
		
		var daysTD = document.createElement('td');
		daysTD = tr.insertCell(3);
		var daysCell = document.createTextNode(employeeLeaveRecord[i].days);
		daysTD.appendChild(daysCell);
		
		var leaveReasonTD = document.createElement('td');
		leaveReasonTD = tr.insertCell(4);
		var leaveReasonCell = document.createTextNode(employeeLeaveRecord[i].leaveReason);
		leaveReasonTD.appendChild(leaveReasonCell);
		
		var approvedStatusTD = document.createElement('td');
		approvedStatusTD = tr.insertCell(5);
		var approvedStatusCell = document.createTextNode(employeeLeaveRecord[i].approveStatus);
		approvedStatusTD.appendChild(approvedStatusCell);
		
		var currentRecord = employeeLeaveRecord[i].id +"$"+ employeeLeaveRecord[i].fromDate +"$" + employeeLeaveRecord[i].toDate +"$"+ employeeLeaveRecord[i].days +"$"+ employeeLeaveRecord[i].employeeId +"$"+ employeeLeaveRecord[i].leaveReason +"$"+ employeeLeaveRecord[i].approveStatus;
		if(employeeLeaveRecord[i].approveStatus == "PENDING"){
			var cancelButtonTD = document.createElement('td');
			cancelButtonTD = tr.insertCell(6);
			var cancelButton = document.createElement('a');
			var cancelText = document.createTextNode("Cancel");
			cancelButton.appendChild(cancelText);
			cancelButton.setAttribute('name', currentRecord);
			cancelButton.setAttribute('onclick', 'cancelLeaveService(this.name)');
			cancelButtonTD.appendChild(cancelButton);
		}
	}
}

function cancelLeaveService(leaveRecordStr){
	var leaveRecord = getLeaveRecordFromString(leaveRecordStr);
	var xhttp = new XMLHttpRequest();

	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			if(response == "FAIL"){
				alert("Internal server error.");
			} else {
				employeeLeaveRecord.length = 0;
				getAllLeavesOfEmployee();
				getAllLeavesCount();
				alert("Leave cancelled successfully.");
			}
		}
	};

	xhttp.open("POST", "EmployeeServlet", true);
	xhttp.setRequestHeader("userEmailId", currentSessionStore.email);
	xhttp.setRequestHeader("callType", "CANCEL_EMPLOYEE_LEAVE");
	xhttp.send(JSON.stringify(leaveRecord));
}

function getLeaveRecordFromString(leaveRecordStr){
	var split = leaveRecordStr.split("$");
	return {
		id: split[0],
		fromDate: new Date(split[1]),
		toDate: new Date(split[2]),
		days: split[3],
		employeeId: split[4],
		leaveReason: split[5],
		approveStatus: split[6]
	};
}

function showDiv(id){
	document.getElementById(id).style.display = "block";
}

function hideDiv(id){
	document.getElementById(id).style.display = "none";
}


function applyLeave(){
	var fromDate = document.getElementById("fromDate").value;
	var toDate = document.getElementById("toDate").value;
	var reason = document.getElementById("leaveReason").value;
	
	var xhttp = new XMLHttpRequest();

	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			if(response == "FAIL"){
				console.log("FAIL");
			} else if (response == "INVALID_DATE_RANGE") {
				alert("Please provide valid date range.");
			} else {
				employeeLeaveRecord.length = 0;
				getAllLeavesOfEmployee();
				getAllLeavesCount();
				alert("Leave applied successfully.");
			}
		}
	};

	xhttp.open("POST", "EmployeeServlet", true);
	xhttp.setRequestHeader("userEmailId", currentSessionStore.email);
	xhttp.setRequestHeader("callType", "APPLY_LEAVE");
	xhttp.setRequestHeader("fromDate", fromDate);
	xhttp.setRequestHeader("toDate", toDate);
	xhttp.setRequestHeader("reason", reason);
	xhttp.send();
}
