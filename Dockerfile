FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/jre:openjdk-21
COPY /target/tiltaksgjennomforing-prosess-0.0.1-SNAPSHOT.jar app.jar
ENV TZ="Europe/Oslo"
EXPOSE 8080
CMD ["-jar", "app.jar"]
