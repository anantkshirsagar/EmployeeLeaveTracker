var day = fillDays();
var month = fillMonths();
var year = fillYears(2020, 3000);

var currentSessionStore = {};

function onLoad(){
	currentSessionStore = getCurrentSession();
	if(currentSessionStore.sessionId == "null" || currentSessionStore.sessionId == "undefined"){
		window.location = "index.html";
	}
	fillDateDropDown();
	getHolidayRecordGridInfo();
	getCurrentYearRecords();
}

function getHolidayRecordGridInfo(){
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			if(response == "FAIL"){
				console.log("FAIL");
			} else {
				var dataArray = JSON.parse(response);
				for(var i = 0; i < dataArray.length; ++i){
					addHolidayRecordIntoArray(dataArray[i]);
				}
				reloadTable();
			}
		}
	};
	
	xhttp.open("POST", "AdminServlet", true);
	xhttp.setRequestHeader("userEmailId", currentSessionStore.email);
	xhttp.setRequestHeader("callType", "GET_HOLIDAY_RECORD_GRID_INFO");
	xhttp.send(JSON.stringify(holidayData));
}

function fillDateDropDown(){
	var daySelect = document.getElementById("day");
	var monthSelect = document.getElementById("month");
	var yearSelect = document.getElementById("year");
	addOptions(daySelect, day, 31);
	addOptionsForMonth(monthSelect, month, 12);
	addOptions(yearSelect, year, 200);
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

function fillDays(){
	var days = [];
	for(var i = 1; i <= 31; i++){
		days.push(i);
	}
	return days;
}

function fillMonths(){
	var months = [];
	var monthNames = ["JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE","JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER"];
	for(var i = 0; i < 12; ++i){
		var monthObj = {
			monthName: monthNames[i],
			monthValue: (i+1)
		}
		months.push(monthObj);
	}
	return months;
}

function fillYears(fromYear, toYear){
	var years = [];
	for(var i = fromYear; i <= toYear; ++i){
		years.push(i);
	}
	return years;
}

function addOptions(selectTagObject, optionsArray, noOfElements){
	var optionTag = document.createElement("option");
	optionTag.text = "";
	selectTagObject.options.add(optionTag, 0);
	for(var i = 0; i < noOfElements; ++i){
		optionTag = document.createElement("option");
		optionTag.text = optionsArray[i];
		selectTagObject.options.add(optionTag, i+1);
	}
}

function addOptionsForMonth(selectTagObject, optionsArray, noOfElements){
	var optionTag = document.createElement("option");
	optionTag.text = "";
	selectTagObject.options.add(optionTag, 0);
	for(var i = 0; i < noOfElements; ++i){
		optionTag = document.createElement("option");
		optionTag.text = optionsArray[i].monthName;
		selectTagObject.options.add(optionTag, i+1);
	}
}


function isHidden(element) {
    var style = window.getComputedStyle(element);
    return (style.display === 'none')
}

function goToHomePage(){
	location.href = "index.html";
}

var header = new Array();
header = ['Sr.No', 'Holiday Name', 'Holiday Date', 'Edit', 'Delete'];
var holidayData = [];

function addHolidayRecordIntoArray(holidayObject){
	holidayData.push(holidayObject);
}

function addHoliday(){
//	showDiv("showDataTableDiv");
	var holidayName = document.getElementById("holidayName").value;
	var selectedDay = document.getElementById("day").value;
	var selectedMonth = document.getElementById("month").value;
	var selectedYear = document.getElementById("year").value;
	var selectedDate = new Date(selectedYear, getMonthNoByMonthName(selectedMonth), selectedDay);
	
	var holidayObject = {
		holidayName: holidayName,
		holidayDate: selectedDate
	};

	addHolidayRecordIntoArray(holidayObject);
	reloadTable();
}

function getMonthNoByMonthName(monthName){
	for(var i = 0; i < 12; ++i){
		if(month[i].monthName == monthName){
			return i;
		}
	}
	return 0;
}

function removeHoliday(holidayName){
	var index = getHolidayDataIndexByHolidayName(holidayName);
	console.log("index: " +index);
	holidayData.splice(index, 1);
	reloadTable();
}

function editHoliday(editedHolidayObj, index){
	holidayData[index].holidayName = editedHolidayObj.holidayName;
	holidayDay[index].holidayDate = editedHolidayObj.holidayDate;
	reloadTable();
}

function getHolidayObjectByHolidayName(holidayName){
	for(var i = 0; i < holidayData.length; ++i){
		if(holidayData[i].holidayName == holidayName){
			return holidayData[i];
		}
	}
	return null;
}

function getHolidayDataIndexByHolidayName(holidayName){
	for(var i = 0; i < holidayData.length; ++i){
		if(holidayData[i].holidayName == holidayName){
			return i;
		}
	}
	return 0;
}

function reloadTable(){
	if(holidayData){
		showDiv("showDataTableDiv");
	}
	createTable();
}

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
	console.log(holidayData);
	for(var record = 0; record < holidayData.length; ++record){
		//Create new row each time
		var tr = table.insertRow(record+1); // TABLE ROW.
		tr = table.insertRow(record+1);

		var td = document.createElement('td');
		td = tr.insertCell(0);
		var srNoCell = document.createTextNode(record+1);
		td.appendChild(srNoCell);
		
		var holidayNameTD = document.createElement('td');
		holidayNameTD = tr.insertCell(1);
		var holidayNameCell = document.createTextNode(holidayData[record].holidayName);
		holidayNameTD.appendChild(holidayNameCell);
		
		var holidayDateTD = document.createElement('td');
		holidayDateTD = tr.insertCell(2);
		var dateString = moment(holidayData[record].holidayDate).format("DD-MMMM-YYYY");
		var holidayDateCell = document.createTextNode(dateString);
		holidayDateTD.appendChild(holidayDateCell);
		
		var currentRecord = holidayData[record].holidayName + "$" + holidayData[record].holidayDate;

		var editButtonTD = document.createElement('td');
		editButtonTD = tr.insertCell(3);
		var editButton = document.createElement('input');
		editButton.setAttribute('type', 'button');
		editButton.setAttribute('value', 'Edit');
		editButton.setAttribute('name', currentRecord);
		editButton.setAttribute('onclick', 'editRow(this.name)');
		editButton.setAttribute('class', 'btn btn-default btn-success');
		editButtonTD.appendChild(editButton);
		
		var deleteButtonTD = document.createElement('td');
		deleteButtonTD = tr.insertCell(4);
		var deleteButton = document.createElement('input');
		deleteButton.setAttribute('type', 'button');
		deleteButton.setAttribute('value', 'Delete');
		deleteButton.setAttribute('name', currentRecord);
		deleteButton.setAttribute('onclick', 'deleteRow(this.name)');
		deleteButton.setAttribute('class', 'btn btn-default btn-danger');
		deleteButtonTD.appendChild(deleteButton);
	}
}

function getMonthNameByMonthValue(monthValue){
	for(var i = 0; i < 12; ++i){
		if(month[i].monthValue == monthValue){
			return month[i].monthName;
		}
	}
	return null;
}

function deleteRow(holidayObject){
	console.log(holidayObject);
	removeHoliday(holidayObject.holidayName);
}

var editedHolidayObject = {};

function editRow(holidayObjectStr){
	var holidayObject = getHolidayObjectFromString(holidayObjectStr);
	editedHolidayObject = holidayObject;
	
	var daySelect = document.getElementById("editDay");
	var monthSelect = document.getElementById("editMonth");
	var yearSelect = document.getElementById("editYear");
	
	addOptions(daySelect, day, 31);
	addOptionsForMonth(monthSelect, month, 12);
	addOptions(yearSelect, year, 200);
		
	document.getElementById("editHolidayName").value = holidayObject.holidayName;
	var date = holidayObject.holidayDate;
	setDefaultValueToDropDown("#editDay", date.getDate());
	setDefaultValueToDropDown("#editMonth", getMonthNameByMonthValue(date.getMonth()+1));
	setDefaultValueToDropDown("#editYear", date.getFullYear());	
}

function getHolidayObjectFromString(holidayObjectStr){
	console.log(holidayObjectStr);
	var split = holidayObjectStr.split("$");
	return {
		holidayName: split[0],
		holidayDate: new Date(split[1])
	};
}

/*Note: please add prefix # to the id. For ex: #<id-name> */
function setDefaultValueToDropDown(id, value){
    $(id).val(value);
}

function modifyHolidayData(){
	var holidayName = document.getElementById("editHolidayName").value;
	var selectedDay = document.getElementById("editDay").value;
	var selectedMonth = document.getElementById("editMonth").value;
	var selectedYear = document.getElementById("editYear").value;
	var selectedDate = new Date(selectedYear, getMonthNoByMonthName(selectedMonth), selectedDay);
		
	var modifiedObject = {
		holidayName: holidayName,
		holidayDate: selectedDate
	};
	
	for(var i = 0; i < holidayData.length; ++i){
		if(holidayData[i].holidayName == editedHolidayObject.holidayName){
			holidayData[i].holidayName = modifiedObject.holidayName;
			holidayData[i].holidayDate = modifiedObject.holidayDate;
			break;
		}
	}
	
	clearEditFields();
	reloadTable();
}

function clearEditFields(){
	document.getElementById("editHolidayName").value = "";
	setDefaultValueToDropDown("#editDay", "");
	setDefaultValueToDropDown("#editMonth", "");
	setDefaultValueToDropDown("#editYear", "");
}

function saveHolidayDataService(){
	console.log(holidayData);
	
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			if(response == "SUCCESS"){
				alert("Data save successfully.");
			} else {
				alert("Data not saved due to internal server error.");
			}
		}
	};
	
	xhttp.open("POST", "AdminServlet", true);
	xhttp.setRequestHeader("userEmailId", currentSessionStore.email);
	xhttp.setRequestHeader("callType", "ADMIN_ADD_HOLIDAY_SERVICE");
	xhttp.send(JSON.stringify(holidayData));
}

function deletePopup(){
	
}

function emptyArray(){
	holidayData.length = 0;
}

function deleteHolidayDataService(){
	console.log(holidayData);

	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			if(response == "SUCCESS") {
				emptyArray();
				closeDialog("deleteDialog");
				alert("Data removed successfully.");
				reloadTable();
			} else {
				alert("Data not removed due to internal server error.");
			}
		}
	};
	
	xhttp.open("POST", "AdminServlet", true);
	xhttp.setRequestHeader("userEmailId", currentSessionStore.email);
	xhttp.setRequestHeader("callType", "DELETE_ALL_HOLIDAY_RECORD_SERVICE");
	xhttp.send();
	
}

function closeDialog(dialogName){
	$('#'+dialogName).modal('hide');
}

function saveCurrentYearDetails(){
	var totalLeaves = document.getElementById("totalLeaves").value;
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var response = xhttp.responseText;
			if(response == "SUCCESS") {
				alert("Data saved successfully.");
			} else {
				alert("Total leaves cannot be empty.");
			}
		}
	};
	
	xhttp.open("POST", "AdminServlet", true);
	xhttp.setRequestHeader("userEmailId", currentSessionStore.email);
	xhttp.setRequestHeader("callType", "SAVE_CURRENT_YEAR_RECORD_SERVICE");
	xhttp.setRequestHeader("totalLeaves", totalLeaves);
	xhttp.send();
}

function getCurrentYearRecords(){
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var totalLeaves = xhttp.getResponseHeader("totalLeaves");
			var currentYear = xhttp.getResponseHeader("currentYear");
			document.getElementById("currentYear").value = currentYear;
			document.getElementById("totalLeaves").value = totalLeaves;
		}
	};
	
	xhttp.open("POST", "AdminServlet", true);
	xhttp.setRequestHeader("userEmailId", currentSessionStore.email);
	xhttp.setRequestHeader("callType", "GET_CURRENT_YEAR_RECORD_SERVICE");
	xhttp.send();
}