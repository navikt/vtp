package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.util.DateUtil;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Periode;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.EgenNaering;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.Frilans;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.Frilansoppdrag;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.NorskOrganisasjon;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.Opptjening;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.Regnskapsfoerer;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v1.Land;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v1.Virksomhetstyper;

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
        periode.setFom(DateUtil.convertToXMLGregorianCalendar(LocalDate.now().minusYears(1)));
        periode.setTom(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()));
        frilans.getPeriode().add(periode);
        frilansoppdrag.setPeriode(periode);

        frilans.getFrilansoppdrag().add(frilansoppdrag);

        opptjening.setFrilans(frilans);
        return opptjening;
    }

    public static Opptjening medEgenNaeringOpptjening(){

        Opptjening opptjening = new Opptjening();
        List<EgenNaering> naeringer = opptjening.getEgenNaering();
        NorskOrganisasjon naering = new NorskOrganisasjon();

        Land land = new Land();
        land.setKode("NOR");
        land.setKodeverk("LANDKODER");
        naering.setArbeidsland(land);

        Regnskapsfoerer regnskapsfoerer = new Regnskapsfoerer();
        regnskapsfoerer.setNavn("Regnar Regnskap");
        regnskapsfoerer.setTelefon("99999999");
        naering.setRegnskapsfoerer(regnskapsfoerer);

        Periode periode = new Periode();
        periode.setFom(DateUtil.convertToXMLGregorianCalendar(LocalDate.now().minusYears(4)));
        periode.setTom(DateUtil.convertToXMLGregorianCalendar(LocalDate.now()));
        naering.setPeriode(periode);

        List<Virksomhetstyper> typer = naering.getVirksomhetstype();
        Virksomhetstyper type = new Virksomhetstyper();
        type.setKode("ANNEN");
        typer.add(type);

        naering.setOppstartsdato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now().minusYears(4)));
        naering.setOrganisasjonsnummer("979191138");
        naering.setNaerRelasjon(false);
        naering.setNavn("Navnet Organisasjon");
        naering.setErVarigEndring(true);
        naering.setBeskrivelseAvEndring("Endringsbeskrivelse");
        naering.setEndringsDato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now().minusWeeks(1)));
        naering.setNaeringsinntektBrutto(BigInteger.valueOf(1_500_000));
        naering.setErNyoppstartet(false);
        naering.setErNyIArbeidslivet(false);

        naeringer.add(naering);
        return opptjening;

    }

}
