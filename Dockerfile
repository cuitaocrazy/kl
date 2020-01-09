FROM java:8-jdk

COPY shouftOfficialWebsite/target/shouftOfficialWebsite-1.0-SNAPSHOT.jar /app
WORKDIR /app

CMD ["java", "-jar", "shouftOfficialWebsite-1.0-SNAPSHOT.jar"]
