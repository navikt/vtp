package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import java.time.LocalDate;

import javax.xml.datatype.DatatypeConfigurationException;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.util.DateUtil;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Periode;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.Frilans;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.Frilansoppdrag;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.Opptjening;

public class OpptjeningErketyper {

    public static Opptjening medFrilansOpptjening(){
        Opptjening opptjening = new Opptjening();
        Frilans frilans = new Frilans();

        frilans.setHarInntektFraFosterhjem(false);
        frilans.setErNyoppstartet(false);
        frilans.setNaerRelasjon(false);
        Frilansoppdrag frilansoppdrag = new Frilansoppdrag();
        frilansoppdrag.setOppdragsgiver("Tims BBQ og fotmassasje");
        Periode periode = new Periode();
        try {
            periode.setFom(DateUtil.convertToXMLGregorianCalendar(LocalDate.now().minusYears(1)));
            periode.setTom(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()));
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        frilans.getPeriode().add(periode);
        frilansoppdrag.setPeriode(periode);

        frilans.getFrilansoppdrag().add(frilansoppdrag);

        opptjening.setFrilans(frilans);
        return opptjening;
    }

}
