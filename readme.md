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
Consisting of several programs itself, IRPact is used through a set of command-line commands that are detailed in the following.

### Starting the Programs

##### General:

java -cp &lt;Classpath&gt; de.unileipzig.irpact.start.Start -i &lt;input file&gt; -o &lt;output file&gt;

##### Initial social network visualization:

java -cp &lt;Classpath&gt; de.unileipzig.irpact.start.Start -i &lt;input file&gt; --image &lt;image output file&gt; --noSimulation

##### Simulation and network image visualization

java -cp &lt;Classpath&gt; de.unileipzig.irpact.start.Start -i &lt;input file&gt; -o &lt;output file&gt; --image &lt;image output file&gt;

#### Example (uber jar):

java -jar IRPact-1.0-SNAPSHOT-uber.jar src/main/resources/irpact/examples/example1.json -o example1-output.json

#### Gradle (uber jar):

./gradlew clean buildUberJar

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
