# GitHub Search

A Java-based application for searching GitHub repositories, built with Maven and packaged using Docker.

## Features

- Search GitHub repositories
- Java 17 (Eclipse Temurin)
- Multi-stage Docker build for efficient image size

## Prerequisites

- Docker installed
- (For local development) Java 17+ and Maven

## Build and Run with Docker

### 1. Build the Docker image

```sh
docker build -t githubsearch .
```

### 2. Run the application

```sh
docker run --rm -p 8080:8080 githubsearch
```

The application will be available at `http://localhost:8080`.

## Local Development

Build and run using Maven:

```sh
mvn clean package
java -jar target/*.jar
```

## Swagger UI for Local Testing
Swagger UI will be available at `http://localhost:8080/swagger-ui.html`