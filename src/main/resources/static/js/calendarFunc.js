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
	
	if (titre.value.length != 0 && content.value.length != 0
			&& location.value.length != 0 && dateStart.value.length != 0 && heureStart.value.length != 0)
	{
		document.getElementById("boutonModifier").disabled = false;
	} else
		{
		document.getElementById("boutonModifier").disabled = true;
		}
}

var from_$input = $('#input_from').pickadate(),
from_picker = from_$input.pickadate('picker')

var to_$input = $('#input_to').pickadate(),
to_picker = to_$input.pickadate('picker')


//Check if there’s a “from” or “to” date to start with.
if ( from_picker.get('value') ) {
to_picker.set('min', from_picker.get('select'))
}
if ( to_picker.get('value') ) {
from_picker.set('max', to_picker.get('select'))
}

//When something is selected, update the “from” and “to” limits.
from_picker.on('set', function(event) {
if ( event.select ) {
to_picker.set('min', from_picker.get('select'))    
}
else if ( 'clear' in event ) {
to_picker.set('min', false)
}
})
to_picker.on('set', function(event) {
if ( event.select ) {
from_picker.set('max', to_picker.get('select'))
}
else if ( 'clear' in event ) {
from_picker.set('max', false)
}
})