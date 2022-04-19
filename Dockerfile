FROM openjdk:17-slim

COPY target/product-service.jar /product-service.jar

ENTRYPOINT ["java", "-jar", "/product-service.jar"]
