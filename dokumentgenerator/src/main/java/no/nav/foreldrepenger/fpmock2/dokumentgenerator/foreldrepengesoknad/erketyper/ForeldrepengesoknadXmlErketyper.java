package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengeYtelseNorskBorgerINorgeTerminMedFrilans;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.SoekerErketyper.morSoeker;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.SoekerErketyper.farSoeker;

import java.time.LocalDate;

import javax.xml.datatype.DatatypeConfigurationException;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.util.DateUtil;

public class ForeldrepengesoknadXmlErketyper {



    public ForeldrepengesoknadBuilder termindatoUttakKunMor(String aktoerId) throws DatatypeConfigurationException {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker basert på termindato") // obs løser ut aksjonspunkt
                .withForeldrepengerYtelse(foreldrepengeYtelseNorskBorgerINorgeTerminMedFrilans())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);

    }

    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMorEngangstonad(String aktoerId) throws DatatypeConfigurationException {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på fødsel som har funnet sted")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeFødselEtterFødsel())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }
    
    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMorEngangstonadFlereBarn(String aktoerId) throws DatatypeConfigurationException {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på fødsel som har funnet sted")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeFødselEtterFødselFlereBarn())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }
    
    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMorEngangstonadSøktForSent(String aktoerId) throws DatatypeConfigurationException {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad("Begrunnelse")
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på fødsel som har funnet sted")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeFødselEtterSøknadsfrist())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder adopsjonMorEngangstonad(String aktoerId) throws DatatypeConfigurationException {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på adopsjon")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeAdopsjon())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }
    
    public ForeldrepengesoknadBuilder adopsjonFarEngangstonad(String aktoerId) throws DatatypeConfigurationException {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på adopsjon")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeAdopsjon())
                .withSoeker(farSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder terminMorEngangstonad(String aktoerId) throws DatatypeConfigurationException {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad("Begrunnelse")
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på fødsel som har funnet sted")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeTerminFørTermin())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder terminFarEngangstonad(String aktoerId) throws DatatypeConfigurationException {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad("Begrunnelse")
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på fødsel som har funnet sted")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeTerminFørTermin())
                .withSoeker(farSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder omsorgsovertakelseFarEngangstonad(String aktoerId) throws DatatypeConfigurationException {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest far søker på omsorgsovertakelse")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeOmsorgsovertakelse())
                .withSoeker(farSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder omsorgsovertakelseMorEngangstonad(String aktoerId) throws DatatypeConfigurationException {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest far søker på omsorgsovertakelse")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeOmsorgsovertakelse())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    

}
