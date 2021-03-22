#! /bin/bash
mvn sonar:sonar \
  -Dsonar.projectKey=quattro-pdf \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=ca7f1f0ca53cfbcd61e79455b061b43633c931b5
