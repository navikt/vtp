Saf mock
========

# Tjenester
Følgende tjenester finnes på SAF:

|Tjeneste                             |Mock-status|Beskrivelse |
|--------                             |-----------|----------- |
|Query: dokumentoversiktBruker        |     |Lar et fagsystem søke opp metadata for alle dokumenter tilknyttet en gitt bruker.|
|Query: dokumentoversiktFagsak        |     |Lar et fagsystem søke opp metadata for alle dokumenter tilknyttet en fagsak i et fagsystem. Tjenesten vil kunne returnere dokumenter tilhørende ulike brukere, og på ulike tema, dersom fagsaken har dette.|
|saf - REST hentdokument	          |     |Returnerer selve dokumentfila, med dokumentId, journalpostID og variantFormat som nøkkel|
|Query: journalpost	                  |     |Returnerer "all" informasjon på en journalpost (uavhengig av journalposttype), med journalpostId som nøkkel.|
|Query: tilknyttedeJournalposter      |     |Returnerer liste over journalposter som er knyttet til dokumentinfoId som oppgis|
|Query: dokumentoversiktJournalstatus |     |Tillater søk på journalpoststatus i Joark. Foreløpig er kun statusene Utgår og Ukjent bruker støttet. Ta kontakt på slack (#saf) dersom dere trenger å søke på andre kanaler.|


### SAF-lenker
https://confluence.adeo.no/display/BOA/saf+-+Tjenester


# Opprette testdata 
* Opprett testdata ved å ..... 

