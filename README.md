Simple Product Service powered by Spring Boot
===

## Introduction

This project is a simple REST application for serving a simple Product API. Written with Java 11, it uses Docker, Spring Boot, Spring Data REST and some other helper libraries.

## Running the API

The easy way to get the API running is using docker with the provided docker compose. Run the following to get it all set up:

```shell
./mvnw clean package && docker compose up -d

# or if you're using docker < 20.10.0
./mvnw clean package && docker-compose up -d
```

## API Endpoints

This project is using [Spring Data REST](https://spring.io/projects/spring-data-rest) and it uses the [HAL format](https://stateless.group/hal_specification.html) for JSON output.

You can see the top level service by hitting the API root resource, like the following example:

```shell
$ curl http://localhost:8080/api
{
  "links" : [ {
    "rel" : "products",
    "href" : "http://localhost:8080/api/products{?page,size,sort}"
  }, {
    "rel" : "profile",
    "href" : "http://localhost:8080/api/profile"
  } ]
}
```

- GET `/api/products`, to get a list of products
- GET `/api/products/{id}`, to get one product from the list
- PUT `/api/products/{id}`, to update a single product
- POST `/api/products`, to create a product

The API also exposes endpoints to search and/or filter:

- GET `/api/products/search/names?name={name}`, to search a product by its name
