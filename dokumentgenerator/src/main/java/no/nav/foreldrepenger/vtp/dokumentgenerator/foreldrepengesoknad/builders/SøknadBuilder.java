package no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.builders;

import java.time.LocalDate;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.SøkersRolle;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.util.JaxbHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import no.nav.foreldrepenger.søknad.v3.SøknadConstants;
import no.nav.vedtak.felles.xml.soeknad.endringssoeknad.v3.Endringssoeknad;
import no.nav.vedtak.felles.xml.soeknad.engangsstoenad.v3.Engangsstønad;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.Vedlegg;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.Ytelse;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.Foreldrepenger;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.Svangerskapspenger;
import no.nav.vedtak.felles.xml.soeknad.v3.ObjectFactory;
import no.nav.vedtak.felles.xml.soeknad.v3.OmYtelse;
import no.nav.vedtak.felles.xml.soeknad.v3.Soeknad;

import static no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.SoekerErketyper.soekerAvType;

public class SøknadBuilder {

    private Soeknad kladd;

    public SøknadBuilder(Ytelse ytelse, String aktørID, SøkersRolle søkersRolle) {
        kladd = new Soeknad();
        withYtelse(ytelse);
        withSoeker(aktørID, søkersRolle);
    }
    private SøknadBuilder withSoeker(String aktoerId, SøkersRolle søkerRolle){
        kladd.setSoeker(soekerAvType(aktoerId, søkerRolle));
        return this;
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
    public SøknadBuilder withMottattDato(LocalDate mottattDato) {
        kladd.setMottattDato(mottattDato);
        return this;
    }
    public SøknadBuilder withBegrunnelseForSenSoeknad(String begrunnelseForSenSoeknad) {
        kladd.setBegrunnelseForSenSoeknad(begrunnelseForSenSoeknad);
        return this;
    }
    public SøknadBuilder withYtelse(Ytelse ytelse) {
        if (ytelse instanceof Foreldrepenger) {
            setOmYtelseJAXBElement((new no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.ObjectFactory()).createForeldrepenger((Foreldrepenger) ytelse));
        }
        else if (ytelse instanceof Endringssoeknad) {
            setOmYtelseJAXBElement((new no.nav.vedtak.felles.xml.soeknad.endringssoeknad.v3.ObjectFactory()).createEndringssoeknad((Endringssoeknad) ytelse));
        }
        else if (ytelse instanceof Engangsstønad) {
            setOmYtelseJAXBElement((new no.nav.vedtak.felles.xml.soeknad.engangsstoenad.v3.ObjectFactory()).createEngangsstønad((Engangsstønad) ytelse));
        }
        else if (ytelse instanceof Svangerskapspenger){
            JAXBElement<? extends Ytelse> omYtelseJAXBElementKladd = (new no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.ObjectFactory()).createSvangerskapspenger((Svangerskapspenger) ytelse);
            Svangerskapspenger svangerskapspenger = (Svangerskapspenger)omYtelseJAXBElementKladd.getValue();
            svangerskapspenger.getTilretteleggingListe().getTilrettelegging().forEach((tilrettelegging) -> {
                tilrettelegging.getVedlegg().forEach((vedlegg) -> {
                    this.kladd.getPaakrevdeVedlegg().add((Vedlegg)vedlegg.getValue());
                });
            });
            this.setOmYtelseJAXBElement(omYtelseJAXBElementKladd);
        }
        return this;
    }
    private void setOmYtelseJAXBElement(JAXBElement<? extends Ytelse> omYtelseJAXBElement) {
        OmYtelse omYtelseKladd = new OmYtelse();
        omYtelseKladd.getAny().add(omYtelseJAXBElement);
        this.kladd.setOmYtelse(omYtelseKladd);
    }

    public SøknadBuilder withTilleggsopplysninger(String tilleggsopplysninger) {
        kladd.setTilleggsopplysninger(tilleggsopplysninger);
        return this;
    }
    public SøknadBuilder withAndreVedlegg(List<Vedlegg> andreVedlegg) {
        if(andreVedlegg != null){
            andreVedlegg.forEach(av -> kladd.getAndreVedlegg().add(av));
        }
        return this;
    }
    public SøknadBuilder withPaakrevdeVedlegg(List<Vedlegg> paakrevdeVedlegg) {
        if(paakrevdeVedlegg != null) {
            paakrevdeVedlegg.forEach(pv -> kladd.getPaakrevdeVedlegg().add(pv));
        }
        return this;
    }
    public Soeknad build() {
        //Hvis ikke verdiene for ikke kritiske variabler er satt blir de satt til en defaultverdi.
        if(kladd.getBegrunnelseForSenSoeknad() == null){
            kladd.setBegrunnelseForSenSoeknad((String) null);
        }
        if(kladd.getTilleggsopplysninger() == null){
            kladd.setTilleggsopplysninger("");
        }
        if(kladd.getMottattDato() == null){
            kladd.setMottattDato(LocalDate.now());
        }
        return kladd;
    }
}
