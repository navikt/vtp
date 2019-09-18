package no.nav.tjeneste.virksomhet.organisasjon.v5;

import no.nav.foreldrepenger.vtp.felles.ConversionUtils;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonDetaljerModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.tjeneste.virksomhet.organisasjon.v5.informasjon.*;

import java.util.Arrays;

public class OrganisasjonsMapper {

    public static Organisasjon mapOrganisasjonFraModell(OrganisasjonModell modell){
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
        if(!(null == detaljer)){
            if(!(null == detaljer.getRegistreringsDato())) {
                organisasjonsDetaljer.setRegistreringsDato(ConversionUtils.convertToXMLGregorianCalendar(detaljer.getRegistreringsDato()));
            }
        }
        return organisasjonsDetaljer;
    }
}


