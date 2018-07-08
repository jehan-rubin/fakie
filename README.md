# Fakie

[![Build Status](https://travis-ci.org/alexandre-clement/fakie.svg?branch=master)](https://travis-ci.org/alexandre-clement/fakie)
[![codecov](https://codecov.io/gh/alexandre-clement/fakie/branch/master/graph/badge.svg)](https://codecov.io/gh/alexandre-clement/fakie)

Mining Mobile Apps to Learn Design Patterns and Code Smells.
Fakie is a tool to automatically generate the Antipattern Queries from the Graph DB for [Paprika](https://github.com/Sriki13/paprika).  

## Table of contents
*   [Getting Started](#getting-started)
*   [Usage](#usage)
*   [Code Smells](#code-smells-file)
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

```
 ███████╗ █████╗ ██╗  ██╗██╗███████╗
 ██╔════╝██╔══██╗██║ ██╔╝██║██╔════╝
 █████╗  ███████║█████╔╝ ██║█████╗
 ██╔══╝  ██╔══██║██╔═██╗ ██║██╔══╝
 ██║     ██║  ██║██║  ██╗██║███████╗
 ╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚══════╝

Usage: fakie [-hV] [COMMAND]

Description:
  Mining Mobile Apps to Learn Design Patterns and Code Smells.

Options:
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.

Commands:
  analyse     Run Paprika analyse on given folder
  query       Run Paprika query on the given db
  load-neo4j  Import Android applications from a Neo4j database.
 ```
 
### Analyse

```
Usage: fakie analyse [-hV] -a=<androidJars> [-db=<db>] -f=<apk> -i=<info>
                     [COMMAND]

Description:
  Run Paprika analyse on given folder

Options:
  -a, --android=<androidJars>
                             Path to the android platform jars
      -db, --database=<db>   Path to the info Paprika db
  -f, --apk-folder=<apk>     Path to the apk folder
  -h, --help                 Show this help message and exit.
  -i, --info-apk=<info>      Path to the info apk file
  -V, --version              Print version information and exit.

Commands:
  query     Run Paprika query on the given db
  fpgrowth  Use the  FPGrowth algorithm on the dataset
  apriori   Use the  Apriori algorithm on the dataset
```

### Query

```
Usage: fakie query [-hV] [-db=<db>] [-s=<suffix>] [COMMAND]

Description:
  Run Paprika query on the given db

Options:
      -db, --database=<db>   Path to the info Paprika db
  -h, --help                 Show this help message and exit.
  -s, --suffix=<suffix>      Suffix for the csv filename
  -V, --version              Print version information and exit.

Commands:
  fpgrowth  Use the  FPGrowth algorithm on the dataset
  apriori   Use the  Apriori algorithm on the dataset
```

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
    * Usage : `fakie GRAPH_LOADER fpgrowth [-hV] -f=<file> [-n=<n>] [-s=<support>] QUERY_EXPORTER`
    * Options :
        ```
          -f, --file=<file>    Path to the file containing the code smells in the database
          -h, --help           Show this help message and exit.
          -n, --nb-rules=<n>   Number of rules to find
                                 Default: 10000
          -s, --min-support=<support>
                               Minimum support bound
                                 Default: 0.1
          -V, --version        Print version information and exit.
        ```
    * Query Exporter :
        ```
          cypher  Export the generated rules in the Cypher query language
        ```

* Apriori `apriori` : Use the Apriori algorithm on the dataset :warning: NOT RECOMMENDED : VERY SLOW :snail:
    * Usage : `fakie GRAPH_LOADER apriori [-hV] QUERY_EXPORTER`
    * Options :
        ```
          -f, --file=<file>   Path to the file containing the code smells in the database
          -h, --help          Show this help message and exit.
          -V, --version       Print version information and exit.
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

## Code Smells File

In order to work properly, the learning algorithm needs an input file containing the code smell present in the targeted project.
This file should look like this (currently, only json is supported) :

```json
{
  "codeSmells": [
    {
      "labels": ["Class"],
      "properties": {"name": "org.torproject.android.service.TorService"},
      "name": "God Class"
    },
    {
      "labels": ["Class"],
      "properties": {"name": "org.torproject.android.OrbotMainActivity"},
      "name": "God Class"
    },
    {
      "labels": ["Class"],
      "properties": {"name": "org.torproject.android.Prefs"},
      "name": "God Class"
    },
    {
      "labels": ["Class"],
      "properties": {"name": "org.torproject.android.settings.TorifiedApp"},
      "name": "God Class"
    }
  ]
}
```

## Overview

![Overview](docs/images/overview.svg)

## Development

### Embedded Paprika
* Run Paprika Analyse :heavy_check_mark:
* Run Paprika Query :x:
* Parse Paprika Query result :heavy_check_mark:

### Load Graph
* Neo4j :heavy_check_mark:

### Process Fakie Model
* Add code smell to the Fakie model :heavy_check_mark:
* Convert vertices numeric properties to nominal :heavy_check_mark:
* Convert vertices arrays properties to nominal :heavy_check_mark:
* Convert vertices nominal properties to boolean :heavy_check_mark:
* Removing properties with a single value :heavy_check_mark:
* Process only vertices which contains a code smell :heavy_check_mark:
* Convert edges properties values to boolean :x:
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
* Filter the rules to keep only the many to one rules :heavy_check_mark:
* Remove consequences in rules that are not a code smell :heavy_check_mark:
* Filter rules with the same support but a different the amount of premises :heavy_check_mark:

### Export Rules to Queries
* Export the rules to Cypher :heavy_check_mark:
* Convert Fakie rules to allow a reuse by Paprika :x:

## Troubleshooting

Fakie is still in development.
Found a bug? We'd love to know about it!
Please report all issues on the github issue tracker.
