function checkForm() {

	var titre = document.getElementById("titre");
	var content = document.getElementById("content");

	if (titre.value.length != 0 && content.value.length != 0) {
		document.getElementById("boutonCreation").disabled = false;
	} else
		document.getElementById("boutonCreation").disabled = true;
}

function checkFormUpdate() {

	var titre = document.getElementById("titre");
	var content = document.getElementById("content");

	if (titre.value.length != 0 && content.value.length != 0) {
		document.getElementById("boutonModifier").disabled = false;
	} else
		document.getElementById("boutonModifier").disabled = true;
}