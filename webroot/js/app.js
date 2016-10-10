var findAll = function(){
  var a=  $.ajax({
      url:"http://localhost:8080/show",
      type:'GET',
      dataType: 'json',
      success:
        function(response){
          var source = $("#entry-template").html();
          var template = Handlebars.compile(source);
          var wrapper={emails:response};
          var html = template(wrapper);
          $("div.row:first").prepend(html);
      }
    });
}
var newEmail = function(){ console.log("new email"); }
var show = function(id){
  console.log("show email"+ id);
  $('input[name="email_1"]').val(id);
}
var deleteEmail = function(id){ console.log("delete email " + id); }

var routes = {
  '/': findAll,
  '/new': newEmail,
  '/show/:mailId': show,
  '/delete/:mailId': deleteEmail
};
var router = Router(routes);
router.init();

