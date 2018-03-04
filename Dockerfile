FROM ubuntu:16.04

RUN apt-get -y  update

RUN apt-get install -y openjdk-8-jdk-headless

ENV WORK /hljava
WORKDIR $WORK/

ADD . .
RUN ./gradlew build
EXPOSE 80

CMD java -jar build/libs/http-server-1.0-SNAPSHOT.jar