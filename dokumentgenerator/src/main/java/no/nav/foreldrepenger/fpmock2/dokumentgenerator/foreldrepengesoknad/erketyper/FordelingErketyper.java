package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import java.time.LocalDate;

import javax.xml.datatype.DatatypeConfigurationException;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.util.DateUtil;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v1.MorsAktivitetsTyper;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v1.Uttaksperiodetyper;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.Fordeling;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.Uttaksperiode;

public class FordelingErketyper {

    public static Fordeling uttaksPeriodeAltTilMor(){
        Fordeling fordeling = new Fordeling();
        fordeling.setAnnenForelderErInformert(true);

        Uttaksperiode uttaksperiode = new Uttaksperiode();
        uttaksperiode.setOenskerSamtidigUttak(false);
        Uttaksperiodetyper uttaksperiodetyper = new Uttaksperiodetyper();
        uttaksperiodetyper.setKodeverk("UTTAK_PERIODE_TYPE");
        uttaksperiodetyper.setKode("MØDREKVOTE");
        uttaksperiode.setType(uttaksperiodetyper);

        MorsAktivitetsTyper morsAktivitetsTyper = new MorsAktivitetsTyper();
        morsAktivitetsTyper.setKodeverk("MORS_AKTIVITET");
        morsAktivitetsTyper.setKode("ARBEID");
        uttaksperiode.setMorsAktivitetIPerioden(morsAktivitetsTyper);

        try {
            uttaksperiode.setFom(DateUtil.convertToXMLGregorianCalendar(LocalDate.now().plusMonths(1)));
            uttaksperiode.setTom(DateUtil.convertToXMLGregorianCalendar(LocalDate.now().plusMonths(4)));
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        fordeling.getPerioder().add(uttaksperiode);

        return fordeling;
    }
}
