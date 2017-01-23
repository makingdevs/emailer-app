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


