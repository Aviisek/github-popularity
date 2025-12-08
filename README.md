# Github repository popularity score

Technologies Used

    JDK 25
    Spring boot 4
    Swagger Documentation for api

IDE/ Tool

    Intellij


## To run locally
there are two ways to run it

Using Docker(Preferred)
Run following commands in root directory:

* "docker build -t github-popularity-app ."
* "docker run -p 8080:8080 github-popularity-app"
* once its up, Swagger can be accessed over http://localhost:8080/swagger-ui/index.html

Using gradle

* Make sure you have java 25 or higher installed otherwise you need install it.
* Make sure you have maven 9.2 or higher installed otherwise you need install it.
* then through terminal in root directly run "./gradlew bootRun".
* if there are errors for dependencies "./gradlew clean build" before. it will up the server and Swagger can be accessed over http://localhost:8080/swagger-ui/index.html.
