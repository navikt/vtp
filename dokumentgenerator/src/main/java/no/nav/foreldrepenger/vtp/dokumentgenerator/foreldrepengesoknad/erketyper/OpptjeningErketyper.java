package no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import no.nav.vedtak.felles.xml.soeknad.felles.v3.Periode;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.AnnenOpptjening;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.EgenNaering;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.Frilans;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.Frilansoppdrag;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.NorskOrganisasjon;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.Opptjening;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.Regnskapsfoerer;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v3.AnnenOpptjeningTyper;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v3.Virksomhetstyper;
@Deprecated
public class OpptjeningErketyper {

    private OpptjeningErketyper(){}

    public static Opptjening medFrilansOpptjening(){

        Periode periode = new Periode();
        periode.setFom((LocalDate.now().minusYears(2)));
        periode.setTom((LocalDate.now()));

        Frilansoppdrag frilansoppdrag = new Frilansoppdrag();
        frilansoppdrag.setOppdragsgiver("Tims BBQ og fotmassasje");
        frilansoppdrag.setPeriode(periode);

        Frilans frilans = new Frilans();
        frilans.setHarInntektFraFosterhjem(false);
        frilans.setErNyoppstartet(false);
        frilans.setNaerRelasjon(false);
        frilans.getPeriode().add(periode);
        frilans.getFrilansoppdrag().add(frilansoppdrag);

        Opptjening opptjening = new Opptjening();
        opptjening.setFrilans(frilans);
        return opptjening;

    }

    public static Opptjening medEgenNaeringOpptjening(boolean erNyIArbeidslivet, BigInteger næringsInntekt, boolean varigEndretNæring){

        Opptjening opptjening = new Opptjening();
        List<EgenNaering> naeringer = opptjening.getEgenNaering();
        NorskOrganisasjon naering = new NorskOrganisasjon();

        Regnskapsfoerer regnskapsfoerer = new Regnskapsfoerer();
        regnskapsfoerer.setNavn("Regnar Regnskap");
        regnskapsfoerer.setTelefon("99999999");
        naering.setRegnskapsfoerer(regnskapsfoerer);

        Periode periode = new Periode();
        periode.setFom((LocalDate.now().minusYears(4)));
        periode.setTom((LocalDate.now()));
        naering.setPeriode(periode);

        List<Virksomhetstyper> typer = naering.getVirksomhetstype();
        Virksomhetstyper type = new Virksomhetstyper();
        type.setKode("ANNEN");
        typer.add(type);

        naering.setOppstartsdato((LocalDate.now().minusYears(4)));
        naering.setOrganisasjonsnummer("910909088");
        naering.setNaerRelasjon(false);
        naering.setNavn("Navnet Organisasjon");
        naering.setErVarigEndring(varigEndretNæring);
        if (varigEndretNæring) {
            naering.setEndringsDato((LocalDate.now().minusWeeks(1)));
        }
        naering.setBeskrivelseAvEndring("Endringsbeskrivelse");
        naering.setNaeringsinntektBrutto(næringsInntekt);
        naering.setErNyoppstartet(false);
        naering.setErNyIArbeidslivet(erNyIArbeidslivet);

        naeringer.add(naering);
        return opptjening;

    }

    public static Opptjening medEgenNaeringOpptjening(){
        return medEgenNaeringOpptjening(false, BigInteger.valueOf(1_500_000), true);
    }

    public static Opptjening medEgenNaeringOgFrilansOpptjening(){

        Periode periode = new Periode();
        periode.setFom((LocalDate.now().minusYears(2)));
        periode.setTom((LocalDate.now()));

        Frilansoppdrag frilansoppdrag = new Frilansoppdrag();
        frilansoppdrag.setOppdragsgiver("Tims BBQ og fotmassasje");
        frilansoppdrag.setPeriode(periode);

        Frilans frilans = new Frilans();
        frilans.setHarInntektFraFosterhjem(false);
        frilans.setErNyoppstartet(false);
        frilans.setNaerRelasjon(false);
        frilans.getPeriode().add(periode);
        frilans.getFrilansoppdrag().add(frilansoppdrag);

        Opptjening opptjening = new Opptjening();
        opptjening.setFrilans(frilans);

        List<EgenNaering> naeringer = opptjening.getEgenNaering();
        NorskOrganisasjon naering = new NorskOrganisasjon();

        Regnskapsfoerer regnskapsfoerer = new Regnskapsfoerer();
        regnskapsfoerer.setNavn("Regnar Regnskap");
        regnskapsfoerer.setTelefon("99999999");
        naering.setRegnskapsfoerer(regnskapsfoerer);

        Periode periodeSN = new Periode();
        periodeSN.setFom((LocalDate.now().minusYears(4)));
        periodeSN.setTom((LocalDate.now()));
        naering.setPeriode(periodeSN);

        List<Virksomhetstyper> typer = naering.getVirksomhetstype();
        Virksomhetstyper type = new Virksomhetstyper();
        type.setKode("ANNEN");
        typer.add(type);

        naering.setOppstartsdato((LocalDate.now().minusYears(4)));
        naering.setOrganisasjonsnummer("910909088");
        naering.setNaerRelasjon(false);
        naering.setNavn("Navnet Organisasjon");
        naering.setErVarigEndring(true);
        naering.setBeskrivelseAvEndring("Endringsbeskrivelse");
        naering.setEndringsDato((LocalDate.now().minusWeeks(1)));
        naering.setNaeringsinntektBrutto(BigInteger.valueOf(1_500_000));
        naering.setErNyoppstartet(false);
        naering.setErNyIArbeidslivet(false);

        naeringer.add(naering);
        return opptjening;

    }

    public static Opptjening medVentelonnVartpengerOpptjening(){
        Opptjening opptjening = new Opptjening();
        List<AnnenOpptjening> annenOpptjening = opptjening.getAnnenOpptjening();
        AnnenOpptjening ventelonn = new AnnenOpptjening();
        AnnenOpptjeningTyper type = new AnnenOpptjeningTyper();
        type.setKode("VENTELØNN_VARTPENGER");
        ventelonn.setType(type);
        Periode periode = new Periode();
        periode.setFom((LocalDate.now().minusYears(4)));
        periode.setTom((LocalDate.now()));
        ventelonn.setPeriode(periode);
        annenOpptjening.add(ventelonn);
        return opptjening;

    }

    public static Opptjening medMilitærOpptjening(){
        Opptjening opptjening = new Opptjening();
        List<AnnenOpptjening> annenOpptjening = opptjening.getAnnenOpptjening();
        AnnenOpptjening militær = new AnnenOpptjening();
        AnnenOpptjeningTyper type = new AnnenOpptjeningTyper();
        type.setKode("MILITÆR_ELLER_SIVILTJENESTE");
        militær.setType(type);
        Periode periode = new Periode();
        periode.setFom((LocalDate.now().minusMonths(4)));
        periode.setTom((LocalDate.now().minusMonths(3)));
        militær.setPeriode(periode);
        annenOpptjening.add(militær);
        return opptjening;

    }

}
