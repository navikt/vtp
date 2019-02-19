package no.nav.foreldrepenger.autotest.base;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import no.nav.foreldrepenger.autotest.base.FpsakTestBase;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.Inntektsperiode;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.Fordeling;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.Uttaksperiode;

public class ForeldrepengerTestBase extends FpsakTestBase {

    protected List<Integer> sorterteInntektsbeløp(TestscenarioDto testscenario) {
        return testscenario.getScenariodata().getInntektskomponentModell().getInntektsperioder().stream()
                .map(Inntektsperiode::getBeløp)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    protected Fordeling fordeling(Uttaksperiode... perioder) {
        return FordelingErketyper.generiskFordeling(perioder);
    }

    protected Uttaksperiode uttaksperiode(String stønadskontotype, LocalDate fom, LocalDate tom) {
        return FordelingErketyper.uttaksperiode(stønadskontotype, fom, tom);
    }

}
