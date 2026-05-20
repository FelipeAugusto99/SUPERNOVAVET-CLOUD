FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

RUN useradd -m appuser

USER appuser

EXPOSE 8080

CMD java -jar target/Novamonitor-0.0.1-SNAPSHOT.jar