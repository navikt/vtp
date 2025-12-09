package no.nav.foreldrepenger.vtp.testmodell.repo;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import no.nav.foreldrepenger.vtp.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.Inntektsperiode;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModeller;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonArbeidsgiver;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.util.VariabelContainer;
import no.nav.foreldrepenger.vtp.testmodell.virksomhet.ScenarioVirksomheter;

public class TestscenarioImpl implements Testscenario {

    private final String templateNavn;

    /** identity cache for dette scenario. Medfører at identer kan genereres dynamisk basert på lokal id referanse i scenarioet. */
    private final LokalIdentIndeks identer;

    private AdresseIndeks adresseIndeks;

    private Personopplysninger personopplysninger;

    private InntektYtelseModell søkerInntektYtelse;

    private Set<YearMonth> søkerEndretInntektMåneder = new HashSet<>();

    private InntektYtelseModell annenpartInntektYtelse;

    private OrganisasjonModeller organisasjonModeller = new OrganisasjonModeller();

    private ScenarioVirksomheter scenarioVirksomheter;

    /** Unik testscenario id. */
    private String id;

    private VariabelContainer vars = new VariabelContainer();

    @SuppressWarnings("unused")
    private TestscenarioBuilderRepository scenarioIndeks;

    public TestscenarioImpl(String templateNavn, String id, TestscenarioBuilderRepository scenarioIndeks) {
        this.templateNavn = templateNavn;
        this.id = id;
        this.scenarioIndeks = scenarioIndeks;

        this.scenarioVirksomheter = new ScenarioVirksomheter(this.templateNavn, scenarioIndeks.getBasisdata().getVirksomhetIndeks());

        this.identer = scenarioIndeks.getIdenter(getId());
    }

    @Override
    public String getTemplateNavn() {
        return templateNavn;
    }

    @Override
    public String getId() {
        return id;
    }

    public LokalIdentIndeks getIdenter() {
        return identer;
    }

    public AdresseIndeks getAdresseIndeks() {
        return adresseIndeks;
    }

    public void setAdresseIndeks(AdresseIndeks adresseIndeks) {
        this.adresseIndeks = adresseIndeks;
    }

    public void setPersonopplysninger(Personopplysninger personopplysninger) {
        this.personopplysninger = personopplysninger;
        personopplysninger.setIdenter(identer);
    }

    @Override
    public Personopplysninger getPersonopplysninger() {
        return this.personopplysninger;
    }

    public OrganisasjonModeller getOrganisasjonModeller() {
        return organisasjonModeller;
    }

    public void leggTil(OrganisasjonModell organisasjonModell) {
        organisasjonModeller.leggTil(organisasjonModell);
    }

    public ScenarioVirksomheter getVirksomheter() {
        return scenarioVirksomheter;
    }

    @Override
    public VariabelContainer getVariabelContainer() {
        return vars;
    }

    @Override
    public InntektYtelseModell getSøkerInntektYtelse() {
        return søkerInntektYtelse;
    }

    @Override
    public InntektYtelseModell getAnnenpartInntektYtelse() {
        return annenpartInntektYtelse;
    }

    public void setSøkerInntektYtelse(InntektYtelseModell inntektYtelse) {
        this.søkerInntektYtelse = inntektYtelse;
    }

    public void leggTilEndretInntekt(YearMonth måned) {
        this.søkerEndretInntektMåneder.add(måned);
    }

    public Set<YearMonth> getEndretInntekt() {
        return this.søkerEndretInntektMåneder;
    }

    public void setAnnenpartInntektYtelse(InntektYtelseModell inntektYtelse) {
        this.annenpartInntektYtelse = inntektYtelse;
    }

    public TestscenarioBuilderRepository getScenarioIndeks() {
        return scenarioIndeks;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "<template, " + templateNavn + ", id=" + id + ">";
    }

    /** Returnerer alle personlige arbeidsgivere (fra søker og annen part). */
    public Set<PersonArbeidsgiver> getPersonligArbeidsgivere() {
        ArrayList<PersonArbeidsgiver> result = new ArrayList<>();
        result.addAll(getPersonArbeidsgivere(getSøkerInntektYtelse()));
        result.addAll(getPersonArbeidsgivere(getAnnenpartInntektYtelse()));
        return Set.copyOf(result);
    }

    private List<PersonArbeidsgiver> getPersonArbeidsgivere(InntektYtelseModell iyModell) {
        if (iyModell == null || iyModell.inntektskomponentModell() == null || iyModell.inntektskomponentModell().inntektsperioder() == null) {
            return Collections.emptyList();
        }
        return iyModell.inntektskomponentModell().inntektsperioder().stream()
            .map(Inntektsperiode::arbeidsgiver)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
