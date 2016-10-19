//Js for all ajax petitions

//New Add Email
function sendNewEmail(){
  //limpiando inputs
  $.ajax({
			data: $("#emailForm").serialize(),
			type: 'post',
			url: 'http://localhost:8080/newEmail',
			success: function(){
      console.log("Email Agregado");
		   // $("#emailAdded").show();
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
      console.log("Email Eliminado ");
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
						$('input[name="updatedEmail"]').val(json.lastUpdate);
						$('input[name="subjectEmail"]').val(json.submit);
						$('textarea').val(json.content);
						tinymce.init({'selector':'textarea'});
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
		    //$("#emailUpdated").show();
      console.log("Email Actualizado ");
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
					$("#previewBody").append(json.content);
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
