package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengeYtelseNorskBorgerINorgeTerminMedFrilans;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.SoekerErketyper.morSoeker;

import java.time.LocalDate;

import javax.xml.datatype.DatatypeConfigurationException;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.util.DateUtil;
import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;

public class ForeldrepengesoknadXmlErketyper {



    public Soeknad termindatoUttakKunMor(String aktoerId) throws DatatypeConfigurationException {
        Soeknad soeknad = ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()))
                .withBegrunnelseForSenSoeknad(null)
                .withTilleggsopplysninger("Autogenerert erketypetest mor søker basert på termindato") // obs løser ut aksjonspunkt
                .withForeldrepengerYtelse(foreldrepengeYtelseNorskBorgerINorgeTerminMedFrilans())
                .withSoeker(morSoeker(aktoerId))
                .withAndreVedlegg(null)
                .withPaakrevdeVedlegg(null)
                .build();


        return soeknad;
    }

}
