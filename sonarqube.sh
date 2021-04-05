#! /bin/bash
mvn sonar:sonar \
  -Dsonar.projectKey=quattro-pdf \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=5380ae947a3850950cbe4c9a061e51d23c88770f
