Apache CXF on Java 10 Example
==============

- build

      mvn clean package

- run

      java --add-modules java.xml.bind \
           --module-path target/modules \
           --module com.example.cxf/com.example.Starter

- open in Google Chrome: `http://localhost:8686/api/people/sse`

- call the API (and see the data appears in Google Chrome page)

      curl -X POST http://localhost:8686/api/people \
           -d '{"email": "john@smith.com", "firstName": "John", "lastName": "Smith"}' \
           -H "Content-Type: application/json"

- create a runtime image

      jlink --add-modules java.xml.bind,java.management \
            --module-path target/modules \
            --verbose \
            --strip-debug \
            --compress 2 \
            --no-header-files \
            --no-man-pages \
            --output target/cxf-java-10-app

- run from the runtime image

      target/cxf-java-10-app/bin/java  \
           --add-modules java.xml.bind \
           --module-path target/modules \
           --module com.example.cxf/com.example.Starter
