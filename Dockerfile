ARG JAVA_VERSION=17

FROM openjdk:${JAVA_VERSION}-jdk AS builder

WORKDIR app

COPY . /app

RUN ./mvnw clean package -Dmaven.test.skip=true --no-transfer-progress

FROM openjdk:${JAVA_VERSION}-slim

COPY --from=builder /app/target/product-service.jar /product-service.jar

ENTRYPOINT ["java", "-jar", "/product-service.jar"]
