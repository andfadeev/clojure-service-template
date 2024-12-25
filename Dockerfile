FROM clojure:temurin-21-lein-alpine AS clj-builder
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY project.clj /usr/src/app/
RUN lein deps
COPY . /usr/src/app
RUN ["lein", "uberjar"]

FROM eclipse-temurin:21-alpine AS runtime
COPY --from=clj-builder /usr/src/app/target/clojure-service-template-0.1.0-SNAPSHOT-standalone.jar /opt/service/clojure-service-template.jar
RUN apk add --no-cache curl
CMD ["java", "-jar", "/opt/service/clojure-service-template.jar"]