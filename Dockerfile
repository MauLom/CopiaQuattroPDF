FROM openjdk:17-alpine

COPY target/quattro-pdf-*.jar /quattro-pdf.jar

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/quattro-pdf.jar"]
