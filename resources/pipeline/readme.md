#Pipeline
Ressursene under pipeline skal kun brukes i jenkins-kontekst. De ligger her 
på grunn av nåværende arkitektur for å forenkle kjøring av  tester i pipe.

### Bruksområder
- Dockerfile - brukes for å enable custom keystore i fpmock2 på testnode
- vtpkeystore - injiseres i runtime på autotest-noden.
- nav_truststore_path - injiseres i fpsak runtime på autotest-noden.

Disse i kombinasjon tillater at dockercontainere (fpmock2 og fpsak) snakker sammen på tvers av containere.

Jenkins-file som styrer kjøringen ligger i vl-jenkins-prosjektet. 
Kjøringen av jenkinsfile fordrer at fpmock2 tagges lokalt med :autotest.