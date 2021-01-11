FROM openjdk:<VERSION>
COPY target/jmetric-1.0.0-SNAPSHOT.jar jmetric-1.0.0.jar
CMD ["java", "-jar", "jmetric-1.0.0.jar"]
