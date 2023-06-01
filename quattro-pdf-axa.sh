#! /bin/bash

mvn clean package -P dev -Duser.timezone=Etc/GMT+6

gcloud builds submit --tag=gcr.io/biibiicaxa/quattro-pdf --project=biibiicaxa

gcloud run deploy quattro-pdf --image=gcr.io/biibiicaxa/quattro-pdf --port=8080 --platform="managed" --region="us-central1" --project=biibiicaxa --update-env-vars=SPRING_PROFILES_ACTIVE=axa --update-env-vars=TZ=Etc/GMT+6 --vpc-connector=biibiic-axa-connector-vpc --ingress=all --allow-unauthenticated --min-instances=1 --max-instances=10
