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
}

function checkEverything() {

	var heureStart = document.getElementById("timeStart");
	var dateStart = document.getElementById("dateStart");
	var titre = document.getElementById("titre");
	var content = document.getElementById("content");
	var location = document.getElementById("location");

	if (titre.value.length != 0 && content.value.length != 0
			&& location.value.length != 0 && dateStart.value.length != 0 && heureStart.value.length != 0)
	{
		document.getElementById("boutonCreation").disabled = false;
	} else
		{
		document.getElementById("boutonCreation").disabled = true;
		}
	
}

function checkEverythingModified() {

	var heureStart = document.getElementById("timeStart");
	var dateStart = document.getElementById("dateStart");
	var titre = document.getElementById("titre");
	var content = document.getElementById("content");
	var location = document.getElementById("location");

	
	if (titre.value.length != 0 && content.value.length != 0
			&& location.value.length != 0 && dateStart.value.length != 0 && heureStart.value.length != 0)
	{
		document.getElementById("boutonModifier").disabled = false;
	} else
		{
		document.getElementById("boutonModifier").disabled = true;
		}
}