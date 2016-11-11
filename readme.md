
#Emailer App

##Integration with Gradle##

1.- First you need to create the file conf.json in this directory: src/main/groovy/

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
2.- Please do bower update in src/main/groovy/
```
bower update
```
Check your directory src/main/groovy/webroot/third-part, here will be contain the JS files.

3.- Run your mongo server
```
mongod
```

# Run the proyect in localhost

Please go to your directory src/main/groovy/ and run vertx with:
```
vertx run webserver.groovy -conf conf.json
```

If you want to run the Unit Test of the project, please do:
```
vertx run test.groovy -conf conf.json
```

#Run the project with Gradle

1.- In root, please do:
```
./gradlew shadowJar
```
2.- If you want yo run, please do:
```
java -jar build/libs/gradle-verticle-groovy-compiled-3.3.3-fat.jar -conf src/main/groovy/conf.json

```




