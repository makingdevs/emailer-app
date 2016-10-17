$(document).ready(function() {
		
		alert("Validando");
		 //show and hide divs
			$("#start").show();
			$("#formEmails").hide();
			$("#readEmails").hide();
			 
		tinymce.init({
        selector: 'textarea',
        setup: function(editor) {
            editor.on('keyup', function(e) {
                // Revalidate the contentEmail field
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
						 		message: 'The email address is required and can\'t be empty'
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
				// Prevent submit form
				e.preventDefault();
				alert("Vamos a hacer la petición Ajax");
				sendNewEmail();
				});
	});


