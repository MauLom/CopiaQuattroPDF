FROM openjdk:17-alpine
ENV TZ="America/Mexico_City"
COPY target/quattro-pdf-*.jar /quattro-pdf.jar
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/quattro-pdf.jar"]
