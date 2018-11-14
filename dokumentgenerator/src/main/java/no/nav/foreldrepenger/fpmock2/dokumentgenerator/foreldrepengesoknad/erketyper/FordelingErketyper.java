package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import java.time.LocalDate;

import javax.xml.datatype.DatatypeConfigurationException;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.util.DateUtil;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v1.Uttaksperiodetyper;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.Fordeling;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.Uttaksperiode;

public class FordelingErketyper {

    private static final String STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL = "FORELDREPENGER_FØR_FØDSEL";
    private static final String STØNADSKONTOTYPE_MØDREKVOTE = "MØDREKVOTE";

    public static Fordeling fordelingMorHappyCase(LocalDate familehendelseDato) {
        Fordeling fordeling = new Fordeling();
        fordeling.setAnnenForelderErInformert(true);

        fordeling.getPerioder().add(uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, familehendelseDato.minusWeeks(3), familehendelseDato.minusDays(1)));
        fordeling.getPerioder().add(uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, familehendelseDato, familehendelseDato.plusWeeks(6)));

        return fordeling;
    }

    private static Uttaksperiode uttaksperiode(String stønadskontotype, LocalDate fom, LocalDate tom) {
        Uttaksperiode uttaksperiode = new Uttaksperiode();
        Uttaksperiodetyper uttaksperiodetyper = new Uttaksperiodetyper();
        uttaksperiodetyper.setKode(stønadskontotype);
        uttaksperiode.setType(uttaksperiodetyper);
        
        uttaksperiode.setFom(DateUtil.convertToXMLGregorianCalendar(fom));
        uttaksperiode.setTom(DateUtil.convertToXMLGregorianCalendar(tom));
        return uttaksperiode;
    }
}
