# otaibe-nginx-with-eureka-demo-config-producer project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Starting the producer

From project dir (not necessary) run the following script:
```
    bash src/test/resources/shell/docker_run.sh
```

## Starting the nginx

From project dir (not necessary) run the following script:
```
    bash src/test/resources/shell/docker_run_nginx.sh
```

## Running the curl tests

From project dir (not necessary) run the following script:
```
    bash src/test/resources/shell/curl_tests.sh
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `otaibe-nginx-with-eureka-demo-config-producer-1.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/otaibe-nginx-with-eureka-demo-config-producer-1.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/otaibe-nginx-with-eureka-demo-config-producer-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image-guide.