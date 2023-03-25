FROM maven:3.8.3-openjdk-17

WORKDIR /app
COPY . /app
RUN mvn clean package

EXPOSE 8080

CMD ["java", "-jar", "target/budgetService-1.0.0.jar"]