FROM adoptopenjdk/openjdk11:alpine-jre

ARG JAR_FILE=alignments.jar

WORKDIR /opt/app

COPY target/${JAR_FILE} /opt/app/
COPY blosum62.txt /opt/app/

ENTRYPOINT ["java","-jar","alignments.jar"]