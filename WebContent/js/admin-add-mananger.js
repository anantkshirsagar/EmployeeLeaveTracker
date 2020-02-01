var currentSessionStore = {};

function onLoad() {
	currentSessionStore = getCurrentSession();
	if(currentSessionStore.sessionId == "null" || currentSessionStore.sessionId == "undefined"){
		window.location = "index.html";
	}
	getAllEmployeeRecordsService();
}

function getCurrentSession() {
	return {
		sessionId : sessionStorage.getItem("sessionId"),
		email : sessionStorage.getItem("email")
	};
}

var employeeRecords = [];

function addEmployee(employee) {
	employeeRecords.push(employee);
}

function getAllEmployeeRecordsService() {
	showDiv("showDataTableDiv");

	var xhttp = new XMLHttpRequest();

	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			if (response == "FAIL") {
				alert("Internal server error");
			} else {
				var dataArray = JSON.parse(response);
				for (var i = 0; i < dataArray.length; ++i) {
					addEmployee(dataArray[i]);
				}
				reloadTable();
			}
		}
	};

	xhttp.open("POST", "AdminServlet", true);
	xhttp.setRequestHeader("userEmailId", currentSessionStore.email);
	xhttp.setRequestHeader("callType", "GET_ALL_EMPLOYEES_SERVICE");
	xhttp.send();
}

function reloadTable() {
	if (employeeRecords) {
		showDiv("showDataTableDiv");
	}
	createTable();
}

function showDiv(id) {
	document.getElementById(id).style.display = "block";
}

function hideDiv(id) {
	document.getElementById(id).style.display = "none";
}

function isHidden(element) {
	var style = window.getComputedStyle(element);
	return (style.display === 'none')
}

var header = new Array();
header = [ 'Sr.No', 'Employee Name', 'City', 'Email', 'Contact No',
		'Make Manager' ];

function createTable() {
	var table = document.createElement('table');
	table.setAttribute('id', 'table'); // SET THE TABLE ID.
	table.setAttribute('class', 'table table-striped valign');

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

	for (var i = 0; i < employeeRecords.length; ++i) {
		// Create new row each time
		// var defaultColor = "#FFA7A7";
		var defaultColor = "";

		if (employeeRecords[i].isManager) {
			defaultColor = "#98D4FF";
		}

		var tr = table.insertRow(i + 1); // TABLE ROW.
		tr = table.insertRow(i + 1);
		tr.style.background = defaultColor;

		var td = document.createElement('td');
		td = tr.insertCell(0);
		var srNoCell = document.createTextNode(i + 1);
		td.appendChild(srNoCell);

		var nameTD = document.createElement('td');
		nameTD = tr.insertCell(1);
		var nameCell = document.createTextNode(employeeRecords[i].name);
		nameTD.appendChild(nameCell);

		var cityTD = document.createElement('td');
		cityTD = tr.insertCell(2);
		var cityCell = document.createTextNode(employeeRecords[i].city);
		cityTD.appendChild(cityCell);

		var emailTD = document.createElement('td');
		emailTD = tr.insertCell(3);
		var emailCell = document.createTextNode(employeeRecords[i].email);
		emailTD.appendChild(emailCell);

		var contactNoTD = document.createElement('td');
		contactNoTD = tr.insertCell(4);
		var contactNoCell = document
				.createTextNode(employeeRecords[i].contactNo);
		contactNoTD.appendChild(contactNoCell);

		var currentRecord = employeeRecords[i].id + "$"
				+ employeeRecords[i].name + "$" + employeeRecords[i].city + "$"
				+ employeeRecords[i].email + "$" + employeeRecords[i].contactNo
				+ "$" + employeeRecords[i].isManager;

		if (employeeRecords[i].isManager) {
			var removeManagerButtonTD = document.createElement('td');
			removeManagerButtonTD = tr.insertCell(5);
			var removeManagerButton = document.createElement('input');
			removeManagerButton.setAttribute('type', 'button');
			removeManagerButton.setAttribute('value', 'Remove Manager');
			removeManagerButton.setAttribute('name', currentRecord);
			removeManagerButton.setAttribute('onclick',
					'removeManager(this.name)');
			removeManagerButton.setAttribute('class',
					'btn btn-default btn-danger');
			removeManagerButtonTD.appendChild(removeManagerButton);
		} else {
			var makeManagerButtonTD = document.createElement('td');
			makeManagerButtonTD = tr.insertCell(5);
			var makeManagerButton = document.createElement('input');
			makeManagerButton.setAttribute('type', 'button');
			makeManagerButton.setAttribute('value', 'Make Manager');
			makeManagerButton.setAttribute('name', currentRecord);
			makeManagerButton.setAttribute('onclick', 'makeManager(this.name)');
			makeManagerButton.setAttribute('class',
					'btn btn-default btn-success');
			makeManagerButtonTD.appendChild(makeManagerButton);
		}

	}
}

function updateEmployeeRecords(employee){
	for (var i = 0; i < employeeRecords.length; ++i) {
		if(employeeRecords[i].name == employee.name){
			employeeRecords[i].isManager = employee.isManager;
		}
	}
}

function removeManager(employeeObjectStr) {
	var employee = getEmployeeObjectFromString(employeeObjectStr);
	employee.isManager = false;

	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			if (response == "FAIL") {
				alert("Internal server error");
			} else {
				updateEmployeeRecords(employee);
				reloadTable();
			}
		}
	};

	xhttp.open("POST", "AdminServlet", true);
	xhttp.setRequestHeader("userEmailId", currentSessionStore.email);
	xhttp.setRequestHeader("callType", "MARK_OR_UNMARK_EMPLOYEE_AS_MANAGER");
	xhttp.send(JSON.stringify(employee));
}

function makeManager(employeeObjectStr) {
	var availableManager = isAnyManagerAvailable();
	if (availableManager != null) {
		alert("You cannnot make more than one manager. Please remove existing manager ["
				+ availableManager.name + "].");
		return;
	}
	var employee = getEmployeeObjectFromString(employeeObjectStr);
	employee.isManager = true;
	
	var xhttp = new XMLHttpRequest();

	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			if (response == "FAIL") {
				alert("Internal server error");
			} else {
				updateEmployeeRecords(employee);
				reloadTable();
			}
		}
	};

	xhttp.open("POST", "AdminServlet", true);
	xhttp.setRequestHeader("userEmailId", currentSessionStore.email);
	xhttp.setRequestHeader("callType", "MARK_OR_UNMARK_EMPLOYEE_AS_MANAGER");
	xhttp.send(JSON.stringify(employee));
}

function isAnyManagerAvailable() {
	if (employeeRecords) {
		for (var i = 0; i < employeeRecords.length; ++i) {
			if (employeeRecords[i].isManager) {
				return employeeRecords[i];
			}
		}
	}
	return null;
}

function getEmployeeObjectFromString(employeeObjectStr) {
	var split = employeeObjectStr.split("$");
	return {
		id : split[0],
		name : split[1],
		city : split[2],
		email : split[3],
		contactNo : split[4],
		isManager : split[5]
	};
}