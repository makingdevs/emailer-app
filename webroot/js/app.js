$( document ).ready(function() {
  $(".jumbotron").hide();
  $("#reader").hide();
});

var findAll = function(){
  readerEmails();
}

var newEmail = function(){
  readerForms();
  $("#toAdd").show();
  $(".jumbotron").css("background","#b0bec5");
  tinymce.init({'selector':'textarea'});
}

var showOne = function(id){
  readerForms();
  $("#toUpdate").show();
  validate();
  $.ajax({
    data: "idEmail="+id,
    url: 'http://localhost:8080/one',
    type: 'post',
    success: function (response) {
      tinyMCE.remove();
      var json=$.parseJSON(response);
      $(".jumbotron").show()
      $('input[name="email_id"]').val(json._id);
      $('input[name="email_1"]').val(json.receiver);
      $('input[name="email_2"]').val(json.sender);
      $('input[name="asunto"]').val(json.submit);
      $('input[name="title"]').val(json.title);
      $('textarea').val(json.content);
      $(".jumbotron").css("background","#eceff1");
      tinymce.init({'selector':'textarea'});
    }
  });
}

var deleteEmail = function(id){
  $.ajax({
    data: "idEmailRemove="+id,
    url: 'http://localhost:8080/remove',
    type: 'post',
    success: function (response) {
      alert("Se elimino registro");
    }
  });
  readerEmails();
}

function validate(){
  alert("validando");
  $('#mail_form').formValidation({
    message: 'This value is not valid',
    icon: {
      valid: 'glyphicon glyphicon-ok',
      invalid: 'glyphicon glyphicon-remove',
      validating: 'glyphicon glyphicon-refresh'
    },
    fields: {
      email_1: {
        err: '#email_1Message',
        validators: {
          notEmpty: {
            message: 'Debe haber un email destinatario.'
          }
        }
      },
      email_2: {
        err: '#email_2Message',
        validators: {
          notEmpty: {
            message: 'Debe haber un email remitente.'
          }
        }
      },
      asunto: {
        err: '#asuntoMessage',
        validators: {
          notEmpty: {
            message: 'Colocar un asunto.'
          }
        }
      },
      title: {
        err: '#titleMessage',
        validators: {
          notEmpty: {
            message:'Colocar un título.'
          }
        }
      }
    }
  });
};

var saveEmail=function(){
  tinymce.remove();
  $.ajax({
    data: $("#mail_form").serialize(),
    type: 'post',
    url: 'http://localhost:8080/newEmail',
    success: function(){
      console.log("Email agregado exitosamente");
    }
  });
  alert("Email Agregado");
  readerEmails();
}

var updateEmail=function(){
  tinymce.remove();
  $.ajax({
    data: $("#mail_form").serialize(),
    type: 'post',
    url: 'http://localhost:8080/update',
    success: function(){
      console.log(" Email Actualizado ");
    }
  });
  alert("Email Actualizado");
  readerEmails();
}

var previewEmail=function(id){
  $("#start").hide();
  $("#reader").hide();
  //ajax request
  $.ajax({
    data: "idEmail="+id,
    url: 'http://localhost:8080/one',
    type: 'post',
    success: function (response) {
     var json=$.parseJSON(response);
     var source=$("#preview-template").html();
     var template=Handlebars.compile(source);
     var html = template(json);
     $("div.row:first").html(html);
    $("#previewBody").append(json.content);
    }
  });
}

function readerEmails(){
  $.ajax({
    url:"http://localhost:8080/show",
    type:'GET',
    dataType: 'json',
    success:
      function(response){
        $(".jumbotron").hide();
        $("#start").hide();
        var source = $("#entry-template").html();
        var template = Handlebars.compile(source);
        var wrapper={emails:response};
        var html = template(wrapper);
        $("div.row:first").html(html);
      }
  });


}

function readerForms(){
  $("#reader").hide();
  $("#start").hide();
  $(".jumbotron").show();
  tinymce.remove();
  $('input').each(function(){ $(this).val(''); });
  $("textarea").val("");
  $("#toUpdate").hide();
  $("#toAdd").hide();
  validate();
}

var routes = {
  '/': findAll,
  '/new': newEmail,
  '/save': saveEmail,
  '/preview/:mailId': previewEmail,
  '/updateEmail': updateEmail,
  '/showOne/:mailId': showOne,
  '/delete/:mailId': deleteEmail
};

var router = Router(routes);
router.init();
