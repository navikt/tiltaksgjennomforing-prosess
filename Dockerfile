FROM navikt/java:12
COPY import-vault-token.sh /init-scripts
COPY /target/tiltaksgjennomforing-prosess-${version}.jar app.jar