//JS for all routes

// static route-->read Emails
var readEmails=function(){
	alert("Leyendo Emails");
	//show and hide divs
	$("#start").hide();
	$("#formEmails").hide();
	$("#showEmails").hide();
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
	$("#showEmails").hide();
  //clean inputs and textarea
  $('input').each(function(){ $(this).val(''); });
  $("textarea").val("");
  tinymce.init({'selector':'textarea'});
}

//route for edit Email
var editEmail=function(id){
	alert("Update email: "+id);
	sendUpdateEmail(id);
	$("#start").hide();
	$("#formEmails").hide();
	$("#readEmails").hide();
	$("#showEmails").hide();
}

//route for save updatedEmail
var saveEmail=function(){
	alert("Salvando Email Actualizado");
	sendRefreshEmail();
	$("#start").hide();
	$("#formEmails").hide();
	$("#readEmails").show();
	$("#showEmails").hide();

	sendReadEmails();

}

//route for preview Email
var viewEmail=function(id){
	alert("Preview email: "+id);
	sendPreviewEmail(id);
	alert("Consulta hecha, pintando divs");
	$("#showEmails").show();
}

//route for delete Email
var removeEmail=function(id){
	alert("Remove email: "+id);
	sendRemoveEmail(id);
	$("#start").hide();
	$("#formEmails").hide();
	$("#readEmails").show();
	$("#showEmails").hide();
	sendReadEmails();
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
