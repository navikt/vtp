package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.util.JaxbHelper;
import no.nav.foreldrepenger.søknad.v3.SøknadConstants;
import no.nav.vedtak.felles.xml.soeknad.endringssoeknad.v3.Endringssoeknad;
import no.nav.vedtak.felles.xml.soeknad.engangsstoenad.v3.Engangsstønad;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.Bruker;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.Vedlegg;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.Ytelse;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.Foreldrepenger;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.Svangerskapspenger;
import no.nav.vedtak.felles.xml.soeknad.v3.ObjectFactory;
import no.nav.vedtak.felles.xml.soeknad.v3.OmYtelse;
import no.nav.vedtak.felles.xml.soeknad.v3.Soeknad;

import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.SoekerErketyper.soekerAvType;

public class ForeldrepengesoknadBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(ForeldrepengesoknadBuilder.class);
    private Soeknad kladd;
    private JAXBElement<? extends Ytelse> omYtelseJAXBElementKladd;

    public ForeldrepengesoknadBuilder() {
        kladd = new Soeknad();
    }

    public static ForeldrepengesoknadBuilder startBuilding() {
        return new ForeldrepengesoknadBuilder();
    }

    public static ForeldrepengesoknadBuilder soeknad() {
        return new ForeldrepengesoknadBuilder();
    }

    /**
     * Konverterer {@link Soeknad} til XML, eller kaster en {@link RuntimeException} ved feil
     */
    public static String tilXML(Soeknad soeknad) {
        String xml = null;
        try {
            JAXBElement<Soeknad> soeknadsskjemaForeldrepengerJAXBElement = (new ObjectFactory().createSoeknad(soeknad));
            xml = JaxbHelper.marshalAndValidateJaxb(SøknadConstants.JAXB_CLASS,
                    soeknadsskjemaForeldrepengerJAXBElement,
                    SøknadConstants.XSD_LOCATION,
                    SøknadConstants.ADDITIONAL_XSD_LOCATION,
                    SøknadConstants.ADDITIONAL_CLASSES);
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
        }
        return xml;
    }

    public ForeldrepengesoknadBuilder withMottattDato(LocalDate mottattDato) {
        kladd.setMottattDato(mottattDato);
        return this;
    }

    public ForeldrepengesoknadBuilder withBegrunnelseForSenSoeknad(String begrunnelseForSenSoeknad) {
        kladd.setBegrunnelseForSenSoeknad(begrunnelseForSenSoeknad);
        return this;
    }

    public ForeldrepengesoknadBuilder withTilleggsopplysninger(String tilleggsopplysninger) {
        kladd.setTilleggsopplysninger(tilleggsopplysninger);
        return this;
    }

    public ForeldrepengesoknadBuilder withForeldrepengerYtelse(Ytelse omYtelse) {
        omYtelseJAXBElementKladd = new no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.ObjectFactory().createForeldrepenger((Foreldrepenger) omYtelse);
        return this;
    }

    public ForeldrepengesoknadBuilder withEndringssoeknadYtelse(Ytelse omYtelse) {
        omYtelseJAXBElementKladd = new no.nav.vedtak.felles.xml.soeknad.endringssoeknad.v3.ObjectFactory().createEndringssoeknad((Endringssoeknad) omYtelse);
        return this;
    }

    public ForeldrepengesoknadBuilder withEngangsstoenadYtelse(Ytelse omYtelse) {
        omYtelseJAXBElementKladd = new no.nav.vedtak.felles.xml.soeknad.engangsstoenad.v3.ObjectFactory().createEngangsstønad((Engangsstønad) omYtelse);
        return this;
    }

    public ForeldrepengesoknadBuilder withSvangerskapspengeYtelse(Ytelse omYtelse) {
        omYtelseJAXBElementKladd = new no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.ObjectFactory().createSvangerskapspenger((Svangerskapspenger) omYtelse);
        Svangerskapspenger svangerskapspenger = (Svangerskapspenger) omYtelseJAXBElementKladd.getValue();
        svangerskapspenger.getTilretteleggingListe().getTilrettelegging().forEach(tilrettelegging -> tilrettelegging.getVedlegg().forEach(vedlegg -> {
            kladd.getPaakrevdeVedlegg().add((Vedlegg) vedlegg.getValue());
        }));
        return this;
    }

    public ForeldrepengesoknadBuilder withSoeker(Bruker soeker) {
        //TODO slettes når ForeldrepengesoknadXmlErketyper er ryddet
        kladd.setSoeker(soeker);
        return this;
    }
    public ForeldrepengesoknadBuilder withSoeker(String aktoerId, String rolle){
        kladd.setSoeker(soekerAvType(aktoerId, rolle));
        return this;
    }

    public ForeldrepengesoknadBuilder withAndreVedlegg(List<Vedlegg> andreVedlegg) {
        if(andreVedlegg != null){
            andreVedlegg.forEach(av -> kladd.getAndreVedlegg().add(av));
        }
        return this;
    }
    public ForeldrepengesoknadBuilder withPaakrevdeVedlegg(List<Vedlegg> paakrevdeVedlegg) {
        if(paakrevdeVedlegg != null) {
            paakrevdeVedlegg.forEach(pv -> kladd.getPaakrevdeVedlegg().add(pv));
        }
        return this;
    }

    public Soeknad build() {
        OmYtelse omYtelseKladd = new OmYtelse();
        omYtelseKladd.getAny().add(this.omYtelseJAXBElementKladd);
        kladd.setOmYtelse(omYtelseKladd);

        if(kladd.getBegrunnelseForSenSoeknad() == null){
            kladd.setBegrunnelseForSenSoeknad((String) null);
        }
        if(kladd.getTilleggsopplysninger() == null){
            kladd.setTilleggsopplysninger("");
        }
        if(kladd.getMottattDato() == null){
            kladd.setMottattDato(LocalDate.now());
        }
        Objects.requireNonNull(kladd.getOmYtelse(), "Ytelse kan ikke være null");
        Objects.requireNonNull(kladd.getSoeker(), "Søker kan ikke være null");

        return kladd;
    }
}
