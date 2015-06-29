# Dependency Graph ![Build Status](https://travis-ci.org/carlosgsouza/dependency-graph.svg?branch=master)
This is a command line application that prints a nicely formated dependency tree. It was built with the goal of exemplifying how to build well tested applications using Groovy, Spock and Gradle.

Given a file with a list of dependencies
```
A->B
A->C
B->C
B->D
D->A
X->Y
```

it will print
```
A
|_ B
| |_ C
| |_ D
|    |_ A (!)
|_ C
X
|_ Y

(!) Circular Dependency
```

## Installing
Dependency Graph is built by [Gradle](https://gradle.org/). It already comes with Gradle Wrapper and the only thing that you need to install on your system is Java. 

### Configuring your IDE
`./gradle eclipse`
or
`./gradle idea`

### Compiling and Packaging
Executing `./gradle assemble` will create a zip and a tar distribution for you under `build/distributions/`. Also, an unpackaged distribution is created under `build/install`.

### Running
`./build/install/dependency-graph/bin/dependency-graph src/test/resources/input2.txt` 

## Testing
A lot of attention was given to testing in this application. Three types of tests were implemented, unit, integration and performance. Unit tests, under `src/test/groovy`, were written with the goal of providing not only functional validation, but also documentation and specification. This is the best place to go if you want to understand how the application works.

Integration tests, under `src/test/integration`, execute on the packaged application and validate that it works from the command line. Finally, performance tests, under `src/test/performance`, check that the application will work and perform well under heavy load. Code coverage reports are generated by JaCoCo.

### Running tests
Use `./gradle check` to run all tests or use one of the following to run a specific test phase

`./gradle test`

`./gradle integrationTest`

`./gradle performanceTest`

### Generating code coverage report
`./gradle check jacocoTestReport`

## Continuous Integration
Continuous builds are executed for every commit by [Travis-CI](https://travis-ci.org/carlosgsouza/dependency-graph) and execute unit, integration and performance tests.

