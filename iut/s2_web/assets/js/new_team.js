function load(){
	defaultSportSelect = document.getElementById("default_sport");
	passwordRequiredCheckbox = document.getElementById("password_required");

	defaultSportSelect.addEventListener("change", defaultSportSelectListener);
	defaultSportSelectListener();
	passwordRequiredCheckbox.addEventListener("change", passwordRequiredCheckboxListener);
	passwordRequiredCheckboxListener();
}

function defaultSportSelectListener(){
	customSportTextBox = document.getElementById("custom_sport");

	if(defaultSportSelect.options[defaultSportSelect.selectedIndex].value == "other"){
		customSportTextBox.style.display = "inline";
	}
	else{
		customSportTextBox.style.display = "none";
	}
}
function passwordRequiredCheckboxListener(){
	passwordTextBox = document.getElementById("password");

	if(passwordRequiredCheckbox.checked){
		passwordTextBox.style.display = "inline";
	}
	else{
		passwordTextBox.style.display = "none";
	}
}


window.onload = load();