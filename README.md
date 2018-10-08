Oppsett og konfigurasjon
====

Starte server
----
* Start mock serveren ved å kjøre MockServer (lokalt).
* Trenger parameter -Dscenarios.dir="./model/scenarios" dersom denne ikke ligger under working dir (dvs. i IDE).
* Swagger UI: http://localhost:8060/swagger/
* SoapWebServiceConfig.java inneholder endepunker for virtuelle tjenester.

Koble foreldrepenger til VTP
------

Følgende properties må settes i fpsak

* #abac.pdp.endpoint.url=https://localhost:8063/asm-pdp/authorize
* #securityTokenService.url=https://localhost:8063/isso/oauth2/access_token
* Aktoer_v2.url=https://localhost:8063/aktoerregister/ws/Aktoer/v2
* Person_v3.url=https://localhost:8063/tpsws/ws/Person/v3
* Journal_v2.url=https://localhost:8063/joark/Journal/v2
* InngaaendeJournal_v1.url=https://localhost:8063/joark/InngaaendeJournal/v1
* #BehandleInngaaendeJournal_v1.url=MANGLER MANGLER MANGLER, portes fra vl-mock
* Dokumentproduksjon_v2.url=https://localhost:8063/dokprod/ws/dokumentproduksjon/v2
* BehandleSak_v2.url=https://localhost:8063/nav-gsak-ws/BehandleSakV2
* Behandleoppgave_v1.url=https://localhost:8063/nav-gsak-ws/BehandleOppgaveV1
* Oppgave_v3.url=https://localhost:8063/nav-gsak-ws/OppgaveV3
* Sak_v1.url=https://localhost:8063/nav-gsak-ws/SakV1
* InfotrygdSak_v1.url=https://localhost:8063/infotrygd-ws/InfotrygdSak/v1
* InfotrygdBeregningsgrunnlag_v1.url=https://localhost:8063/infotrygd-ws/InfotrygdBeregningsgrunnlag/v1
* #infotrygd.hendelser.api.url=MANGLER MANGLER MANGLER, portes fra testhub
* Inntekt_v3.url=https://localhost:8063/inntektskomponenten-ws/inntekt/v3/Inntekt
* Arbeidsforhold_v3.url=https://localhost:8063/aareg-core/ArbeidsforholdService/v3
* Organisasjon_v4.url=https://localhost:8063/ereg/ws/OrganisasjonService/v4
* Medlem_v2.url=https://localhost:8063/medl2/ws/Medlemskap/v2
* Kodeverk_v2.url=https://localhost:8063/kodeverk/ws/Kodeverk/v2
* MeldekortUtbetalingsgrunnlag_v1.url=https://localhost:8063/ail_ws/MeldekortUtbetalingsgrunnlag_v1
* #SigrunRestBeregnetSkatt.url= MANGLER MANGLER MANGLER, port fra httpss://fpmock-t10.nais.preprod.local
* Arbeidsfordeling_v1.url=https://localhost:8063/norg2/ws/Arbeidsfordeling/v1

TODO
----
1. HTTPS?
2. Se https://jira.adeo.no/browse/PFP-439

Utvikling, wsdl
----
Se MockServer for liste over url til genererte wsdl'er for test via SoapUi eller lignende.
