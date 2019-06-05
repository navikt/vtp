package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

public class ForeldrepengesoknadBuilder implements MottattDatoStep<ForeldrepengesoknadBuilder>,
        BegrunnelseForSenSoeknadStep<ForeldrepengesoknadBuilder>,
        TilleggsopplysningerStep<ForeldrepengesoknadBuilder>,
        OmYtelseStep<ForeldrepengesoknadBuilder>,
        SoekerStep<ForeldrepengesoknadBuilder>,
        AndreVedleggStep<ForeldrepengesoknadBuilder>,
        PaakrevdeVedlegg<ForeldrepengesoknadBuilder>,
        BuildStep {

    private static final Logger LOG = LoggerFactory.getLogger(ForeldrepengesoknadBuilder.class);

    private LocalDate mottattDato;
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
    public ForeldrepengesoknadBuilder withMottattDato(LocalDate mottattDato) {
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
        this.omYtelse = new no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.ObjectFactory().createForeldrepenger((Foreldrepenger) omYtelse);
        return this;
    }

    @Override
    public ForeldrepengesoknadBuilder withEndringssoeknadYtelse(Ytelse omYtelse) {
        this.omYtelse = new no.nav.vedtak.felles.xml.soeknad.endringssoeknad.v3.ObjectFactory().createEndringssoeknad((Endringssoeknad) omYtelse);
        return this;
    }

    @Override
    public ForeldrepengesoknadBuilder withEngangsstoenadYtelse(Ytelse omYtelse) {
        this.omYtelse = new no.nav.vedtak.felles.xml.soeknad.engangsstoenad.v3.ObjectFactory().createEngangsstønad((Engangsstønad) omYtelse);
        return this;
    }

    @Override
    public ForeldrepengesoknadBuilder withSvangerskapspengeYtelse(Ytelse omYtelse) {
        this.omYtelse = new no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.ObjectFactory().createSvangerskapspenger((Svangerskapspenger) omYtelse);
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

        if(erSvangerskapspenger(soeknad)){
            Svangerskapspenger svangerskapspenger = (Svangerskapspenger) this.omYtelse.getValue();
            svangerskapspenger.getTilretteleggingListe().getTilrettelegging().forEach(tilrettelegging -> {
                tilrettelegging.getVedlegg().forEach(vedlegg -> {
                     soeknad.getPaakrevdeVedlegg().add((Vedlegg) vedlegg.getValue());
                });
            });
        }



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

    private boolean erSvangerskapspenger(Soeknad soeknad) {
        for(Object jaxbYtelseElement : soeknad.getOmYtelse().getAny()){
            Ytelse ytelse = (Ytelse) (((JAXBElement<? extends Ytelse>) jaxbYtelseElement).getValue());
            if(ytelse instanceof Svangerskapspenger){
                return true;
            }
        }
        return false;
    }

    public Soeknad buildEndring() {
        Soeknad soeknad = new Soeknad();
        soeknad.setMottattDato(this.mottattDato);

        OmYtelse omYtelse = new OmYtelse();
        omYtelse.getAny().add(this.omYtelse);
        soeknad.setOmYtelse(omYtelse);
        soeknad.setSoeker(this.soeker);

        return soeknad;
    }
}
