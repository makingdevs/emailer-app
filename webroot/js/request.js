//Js for all ajax petitions

//New Add Email
function sendNewEmail(){
  //limpiando inputs
  $.ajax({
			data: $("#emailForm").serialize(),
			type: 'post',
			url: 'http://localhost:8080/newEmail',
			success: function(){
		  $("#emailAdded").show();
      router.setRoute('/');
      }
	});

}

function sendRemoveEmail(id){
	$.ajax({
			data: "idEmail="+id,
			url: 'http://localhost:8080/remove',
			type: 'post',
			success: function (response) {
		  $("#emailDeleted").show();
			}
	});
}

function sendUpdateEmail(id){
	$.ajax({
			data: "idEmail="+id,
			url: 'http://localhost:8080/showEmail',
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
		url: 'http://localhost:8080/update',
		success: function(){
		  $("#emailUpdated").show();
		}
	});
}

function sendPreviewEmail(id){
	$.ajax({
			data: "idEmail="+id,
			url: 'http://localhost:8080/showEmail',
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

      }
			});
}

function sendSetEmails(skip){
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
        count=response;
        var pages= (count%10==0)? parseInt(count/10) : parseInt((count/10)+1);
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

function sendRequestSend(){

				$.ajax({
						data: $("#previewForm").serialize(),
						type: 'post',
						url: 'http://localhost:8080/send',
						success: function(){
		        //$(".alert").show();
            $("#emailSended").show();
						},
						error: function(){
						//alert("error al enviar");
            $("#emailSended").show();
						}
				});


}



