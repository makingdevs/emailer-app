//JS for all routes

// static route-->read Emails
var readEmails=function(){
	alert("Leyendo Emails");
	//show and hide divs
	$("#start").hide();
	$("#formEmails").hide();
	$("#readEmails").show();
	sendReadEmails();
}

// route for new Email
var newAdd=function(){
	alert("Mostrando Forma para Agregar un correo nuevo");
	//show and hide divs
	$("#start").hide();
	$("#formEmails").show();
	$("#readEmails").hide();
}

//route for update Email
var updateEmail=function(id){
	alert("Update email: "+id);
	sendUpdateEmail(id);
}

//route for preview Email
var viewEmail=function(id){
	alert("Preview email: "+id);
}

//route for delete Email
var removeEmail=function(id){
	alert("Remove email: "+id);
	sendRemoveEmail(id);
}

//Routes Director JS
var routes = {
	'/': readEmails,
	'/newEmail': newAdd,
  '/editEmail/:mailId': updateEmail,
  '/previewEmail/:mailId': viewEmail,
  '/deleteEmail/:mailId': removeEmail
};

var router = Router(routes);
router.init();
