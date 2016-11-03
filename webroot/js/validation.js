
function validate(){

    tiny();

    $('#emailForm')
        .formValidation({
            framework: 'bootstrap',
            excluded: [':disabled'],
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {

								senderEmail: {
								validators: {
								notEmpty: {
								message: 'The email address is required and can\'t be empty'
													},
								emailAddress: {
								message: 'The input is not a valid email address'
															}
									}
						 },

						 receiverEmail: {
						 validators: {
						 		notEmpty: {
						 		message: 'The email address is required and can\'t be empty'
						 							},
						 		emailAddress: {
						 		message: 'The input is not a valid email address'
						 									}
						 			}
						  },

						 subjectEmail: {
						 validators: {
						 		notEmpty: {
						 		message: 'The subject input is required and can\'t be empty'
						 							}

						 			}
						  },

                contentEmail: {
                    validators: {
                        callback: {
                            message: 'The contentEmail must be between 5 and 200 characters long',
                            callback: function(value, validator, $field) {
                                // Get the plain text without HTML
                                var text = tinyMCE.activeEditor.getContent({
                                    format: 'text'
                                });

                                return text.length <= 20000 && text.length >= 10;
                            }
                        }
                    }
                }
            }
        })
	.on('success.form.fv', function(e) {
				e.preventDefault();

        if($("#email_id").val() == "") {
          //alert("Agregando correo nuevo");
          tinymce.remove();
          sendNewEmail();
          $('#emailForm').formValidation('resetForm', true);
          sendSetEmails(0);
          $('#emailAdded').show();
          $("#readEmails").show();
          $("#formEmails").hide();
        }
        else{
          //alert("Email Update");
          tinymce.remove();
          sendRefreshEmail();
          $('#emailForm').formValidation('resetForm', true);
          $("#start").hide();
          $("#formEmails").hide();
          $("#readEmails").show();
          $("#showEmails").hide();
          sendSetEmails(0);
          $('#emailUpdated').show();
          //window.location.href = "http://localhost:8080/static/";
        }
    });
}

function tiny(){

      tinymce.init({
          selector: 'textarea',
          menubar: false,
          height: 500,
          width: 870,
                plugins: [
                      'autoresize advlist autolink lists link image charmap print preview hr anchor ',
                          'searchreplace wordcount visualblocks visualchars code fullscreen',
                              'insertdatetime media nonbreaking save table contextmenu directionality',
                                  'textcolor colorpicker textpattern imagetools codesample'
                                      ],
                toolbar1: 'formatselect fontsizeselect fontselect | code ',
                toolbar2: ' preview forecolor bold italic underline | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image',
                image_advtab: true,
                setup: function(editor) {
                  editor.on('keyup', function(e) {
                    $('#emailForm').formValidation('revalidateField', 'contentEmail');
                  });
                }
      });

}
