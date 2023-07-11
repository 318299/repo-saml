function myFunction() {
    var fnameText;
    var lnameText;
    var phnoText;
    var emailText;
    if (confirm("Do you want to submit your details")) {
        alert ("Your details are submitted successfully!")
    }
    else {
        alert( "Kindly Resubmit the details")
    }
    let fname = document.getElementById("fname").value;
    let lname = document.getElementById("lname").value;
    let phno = document.getElementById("phno").value;
    let gmail = document.getElementById("gmail").value;
  // If x is Not a Number or less than one or greater than 10
    if (isNaN(fname)) {
        fnameText = "Please enter your first name";
    }
    if (isNaN(lname)) {
        lnameText = "Please enter your last name";
    }
    if (isNaN(phno)) {
        phnoText = "Please enter a valid contact number";
    }
    if (isNaN(gmail)) {
        emailText = "Please enter a valid email id";
    }
    document.getElementById("fname-error").innerHTML = fnameText;
    document.getElementById("lname-error").innerHTML = lnameText;
    document.getElementById("phno-error").innerHTML = phnoText;
    document.getElementById("gmail-error").innerHTML = fnameText;
}
