# Use a smaller base image for the final build
FROM openjdk:11-ea-17-jre-slim

# Set the working directory
WORKDIR /app

ADD ./target/java-demo-1.0-SNAPSHOT.jar app.jar
ADD ./apache-skywalking-java-agent-9.3.0.tgz ./

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-javaagent:/app/skywalking-agent/skywalking-agent.jar", "-jar", "app.jar"]