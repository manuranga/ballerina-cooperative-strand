# Ballerina Cooperative Strand

Cooperative user space thread (strand) POC for ballerina lang.

## Running

```sh
# run direcly
./gradlew run --args="strand 10"


# run from jar
./gradlew shadowJar
java -jar ./build/libs/bal-strand-1.0-SNAPSHOT-all.jar strand 10
```

## CLI commands

* `native` - Naive java fib implementation
* `native-time` - Time taken to naive java fib in nano sec
* `stand` -  Stand based single threaded fib implementation
* `stand-time` - Time taken to stand based single threaded fib implementation in nano sec
* `ratio` - Average (uses 200 runs) slow down ratio of strand compared to native
