#Beschreibung
Testet die Interaktion zwischen zwei Platformen.

**StartSimulation** startet die Simulationsplatform mit einem SimulationAgent, welcher
den Lebenszyklus der Platform steuert.

**KillSimulation** startet denn KillAgent mittels einer zweiten Platform, welcher
die **killPlatform** Methode des SimulationAgents aufruft und somit die Simulation beendet. 

**UpdateSimulation** ist (später) für die eigentliche Interaktion/Datenaustausch zwischen
IRPopt und IRPact zuständig.