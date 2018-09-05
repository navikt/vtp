Oppsett og konfigurasjon
====

Starte server
----
* Start mock serveren ved å kjøre MockServer (lokalt).
* Trenger parameter -Dscenarios.dir="./model/scenarios" dersom denne ikke ligger under working dir (dvs. i IDE)
* Swagger UI: http://localhost:8060/web/swagger/
* SoapWebServiceConfig.java inneholder endepunker for virtuelle tjenester. 

TODO
----
1. HTTPS?
2. Se https://jira.adeo.no/browse/PFP-439

Utvikling, wsdl
----
Se MockServer for liste over url til genererte wsdl'er for test via SoapUi eller lignende.
