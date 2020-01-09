FROM java:8-jdk

RUN mkdir -p /app
COPY shouftOfficialWebsite/target/shouftOfficialWebsite-1.0-SNAPSHOT.jar /app

WORKDIR /app
CMD ["java", "-jar", "shouftOfficialWebsite-1.0-SNAPSHOT.jar"]
