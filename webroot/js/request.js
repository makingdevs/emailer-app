//Js for all ajax petitions

//New Add Email
function sendNewEmail(){
//	alert("Agregando Nuevo Email...Espera siguiente mensaje");
  //limpiando inputs
  $.ajax({
			data: $("#emailForm").serialize(),
			type: 'post',
			url: 'http://localhost:8080/newEmail',
			success: function(){
			alert("Email agregado exitosamente");
	//		alert($("#emailForm").serialize());
      }
	});

}

//ajax function for show all emails
function sendReadEmails(){
  //	alert("Send Read Emails");
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
  paginate();
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
//	alert("Ajax submit");
	$.ajax({
			data: "idEmail="+id,
			url: 'http://localhost:8080/showEmail',
			type: 'post',
			success: function (response) {
//						alert("populate");
						//show divs
						$("#formEmails").show();
						$("#readEmails").hide();
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

//Ajax request for update the form at vertx
function sendRefreshEmail(){
//	var tiny= tinyMCE.activeEditor.getContent();
//	alert("Esto se va a guardar:"+ $("#emailForm").serialize()); //+ " &contentEmail="+tiny,
	$.ajax({
		data: $("#emailForm").serialize(), //+ " &contentEmail="+tiny,
		type: 'post',
		url: 'http://localhost:8080/update',
		success: function(){
		alert("Email Actualizado ");
		}
	});
}

function sendPreviewEmail(id){
//	alert("Previsualizando Enviando AJAX request");
	$.ajax({
			data: "idEmail="+id,
			url: 'http://localhost:8080/showEmail',
			type: 'post',
			success: function (response) {
	//				alert("ok recuperando y visualizando");
					var json=$.parseJSON(response);
					var source=$("#previewTemplate").html();
					var template=Handlebars.compile(source);
					var html = template(json);
					$("#showEmails").html(html);
					$("#previewBody").append(json.content);
      }
			});
}

function sendSetEmails(skip){
  alert("Skip: "+skip);
  $.ajax({
    data: "setValue="+skip,
    url:"http://localhost:8080/showSet",
    type:'post',
    success:
      function(response){
        var source = $("#entry-template").html();
        var template = Handlebars.compile(source);
        var wrapper={emails:response};
        var html = template(wrapper);
        $("#readEmails").html(html);
        paginate();
      },
    error:
      function(){
        alert("error al procesar");
      }
  });
}

function paginate(){
  $.ajax({
    url:"http://localhost:8080/countTotal",
    type:'GET',
    dataType: 'json',
    success:
      function(response){
        alert(response);
        count=response;
        var pages= (count%10==0)? parseInt(count/10) : parseInt((count/10)+1);
//        alert(pages);
        var html="<ul>";
        var i;
        var skip=0;
        for(i=1; i<=pages; i++){
          html=html.concat("<li><a href='#/setEmails/"+skip+"'>"+i+"</a></li>");
          skip=skip+10;
        }
        html=html.concat("</ul>");
        $("#reader").append(html);
        $("ul").addClass("pagination");
      }
  });
}
