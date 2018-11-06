package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad;

import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.util.JaxbHelper;
import no.nav.foreldrepenger.søknad.v1.SøknadConstants;
import no.nav.vedtak.felles.xml.soeknad.endringssoeknad.v1.Endringssoeknad;
import no.nav.vedtak.felles.xml.soeknad.engangsstoenad.v1.Engangsstønad;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Bruker;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Vedlegg;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Ytelse;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.Foreldrepenger;
import no.nav.vedtak.felles.xml.soeknad.v1.ObjectFactory;
import no.nav.vedtak.felles.xml.soeknad.v1.OmYtelse;
import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;

public class ForeldrepengesoknadBuilder implements MottattDatoStep<ForeldrepengesoknadBuilder>,
        BegrunnelseForSenSoeknadStep<ForeldrepengesoknadBuilder>,
        TilleggsopplysningerStep<ForeldrepengesoknadBuilder>,
        OmYtelseStep<ForeldrepengesoknadBuilder>,
        SoekerStep<ForeldrepengesoknadBuilder>,
        AndreVedleggStep<ForeldrepengesoknadBuilder>,
        PaakrevdeVedlegg<ForeldrepengesoknadBuilder>,
        BuildStep {

    private static final Logger log = LoggerFactory.getLogger(ForeldrepengesoknadBuilder.class);
    private XMLGregorianCalendar mottattDato;
    private String begrunnelseForSenSoeknad;
    private String tilleggsopplysninger;
    private JAXBElement<? extends Ytelse> omYtelse;
    private Bruker soeker;
    private List<Vedlegg> andreVedlegg;
    private List<Vedlegg> paakrevdeVedlegg;


    private ForeldrepengesoknadBuilder() {
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

    @Override
    public ForeldrepengesoknadBuilder withMottattDato(XMLGregorianCalendar mottattDato) {
        this.mottattDato = mottattDato;
        return this;
    }

    @Override
    public ForeldrepengesoknadBuilder withBegrunnelseForSenSoeknad(String begrunnelseForSenSoeknad) {
        this.begrunnelseForSenSoeknad = begrunnelseForSenSoeknad;
        return this;
    }

    @Override
    public ForeldrepengesoknadBuilder withTilleggsopplysninger(String tilleggsopplysninger) {
        this.tilleggsopplysninger = tilleggsopplysninger;
        return this;
    }

    @Override
    public ForeldrepengesoknadBuilder withForeldrepengerYtelse(Ytelse omYtelse) {
        this.omYtelse = new no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.ObjectFactory().createForeldrepenger((Foreldrepenger) omYtelse);
        return this;
    }

    @Override
    public ForeldrepengesoknadBuilder withEndringssoeknadYtelse(Ytelse omYtelse) {
        this.omYtelse = new no.nav.vedtak.felles.xml.soeknad.endringssoeknad.v1.ObjectFactory().createEndringssoeknad((Endringssoeknad) omYtelse);
        return this;
    }

    @Override
    public ForeldrepengesoknadBuilder withEngangsstoenadYtelse(Ytelse omYtelse) {
        this.omYtelse = new no.nav.vedtak.felles.xml.soeknad.engangsstoenad.v1.ObjectFactory().createEngangsstønad((Engangsstønad) omYtelse);
        return this;
    }

    @Override
    public ForeldrepengesoknadBuilder withSoeker(Bruker soeker) {
        this.soeker = soeker;
        return this;
    }

    @Override
    public ForeldrepengesoknadBuilder withAndreVedlegg(List<Vedlegg> andreVedlegg) {
        this.andreVedlegg = andreVedlegg;
        return this;
    }

    @Override
    public ForeldrepengesoknadBuilder withPaakrevdeVedlegg(List<Vedlegg> paakrevdeVedlegg) {
        this.paakrevdeVedlegg = paakrevdeVedlegg;
        return this;
    }

    @Override
    public Soeknad build() {
        Soeknad soeknad = new Soeknad();
        soeknad.setMottattDato(this.mottattDato);
        soeknad.setBegrunnelseForSenSoeknad(this.begrunnelseForSenSoeknad);
        soeknad.setTilleggsopplysninger(this.tilleggsopplysninger);

        OmYtelse omYtelse = new OmYtelse();
        omYtelse.getAny().add(this.omYtelse);
        soeknad.setOmYtelse(omYtelse);

        soeknad.setSoeker(this.soeker);
        if (null != this.paakrevdeVedlegg) {
            this.paakrevdeVedlegg.forEach(pkv -> {
                soeknad.getPaakrevdeVedlegg().add(pkv);
            });
        }

        if (null != this.andreVedlegg) {
            this.andreVedlegg.forEach(av -> {
                soeknad.getAndreVedlegg().add(av);
            });
        }


        return soeknad;
    }
}
