FROM openjdk
WORKDIR shortcut
ADD target/shortcut-1.0.jar app.jar
ENTRYPOINT java -jar app.jar
