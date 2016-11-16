//Js for all ajax petitions

//New Add Email
function sendNewEmail(){
  //limpiando inputs
  $.ajax({
			data: $("#emailForm").serialize(),
			type: 'post',
			url:  window.APP.url + '/newEmail',
			success: function(){
       new PNotify({
          title: 'Email Agregado Correctamente.',
          text: 'Haz agregado un nuevo Emailer Template al Almacén.',
        });
      router.setRoute('/');
      }
	});

}

function sendRemoveEmail(id){
	$.ajax({
			data: "idEmail="+id,
			url:  window.APP.url + '/remove',
			type: 'post',
			success: function (response) {
        new PNotify({
          title: 'Email Eliminado correctamente.',
          text: 'Haz removido un registro del almacén de Templates.',
        });

      }
  });
}

function sendUpdateEmail(id){
	$.ajax({
			data: "idEmail="+id,
			url:  window.APP.url + '/showEmail',
			type: 'post',
			success: function (response) {
						$("#formEmails").show();
						$("#readEmails").hide();
						tinyMCE.remove();
						var json=$.parseJSON(response);
						$('input[name="email_id"]').val(json._id);
						$('input[name="versionEmail"]').val(json.version);
						$('input[name="createdEmail"]').val(json.dateCreated);
						$('input[name="updatedEmail"]').val(json.lastUpdated);
						$('input[name="subjectEmail"]').val(json.subject);
						$('textarea').val(json.content);
						//tinymce.init({'selector':'textarea'});
					  tiny();
            // validate();
          }
			});
}

function sendRefreshEmail(){
	$.ajax({
		data: $("#emailForm").serialize(), //+ " &contentEmail="+tiny,
		type: 'post',
		url:  window.APP.url + '/update',
    success:function(){
       new PNotify({
        title: 'Actualización Exitosa',
        text: 'El emailer template se actualizó correctamente',
        type: 'success'
      });
    },
    error: function(){
      console.log("Error update")
    },
    complete: function(){
      /*new PNotify({
        title: 'Actualización Completa',
        text: 'El emailer template se actualizó',
      });*/
    }

	});
}

function sendPreviewEmail(id){
	$.ajax({
			data: "idEmail="+id,
			url:  window.APP.url + '/showEmail',
			type: 'post',
			success: function (response) {
					var json=$.parseJSON(response);
					var source=$("#previewTemplate").html();
					var template=Handlebars.compile(source);
					var html = template(json);
					$("#showEmails").html(html);
          $('#iframeID').contents().find("body").html(json.content)
          //tomando el valor de las fechas
          var date=json.dateCreated;
          var update=json.lastUpdate;
          date=parseInt(date);
          update=parseInt(update);
          var dateCreate=moment(date).format('MMMM Do YYYY');
          var updateCreate=moment(update).format('MMMM Do YYYY, h:mm:ss');
          $("#createdDate").html("<small>"+dateCreate+"</small>");
          $("#updatedDate").html("<small>"+updateCreate+"</small>");
          validatePreview();
      }
			});
}

function sendSetEmails(skip){
  $.ajax({
    data: "setValue="+skip,
    url: window.APP.url + '/showSet',
    type:'post',
    success:
      function(response){
        var source = $("#entry-template").html();
        var template = Handlebars.compile(source);
        var wrapper={emails:response, skip:skip};
        var html = template(wrapper);
        $("#readEmails").html(html);
        paginate();
      },
    error:
      function(){
        alert("error al procesar");
      }
  });
  Handlebars.registerHelper('currentIndex', function(idx) {
    //debugger;
    return parseInt(idx) + 1 + parseInt(skip);
  });
}

function paginate(){
  $.ajax({
    url: window.APP.url + '/countTotal',
    type:'GET',
    success:
      function(response){
			  var counter=$.parseJSON(response);
        var count=counter.count;
        var pages= (count%10==0)? parseInt(count/10) : parseInt((count/10)+1);
        var html="<ul>";
        var i;
        var skip=0;
        for(i=1; i<=pages; i++){
          html=html.concat("<li><a href='#/setEmails/"+skip+"'>"+i+"</a></li>");
          skip=skip+10;
        }
        html=html.concat("</ul>");
        $("#paginas").html(html);
        $("ul").addClass("pagination");
        $("li").addClass("marker");
      }
  });
}

function sendRequestSend(){

  var request = $.ajax({
    data: $("#previewForm").serialize(),
    type: 'post',
    url:  window.APP.url + '/send'})
  request.done(function(msg) {
    new PNotify({
      title: 'Solicitud mandada correctamente.',
      text: 'Mensaje: Espera la siguiente notificación de que el correo fue mandado exitosamente.'
    });
  });
  request.fail(function(jqXHR, textStatus){
    console.log("Error al procesar la petición");
  });
  request.always(function(){
    //new PNotify({
    //  title: 'Solicitud mandada correctamente.',
    //  text: 'Mensaje: Espera la siguiente notificación de que el correo fue mandado exitosamente.',
    //  type: 'success'
    //});
  });
}



