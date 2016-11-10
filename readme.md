# Emailer App

#Before

Please install Vertx 3.0.0, MongoDB and Bower.

The next steps are:
```
1.- bower update
2.- mongod
```
Please check that Mongo are Mode On.
Check the directory third-party

#Run the project

Before you need to create your file conf.json
Please go to https://github.com/makingdevs/emailer-app/wiki/Emailer-App and check how to create.

Clone, and run with:

```
vertx run webserver.groovy -conf conf.json
```

#For Use the Project

+ Please go to your localhost:8080/static/
+ Add new Email
+ Write some like this:
```
Prueba de mi emailer.

Atentamente: ${name}
```
+ Check your email added

#Functions:
+ You can send you a preview
+ You can update your email
+ You can delete your email

#Service

How to call the service:

```
curl -i -H "Content-Type: application/json" -X POST -d '{"id":"58126387e3bce5edc834f6a1", "subject":"Pruebas del Dia Jueves Emailer", "to":"carlo@makingdevs.com", "params":{ "name":"MakingDevs" }}' localhost:8080/serviceEmail``
```


