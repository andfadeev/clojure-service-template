FROM alpine:3.21 AS tailwind-builder

WORKDIR /app
COPY . .

RUN apk update
RUN apk upgrade
RUN apk add bash

RUN apk add gcompat build-base
RUN wget https://github.com/tailwindlabs/tailwindcss/releases/latest/download/tailwindcss-linux-x64
RUN chmod +x tailwindcss-linux-x64
RUN mv tailwindcss-linux-x64 /bin/tailwindcss
RUN alias tailwindcss=/bin/tailwindcss

RUN /bin/tailwindcss -i ./input.css -o ./resources/public/css/output.css --minify

FROM clojure:temurin-21-lein-alpine AS clj-builder
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY project.clj /usr/src/app/
RUN lein deps
COPY . /usr/src/app
COPY --from=tailwind-builder /app/resources/public/css/output.css /usr/src/app/resources/public/css/output.css
RUN ["lein", "uberjar"]

FROM eclipse-temurin:21-alpine AS runtime
COPY --from=clj-builder /usr/src/app/target/uberjar/clojure-service-template-0.1.0-SNAPSHOT-standalone.jar /opt/service/clojure-service-template.jar
RUN apk add --no-cache curl
CMD ["java", "-jar", "/opt/service/clojure-service-template.jar"]