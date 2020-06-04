package no.nav.tjeneste.virksomhet.organisasjon.v4;

import java.util.Arrays;

import no.nav.foreldrepenger.vtp.felles.ConversionUtils;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonDetaljerModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.Organisasjon;
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.OrganisasjonsDetaljer;
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.UstrukturertNavn;
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.Virksomhet;
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.VirksomhetDetaljer;

public class OrganisasjonsMapper {
    public OrganisasjonsMapper() {
    }

    public static Organisasjon mapOrganisasjonFraModell(OrganisasjonModell modell) {
        Virksomhet organisasjon = new Virksomhet();
        organisasjon.setOrgnummer(modell.getOrgnummer());
        UstrukturertNavn ustrukturertNavn = new UstrukturertNavn();
        ustrukturertNavn.getNavnelinje().addAll(Arrays.asList(modell.getNavn().getNavnelinje()));
        organisasjon.setNavn(ustrukturertNavn);
        organisasjon.setOrganisasjonDetaljer(mapOrganisasjonDetaljerFraModell(modell.getOrganisasjonDetaljer()));
        organisasjon.setVirksomhetDetaljer(new VirksomhetDetaljer());
        return organisasjon;
    }

    public static OrganisasjonsDetaljer mapOrganisasjonDetaljerFraModell(OrganisasjonDetaljerModell detaljer) {
        OrganisasjonsDetaljer organisasjonsDetaljer = new OrganisasjonsDetaljer();
        if (!(null == detaljer)) {
            if (!(null == detaljer.getRegistreringsDato())) {
                organisasjonsDetaljer.setRegistreringsDato(ConversionUtils.convertToXMLGregorianCalendar(detaljer.getRegistreringsDato()));
            }
            if (!(null == detaljer.getDatoSistEndret())) {
                organisasjonsDetaljer.setDatoSistEndret(ConversionUtils.convertToXMLGregorianCalendar(detaljer.getDatoSistEndret()));
            }
            if(!(null == detaljer.getPostadresse())) {
                organisasjonsDetaljer.getPostadresse();
            }
        }
        return organisasjonsDetaljer;
    }
}
