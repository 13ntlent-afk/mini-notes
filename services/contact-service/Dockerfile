FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace
RUN apk add --no-cache curl unzip
COPY mvnw ./mvnw
COPY .mvn .mvn
RUN chmod +x mvnw
COPY pom.xml ./pom.xml
COPY platform/bom/pom.xml platform/bom/pom.xml
COPY services/gateway/pom.xml services/gateway/pom.xml
COPY services/auth-service/pom.xml services/auth-service/pom.xml
COPY services/notes-service/pom.xml services/notes-service/pom.xml
COPY services/contact-service/pom.xml services/contact-service/pom.xml
RUN ./mvnw -B -pl services/contact-service -am dependency:go-offline
COPY platform platform
COPY services services
RUN ./mvnw -B -pl services/contact-service -am package -DskipTests

FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache wget && addgroup -S app && adduser -S app -G app
WORKDIR /app
COPY --chown=app:app --from=build /workspace/services/contact-service/target/contact-service-0.1.0-SNAPSHOT.jar app.jar
USER app:app
EXPOSE 8083
HEALTHCHECK --interval=30s --timeout=3s --start-period=25s --retries=3 CMD wget -q -O /dev/null http://127.0.0.1:8083/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "/app/app.jar"]