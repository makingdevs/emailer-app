class @.EmailerManager

  baseURL = "http://localhost:8000"

  new:  ->
    $.ajax
      data: $('#emailForm').serialize()
      type: 'post'
      url: baseURL + '/newEmail'
      success: ->
        console.log "Agregado ok"
        router.setRoute '/'

  readSet: ->
     $.ajax
       data: 'setValue=0'
       url: baseURL + '/showSet'
       type: 'post'
       success: (response) ->
         console.log 'response'
       error: ->
         console.log 'error al procesar'

