//JS for all routes

// static route-->read Emails
var readEmails=function(){
	alert("Leyendo Emails");
	//show and hide divs
	$("#start").hide();
	$("#preview").hide();
	$("#formEmails").hide();
	$("#readEmails").show();
	sendReadEmails();
}

// route for new Email
var newAdd=function(){
	alert("Mostrando Forma para Agregar un correo nuevo");
	//show and hide divs
	$("#start").hide();
	$("#preview").hide();
	$("#formEmails").show();
	$("#readEmails").hide();
}

//route for edit Email
var editEmail=function(id){
	alert("Update email: "+id);
	sendUpdateEmail(id);
}

//route for save updatedEmail
var saveEmail=function(){
	alert("Salvando Email Actualizado");
	sendRefreshEmail();
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
	'/updateEmail': saveEmail,
  '/editEmail/:mailId': editEmail,
  '/previewEmail/:mailId': viewEmail,
  '/deleteEmail/:mailId': removeEmail
};

var router = Router(routes);
router.init();
