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

$(function() {
    
    $('select').on('change', function() {
    	var elem = document.getElementById("elementType");
    	var checking = false;
    	for (var i = 0; i < elem.options.length;i++)
    		{
    		 if(elem.options[i].selected)
    			 if (elem.options[i].value == 'Evenement')
    				 {
    				 document.getElementById("calendarSearch").style.display = 'block';
    				 checking = true;
    				 }
    		 	//document.getElementById("calendarSearch").style.display = 'none';
    		}
    	if (checking == false)
			 document.getElementById("calendarSearch").style.display = 'none';
    });
});