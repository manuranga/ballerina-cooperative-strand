# Ballerina Cooperative Strand

Cooperative user space thread (strand) POC for ballerina lang.

## Compile

```sh
# compile jar and create dist
./gradlew installShadowDist
```

## CLI usage

```
Usage: ./build/install/bal-strand-shadow/bin/bal-strand [-s=<schedulerType>] [-u=<useCase>] [COMMAND]
  -s=<schedulerType>    one of: EAGER, FAIR
                          Default: EAGER
  -u=<useCase>          one of: FUNCTION, FIB, INTERLEAVE
                          Default: FIB
Commands:
  help    Displays help information about the specified command
  native  run native implementation
  strand  run strand implementation
```

## Example

```sh
$ ./build/install/bal-strand-shadow/bin/bal-strand strand 32
2178309
t=90040820
```

