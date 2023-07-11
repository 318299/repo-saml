function myRegisterForm()() {
    let fname = document.getElementById("firstname").value;
    let lname = document.getElementById("lastname").value;
    let phno = document.getElementById("contact").value;
    let gmail = document.getElementById("mail").value;
	console.log("fname="+fname+" lname="+lname+" phno="+phno+" gmail="+gmail);
    if (isNaN(fname) || isNaN(lname) || isNaN(phno) || isNaN(gmail)) {
		if(isNaN(fname)){
			document.getElementById("firstname-error").innerHTML = "Please enter your first name";
			console.log("fname error msg: "+document.getElementById("fname-error").innerHTML);
		}
        if (isNaN(lname)) {
			document.getElementById("lastname-error").innerHTML = "Please enter your last name";
			console.log("lname error msg: "+document.getElementById("lname-error").innerHTML);
		}
		if (isNaN(phno)) {
			document.getElementById("tel-error").innerHTML = "Please enter a valid contact number";
			console.log("phno error msg: "+document.getElementById("phno-error").innerHTML);
		}
		if (isNaN(gmail)) {
			document.getElementById("mail-error").innerHTML = "Please enter a valid email id";
			console.log("gmail error msg: "+document.getElementById("gmail-error").innerHTML);
		}
    }
    else{
		alert ("Your details are submitted successfully!")
	}
}
