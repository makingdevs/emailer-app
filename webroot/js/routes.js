//JS for all routes

var readEmails=function(){
	alert("Leyendo Emails");
	sendReadEmails();
}

var newAdd=function(){
	alert("Mostrando Forma para Agregar un correo nuevo");
}

//Routes Director JS
var routes = {
	'/': readEmails,
	'/newEmail': newAdd,
};
var router = Router(routes);
router.init();
