package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import no.nav.vedtak.felles.integrasjon.felles.ws.DateUtil;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Periode;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.Frilans;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.Frilansoppdrag;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.Opptjening;

import javax.xml.datatype.DatatypeConfigurationException;
import java.time.LocalDate;

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
        frilansoppdrag.setPeriode(periode);
        frilans.getFrilansoppdrag().add(frilansoppdrag);

        opptjening.setFrilans(frilans);
        return opptjening;
    }

}
