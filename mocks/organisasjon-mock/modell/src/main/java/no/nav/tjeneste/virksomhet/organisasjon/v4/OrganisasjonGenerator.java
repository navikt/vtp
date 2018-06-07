package no.nav.tjeneste.virksomhet.organisasjon.v4;

import java.time.LocalDate;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.Enhetstyper;
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.Naering;
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.OrganisasjonsDetaljer;
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.Organisasjonsnavn;
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.UstrukturertNavn;
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.Organisasjon;
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.ObjectFactory;

/**
 * Genererer tilfeldige organisasjoner til testformål. Første utgave gir et enkelt navn og enhetstype til innkommende orgnr. Vi ikke feile.
 */
public class OrganisasjonGenerator {

    private ObjectFactory objectFactory = new ObjectFactory();
    private static final String SESAM_AS = "976030788";
    private static final String SESAM_BEDR = "976037286";
    private static final String EPLE_AS = "986498192";
    private static final String EPLE_BEDR = "986507035";

    public Organisasjon lagOrganisasjon(String orgnummer) {
        Organisasjon org = objectFactory.createOrganisasjon();
        UstrukturertNavn navn = objectFactory.createUstrukturertNavn();
        OrganisasjonsDetaljer orgdet = objectFactory.createOrganisasjonsDetaljer();
        Organisasjonsnavn orgnavn = objectFactory.createOrganisasjonsnavn();
        Naering naering = objectFactory.createNaering();

        org.setOrgnummer(orgnummer);
        naering.setNaeringskode(objectFactory.createNaeringskoder());
        if (orgnummer.equals(EPLE_AS) || orgnummer.equals(EPLE_BEDR)) {
            orgnavn.setRedigertNavn("EPLEHUSET NORGE AS");
            navn.getNavnelinje().add("EPLEHUSET NORGE AS");
            if (orgnummer.equals("986507035")) {
                navn.getNavnelinje().add("AVD SOLSIDEN");
            }
            orgdet.setStiftelsesdato(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(2004, 1, 19)));
            orgdet.setRegistreringsDato(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(2004, 1, 27)));
            naering.getNaeringskode().setKodeRef("47.410");
        } else if (orgnummer.equals(SESAM_AS) || orgnummer.equals(SESAM_BEDR)) {
            orgnavn.setRedigertNavn("SESAM AS");
            navn.getNavnelinje().add("SESAM AS");
            orgdet.setStiftelsesdato(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(1995, 12, 12)));
            orgdet.setRegistreringsDato(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(1996, 1, 25)));
            naering.getNaeringskode().setKodeRef("56.102");
        } else {
            orgnavn.setRedigertNavn("INGEN MOCK AS");
            navn.getNavnelinje().add("IKKE NOEN AS");
            navn.getNavnelinje().add("AVD MOCK");
            orgdet.setStiftelsesdato(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(2010, 1, 1)));
            orgdet.setRegistreringsDato(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(2010, 1, 9)));
            naering.getNaeringskode().setKodeRef("47.410");
        }
        orgnavn.setNavn(navn);
        orgdet.getNavn().add(orgnavn);
        orgdet.setGjeldendeMaalform(objectFactory.createMaalformer());
        orgdet.getGjeldendeMaalform().setKodeRef("NB");
        orgdet.setDatoSistEndret(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(2017, 12, 1)));
        naering.setHjelpeenhet(false);
        orgdet.getNaering().add(naering);

        org.setNavn(navn);
        org.setOrganisasjonDetaljer(orgdet);

        return org;
    }

    public Enhetstyper lagEnhetstype(String orgnummer) {
        Enhetstyper enhet = objectFactory.createEnhetstyper();
        if (orgnummer.equals(SESAM_AS) || orgnummer.equals(EPLE_AS)) {
            enhet.setKodeRef("AS");
        } else {
            enhet.setKodeRef("BEDR");
        }
        return enhet;
    }
}
