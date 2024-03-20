call mvn clean package -DskipTests
call docker build . --tag=432512331817.dkr.ecr.us-west-2.amazonaws.com/quattro-pdf
call docker push 432512331817.dkr.ecr.us-west-2.amazonaws.com/quattro-pdf