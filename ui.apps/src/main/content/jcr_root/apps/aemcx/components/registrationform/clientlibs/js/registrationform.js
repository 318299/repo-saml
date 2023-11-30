//function myRegisterForm() {
//    let fname = document.getElementById("firstname").value;
//    let lname = document.getElementById("lastname").value;
//    let phno = document.getElementById("contact").value;
//    let gmail = document.getElementById("mail").value;
//	console.log("fname="+fname+" lname="+lname+" phno="+phno+" gmail="+gmail);
//    if (isNaN(fname) || isNaN(lname) || isNaN(phno) || isNaN(gmail)) {
//		if(isNaN(fname)){
//			document.getElementById("firstname-error").innerHTML = "Please enter your first name";
//			console.log("fname error msg: "+document.getElementById("fname-error").innerHTML);
//		}
//        if (isNaN(lname)) {
//			document.getElementById("lastname-error").innerHTML = "Please enter your last name";
//			console.log("lname error msg: "+document.getElementById("lname-error").innerHTML);
//		}
//		if (isNaN(phno)) {
//			document.getElementById("tel-error").innerHTML = "Please enter a valid contact number";
//			console.log("phno error msg: "+document.getElementById("phno-error").innerHTML);
//		}
//		if (isNaN(gmail)) {
//			document.getElementById("mail-error").innerHTML = "Please enter a valid email id";
//			console.log("gmail error msg: "+document.getElementById("gmail-error").innerHTML);
//		}
//    }
//    else{
//		alert ("Your details are submitted successfully!")
//	}
//}

function stripeCheck () {
	let form = document.getElementById("form");
		form.addEventListener("submit", (e) => {
			e.preventDefault();
			
			let eventBox = document.getElementById('events');
			let eventVal = eventBox.options[eventBox.selectedIndex].getAttribute("data-price");
			let eventDesc = eventBox.options[eventBox.selectedIndex].getAttribute("data-desc");
			
			
			//Hidden input for price
			const hiddenPriceVal = document.createElement("input");
			hiddenPriceVal.type = "hidden";
			hiddenPriceVal.id = "price";
			hiddenPriceVal.name = "price-val"
			hiddenPriceVal.value = eventVal;
			
			//Hidden input for desc
			const hiddenDescVal = document.createElement("input");
			hiddenDescVal.type= "hidden";
			hiddenDescVal.id = "desc";
			hiddenDescVal.name = "desc-val";
			hiddenDescVal.value = eventDesc;
			
			
			form.appendChild(hiddenPriceVal);
			form.appendChild(hiddenDescVal);
			
			console.log(eventVal);
			console.log(eventDesc);
			console.log(hiddenPriceVal);
			console.log(hiddenDescVal);
			
			form.submit();
			form.removeChild(document.getElementById("price"));
			form.removeChild(document.getElementById("desc"));
			
		})
	}