
#EMAILER APP Version 2

```
Vertx 3.3.3
Gradle 2.10
Groovy 2.4.7
Bower 1.8.0
```

##Local installation##

1.- First you need to create the file conf.json on root

The content is:
```
{
  "mail" : {
    "hostname" : "smtp.gmail.com",
    "port" : 587,
    "starttls" : "REQUIRED",
    "username" : "user@gmail.com",
    "password" : "password"
  },
  "mongo" : {
    "connection_string" : "mongodb://localhost:27017",
    "db_name" : "emailerDevelop"
  }
}
```
2.- Please update bower in src/main/groovy/
```
bower update
```
Check your directory src/main/groovy/webroot/third-part, here will be contain the JS files.

3.- Run your mongo server. You don't need to create a Mongo Db before.
```
mongod
```
4.- The project runs on localhost:8000, so apply the follow changes:

CHANGE 1: __src/main/resources/webroot/index.html__ change the url _http://emailerv2.modulusuno.com/static/_ for **http://localhost:8000/static/**
```
Line 138: <nav class="navbar">
  (...)
      <a href="http://emailerv2.modulusuno.com/static/">
   (...)
```

CHANGE 2: On __src/main/resources/webroot/js/enviroment.js__ change "http://emailerv2.modulusuno.com" for http://localhost:8000/
```
window.APP = {
  url : "http://emailerv2.modulusuno.com"
};
```

# Finally run the proyect with Gradle
In root:

```
gradle run

```

##How it works##

### Web App ###
1.- Go to **http://localhost:8000/static/** and select "añadir template"

2.- Please fill the emailer form, this going to be an emailer template, include params like **${i_am_groovy_param}**

3.- Preview your template on "Almacén de Emailers", and send you an email preview.

4.- Modify a template, check the last update, and the last version available.

5.- Check the table on "Almacén de Emailers", it contains: #, ID, Subject, Version, and Preview Button.


### Web Service ###
1.- If the web app have a template, you can use the webservice.

2.- Please identify the ID on "Almacén de Emailers"

3.- Please identify the params on the template, this are identify with **${param}**

4.- Send a request to **http://localhost:8000/serviceEmail**

```
Request: POST
Url: http://localhost:8000/serviceEmail
Body:
  {
  "id":"586d4944...",
  "to":"hi@me.com",
  "cc":"hi@me.com",
  "cco":"hi@me.com",
  "subject":"Preview Emailer",
  "params":{
            "message":"Hello world",
            "name":"Vertx",
            "date":"12 de Junio"
            }
  }
```

##### Response of Web Service#####

1.- Correct request.
```
Code: 201
Message: "Solicitud enviada correctamente."
```

2.- Request without body
```
Code: 400
Message: "I can't do my job, please send me something please."
```

3.- Bad request
```
Code: 400
Message: "I can't do my job. You have the follow errors"
```

jj
