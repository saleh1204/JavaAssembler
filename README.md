Java Assembler
===========================

What Is This?
------------
This is a configurable Java based assembler for RISC based processor. 
The configuration file is a JSON file and can be modified after project compilation to modify the assembler functionalities. 

How to build this project?
------------
This project can be built using the following maven command `mvn package`. Afterwards, the jar file should be located under the `target` directory. 
Java JDK version 11 is required to build this project.


Configuration File
------------
The configuration file is a simple JSON file. Below is a sample configuration file. 


Project Organization
------------
    ├── config.json     <- Sample configuration file
    ├── icon.jpg        <- Application Icon
    ├── lib             <- Libraries folder
    ├── nbactions.xml   <- Netbeans configuration file
    ├── pom.xml         <- Maven pom.xml file
    ├── README.md       <- This readme document
    └── src             <- Source folder containing Java code