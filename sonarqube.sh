#! /bin/bash
mvn sonar:sonar \
  -Dsonar.projectKey=quattro-pdf \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=197b58657f8fe1e47a5da7b2b815f0dcf3e741ba
