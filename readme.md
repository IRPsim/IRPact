# IRPact
IRPact is a framework for agent-based modelling of innovation diffusion of sustainable products in private households. It is written in the Java programming language based on the Jadex agent framework and can be used as a free-standing program or integrated in modeling infrastructure, such as [IRPsim](https://irpsim.uni-leipzig.de/artifacts/ui-client-irpact-develop/#!/models/modelDefinition/3).

It provides a rich, empirically and theoretically grounded set of variables and is temporally discrete and spatially explicit and features a rich process and decision process, as well as flexible social network modeling. Agents are modeled explicitly as instantances derived from (socio-economic) agent groups that are described through probability distributions the agents are based on. The framework has been developed and implemented within the [SUSIC project](https://www.wifa.uni-leipzig.de/institut-fuer-infrastruktur-und-ressourcenmanagement/professuren/professur-fuer-energiemanagement-und-nachhaltigkeit/forschung/susic/).

This project is financed by the Saxon State government out of the State budget approved by the Saxon State Parliament.  <img src="https://user-images.githubusercontent.com/3297034/133747545-013bf555-44b5-4b26-b2fa-06a29c1a2b92.gif" width="20">


## Motivation 
The adoption of eco-innovation is seen as a promising approach in supporting to reach emission goals. Yet, even promising products fail to diffuse in consumer households. Agent-based innovation diffusion models aim to inform about diffusion dynamics and help entities to evaluate strategies for the role out of products. While numerous models exist, they are often developed without regarding existing approaches and few common structures and code bases exist. IRPact addresses this by developing a more generic framework rather than a model on the diffusion of eco-innovations.

## Purpose
IRPact aims to provide a rather generic framework for innovation diffusion of eco-innovation. It is based on a synthesis of existing literature and aims to provide a common code-base for a wide range of models. It is designed to be flexible and modular, and allow for easy extension to cater more specific needs.

## How do I use it?
As a java-based model, IRPact can be used with the respective tools needed to compile and run java programs.
In order to use it, you need a valid SDK with the paths correctly set, with Java v11+.
The repository provides a build process based on gradle(w) and bundles all necessary files in a single .jar file (uber-jar or fat-jar).

### Building

In order to build the .jar file, you need to execute gradlew with the following command
```
./gradlew clean buildUberJar
```
(on a unix-based machine) or
```
gradlew clean buildUberJar
```
(on a windows-based machine).

This will create the respective .jar file (IRPact-1.0-SNAPSHOT-uber.jar) in the build/libs folder that bundles all required functionality.

### Running the Model
In order to run the model, you need the mentioned .jar file and a valid model configuration file (scenario-file) in the .json format (see configuration guide on how to create these files). 
The uber-jar file is invoked with the _java -jar_ command and requires a number of flags:
* -i specifies the input file (configuration json file); required
* -o specifies the output file (where the results should be written); required
* --image specifies where the visualization of the agent network should be written; optional
* --noSimulation indicates that the simulation should be aborted after the initialization of the agent network. Only makes sense in conjunction with the --image flag if the user is only interested in generating an image; optional

### Configuring the Model

IRPact was designed with the idea of configurability in mind. It is a framework for agent-based modelling of innovation diffusion of sustainable products in private households and is primarily suited for this purpose. Through agent groups and their attributes, product modeling, a range of social networks, a flexible process model and the possibility for non-household actors, it enables a multitude of model structures and variations that can easily be configured through a set of configuration files. 

How the model is configured is explained in more detail in the configuration guide that will be published in the future.

## How Can I Contribute?
We are always happy about people interested in joining us and supporting our research and development. If you would like to get involved, feel free to send us a message (at johanning[at]wifa.uni-leipzig.de). Some ways you could get involved are through

* Contributing to the code-base
* Test the framework and help to improve it through bug reports
* Use IRPact as a framework for your own model (we gladly help support you adapting it according to your needs)
* Proposals for joint research endeavors
* Anything else you can think of

Just get in touch with us and lets discuss how we can collaborate.
