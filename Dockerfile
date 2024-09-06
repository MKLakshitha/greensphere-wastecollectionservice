FROM ubuntu:latest
LABEL authors="KavinduLakshitha"

ENTRYPOINT ["top", "-b"]


# Install OpenJDK (adjust version if needed)
RUN apt-get update && apt-get install -y openjdk-17-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY build/libs/greensphere-wastecollectionservice-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 to the outside world
EXPOSE 8095

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
