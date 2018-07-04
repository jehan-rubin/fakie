# Fakie

Mining Mobile Apps to Learn Design Patterns and Code Smells.
Fakie is a tool to automatically generate the Antipattern Queries from the Graph DB for [Paprika](https://github.com/Sriki13/paprika).  

## Table of contents
*   [Getting Started](#getting-started)
*   [Usage](#usage)
*   [Overview](#overview)
*   [Development](#development)
*   [Troubleshooting](#troubleshooting)

## Getting Started

### Build with [Maven](https://maven.apache.org/) - Dependency Management

* Compile `mvn clean compile`
* Running the test `mvn clean install`
* Create jar `mvn clean package`
* Execute `mvn exec:java -Dexec.args="here goes your arguments separated by space"`

## Usage

Usage : `fakie [-hV] GRAPH_LOADER LEARNING_ALGORITHM QUERY_EXPORTER`

### Graph loader
* Neo4j `load-neo4j` : Load graph from a Neo4j database
    * Usage : `fakie load-neo4j [-hV] DB_PATH LEARNING_ALGORITHM QUERY_EXPORTER`
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
    * Usage : `fakie GRAPH_LOADER fpgrowth [-hV] QUERY_EXPORTER`
    * Options :
        ```
          -h, --help      Show this help message and exit.
          -V, --version   Print version information and exit.
        ```
    * Query Exporter :
        ```
          cypher  Export the generated rules in the Cypher query language
        ```

* Apriori `apriori` : Use the Apriori algorithm on the dataset :warning: NOT RECOMMENDED : VERY SLOW :snail:
    * Usage : `fakie GRAPH_LOADER apriori [-hV] QUERY_EXPORTER`
    * Options :
        ```
          -h, --help      Show this help message and exit.
          -V, --version   Print version information and exit.
        ```
    * Query Exporter :
        ```
          cypher  Save the generated rules in the Cypher query language
        ```
        
### Query Exporter

* Cypher `cypher` : Export the generated rules in the Cypher query language
    * Usage : `fakie GRAPH_LOADER LEARNING_ALGORITHM cypher [-hV] [-o=<path>]`
    * Options :
        ```
          -h, --help            Show this help message and exit.
          -o, --output=<path>   Destination folder for the generated queries
          -V, --version         Print version information and exit.
        ```

### Examples

* To run the FPGrowth algorithm on a neo4j database, and then export the generated rules as Cypher queries, use :

`load-neo4j ./path/to/your/neo4j/db fpgrowth cypher -o queries/destination/folder`
* To run the Apriori algorithm on a neo4j database, and then export the generated rules as Cypher queries, use :

`load-neo4j ./path/to/your/neo4j/db apriori cypher --output="queries/destination/folder`

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
* Filter the rules to keep only those that identify a smell code :heavy_check_mark:

### Export Rules to Queries
* Convert Fakie rules to Cypher queries to allow a reuse by Paprika :heavy_check_mark:

## Troubleshooting

Fakie is still in development.
Found a bug? We'd love to know about it!
Please report all issues on the github issue tracker.
