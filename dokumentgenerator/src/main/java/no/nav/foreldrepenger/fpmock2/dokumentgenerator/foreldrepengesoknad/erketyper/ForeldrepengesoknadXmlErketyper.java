package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.endringssoeknadForeldrepengerYtelseNorskBorgerINorgeFødselMor;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.endringssoeknadYtelse;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengeYtelseNorskBorgerINorge;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengeYtelseNorskBorgerINorgeFodselMedFrilans;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengeYtelseNorskBorgerINorgeFødselFarAleneomsorg;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengeYtelseNorskBorgerINorgeFødselMorAleneomsorg;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengeYtelseNorskBorgerINorgeMedAnnenForelder;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengeYtelseNorskBorgerINorgeTerminFar;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengeYtelseNorskBorgerINorgeTerminMor;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengeYtelseNorskBorgerINorgeTerminMorEkstraUttakFørFødsel;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengerYtelseNorskBorgerINorgeFødselFar;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengerYtelseNorskBorgerINorgeFødselMor;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengerYtelseNorskBorgerINorgeFødselMorMedEgenNaering;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengerYtelseNorskBorgerINorgeFødselMorVentelonnVartpenger;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengerYtelseNorskBorgerINorgeFødselSøkerMedAnnenpart;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.SoekerErketyper.*;

import java.time.LocalDate;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.SoekersRelasjonTilBarnet;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Fordeling;

public class ForeldrepengesoknadXmlErketyper {

    public ForeldrepengesoknadBuilder termindatoUttakKunMor(String aktoerId, LocalDate termindato) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("")
                .withForeldrepengerYtelse(foreldrepengeYtelseNorskBorgerINorgeTerminMor(termindato))
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder termindatoUttakKunFar(String aktoerId, LocalDate termindato) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("")
                .withForeldrepengerYtelse(foreldrepengeYtelseNorskBorgerINorgeTerminFar(termindato))
                .withSoeker(farSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder termindatoUttakKunMor(String aktoerId,Fordeling fordeling, LocalDate termindato) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("")
                .withForeldrepengerYtelse(foreldrepengeYtelseNorskBorgerINorgeTerminMor(termindato, fordeling))
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }
    public ForeldrepengesoknadBuilder uttakKunMor(String aktoerId, Fordeling fordeling, SoekersRelasjonTilBarnet soekersRelasjonTilBarnet) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("")
                .withForeldrepengerYtelse(foreldrepengeYtelseNorskBorgerINorge(fordeling, soekersRelasjonTilBarnet))
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }
    public ForeldrepengesoknadBuilder uttakMedAnnenpart(String aktoerId, String annenpartAktørId, Fordeling fordeling, SoekersRelasjonTilBarnet soekersRelasjonTilBarnet) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("")
                .withForeldrepengerYtelse(foreldrepengeYtelseNorskBorgerINorgeMedAnnenForelder(fordeling, soekersRelasjonTilBarnet, annenpartAktørId))
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }


    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMor(String aktoerId, LocalDate fødselsdato) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((fødselsdato))
                .withBegrunnelseForSenSoeknad(null)
                .withForeldrepengerYtelse(foreldrepengerYtelseNorskBorgerINorgeFødselMor(fødselsdato))
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }


    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMorEkstraUttakFørFødsel(String aktoerId, LocalDate fødselsdato) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((fødselsdato))
                .withBegrunnelseForSenSoeknad(null)
                .withForeldrepengerYtelse(foreldrepengeYtelseNorskBorgerINorgeTerminMorEkstraUttakFørFødsel(fødselsdato))
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMor(String aktoerId, Fordeling fordeling, LocalDate fødselsdato) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((fødselsdato.minusMonths(1)))
                .withBegrunnelseForSenSoeknad(null)
                .withForeldrepengerYtelse(foreldrepengerYtelseNorskBorgerINorgeFødselMor(fordeling, fødselsdato))
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedUttakMorAleneomsorg(String aktoerId, LocalDate fødselsdato) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((fødselsdato.minusMonths(1)))
                .withBegrunnelseForSenSoeknad(null)
                .withForeldrepengerYtelse(foreldrepengeYtelseNorskBorgerINorgeFødselMorAleneomsorg(fødselsdato))
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMorEndring(String aktoerId, LocalDate startDatoForeldrepenger, String saksnummer) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((startDatoForeldrepenger.plusWeeks(3)))
                .withEndringssoeknadYtelse(endringssoeknadForeldrepengerYtelseNorskBorgerINorgeFødselMor(startDatoForeldrepenger, saksnummer))
                .withSoeker(morSoeker(aktoerId));
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedKunMorEndring(String aktoerId, Fordeling fordeling, String saksnummer) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((LocalDate.now().minusDays(1)))
                .withEndringssoeknadYtelse(endringssoeknadYtelse(fordeling, saksnummer))
                .withSoeker(morSoeker(aktoerId));
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMorEngangstonad(String aktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på fødsel som har funnet sted")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeFødselEtterFødsel())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }
    
    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMorEngangstonadFlereBarn(String aktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på fødsel som har funnet sted")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeFødselEtterFødselFlereBarn())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }
    
    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMorEngangstonadSøktForSent(String aktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((LocalDate.now()))
                .withBegrunnelseForSenSoeknad("Begrunnelse")
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på fødsel som har funnet sted")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeFødselEtterSøknadsfrist())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder adopsjonMorEngangstonad(String aktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på adopsjon")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeAdopsjon())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }
    
    public ForeldrepengesoknadBuilder adopsjonFarEngangstonad(String aktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på adopsjon")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeAdopsjon())
                .withSoeker(farSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder terminMorEngangstonad(String aktoerId) {
        return terminMorEngangstonad(aktoerId, LocalDate.now().plusWeeks(3));
    }
    
    public ForeldrepengesoknadBuilder terminMorEngangstonad(String aktoerId, LocalDate termindato) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((LocalDate.now()))
                .withBegrunnelseForSenSoeknad("Begrunnelse")
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på fødsel som har funnet sted")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeTermin(termindato))
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder terminFarEngangstonad(String aktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((LocalDate.now()))
                .withBegrunnelseForSenSoeknad("Begrunnelse")
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på fødsel som har funnet sted")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeTerminFørTermin())
                .withSoeker(farSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder omsorgsovertakelseFarEngangstonad(String aktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest far søker på omsorgsovertakelse")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeOmsorgsovertakelse())
                .withSoeker(farSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder omsorgsovertakelseMorEngangstonad(String aktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest far søker på omsorgsovertakelse")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeOmsorgsovertakelse())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunFar(String aktoerId,
            LocalDate fødselsdato) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((fødselsdato))
                .withBegrunnelseForSenSoeknad(null)
                .withForeldrepengerYtelse(foreldrepengerYtelseNorskBorgerINorgeFødselFar(fødselsdato))
                .withSoeker(farSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedMorMedFar(String morAktørId, String farAktørId, LocalDate fødselsdato, LocalDate mottattdato, Fordeling fordeling) {
        return fodselfunnetstedMorMedFar(morAktørId, farAktørId, fødselsdato, mottattdato, fordeling, 1);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedMorMedFar(String morAktørId, String farAktørId, LocalDate fødselsdato, LocalDate mottattdato, Fordeling fordeling, int antallBarn) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((mottattdato))
                .withBegrunnelseForSenSoeknad(null)
                .withForeldrepengerYtelse(foreldrepengerYtelseNorskBorgerINorgeFødselSøkerMedAnnenpart(fødselsdato, farAktørId, fordeling, antallBarn))
                .withSoeker(morSoeker(morAktørId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedUttakFarAleneomsorg(String aktoerId, LocalDate fødselsdato) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((fødselsdato.minusMonths(1)))
                .withBegrunnelseForSenSoeknad(null)
                .withForeldrepengerYtelse(foreldrepengeYtelseNorskBorgerINorgeFødselFarAleneomsorg(fødselsdato))
                .withSoeker(farSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedFarMedMor(String farAktørId, String morAktørId, LocalDate fødselsdato, LocalDate mottattdato, Fordeling fordeling, int antallBarn) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((mottattdato))
                .withBegrunnelseForSenSoeknad(null)
                .withForeldrepengerYtelse(foreldrepengerYtelseNorskBorgerINorgeFødselSøkerMedAnnenpart(fødselsdato, morAktørId, fordeling, antallBarn))
                .withSoeker(farSoeker(farAktørId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedFarMedMor(String farAktørId, String morAktørId, LocalDate fødselsdato, LocalDate mottattdato, Fordeling fordeling) {
        return fodselfunnetstedFarMedMor(farAktørId, morAktørId, fødselsdato, mottattdato, fordeling, 1);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMorMedEgenNaering(String aktoerId, LocalDate fodselsdato, LocalDate mottattdato) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((mottattdato))
                .withBegrunnelseForSenSoeknad(null)
                .withForeldrepengerYtelse(foreldrepengerYtelseNorskBorgerINorgeFødselMorMedEgenNaering(fodselsdato))
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMorMedFrilans(String aktoerId, LocalDate fodselsdato) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((fodselsdato.plusDays(2)))
                .withBegrunnelseForSenSoeknad(null)
                .withForeldrepengerYtelse(foreldrepengeYtelseNorskBorgerINorgeFodselMedFrilans(fodselsdato))
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }
    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMorMedFrilans(Fordeling fordeling,String aktoerId, LocalDate fodselsdato) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((fodselsdato.plusDays(2)))
                .withBegrunnelseForSenSoeknad(null)
                .withForeldrepengerYtelse(foreldrepengeYtelseNorskBorgerINorgeFodselMedFrilans(fordeling,fodselsdato))
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMorVentelonnVartpenger(String aktoerId, LocalDate fodselsdato) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((fodselsdato.plusDays(2)))
                .withBegrunnelseForSenSoeknad(null)
                .withForeldrepengerYtelse(foreldrepengerYtelseNorskBorgerINorgeFødselMorVentelonnVartpenger(fodselsdato))
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMorEngangstonadIGår(String aktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på fødsel som har funnet sted")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeFødsel4DagerEtterFødsel())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder terminMorEngangstonadEtter26Uke(String aktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((LocalDate.now()))
                .withBegrunnelseForSenSoeknad("Begrunnelse")
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på fødsel som har funnet sted")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeTerminFørTermin())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder terminEngangstonadFarPåVegneAvMor(String aktoerId, String annenpartAktoerId) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på fødsel som har funnet sted")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadTerminKjentAnnenForelder(annenpartAktoerId))
                .withSoeker(morSoeker(aktoerId))//Far søker på vegne av mor
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedUttakMedmorEngangstonad(String søkerAktørIdent) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker på fødsel som har funnet sted")
                .withEngangsstoenadYtelse(EngangstonadYtelseErketyper.engangsstønadUkjentForelderNorgeFødselEtterFødsel())
                .withSoeker(medSoeker(søkerAktørIdent))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }

    public ForeldrepengesoknadBuilder fodselfunnetstedUttakKunMorForeldrepengerFlereBarn(String aktoerId,
            LocalDate fødselsdato, int antallBarn) {
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato((fødselsdato))
                .withBegrunnelseForSenSoeknad(null)
                .withForeldrepengerYtelse(foreldrepengerYtelseNorskBorgerINorgeFødselMor(fødselsdato, antallBarn))
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null);
    }
}
