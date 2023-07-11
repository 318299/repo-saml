function myContactUsForm() {
    let fname = document.getElementById("fname").value;
    let lname = document.getElementById("lname").value;
    let phno = document.getElementById("phno").value;
    let gmail = document.getElementById("gmail").value;
	console.log("fname="+fname+" lname="+lname+" phno="+phno+" gmail="+gmail);
    if (isNaN(fname) || isNaN(lname) || isNaN(phno) || isNaN(gmail)) {
		if(isNaN(fname)){
			document.getElementById("fname-error").innerHTML = "Please enter your first name";
			console.log("fname error msg: "+document.getElementById("fname-error").innerHTML);
		}
        if (isNaN(lname)) {
			document.getElementById("lname-error").innerHTML = "Please enter your last name";
			console.log("lname error msg: "+document.getElementById("lname-error").innerHTML);
		}
		if (isNaN(phno)) {
			document.getElementById("phno-error").innerHTML = "Please enter a valid contact number";
			console.log("phno error msg: "+document.getElementById("phno-error").innerHTML);
		}
		if (isNaN(gmail)) {
			document.getElementById("gmail-error").innerHTML = "Please enter a valid email id";
			console.log("gmail error msg: "+document.getElementById("gmail-error").innerHTML);
		}
    }
    else{
		if (confirm("Do you want to submit your details")) {
			alert ("Your details are submitted successfully!")
		}
		else {
			alert( "Kindly Resubmit the details")
		}
	}
}