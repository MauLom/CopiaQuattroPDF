FROM adoptopenjdk/openjdk11:alpine-slim

COPY target/quattro-pdf-*.jar /quattro-pdf.jar

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/quattro-pdf.jar"]
