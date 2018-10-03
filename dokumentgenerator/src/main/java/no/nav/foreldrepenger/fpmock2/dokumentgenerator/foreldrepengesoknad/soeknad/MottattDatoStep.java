package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad;

import javax.xml.datatype.XMLGregorianCalendar;

public interface MottattDatoStep<T extends MottattDatoStep<T>> {
    T withMottattDato(XMLGregorianCalendar mottattDato);
}
