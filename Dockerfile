FROM openjdk
WORKDIR shortcut
ADD target/shortcut.jar app.jar
ENTRYPOINT java -jar app.jar
