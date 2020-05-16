FROM adoptopenjdk/openjdk14-openj9:ubi as builder

# Assumes build context is entire repo
WORKDIR /work
ADD . /work/

RUN ./gradlew test war

FROM openliberty/open-liberty:full-java13-openj9-ubi

COPY --chown=1001:0 src/main/liberty/config/ /config/
COPY --chown=1001:0 --from=builder /work/build/libs/ForecastDiscussions.war /config/apps/

ARG VERBOSE=true
RUN mkdir -p /output/resources/ && touch /output/resources/test && configure.sh

EXPOSE 8080 9443