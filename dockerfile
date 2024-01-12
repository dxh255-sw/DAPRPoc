# Start with the Gradle image
FROM gradle:jdk17-alpine AS builder
USER root

# Set the working directory
WORKDIR /home/gradle/project

# First, add only the build file
# This ensures that the project's dependencies are cached unless the build file changes
COPY build.gradle settings.gradle ./

# Install necessary packages
RUN apk add gcompat
ENV LD_PRELOAD=/lib/libgcompat.so.0

# Now, run the dependency resolution command
# This layer will be cached unless build.gradle changes
RUN gradle dependencies

# Add the rest of the project files
# This step will invalidate the cache only if there are changes in the project files
COPY . .

# Change the ownership of the files to the gradle user
RUN chown gradle:gradle -R /home/gradle

# Switch back to the gradle user
USER gradle

# Build the application
RUN gradle bootJar

# Start the next stage with a Java base image
FROM openjdk:17-alpine

# Install necessary packages
RUN apk add gcompat
ENV LD_PRELOAD=/lib/libgcompat.so.0

COPY --from=builder /home/gradle/project/build/libs/*.jar /home/gradle/project/app.jar

# Expose the port the app runs on
EXPOSE 3500 8080 8093 50001

# Set the entrypoint to run the application
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-Djava.security.egd=file:/dev/./urandom","-jar","/home/gradle/project/app.jar"]