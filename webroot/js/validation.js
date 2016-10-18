$(document).ready(function() {

			$("#start").show();
			$("#preview").hide();
			$("#formEmails").hide();
			$("#readEmails").hide();
      //validate();
		});

function validate(){
	tinymce.init({
        selector: 'textarea',
        setup: function(editor) {
            editor.on('keyup', function(e) {
                $('#emailForm').formValidation('revalidateField', 'contentEmail');
            });
        }
    });

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

                                return text.length <= 200 && text.length >= 5;
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
          sendSetEmails(0);
          $("#readEmails").show();
          $("#formEmails").hide();
        }
        else{
          alert("Email Update");
          tinymce.remove();
          sendRefreshEmail();
          $("#start").hide();
          $("#formEmails").hide();
          $("#readEmails").show();
          $("#showEmails").hide();
          //sendSetEmails(0);
          window.location.href = "http://localhost:8080/static/";
        }
    });
}


