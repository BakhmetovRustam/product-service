FROM openjdk:11-jre-slim

COPY target/product-service.jar /product-service.jar

ENTRYPOINT ["java", "-jar", "/product-service.jar"]
