FROM icr.io/appcafe/ibm-semeru-runtimes:open-17-jdk-ubi as builder

# Assumes build context is entire repo
WORKDIR /work
ADD . /work/

RUN ./gradlew test war

FROM icr.io/appcafe/open-liberty:kernel-slim-java17-openj9-ubi

ARG VERBOSE=true

COPY --chown=1001:0 src/main/liberty/config/ /config/
RUN features.sh

COPY --chown=1001:0 --from=builder /work/build/libs/ForecastDiscussions.war /config/apps/

RUN mkdir -p /output/resources/ && touch /output/resources/test && configure.sh

EXPOSE 8080 9443
