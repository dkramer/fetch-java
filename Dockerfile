FROM maven:3.8.5-openjdk-17

RUN mkdir /project

COPY ./jar/fetch_challenge-0.0.1-SNAPSHOT.jar /project/fetch_challenge-0.0.1-SNAPSHOT.jar
WORKDIR /project

CMD "java" "-jar" "fetch_challenge-0.0.1-SNAPSHOT.jar"