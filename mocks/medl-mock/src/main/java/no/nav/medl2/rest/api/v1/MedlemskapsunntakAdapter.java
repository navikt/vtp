package no.nav.medl2.rest.api.v1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import no.nav.foreldrepenger.vtp.testmodell.medlemskap.MedlemskapperiodeModell;
import no.nav.foreldrepenger.vtp.testmodell.medlemskap.PeriodeStatus;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.tjenester.medlemskapsunntak.api.v1.Medlemskapsunntak;
import no.nav.tjenester.medlemskapsunntak.api.v1.Sporingsinformasjon;

public class MedlemskapsunntakAdapter {

    private TestscenarioBuilderRepository scenarioRepository;

    public MedlemskapsunntakAdapter(TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    public List<Medlemskapsunntak> finnMedlemsunntak(String aktørId) {
        if (aktørId != null) {
            BrukerModell brukerModell = scenarioRepository.getPersonIndeks().finnByAktørIdent(aktørId);
            if (brukerModell instanceof PersonModell) {
                PersonModell pm = (PersonModell)brukerModell;

                List<Medlemskapsunntak> periodeListe = new ArrayList<>();
                if(pm.getMedlemskap() != null && pm.getMedlemskap().getPerioder() != null) {
                    pm.getMedlemskap().getPerioder().forEach(medlemsskapsperiode -> periodeListe.add(tilMedlemsperiode(medlemsskapsperiode)));
                }
                return periodeListe;
            }
        }
        return Collections.emptyList();
    }

    private Medlemskapsunntak tilMedlemsperiode(MedlemskapperiodeModell medlemsskapsperiode) {
        return Medlemskapsunntak.builder()
                .medlem(Set.of(PeriodeStatus.INNV, PeriodeStatus.GYLD).contains(medlemsskapsperiode.status()))
                .fraOgMed(medlemsskapsperiode.fom())
                .tilOgMed(medlemsskapsperiode.tom())
                .dekning(medlemsskapsperiode.trygdedekning().kode())
                .lovvalg(medlemsskapsperiode.lovvalgType().name())
                .lovvalgsland(medlemsskapsperiode.land().getKode())
                .status(medlemsskapsperiode.status().name())
                .unntakId(medlemsskapsperiode.id())
                .sporingsinformasjon(Sporingsinformasjon.builder()
                        .kilde(medlemsskapsperiode.kilde().name())
                        .besluttet(medlemsskapsperiode.besluttetDato())
                        .build())
                .build();
    }
}
