package no.nav.medl2.rest.api.v1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import no.nav.foreldrepenger.vtp.testmodell.medlemskap.MedlemskapperiodeModell;
import no.nav.foreldrepenger.vtp.testmodell.medlemskap.PeriodeStatus;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;

public class MedlemskapsunntakAdapter {

    private TestscenarioBuilderRepository scenarioRepository;

    public MedlemskapsunntakAdapter(TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    public List<Medlemskapsunntak> finnMedlemsunntak(String aktørId) {
        if (aktørId != null) {
            var brukerModell = scenarioRepository.getPersonIndeks().finnByAktørIdent(aktørId);
            if (brukerModell instanceof PersonModell pm) {
                List<Medlemskapsunntak> periodeListe = new ArrayList<>();
                if(pm.getMedlemskap() != null && pm.getMedlemskap().getPerioder() != null) {
                    pm.getMedlemskap().getPerioder().forEach(medlemsskapsperiode -> periodeListe.add(tilMedlemsperiode(medlemsskapsperiode)));
                }
                return periodeListe;
            }
        }
        return Collections.emptyList();
    }

    private Medlemskapsunntak tilMedlemsperiode(MedlemskapperiodeModell medlPeriode) {
        return new Medlemskapsunntak(
                medlPeriode.id(),
                medlPeriode.fom(),
                medlPeriode.tom(),
                medlPeriode.trygdedekning().kode(),
                null,
                medlPeriode.lovvalgType().name(),
                medlPeriode.land().getKode(),
                null,
                Set.of(PeriodeStatus.INNV, PeriodeStatus.GYLD).contains(medlPeriode.status()),
                new Medlemskapsunntak.Sporingsinformasjon(medlPeriode.besluttetDato(), medlPeriode.kilde().name()),
                null);
    }
}
