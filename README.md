OBS OBS IKKE OFFENTLIGGJØR ENDA
=================================
Fortsatt litt opprydding igjen

Virtuell Tjeneste Plattform (VTP)
=================================

Applikasjon for å virtualisere grensesnitt rundt applikasjonene i FP-familien. 
Instansierer og holder testdata konsistente på tvers av virtualiserte grensesnitt.

Støtter grensesnitt på SOAP, REST og Kafka, og har også sikkerhetshåndtering innebygd. 

Kan brukes av mennesker for manuell testing, og av automatiske tester for å sette testdata-tilstand før testeksekvering. 


# Henvendelser

Team Foreldrepenger <teamforeldrepenger(at)nav.no>

## For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen #vtp-chatten


# Hvordan komme igang - Oppsett og konfigurasjon


Starte server
----
* Start mock serveren ved å kjøre MockServer (lokalt).
* Trenger parameter -Dscenarios.dir="./model/scenarios" dersom denne ikke ligger under working dir (dvs. i IDE).
* Swagger UI: https://localhost:8063/swagger/ - Bruk HTTP for kall
* SoapWebServiceConfig.java inneholder endepunker for virtuelle tjenester.

Front-end
---
* I mappen frontend kjør yarn run serve. Når VTP bygges så pakkes det også med en statisk versjon av front-end som er tilgjengelig på rot av localhost:8060 eller https://localhost:8063.
* Dersom bygg feiler på utviklerimage, forsøk å oppdater node / yarn. Oppdaterte versjoner ligger på http://a34apvl063.devillo.no:81/software/.  


Opprette testdata 
----
* Opprett testdata ved å lage scenario i ./model/scenarios. Innledende tall brukes som referanse for å få instansiert scenario fra Autotest. 

Kjør tester
----
* Tester ligger i autotest-modulen test-scope. Klienter, roller og annen kode som muliggjør testeksevering ligger i autotestmodulen i main-scope
* Tester kjøres fra IntelliJ eller Eclipse gjennom IDEs innebygde test runner eller gjennom maven 
* Se autotest-modulen for egen README på Autotest.


Koble foreldrepenger til VTP
------

*Ved å starte FPSAK med '--vtp' setter du følgende endepunkter:*


* Aktoer_v2.url=https://localhost:8063/soap/aktoerregister/ws/Aktoer/v2
* Person_v3.url=https://localhost:8063/soap/tpsws/ws/Person/v3
* Journal_v2.url=https://localhost:8063/soap/joark/Journal/v2
* Journal_v3.url=https://localhost:8063/soap/joark/Journal/v3
* InngaaendeJournal_v1.url=https://localhost:8063/soap/joark/InngaaendeJournal/v1
* \#BehandleInngaaendeJournal_v1.url=MANGLER MANGLER MANGLER, portes fra vl-mock
* Dokumentproduksjon_v2.url=https://localhost:8063/soap/dokprod/ws/dokumentproduksjon/v2
* BehandleSak_v2.url=https://localhost:8063/soap/nav-gsak-ws/BehandleSakV2
* Behandleoppgave_v1.url=https://localhost:8063/soap/nav-gsak-ws/BehandleOppgaveV1
* Oppgave_v3.url=https://localhost:8063/soap/nav-gsak-ws/OppgaveV3
* Sak_v1.url=https://localhost:8063/soap/nav-gsak-ws/SakV1
* InfotrygdSak_v1.url=https://localhost:8063/soap/infotrygd-ws/InfotrygdSak/v1
* InfotrygdBeregningsgrunnlag_v1.url=https://localhost:8063/soap/infotrygd-ws/InfotrygdBeregningsgrunnlag/v1
* \#infotrygd.hendelser.api.url=MANGLER MANGLER MANGLER, portes fra testhub
* Inntekt_v3.url=https://localhost:8063/soap/inntektskomponenten-ws/inntekt/v3/Inntekt
* Arbeidsforhold_v3.url=https://localhost:8063/soap/aareg-core/ArbeidsforholdService/v3
* Organisasjon_v4.url=https://localhost:8063/soap/ereg/ws/OrganisasjonService/v4
* Medlem_v2.url=https://localhost:8063/soap/medl2/ws/Medlemskap/v2
* Kodeverk_v2.url=https://localhost:8063/soap/kodeverk/ws/Kodeverk/v2
* MeldekortUtbetalingsgrunnlag_v1.url=https://localhost:8063/soap/ail_ws/MeldekortUtbetalingsgrunnlag_v1
* SigrunRestBeregnetSkatt.url=https://localhost:8063
* Arbeidsfordeling_v1.url=https://localhost:8063/soap/norg2/ws/Arbeidsfordeling/v1
* infotrygd.hendelser.api.url=https://localhost:8063/rest/infotrygd/hendelser
* hentinntektlistebolk.url=https://localhost:8063/rest/inntektskomponenten-ws/rs/api/v1/hentinntektlistebolk

I tillegg, for å overstyre sikkerhet (PDP, STS, OpenAM):
---
* abac.pdp.endpoint.url=https://localhost:8063/rest/asm-pdp/authorize
* oidc_sts.issuer.url=https://localhost:8063/rest/sts/issuer
* oidc_sts.jwks.url=https://localhost:8063/rest/sts/jwks

### STS web service
* securityTokenService.url=https://localhost:8063/soap/SecurityTokenServiceProvider/

### LDAP
* ldap.url=ldaps://localhost:8636/
* ldap.auth=none
* ldap.user.basedn=ou\=NAV,ou\=BusinessUnits,dc\=test,dc\=local

* OpenIdConnect.issoHost=https://localhost:8063/rest/isso/oauth2
For å sjekke om LDAP kjører riktig kan man bruke `ldapsearch`, eksempel fra under.
```bash
ldapsearch -x -h localhost:8389 -b "ou=NAV,ou=BusinessUnits,dc=test,dc=local"
```

* OpenIdConnect.issoHost=https://localhost:8063/rest/isso/oauth2
* OpenIdConnect.issoIssuer=https://localhost:8063/rest/isso/oauth2
* OpenIdConnect.issoJwks=https://localhost:8063/rest/isso/oauth2/connect/jwk_uri
* OpenIdConnect.username=fpsak-localhost

* systembruker.username=vtp
* systembruker.password=brukes.ikke.av.vtp.men.er.paakrevd.av.api

Utvikling, wsdl
----
Se MockServer for liste over url til genererte wsdl'er for test via SoapUi eller lignende.


### Kjøre via docker run / docker-compose
Lagt til noen forenklinger på environment variabler når vi kjører opp VTP + Autotest i docker. Se
`./resources/pipeline/readme.md` for mer info. Test certifikater er allerede lagt inn i imaget. Men man må
fortsatt sette path riktig.


Bygge fpmock lokalt. Imaget blir da tilgjengelig som fpmock2:latest
```
docker build -t fpmock2 . 
```
