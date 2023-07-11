function myFunction() {
    let fname = document.getElementById("fname").value;
    let lname = document.getElementById("lname").value;
    let phno = document.getElementById("phno").value;
    let gmail = document.getElementById("gmail").value;
	console.log("fname="+fname+" lname="+lname+" phno="+phno+" gmail="+gmail);
    if (isNaN(fname)) {
        document.getElementById("fname-error").innerHTML = "Please enter your first name";
    }
    if (isNaN(lname)) {
        document.getElementById("lname-error").innerHTML = "Please enter your last name";
    }
    if (isNaN(phno)) {
        document.getElementById("phno-error").innerHTML = "Please enter a valid contact number";
    }
    if (isNaN(gmail)) {
        document.getElementById("gmail-error").innerHTML = "Please enter a valid email id";
    }
	console.log("fname error msg: "+document.getElementById("fname-error").innerHTML);
    if (confirm("Do you want to submit your details")) {
        alert ("Your details are submitted successfully!")
    }
    else {
        alert( "Kindly Resubmit the details")
    }
}
