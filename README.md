# Fakie

Mining Mobile Apps to Learn Design Patterns and Code Smells. 

## Getting Started

### Build with [Maven](https://maven.apache.org/) - Dependency Management

* Compile `mvn clean compile`
* Running the test `mvn clean install`
* Create jar `mvn clean package`
* Execute `mvn exec:java -Dexec.args="here goes your arguments separated by space"`

## Usage

Usage : `fakie [-hV] GRAPH_LOADER graph_loader_arguments... LEARNING_ALGORITHM learning_algorithm_argument...`

### Graph loader
* Neo4j `load-neo4j` : Load graph from a Neo4j database
    * Usage : `fakie load-neo4j [-hV] DB_PATH LEARNING_ALGORITHM learning_algorithm_argument...`
    * Parameters :
        ```
      DB_PATH     Path to the Neo4j database
        ```
    * Options :
        ```
          -h, --help      Show this help message and exit.
          -V, --version   Print version information and exit.
        ```
    * Learning Algorithm :
        ```
          fpgrowth  Use the  FPGrowth algorithm on the dataset
          apriori   Use the  Apriori algorithm on the dataset
        ```

### Learning Algorithm
* FPGrowth `fpgrowth` : Use the FPGrowth algorithm on the dataset
    * Usage : `fakie [GRAPH_LOADER] graph_loader_arguments... fpgrowth [-hV]`
    * Options :
        ```
          -h, --help      Show this help message and exit.
          -V, --version   Print version information and exit.
        ```

* Apriori `apriori` : Use the Apriori algorithm on the dataset :warning: NOT RECOMMENDED : VERY SLOW :snail:
    * Usage : `fakie [GRAPH_LOADER] graph_loader_arguments... apriori [-hV]`
    * Options :
        ```
          -h, --help      Show this help message and exit.
          -V, --version   Print version information and exit.
        ```

### Examples

* To run the FPGrowth algorithm on a neo4j database, use :
`load-neo4j ./path/to/your/neo4j/db fpgrowth`
* To run the Apriori algorithm on a neo4j database, use :
`load-neo4j ./path/to/your/neo4j/db apriori`

## Overview

![Overview](docs/images/overview.svg)

## Development

### Load Graph
* Neo4j :heavy_check_mark:

### Process Fakie Model
* Convert vertices properties value to boolean :heavy_check_mark:
* Convert edges properties value to boolean :x:
* Resolution of collision among properties :x:

### Dump Fakie Model
* Dump Fakie Model to an ARFF dataset :heavy_check_mark:

### Read Dataset
* Read an ARFF dataset :heavy_check_mark:

### Learning Algorithm
* Implement FPGrowth algorithm to infer associations rules among properties :heavy_check_mark:
* Implement Apriori algorithm to infer associations rules among properties :heavy_check_mark:

### Filter Rules
* Filter the rules to keep only those that identify a smell code :x:

### Convert Rules to Queries
* Convert Fakie rules to Cypher queries to allow a reuse by Paprika :x:

