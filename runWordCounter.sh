#!/bin/bash
# chmod u+x runWordCounter.sh ### enables the file to become a executable shell script
# thereafter you can run the script `./runWordCounter.sh `

# WordCounter - Spring Boot Java JPA persisted Kotlin word counter application
# This application is built on Spring Boot web server
# Spring Boot Java/H2 Datasource is used as a lightweight database
# Kotlin
# Developer: Kevin Phillips

# Use Case
### Accept a JSON POST request to /wordCount endpoint and return a count of the words originally sent
### Handling: Only return a word count response if the ID is unique -> disregard duplicate IDs

# Instructions
### Two options: run this shell script on a UNIX terminal or copy and paste each line into a UNIX terminal

# Notes
### src/main/resources/application.properties -> Spring Datasource SQL schema properties
### src/main/resources/sql/schema.sql -> Spring Datasource SQL Database install script
# Troubleshoot the Database
### The Spring Datasource Database will be created in the /data/ folder within the project path
### Try deleting the *.sql files within this folder to remove and reset the persisted database


### Start
curl -H "Content-Type: application/json" --request POST --data '{ "id": "123", "message": "hello world" }' http://localhost:8080/wordCount
# return 2
curl -H "Content-Type: application/json" --request POST --data '{ "id": "123", "message": "hello world" }' http://localhost:8080/wordCount
# no response
curl -H "Content-Type: application/json" --request POST --data '{ "id": "111", "message": "hello world three four five" }' http://localhost:8080/wordCount
# return 7
curl -H "Content-Type: application/json" --request POST --data '{ "id": "1", "message": "hello world" }' http://localhost:8080/wordCount
# return 9
curl -H "Content-Type: application/json" --request POST --data '{ "id": "1", "message": "hello world" }' http://localhost:8080/wordCount
# no response
curl -H "Content-Type: application/json" --request POST --data '{ "id": "111", "message": "hello world three four five" }' http://localhost:8080/wordCount
# no response
curl -H "Content-Type: application/json" --request POST --data '{ "id": "1013", "message": "helloworldthreefourfive" }' http://localhost:8080/wordCount
# return 10
curl -H "Content-Type: application/json" --request POST --data '{ "id": "1014", "message": "hello world three four five" }' http://localhost:8080/wordCount
# return 15
curl -H "Content-Type: application/json" --request POST --data '{ "id": "1015", "message": "hello world three four five" }' http://localhost:8080/wordCount
# return 20
curl -H "Content-Type: application/json" --request POST --data '{ "id": "10136", "message": "hello world three four five" }' http://localhost:8080/wordCount
# return 25
curl -H "Content-Type: application/json" --request POST --data '{ "id": "1117", "message": "hello world three four five" }' http://localhost:8080/wordCount
# return 30

### Finish





### References
### https://docs.spring.io/spring-data/jpa/docs/2.4.5/reference/html/#jpa.query-methods.at-query
### https://spring.io/guides/tutorials/spring-boot-kotlin/
### https://kotlinlang.org/docs/jvm-spring-boot-restful.html
