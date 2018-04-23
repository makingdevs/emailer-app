class @.ViewResolver
  @mergeViewWithModel = (templateName, model) ->
    source = $(templateName).html()
    template = Handlebars.compile source
    template(model)

class @.Paginator
  @paginate: ->
    $.get('http://35.193.189.157/countTotal').done((response)->
      counter = response
      count = counter.count
      pages = if count % 10 == 0 then parseInt(count / 10) else parseInt(count / 10 + 1)
      html = '<ul class=\'paginator\'>'
      skip = 0
      i = 1
      while i <= pages
        html = html.concat('<li class=\'paginate\'><a href=\'#/setEmails/' + skip + '\'>' + i + '</a></li>')
        skip = skip + 10
        i++
      html = html.concat('</ul>')
      $('#paginas').html html
      $('ul.paginator').addClass 'pagination'
      $('li.paginate').addClass 'waves-effect'
      $('#numberPage').html "Emailers: #{count}"
    ).fail ->
      console.log "Error al consultar conteo"

class @.TinyMce
  @addingTextEditor: ->
    tinymce.remove()
    tinymce.init
      selector: 'textarea'
      menubar: false
      plugins:['autoresize advlist autolink lists link image charmap print preview hr anchor '
      'searchreplace wordcount visualblocks visualchars code fullscreen'
      'insertdatetime media nonbreaking save table contextmenu directionality'
      'textcolor colorpicker textpattern imagetools codesample']
      toolbar1: 'formatselect fontsizeselect fontselect | code  preview forecolor bold italic underline | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image'
      image_advtab: true

class @.Validator
  @validateNewForm: ->
    $('#submitEmailer').click ->
      tinyMCE.triggerSave()
      if $('#subjectEmail').val() == ""
        Materialize.toast 'Agrega un título al emailer', 4000
      else if $('#contentEmail').val() == ""
        Materialize.toast 'Agrega contenido al emailer', 4000
      else
        EmailerController.add()

  @validateUpdateForm: ->
    TinyMce.addingTextEditor()
    $('#submitUpdate').click ->
      tinyMCE.triggerSave()
      if $('#subjectEmail').val() == ""
        Materialize.toast 'Agrega un título al emailer', 4000
      else if $('#contentEmail').val() == ""
        Materialize.toast 'Agrega contenido al emailer', 4000
      else
        EmailerController.update()

   @validateSendPreview: ->
     $('#submitPreview').click ->
       if $('#emailPreview').val() != ""
         EmailerController.send()
       else
         Materialize.toast 'Agrega un email para enviar preview', 3000


