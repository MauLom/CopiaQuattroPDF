
FROM maven:3.6-jdk-11 as builder

WORKDIR /app

COPY target/quattro-pdf-1.0.0.jar .

FROM adoptopenjdk/openjdk11:alpine-slim

COPY target/quattro-pdf-*.jar /quattro-pdf.jar

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/quattro-pdf.jar"]
