FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
ADD target/tenpo-0.0.1-SNAPSHOT.jar tenpo-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/tenpo-0.0.1-SNAPSHOT.jar"]