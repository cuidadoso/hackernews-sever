FROM openjdk:8-jre-alpine
ADD target/hackernews-graphql-java-*.jar /app.jar
CMD java $JAVA_MEM $JAVA_EXT -jar /app.jar
