function maxToday() {
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth() + 1; // January is 0!
	var yyyy = today.getFullYear();
	if (dd < 10) {
		dd = '0' + dd
	}
	if (mm < 10) {
		mm = '0' + mm
	}

	today = yyyy + '-' + mm + '-' + dd;
	document.getElementById("dateStart").setAttribute("min", today);
	document.getElementById("dateEnd").setAttribute("min", today);
}

function checkMinBetweenDate() {
	var dateStart = new Date();
	var dateEnd = new Date();

	dateStart = document.getElementById("dateStart");
	dateEnd = document.getElementById("dateEnd");
	if (dateStart.value > dateEnd.value) {
		document.getElementById("dateEnd").setAttribute("min", dateStart);
		document.getElementById("warningDate").style.visibility = "visible";
		document.getElementById("boutonCreation").disabled = true;
	}
	else
		document.getElementById("boutonCreation").disabled = false;
}