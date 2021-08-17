# IRPact

#### Start

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
