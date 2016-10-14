$( document ).ready(function() {
  $(".jumbotron").hide();
  $("#reader").hide();
});

var findAll = function(){
  countEmails();
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

var showNext=function(skip){
  alert("Skip number: "+skip);
  $.ajax({
    data: "setValue="+skip,
    url:"http://localhost:8080/showNext",
    type:'post',
    success:
      function(response){
        alert(response);
        $(".jumbotron").hide();
        $("#start").hide();
        var source = $("#entry-template").html();
        var template = Handlebars.compile(source);
        var wrapper={emails:response};
        var html = template(wrapper);
        $("div.row:first").html(html);
      },
    error:
      function(){
      alert("error al procesar");
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

function countEmails(){
  $.ajax({
    url:"http://localhost:8080/countTotal",
    type:'GET',
    dataType: 'json',
    success:
      function(response){
        count=response;
        alert("El total es de: "+response);
        var pages=parseInt((response/5)+1);
        alert("El números de páginas es "+pages);
      }
  });
}

function readerEmails(){
  $.ajax({
    url:"http://localhost:8080/showFirst",
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
}

var routes = {
  '/': findAll,
  '/new': newEmail,
  '/save': saveEmail,
  '/preview/:mailId': previewEmail,
  '/updateEmail': updateEmail,
  '/showOne/:mailId': showOne,
  '/showNext/:skipMail': showNext,
  '/delete/:mailId': deleteEmail
};

var router = Router(routes);
router.init();
