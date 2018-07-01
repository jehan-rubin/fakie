# Fakie

Mining Mobile Apps to Learn Design Patterns and Code Smells. 

## Getting Started

### Build with [Maven](https://maven.apache.org/) - Dependency Management

* Compile `mvn clean compile`
* Running the test `mvn clean install`
* Create jar `mvn clean package`
* Execute `mvn exec:java -Dexec.args="here goes your arguments separated by space"`


## Overview

![Overview](docs/images/overview.svg)


## CommandLine

To run the FPGrowth algorithm on a neo4j database, use :
`load-neo4j ./path/to/your/neo4j/db FPGrowth`
