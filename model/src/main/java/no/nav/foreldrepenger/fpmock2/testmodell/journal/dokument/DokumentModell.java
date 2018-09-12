package no.nav.foreldrepenger.fpmock2.testmodell.journal.dokument;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({@JsonSubTypes.Type(SøknadModell.class), @JsonSubTypes.Type(UstrukturertDokumentModell.class), @JsonSubTypes.Type(InntektsmeldingModell.class)})
public class DokumentModell {


}
