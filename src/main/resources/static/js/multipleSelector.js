function selectIngredient(select) {
	var option = select.options[select.selectedIndex];
	var ul = select.parentNode.getElementsByTagName('ul')[0];
	
	var choices = ul.getElementsByTagName('input');
	var text = document.createTextNode(option.firstChild.data);
	
	for (var i = 0; i < choices.length; i++) {
		if (choices[i].value == option.value)
			return;
	}
	
	var li = document.createElement('li');
	var input = document.createElement('input');
	
	input.type = 'hidden';
	input.name = 'ingredients[]';
	input.value = option.value;

	li.appendChild(input);
	li.appendChild(text);
	li.setAttribute('onclick', 'this.parentNode.removeChild(this);');

	ul.appendChild(li);
}

function selectIngredientChecking(select) {
	var option = select.options[select.selectedIndex];
	var ul = select.parentNode.getElementsByTagName('ul')[0];
	var ulList = document.getElementById("categoryOption").getElementsByTagName("li");
	
	var choices = ul.getElementsByTagName('input');
	var text = document.createTextNode(option.firstChild.data);
	
	for (var i = 0; i < ulList.length; i++) {
		if (ulList[i].innerText == option.firstChild.data)
			return;
	}
	
	/*for (var i = 0; i < choices.length; i++) {
		if (choices[i].value == option.value)
			return;
	}*/
	
	var li = document.createElement('li');
	var input = document.createElement('input');

	input.type = 'hidden';
	input.name = 'ingredients[]';
	input.value = option.value;

	li.appendChild(input);
	li.appendChild(text);
	li.setAttribute('onclick', 'this.parentNode.removeChild(this);');

	ul.appendChild(li);
}

function addingInput()
{
	var ul =  document.getElementById("categoryOption");
	var option = document.getElementById("itemList");
	var input = document.createElement('input');
	var li = document.createElement('li');
	var text = document.createTextNode(option.firstChild.data);
	
	
	input.type = 'hidden';
	input.name = 'ingredients[]';
	input.value = option.value;
	
	li.appendChild(input);
	li.appendChild(text);
	li.setAttribute('onclick', 'this.parentNode.removeChild(this);');

	ul.removeChild(option);
	ul.appendChild(li);
}