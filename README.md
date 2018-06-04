Oppsett og konfigurasjon
====

Starte server
----
* Start mock serveren ved å kjøre no.nav.engangsstønad.mock.MockServer.
* Ingen konfigurering av selv mock er nødvendig.

Utvikling, database
----
I skrivende stund går tester (verifisert i Infotrygdmock) direkte mot prodbasen. Se feks infotrygd-mock -> modell -> src/main/resources/META_INF -> persistence.xml

Øvrige mocker som har persistence.xml konfigurering, men hvor det ikke er verifisert hvilken base de går mot
* inntekt-mock
* arbeidsforhold-mock
* arenda-mock
* journal-mock
* medl-mock
* norg2-mock
* organisasjon-mock
* tps-mock

Utvikling, wsdl
----
Se no.nav.engangsstønad.mock.MockServer for liste over url til genererte wsdl'er for test via SoapUi eller lignende.
