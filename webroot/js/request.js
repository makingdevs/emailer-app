//Js for all ajax petitions

//New Add Email
function sendNewEmail(){
	alert("Agregando Nuevo Email...Espera siguiente mensaje");
	$.ajax({
			data: $("#emailForm").serialize(),
			type: 'post',
			url: 'http://localhost:8080/newEmail',
			success: function(){
			alert("Email agregado exitosamente");
			}
	});

}

//ajax function for show all emails
function sendReadEmails(){
	alert("Send Read Emails");
	$.ajax({
				url:"http://localhost:8080/show",
				type:'GET',
				dataType: 'json',
				success:
				function(response){
						var source = $("#entry-template").html();
						var template = Handlebars.compile(source);
						var wrapper={emails:response};
						var html = template(wrapper);
						$("#readEmails").html(html);
				}
		});
}

//ajax function for remove email
function sendRemoveEmail(id){
	$.ajax({
			data: "idEmail="+id,
			url: 'http://localhost:8080/remove',
			type: 'post',
			success: function (response) {
				alert("Se elimino registro");
			}
	});

}
