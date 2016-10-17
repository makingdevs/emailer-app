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

//ajax function for show one email
function sendUpdateEmail(id){
	alert("Ajax submit");
	$.ajax({
			data: "idEmail="+id,
			url: 'http://localhost:8080/showEmail',
			type: 'post',
			success: function (response) {
						alert("populate");
						//show divs
						$("#formEmails").show();
						//populate inputs
						tinyMCE.remove();
						var json=$.parseJSON(response);
						$('input[name="email_id"]').val(json._id);
						$('input[name="receiverEmail"]').val(json.receiver);
						$('input[name="senderEmail"]').val(json.sender);
						$('input[name="subjectEmail"]').val(json.submit);
						$('textarea').val(json.content);
						tinymce.init({'selector':'textarea'});
					}
			});
}
