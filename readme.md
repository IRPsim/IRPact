#Demo

Die Demo ist im demo-Verzeichnis zu finden. Sie kann auf zwei Arten gestartet werden.

Testdateien für Eingaben sind in "src/main/resources/irpact/examples/input" zu finden (input.json und input2.json).
Entsprechende Ausgaben, zum Vergleichen, befinden sich in "src/main/resources/irpact/examples/output".

##Variante 1 (-cp mit spezifizierter main)

Zum Starten kann folgender Befehl genutzt werden:

Windows:

java -cp Demo-1.0-SNAPSHOT.jar;dependencies/\* de.unileipzig.irpact.start.Start -i \<Pfad zum Inputfile\> -o \<Pfad zum Outputfile\>

Nicht-Windows:

java -cp Demo-1.0-SNAPSHOT.jar:dependencies/\* de.unileipzig.irpact.start.Start -i \<Pfad zum Inputfile\> -o \<Pfad zum Outputfile\>

###Beispiel

Mittels

java -cp Demo-1.0-SNAPSHOT.jar:dependencies/\* de.unileipzig.irpact.start.Start -i src/main/resources/irpact/examples/output/input.json -o testoutput.json

wird die Demo gestartet und das Ergebnis in die testoutput.json geschrieben. (Absoluten Pfad bei der Eingabedatei beachten.)

###Hilfe

Der Befehl

java -cp Demo-1.0-SNAPSHOT.jar:dependencies/\* de.unileipzig.irpact.start.Start -?

gibt eine Liste möglicher Parameter aus. 

##Variante 2 (-jar)

###Programmstart

java -jar Demo-1.0-SNAPSHOT.jar -i \<Pfad zum Inputfile\> -o \<Pfad zum Outputfile\>

###Beispiel

java -jar Demo-1.0-SNAPSHOT.jar -i src/main/resources/irpact/examples/output/input.json -o testoutput.json

(Absoluten Pfad bei der Eingabedatei beachten.)

###Hilfe

java -jar Demo-1.0-SNAPSHOT.jar -?