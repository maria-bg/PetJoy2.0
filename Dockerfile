FROM openjdk:17
WORKDIR /app
COPY . /app
RUN javac -cp "lib/mysql-connector.jar" *.java
CMD ["java", "-cp", "lib/mysql-connector-j-9.3.0.jar:.", "Main"]
