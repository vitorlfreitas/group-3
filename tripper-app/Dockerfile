# BUILD STAGE
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Cache Maven dependencies
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline -B

# Copy sources and build
COPY src src
RUN ./mvnw clean package -DskipTests -B

#RUNTIME STAGE
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the fat JAR from build stage
COPY --from=build /app/target/*.jar app.jar

ARG PROFILE=prod
ARG PORT=8080

# Runtime env vars, picked up by Spring Boot automatically
ENV SPRING_PROFILES_ACTIVE=${PROFILE} \
    SERVER_PORT=${PORT}

# Expose the selected port
EXPOSE ${PORT}

# Launch the app; Spring Boot will read SPRING_PROFILES_ACTIVE & SERVER_PORT
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
