package no.nav.tjeneste.virksomhet.medlemskap.v2;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.foreldrepenger.fpmock2.testmodell.medlemskap.MedlemskapperiodeModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.Medlemsperiode;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.KildeMedTerm;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.LandkodeMedTerm;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.LovvalgMedTerm;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.PeriodetypeMedTerm;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.StatuskodeMedTerm;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.TrygdedekningMedTerm;

public class MedlemskapperioderAdapter {

    private TestscenarioBuilderRepository scenarioRepository;

    public MedlemskapperioderAdapter(TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    public List<Medlemsperiode> finnMedlemsperioder(String personIdent) {
        if (personIdent != null) {
            BrukerModell brukerModell = scenarioRepository.getPersonIndeks().finnByIdent(personIdent);
            if (brukerModell!=null && brukerModell instanceof PersonModell) {
                PersonModell pm = (PersonModell)brukerModell;
                
                List<Medlemsperiode> periodeListe = new ArrayList<>();
                pm.getMedlemskap().getPerioder().forEach(medlemsskapsperiode -> periodeListe.add(tilMedlemsperiode(medlemsskapsperiode)));
                return periodeListe;
            }
        }
        return null;
    }

    private Medlemsperiode tilMedlemsperiode(MedlemskapperiodeModell medlemsskapsperiode) {
        return new Medlemsperiode()
            .withId(medlemsskapsperiode.getId())
            .withFraOgMed(ConversionUtils.convertToXMLGregorianCalendar(medlemsskapsperiode.getFom()))
            .withTilOgMed(ConversionUtils.convertToXMLGregorianCalendar(medlemsskapsperiode.getTom()))
            .withDatoBesluttet(ConversionUtils.convertToXMLGregorianCalendar(medlemsskapsperiode.getBesluttetDato()))
            .withLand(new LandkodeMedTerm().withValue(medlemsskapsperiode.getLandkode().getKode()))

            .withTrygdedekning(new TrygdedekningMedTerm().withValue(medlemsskapsperiode.getDekningType().getKode()))
            .withType(new PeriodetypeMedTerm().withValue("PMMEDSKP")) // medlemsskapsperiode.getType().getKode()
            .withKilde(new KildeMedTerm().withValue(medlemsskapsperiode.getKilde().getKode()))
            .withStatus(new StatuskodeMedTerm().withValue(medlemsskapsperiode.getStatus().getKode()))
            .withLovvalg(new LovvalgMedTerm().withValue(medlemsskapsperiode.getLovvalgType().getKode()))

        /**
         * disse brukes ikke i FPSAK, ignorerer inntil det er behov.
         <code>
            .withLovvalg(new LovvalgMedTerm().withValue(medlemsskapsperiode.getLovvalg()))
        .withGrunnlagstype(new GrunnlagstypeMedTerm().withValue(medlemsskapsperiode.getGrunnlag()))
        
        .withStudieinformasjon(new Studieinformasjon()
                .withStatsborgerland(new LandkodeMedTerm().withValue(medlemsskapsperiode.getStudieStatsborgerland()))
                .withStudieland(new LandkodeMedTerm().withValue(medlemsskapsperiode.getStudieStudieland())))
         </code>
         * 
         */
        ;
    }
}
