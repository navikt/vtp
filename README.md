Oppsett og konfigurasjon
====

#Repo skal ligge som private inntil historikken er gjennomgått. #

Starte server
----
* Start mock serveren ved å kjøre MockServer (lokalt).
* Trenger parameter -Dscenarios.dir="./model/scenarios" dersom denne ikke ligger under working dir (dvs. i IDE).
* Swagger UI: https://localhost:8063/swagger/ - Bruk HTTP for kall
* SoapWebServiceConfig.java inneholder endepunker for virtuelle tjenester.

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


* Aktoer_v2.url=https://localhost:8063/aktoerregister/ws/Aktoer/v2
* Person_v3.url=https://localhost:8063/tpsws/ws/Person/v3
* Journal_v2.url=https://localhost:8063/joark/Journal/v2
* Journal_v3.url=https://localhost:8063/joark/Journal/v3
* InngaaendeJournal_v1.url=https://localhost:8063/joark/InngaaendeJournal/v1
* Dokumentproduksjon_v2.url=https://localhost:8063/dokprod/ws/dokumentproduksjon/v2
* BehandleSak_v2.url=https://localhost:8063/nav-gsak-ws/BehandleSakV2
* Behandleoppgave_v1.url=https://localhost:8063/nav-gsak-ws/BehandleOppgaveV1
* Oppgave_v3.url=https://localhost:8063/nav-gsak-ws/OppgaveV3
* Sak_v1.url=https://localhost:8063/nav-gsak-ws/SakV1
* InfotrygdSak_v1.url=https://localhost:8063/infotrygd-ws/InfotrygdSak/v1
* InfotrygdBeregningsgrunnlag_v1.url=https://localhost:8063/infotrygd-ws/InfotrygdBeregningsgrunnlag/v1
* Inntekt_v3.url=https://localhost:8063/inntektskomponenten-ws/inntekt/v3/Inntekt
* Arbeidsforhold_v3.url=https://localhost:8063/aareg-core/ArbeidsforholdService/v3
* Organisasjon_v4.url=https://localhost:8063/ereg/ws/OrganisasjonService/v4
* Medlem_v2.url=https://localhost:8063/medl2/ws/Medlemskap/v2
* Kodeverk_v2.url=https://localhost:8063/kodeverk/ws/Kodeverk/v2
* MeldekortUtbetalingsgrunnlag_v1.url=https://localhost:8063/ail_ws/MeldekortUtbetalingsgrunnlag_v1
* #SigrunRestBeregnetSkatt.url= MANGLER MANGLER MANGLER, port fra httpss://fpmock-t10.nais.preprod.local
* Arbeidsfordeling_v1.url=https://localhost:8063/norg2/ws/Arbeidsfordeling/v1
* infotrygd.hendelser.api.url=https://localhost:8063/infotrygd/hendelser

I tillegg, for å overstyre sikkerhet (PDP, STS, OpenAM):
---
* abac.pdp.endpoint.url=https://localhost:8063/asm-pdp/authorize
* oidc_sts.issuer.url=https://localhost:8063/sts/issuer
* oidc_sts.jwks.url=https://localhost:8063/sts/jwks

### STS web service
* securityTokenService.url=https://localhost:8063/SecurityTokenServiceProvider/

### LDAP
* ldap.url=ldaps://localhost:8636/
* ldap.auth=none
* ldap.user.basedn=ou\=NAV,ou\=BusinessUnits,dc\=test,dc\=local

* OpenIdConnect.issoHost=https://localhost:8063/isso/oauth2
* OpenIdConnect.issoIssuer=https://localhost:8063/isso/oauth2
* OpenIdConnect.issoJwks=https://localhost:8063/isso/oauth2/connect/jwk_uri
* OpenIdConnect.username=fpsak-localhost

* systembruker.username=vtp
* systembruker.password=brukes.ikke.av.vtp.men.er.påkrevd.av.api 

Utvikling, wsdl
----
Se MockServer for liste over url til genererte wsdl'er for test via SoapUi eller lignende.


### Kjøre via docker run / docker-compose
Lagt til noen forenklinger på environment variabler når vi kjører opp VTP + Autotest i docker. Se
`./resources/pipeline/readme.md` for mer info. Test certifikater er allerede lagt inn i imaget. Men man må
fortsatt sette path riktig.

```
CUSTOM_KEYSTORE_PATH=/app/testcerts/vtpkeystore.jks
CUSTOM_KEYSTORE_PASSWORD=changeit
NAV_TRUSTSTORE_PATH=/app/testcerts/nav_truststore_path
NAV_TRUSTSTORE_PASSWORD=changeit
ENABLE_CUSTOM_TRUSTSTORE=true
AUTOTEST_OAUTH2_ISSUER=https://fpmock2:8063/isso/oauth2
AUTOTEST_FPSAK_BASE_URL=http://fpsak:8080
AUTOTEST_VTP_BASE_URL=http://fpmock2:8060
LDAP_PROVIDER_URL=ldaps://fpmock2:8636
AUTHORIZE_BASE_URL=http://localhost:8060
```      

