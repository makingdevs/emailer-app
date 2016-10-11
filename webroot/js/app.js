var findAll = function(){
  $.ajax({
      url:"http://localhost:8080/show",
      type:'GET',
      dataType: 'json',
      success:
        function(response){
          $(".jumbotron").hide()
          var source = $("#entry-template").html();
          var template = Handlebars.compile(source);
          var wrapper={emails:response};
          var html = template(wrapper);
          $("div.row:first").prepend(html);
      }
    });
}

//-------------------------------------------------------New Email
var newEmail = function(){
  //limpiando los input
  $("#reader").hide()
  $(".jumbotron").show()
  $('input').each(function(){ $(this).val(''); })
  $("textarea").val("");
//  $("form").attr("action","/newEmail");
  $(".jumbotron").css("background","#b0bec5");
//--------------------------------------------mandando datos del formulario
  $("#sendData").click(function(){
    alert("Agregando Email");
    $.ajax({
      data: $("#data").serialize(),
      type: 'post',
      url: 'http://localhost:8080/newEmail',
      success: function(){
        alert("Email agregado");
      },
      complete: function(){
        alert("Proceso completado");
      }
    });

//  window.location.href = "http://localhost:8080/static/#/";

  })

}//end

//-------------------------------------------------------Show Email
var showOne = function(id){
  $.ajax({
    data: "idEmail="+id,
    url: 'http://localhost:8080/one',
    type: 'post',
    success: function (response) {
          //parseando JSON
          var json=$.parseJSON(response);
          //inputs 1by1
          $(".jumbotron").show()
          $('input[name="email_id"]').val(json._id);
          $('input[name="email_1"]').val(json.receiver);
          $('input[name="email_2"]').val(json.sender);
          $('input[name="asunto"]').val(json.submit);
          $('input[name="title"]').val(json.title);
          $('textarea').val(json.content);
          //change the form action
          $(".jumbotron").css("background","#eceff1");
          //$("form").attr("action","/update")

          //---------------------------ajax request
            $("#sendData").click(function(){
              alert("Actualizando");
              $.ajax({
                data: $("#data").serialize(),
                type: 'post',
                url: 'http://localhost:8080/update',
                success: function(){
                  alert("Email Actualizado");
                 //window.location.href = "http://localhost:8080/static/#/";
                },
                complete: function(){
                  alert("Email actualizado");
                }

              });

      })
    }
  });

}

//------------------------------------------------Delete Email
var deleteEmail = function(id){
  $.ajax({
    data: "idEmailRemove="+id,
    url: 'http://localhost:8080/remove',
    type: 'post',
    success: function (response) {
    alert("Se elimino registro");
    $("#reader").hide()
    window.location.href = "http://localhost:8080/static/#/";
    }
  });


}

//------------------------------------------------------------------------routes DirectorJS
var routes = {
  '/': findAll,
  '/new': newEmail,
  '/showOne/:mailId': showOne,
  '/delete/:mailId': deleteEmail
};

var router = Router(routes);
router.init();
