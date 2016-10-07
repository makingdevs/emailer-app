tinymce.init({
  selector: 'textarea',
  height: 500,
  toolbar: 'mybutton | mybutton2 | mybutton3 | insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify',

  menubar: false,
  setup: function (editor) {
    editor.addButton('mybutton', {
      text: 'Add Client Name',
      icon: false,
      onclick: function () {
        editor.insertContent('&nbsp;<b> \{\{nombre_cliente\}\} </b>&nbsp;');
      }
    });
    editor.addButton('mybutton2', {
      text: 'Add Company',
      icon: false,
      onclick: function () {
        editor.insertContent('&nbsp;<b> \{\{nombre_empresa\}\} </b>&nbsp;');
      }
    });
    editor.addButton('mybutton3', {
      text: 'Add Sender',
      icon: false,
      onclick: function () {
        editor.insertContent('&nbsp;<b> \{\{sender\}\} </b>&nbsp;');
      }
    });
  }
});

