$( document ).ready(function() {
  $(".jumbotron").hide();
  $("#reader").hide();
});

//---------------------------------------------MyFunctions
function readerForms(){
  $("#reader").hide();
  $("#start").hide();
  $(".jumbotron").show();
  $('input').each(function(){ $(this).val(''); });
  $("textarea").val("");
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
        $("div.row:first").prepend(html);
      }
  });
}

//-------------------------------------------Routes
var findAll = function(){
   readerEmails();
}

var saveEmail=function(){
      $.ajax({
      data: $("#mail_form").serialize(),
      type: 'post',
      url: 'http://localhost:8080/newEmail',
      success: function(){
        alert("Email agregado exitosamente");
      }
      });
      window.location.href = "http://localhost:8080/static/#/";
}


var newEmail = function(){
  readerForms();
  $("#sendForm").attr("href","#/save");
  $(".jumbotron").css("background","#b0bec5");
 /* $("form").attr("onsubmit","newEmail");
      $.ajax({
      data: $("#mail_form").serialize(),
      type: 'post',
      url: 'http://localhost:8080/newEmail',
      success: function(){
        alert("Email agregado exitosamente");
      }
    });
*/
  //$( "#mail_form" ).submit(function( event ) {
  /*   $.ajax({
      data: $("#mail_form").serialize(),
      type: 'post',
      url: 'http://localhost:8080/newEmail',
      success: function(){
        alert("Email agregado exitosamente");
      }
    });*/
   // console.log("Antes del prevent default: "+$("#mail_form").serialize());
   // event.preventDefault();
   // console.log("Despues del prevent default---: "+ $("#mail_form").serialize());
 // });
}

var showOne = function(id){
  $("#reader").hide();
  $("#sendForm").attr("href","#/updateEmail");
  $.ajax({
    data: "idEmail="+id,
    url: 'http://localhost:8080/one',
    type: 'post',
    success: function (response) {
      var json=$.parseJSON(response);
      $(".jumbotron").show()
      $('input[name="email_id"]').val(json._id);
      $('input[name="email_1"]').val(json.receiver);
      $('input[name="email_2"]').val(json.sender);
      $('input[name="asunto"]').val(json.submit);
      $('input[name="title"]').val(json.title);
      $('textarea').val(json.content);
      $(".jumbotron").css("background","#eceff1");
    }
  });
  /*
  $( "#mail_form" ).submit(function( event ) {
    $.ajax({
      data: $("#mail_form").serialize(),
      type: 'post',
      url: 'http://localhost:8080/update',
      success: function(){
        alert("Email Actualizado");
      }
    });
    event.preventDefault();
  });
*/
}

var updateEmail=function(){
  $.ajax({
    data: $("#mail_form").serialize(),
    type: 'post',
    url: 'http://localhost:8080/update',
    success: function(){
      alert("Email Actualizado");
    }
  });
  window.location.href = "http://localhost:8080/static/#/";
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
  $("#reader").hide()
  readerEmails();
}

var routes = {
  '/': findAll,
  '/new': newEmail,
  '/save': saveEmail,
  '/updateEmail': updateEmail,
  '/showOne/:mailId': showOne,
  '/delete/:mailId': deleteEmail
};

var router = Router(routes);
router.init();

