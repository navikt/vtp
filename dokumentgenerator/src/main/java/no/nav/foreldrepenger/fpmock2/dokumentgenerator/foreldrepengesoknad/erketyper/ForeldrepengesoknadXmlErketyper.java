package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengeYtelseNorskBorgerINorgeTerminFar;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengeYtelseNorskBorgerINorgeTerminMor;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengerYtelseNorskBorgerINorgeFødselFar;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengerYtelseNorskBorgerINorgeFødselFarMedMor;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengerYtelseNorskBorgerINorgeFødselMor;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengerYtelseNorskBorgerINorgeFødselMorMedEgenNaering;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengerYtelseNorskBorgerINorgeFødselMorMedFar;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.SoekerErketyper.farSoeker;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.SoekerErketyper.morSoeker;

import java.time.LocalDate;

import javax.xml.datatype.DatatypeConfigurationException;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.util.DateUtil;

public class ForeldrepengesoknadXmlErketyper {



    public ForeldrepengesoknadBuilder termindatoUttakKunMor(String aktoerId, LocalDate termindato) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("")
                .withForeldrepengerYtelse(foreldrepengeYtelseNorskBorgerINorgeTerminMor(termindato))
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder termindatoUttakKunFar(String aktoerId, LocalDate termindato) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("")
                .withForeldrepengerYtelse(foreldrepengeYtelseNorskBorgerINorgeTerminFar(termindato))
                .withSoeker(farSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMor(String aktoerId, LocalDate startDatoForeldrepenger) throws DatatypeConfigurationException {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(startDatoForeldrepenger.plusWeeks(2)))
                .withBegrunnelseForSenSoeknad(null)
                .withForeldrepengerYtelse(foreldrepengerYtelseNorskBorgerINorgeFødselMor(startDatoForeldrepenger))
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMorEngangstonad(String aktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på fødsel som har funnet sted")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeFødselEtterFødsel())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }
    
    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMorEngangstonadFlereBarn(String aktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på fødsel som har funnet sted")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeFødselEtterFødselFlereBarn())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }
    
    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMorEngangstonadSøktForSent(String aktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad("Begrunnelse")
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på fødsel som har funnet sted")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeFødselEtterSøknadsfrist())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder adopsjonMorEngangstonad(String aktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på adopsjon")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeAdopsjon())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }
    
    public ForeldrepengesoknadBuilder adopsjonFarEngangstonad(String aktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på adopsjon")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeAdopsjon())
                .withSoeker(farSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder terminMorEngangstonad(String aktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad("Begrunnelse")
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på fødsel som har funnet sted")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeTerminFørTermin())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder terminFarEngangstonad(String aktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad("Begrunnelse")
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på fødsel som har funnet sted")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeTerminFørTermin())
                .withSoeker(farSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder omsorgsovertakelseFarEngangstonad(String aktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest far søker på omsorgsovertakelse")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeOmsorgsovertakelse())
                .withSoeker(farSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder omsorgsovertakelseMorEngangstonad(String aktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest far søker på omsorgsovertakelse")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeOmsorgsovertakelse())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunFar(String aktoerId,
            LocalDate startDatoForeldrepenger) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(startDatoForeldrepenger.minusWeeks(3)))
                .withBegrunnelseForSenSoeknad(null)
                .withForeldrepengerYtelse(foreldrepengerYtelseNorskBorgerINorgeFødselFar(startDatoForeldrepenger))
                .withSoeker(farSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedMorMedFar(String morAktørId, String farAktørId, LocalDate startDatoForeldrepenger) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(startDatoForeldrepenger.plusWeeks(2)))
                .withBegrunnelseForSenSoeknad(null)
                .withForeldrepengerYtelse(foreldrepengerYtelseNorskBorgerINorgeFødselMorMedFar(startDatoForeldrepenger, farAktørId))
                .withSoeker(morSoeker(morAktørId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedFarMedMor(String farAktørId, String morAktørId, LocalDate startDatoForeldrepenger) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(startDatoForeldrepenger.minusWeeks(3)))
                .withBegrunnelseForSenSoeknad(null)
                .withForeldrepengerYtelse(foreldrepengerYtelseNorskBorgerINorgeFødselFarMedMor(startDatoForeldrepenger, morAktørId))
                .withSoeker(farSoeker(farAktørId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMorMedEgenNaering(String aktoerId, LocalDate startDatoForeldrepenger) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(startDatoForeldrepenger.plusWeeks(2)))
                .withBegrunnelseForSenSoeknad(null)
                .withForeldrepengerYtelse(foreldrepengerYtelseNorskBorgerINorgeFødselMorMedEgenNaering(startDatoForeldrepenger))
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

}
