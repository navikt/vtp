Virtuell Tjeneste Plattform (VTP)
=================================
![alt text](vtp.png "Logo Title Text 1")


#### VTP hjelper deg med å: 
- virtualisere grensesnitt rundt applikasjonene i FP-familien. 
- instansierer og holde testdata konsistente på tvers av grensesnittene
- ha en plattform for å simulere grensesnitt over REST, SOAP, Kafka, LDAP.
- sikkerhetshåndtering over OAuth2
- REST-grensesnitt for å programatisk opprette testdata (for automatiske tester) 
- GUI for å opprette testdata for mennesker.

## Henvendelser
- Team Foreldrepenger <teamforeldrepenger(at)nav.no>
- Team Sykdom-i-familien

## For NAV-ansatte
Interne henvendelser kan sendes via Slack i kanalen #vtp-chatten

## Teknologi som må installeres
- Java (https://adoptopenjdk.net/)
- Node.js (https://nodejs.org/en/download/)
- Yarn (https://classic.yarnpkg.com/)
- Maven (http://maven.apache.org/)

## Starte applikasjon
For utvikling på VTP benytt oppsett for å starte server gjennom IDE.
I verdikjedetester benyttes oftest Docker-image av VTP. Dette bygges i VTP sin pipeline. 

#### Starte backend-server via IDE
* IntelliJ må konfigureres med å sette classpath of module til server, og sette working directory til `$MODULE_WORKING_DIR$` i run/debug-konfiguration.
* Start mock serveren ved å kjøre MockServer (lokalt).
* Trenger parameter `-Dscenarios.dir="../model/scenarios"` dersom denne ikke ligger under working dir (dvs. i IDE).

#### Kjøre via docker run / docker-compose
##### For å bygge docker image lokalt: 
Bygg prosjektet med `mvn clean install"`, bygg deretter docker-imaget med `docker build -t vtp .` 
Imaget blir da tilgjengelig som vtp:latest

##### Hente docker-image bygget i pipe: 
docker pull docker.pkg.github.com/navikt/vtp/vtp

#### Starte front-end
* I modulen frontend (/frontend), kjør `yarn run serve`. Når VTP bygges så pakkes det også med en statisk versjon av front-end som er tilgjengelig på rot av localhost:8060 eller https://localhost:8063.
* Dersom bygg feiler på utviklerimage, forsøk å oppdater node / yarn. Oppdaterte versjoner ligger på http://a34apvl063.devillo.no:81/software/.  


## Opprette testdata 
* Opprett testdata ved å legge scenario i /model/scenarios. Innledende tall brukes som referanse for å få instansiert scenario.
* Tjenester for å opprette testdata finnes på [Swagger UI](https://localhost:8063/swagger/)  (Bruk HTTP for kall)

## Kjør tester
* Automatiske tester for FPSAK (og andre FP*-familieapplikasjoner) ligger i eget repo på GitHub (private): [fpsak-autotest](https://github.com/navikt/fpsak-autotest)
* Automatiske tester for K9-applikasjoner ligger i eget repo på GitHub (private): [k9-verdikjede](https://github.com/navikt/k9-verdikjede)
 

## Sertifiakter for SSL/TLS
For å få VTP til å kjøre med SSL/TLS forbindelse over SOAP, REST og Kafka må keystore angitt i VTP være tilgjengelig i SUT (System under test) sin Truststore. 
For lokal utvikling på Team Foreldrepenger og Sykdom-i-familien opprettes key- og truststore i.h.t til instruksjoner i verdikjede/autotest. 
Følgende systemvariabler kan defineres for å overstyre defaults i VTP: 

```bash
JAVAX_NET_SSL_TRUSTSTORE
JAVAX_NET_SSL_TRUSTSTOREPASSWORD
NO_NAV_MODIG_SECURITY_APPCERT_KEYSTORE
NO_NAV_MODIG_SECURITY_APPCERT_PASSWORD
```

## Konfigurere nye tjenester
Se SoapWebServerConfig for liste over url til genererte wsdl'er. Nye webtjenester registreres her. 
Se ApplicationConfig for liste over registrerte REST-tjenester. Nye REST-tjenester registreres her.     
