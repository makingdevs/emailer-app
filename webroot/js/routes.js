//JS for all routes
// static route-->read Emails
var readEmails=function(){
	//show and hide divs
	$("#start").hide();
	$("#formEmails").hide();
	$("#showEmails").hide();
	$("#readEmails").show();
  sendSetEmails(0);
}

// route for new Email
var newAdd=function(){
//clean inputs and textarea
  $('input').each(function(){ $(this).val(''); });
  tinymce.remove();
  $("textarea").val("");
  //tinymce.init({'selector':'textarea'});
  tiny();
  //validate();
  //show and hide divs
	$("#start").hide();
	$("#formEmails").show();
  $("#readEmails").hide();
	$("#showEmails").hide();
 }

//route for edit Email
var editEmail=function(id){
	sendUpdateEmail(id);
  $("#start").hide();
	$("#formEmails").show();
	$("#readEmails").hide();
	$("#showEmails").hide();
}

//route for preview Email
var viewEmail=function(id){
	sendPreviewEmail(id);
	$("#readEmails").hide();
	$("#showEmails").show();
}

//route for delete Email
var removeEmail=function(id){
	sendRemoveEmail(id);
  //$('#emailDeleted').show();
	$("#start").hide();
	$("#formEmails").hide();
	$("#readEmails").show();
	$("#showEmails").hide();
  sendSetEmails(0);
}

//read the paginates
var readSetEmails=function(skip){
  sendSetEmails(skip);
}

var sendEmail=function(){
  sendRequestSend();
}

//Routes Director JS
var routes = {
	'/': readEmails,
	'/newEmail': newAdd,
	'/sendEmail': sendEmail,
  '/editEmail/:mailId': editEmail,
  '/setEmails/:mailId': readSetEmails,
  '/previewEmail/:mailId': viewEmail,
  '/deleteEmail/:mailId': removeEmail
};

var router = Router(routes);
router.init();
