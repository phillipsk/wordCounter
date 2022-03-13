# wordCounter

### Project overview
- WordCounter - Spring Boot Java JPA persisted Kotlin word counter application
- This application is built on Spring Boot web server
- Spring Boot Java/H2 Datasource is used as a lightweight database
- Developer: Kevin Phillips

### Use Case
- Accept a JSON POST request to `/wordCount` endpoint and return a count of the words contained in the POST Body
- Handling: Only return a word count response if the ID is unique -> disregard duplicate IDs

### Instructions
- IntelliJ IDEA IDE
  - clone repository and run the application to start the server
- Run `runWordCounter.sh` shell script on a UNIX terminal OR copy and paste each cURL line from the script into a UNIX terminal

### Run in terminal  
1. Clone the repository
2. Use the terminal to change directory `cd wordCounter/` into the root folder 
3. Start the Spring Boot server with `./gradlew bootRun`
4. Open a secondary terminal to run the script
5. `chmod u+x runWordCounter.sh` enables the file to become an executable shell script 
6. `./runWordCounter.sh ` run the script
7. If you run the script twice - no output is returned
   - This is accurate - the use case is to prevent storage and response from duplicate IDs responses received 
   - Change the ID fields of the JSON input to test 
   - Change the message "hello world" to test

### Notes
- src/main/resources/application.properties -> Spring Datasource SQL schema properties
- src/main/resources/sql/schema.sql -> Spring Datasource SQL Database install script
### Troubleshoot the Database
- The Spring Datasource Database will be created in the /data/ folder within the project path
- Try deleting the *.sql files within this folder to remove and reset the persisted database


### References
- https://docs.spring.io/spring-data/jpa/docs/2.4.5/reference/html/#jpa.query-methods.at-query
- https://spring.io/guides/tutorials/spring-boot-kotlin/
- https://kotlinlang.org/docs/jvm-spring-boot-restful.html
