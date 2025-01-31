FROM eclipse-temurin:17-jre
ADD target/Reporting_and_Analytics-0.0.1-SNAPSHOT.jar myapp.jar
RUN sh -c 'touch /myapp.jar'
ENTRYPOINT ["java","-jar","/myapp.jar"]