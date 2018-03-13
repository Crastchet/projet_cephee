function checkActivationForm() {

	var displayname = document.getElementById("userDisplayname");
	var email = document.getElementById("userEmail");
	var description = document.getElementById("userDescription");

	if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email.value) 
			&& description.value.length >= 50 
			&& description.value.length <= 500
			&& /^(\w{3,10})$/.test(displayname.value) ) {
		document.getElementById("buttonActivate").disabled = false;
	} else
		document.getElementById("buttonActivate").disabled = true;
}

function getDescriptionMaxLength() {
	return 500;
}

function checkEditProfileForm() {

	var email = document.getElementById("userEmail");
	var description = document.getElementById("userDescription");

	if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email.value) && description.value.length >= 50 && description.value.length <= 500) {
		document.getElementById("buttonEditProfile").disabled = false;
	} else
		document.getElementById("buttonEditProfile").disabled = true;
}