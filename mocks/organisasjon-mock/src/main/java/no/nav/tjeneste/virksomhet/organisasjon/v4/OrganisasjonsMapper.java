package no.nav.tjeneste.virksomhet.organisasjon.v4;

import java.util.Arrays;

import no.nav.foreldrepenger.fpmock2.testmodell.organisasjon.OrganisasjonDetaljerModell;
import no.nav.foreldrepenger.fpmock2.testmodell.organisasjon.OrganisasjonModell;
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.Organisasjon;
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.OrganisasjonsDetaljer;
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.UstrukturertNavn;

public class OrganisasjonsMapper {
    public OrganisasjonsMapper() {
    }

    public static Organisasjon mapOrganisasjonFraModell(OrganisasjonModell modell) {
        Organisasjon organisasjon = new Organisasjon();
        organisasjon.setOrgnummer(modell.getOrgnummer());
        UstrukturertNavn ustrukturertNavn = new UstrukturertNavn();
        ustrukturertNavn.getNavnelinje().addAll(Arrays.asList(modell.getNavn().getNavnelinje()));
        organisasjon.setNavn(ustrukturertNavn);
        organisasjon.setOrganisasjonDetaljer(mapOrganisasjonDetaljerFraModell(modell.getOrganisasjonDetaljer()));
        return organisasjon;
    }

    public static OrganisasjonsDetaljer mapOrganisasjonDetaljerFraModell(OrganisasjonDetaljerModell detaljer) {
        OrganisasjonsDetaljer organisasjonsDetaljer = new OrganisasjonsDetaljer();
        //organisasjonsDetaljer.setRegistreringsDato(ConversionUtils.convertToXMLGregorianCalendar(detaljer.getRegistreringsDato()));
        //organisasjonsDetaljer.setDatoSistEndret(ConversionUtils.convertToXMLGregorianCalendar(detaljer.getDatoSistEndret()));
        return organisasjonsDetaljer;
    }
}
