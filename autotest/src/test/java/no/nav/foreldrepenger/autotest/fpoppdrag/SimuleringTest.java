package no.nav.foreldrepenger.autotest.fpoppdrag;

import static no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle.SAKSBEHANDLER;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Random;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import no.nav.foreldrepenger.autotest.base.FpoppdragTestBase;
import no.nav.foreldrepenger.autotest.klienter.fpoppdrag.simulering.dto.BehandlingIdDto;
import no.nav.foreldrepenger.autotest.klienter.fpoppdrag.simulering.dto.SimulerOppdragDto;
import no.nav.foreldrepenger.autotest.klienter.fpoppdrag.simulering.dto.SimuleringDto;
import no.nav.foreldrepenger.autotest.klienter.fpoppdrag.simulering.dto.SimuleringResultatDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.util.DateUtil;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.util.JaxbHelper;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.integrasjon.økonomistøtte.oppdrag.Attestant180;
import no.nav.foreldrepenger.integrasjon.økonomistøtte.oppdrag.Avstemming115;
import no.nav.foreldrepenger.integrasjon.økonomistøtte.oppdrag.Grad170;
import no.nav.foreldrepenger.integrasjon.økonomistøtte.oppdrag.Oppdrag;
import no.nav.foreldrepenger.integrasjon.økonomistøtte.oppdrag.Oppdrag110;
import no.nav.foreldrepenger.integrasjon.økonomistøtte.oppdrag.OppdragSkjemaConstants;
import no.nav.foreldrepenger.integrasjon.økonomistøtte.oppdrag.OppdragsEnhet120;
import no.nav.foreldrepenger.integrasjon.økonomistøtte.oppdrag.OppdragsLinje150;
import no.nav.foreldrepenger.integrasjon.økonomistøtte.oppdrag.TfradragTillegg;

@Tag("fpoppdrag")
public class SimuleringTest extends FpoppdragTestBase {

    @Test
    public void starterSimuleringHenterResultatOgKansellererSimulering() throws IOException, JAXBException, SAXException {
        /*********** Steg 1: Start simulering **************/
        // Arrange
        TestscenarioDto testscenarioDto = testscenarioKlient.opprettTestscenario("49");
        String søkerIdent = testscenarioDto.getPersonopplysninger().getSøkerIdent();
        long behandlingId = new Random().nextInt(999999990) + 1;
        BehandlingIdDto behandlingIdDto = new BehandlingIdDto(String.valueOf(behandlingId));
        Oppdrag oppdrag = new Oppdrag();
        oppdrag.setOppdrag110(opprettOppdrag110(søkerIdent));
        String oppdragXml = JaxbHelper.marshalAndValidateJaxb(OppdragSkjemaConstants.JAXB_CLASS, oppdrag, OppdragSkjemaConstants.XSD_LOCATION);

        SimulerOppdragDto simulerOppdragDto = SimulerOppdragDto.lagDto(behandlingId, Collections.singletonList(oppdragXml));
        saksbehandler.erLoggetInnMedRolle(SAKSBEHANDLER);

        // Act
        saksbehandler.startSimulering(simulerOppdragDto);

        /********** Steg 2: Hent resultat ***********/
        // Act
        SimuleringDto simuleringDto = saksbehandler.hentSimuleringResultat(behandlingIdDto);

        // Assert
        SimuleringResultatDto simuleringResultat = simuleringDto.getSimuleringResultat();
        assertThat(simuleringResultat.getSumEtterbetaling()).isEqualTo(11070);
        assertThat(simuleringResultat.getSumFeilutbetaling()).isEqualTo(0);
        assertThat(simuleringResultat.getSumInntrekk()).isEqualTo(0);
        assertThat(simuleringResultat.getPeriodeFom()).isEqualTo(LocalDate.of(2018, 5, 11));
        assertThat(simuleringResultat.getPeriodeTom()).isEqualTo(LocalDate.of(2018, 5, 31));

        /********* Steg 3: Kanseller simulering *********/
        // Act
        saksbehandler.kansellerSimulering(behandlingIdDto);
        SimuleringDto simuleringDtoKansellert = saksbehandler.hentSimuleringResultat(behandlingIdDto);

        // Assert
        assertThat(simuleringDtoKansellert).isNull();
    }

    private Oppdrag110 opprettOppdrag110(String søkerIdent) {
        Oppdrag110 oppdrag110 = new Oppdrag110();
        oppdrag110.setKodeAksjon("1");
        oppdrag110.setKodeEndring("NY");
        oppdrag110.setKodeFagomraade("FP");
        oppdrag110.setFagsystemId("130158784200");
        oppdrag110.setUtbetFrekvens("MND");
        oppdrag110.setOppdragGjelderId(søkerIdent);
        oppdrag110.setDatoOppdragGjelderFom(DateUtil.convertToXMLGregorianCalendarRemoveTimezone(LocalDate.now()));
        oppdrag110.setSaksbehId("Z991097");
        oppdrag110.setAvstemming115(opprettAvstemming115());
        oppdrag110.getOppdragsEnhet120().add(opprettOppdragsEnhet120());
        oppdrag110.getOppdragsLinje150().add(opprettOppdragsLinje150(søkerIdent));
        return oppdrag110;
    }

    private Avstemming115 opprettAvstemming115() {
        Avstemming115 avstemming115 = new Avstemming115();
        avstemming115.setKodeKomponent("VLFP");
        avstemming115.setNokkelAvstemming("2018-08-16-15.36.55.543");
        avstemming115.setTidspktMelding("2018-08-16-15.36.55.543");
        return avstemming115;
    }

    private OppdragsEnhet120 opprettOppdragsEnhet120() {
        OppdragsEnhet120 oppdragsEnhet120 = new OppdragsEnhet120();
        oppdragsEnhet120.setTypeEnhet("BOS");
        oppdragsEnhet120.setEnhet("8020");
        oppdragsEnhet120.setDatoEnhetFom(DateUtil.convertToXMLGregorianCalendarRemoveTimezone(LocalDate.of(1900, 1, 1)));
        return oppdragsEnhet120;
    }

    private OppdragsLinje150 opprettOppdragsLinje150(String søkerIdent) {
        OppdragsLinje150 oppdragsLinje150 = new OppdragsLinje150();
        oppdragsLinje150.setKodeEndringLinje("NY");
        oppdragsLinje150.setVedtakId("2018-08-16");
        oppdragsLinje150.setDelytelseId("130158784200100");
        oppdragsLinje150.setKodeKlassifik("FPATORD");
        oppdragsLinje150.setDatoVedtakFom(DateUtil.convertToXMLGregorianCalendarRemoveTimezone(LocalDate.of(2018, 5, 11)));
        oppdragsLinje150.setDatoVedtakTom(DateUtil.convertToXMLGregorianCalendarRemoveTimezone(LocalDate.of(2018, 5, 31)));
        oppdragsLinje150.setSats(BigDecimal.valueOf(738));
        oppdragsLinje150.setFradragTillegg(TfradragTillegg.T);
        oppdragsLinje150.setTypeSats("DAG");
        oppdragsLinje150.setBrukKjoreplan("N");
        oppdragsLinje150.setSaksbehId("Z991097");
        oppdragsLinje150.setUtbetalesTilId(søkerIdent);
        oppdragsLinje150.setHenvisning("1000202");
        oppdragsLinje150.getGrad170().add(opprettGrad170());
        oppdragsLinje150.getAttestant180().add(opprettAttestant180());
        return oppdragsLinje150;
    }

    private Attestant180 opprettAttestant180() {
        Attestant180 attestant180 = new Attestant180();
        attestant180.setAttestantId("Z991097");
        return null;
    }

    private Grad170 opprettGrad170() {
        Grad170 grad170 = new Grad170();
        grad170.setGrad(BigInteger.valueOf(100));
        grad170.setTypeGrad("UFOR");
        return grad170;
    }
}
