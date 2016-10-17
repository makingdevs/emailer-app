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

var newAdd=function(){
	alert("Mostrando Forma para Agregar un correo nuevo");
	//show and hide divs
	$("#start").hide();
	$("#formEmails").show();
	$("#readEmails").hide();
}

//Routes Director JS
var routes = {
	'/': readEmails,
	'/newEmail': newAdd,
};
var router = Router(routes);
router.init();
