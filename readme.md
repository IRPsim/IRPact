# IRPact

#### Start

##### General:

java -cp \<Classpath\> de.unileipzig.irpact.start.Start -i \<input file\> -o \<output file\>

##### Initial social network visualization:

java -cp \<Classpath\> de.unileipzig.irpact.start.Start -i \<input file\> --image \<image output file\> --noSimulation

##### Simulation and network image visualization

java -cp \<Classpath\> de.unileipzig.irpact.start.Start -i \<input file\> -o \<output file\> --image \<image output file\>

#### Example (uber jar):

java -jar IRPact-1.0-SNAPSHOT-uber.jar src/main/resources/irpact/examples/example1.json -o example1-output.json

#### Gradle (uber jar):

./gradlew clean buildUberJar
