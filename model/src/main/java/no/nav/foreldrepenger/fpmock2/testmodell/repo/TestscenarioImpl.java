package no.nav.foreldrepenger.fpmock2.testmodell.repo;

import no.nav.foreldrepenger.fpmock2.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.fpmock2.testmodell.organisasjon.OrganisasjonModeller;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.fpmock2.testmodell.util.VariabelContainer;
import no.nav.foreldrepenger.fpmock2.testmodell.virksomhet.ScenarioVirksomheter;

public class TestscenarioImpl implements Testscenario {

    private final String templateNavn;

    /** identity cache for dette scenario. Medfører at identer kan genereres dynamisk basert på lokal id referanse i scenarioet. */
    private final LokalIdentIndeks identer;

    private AdresseIndeks adresseIndeks;

    private Personopplysninger personopplysninger;

    private InntektYtelseModell søkerInntektYtelse;

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

    public void setAnnenpartInntektYtelse(InntektYtelseModell inntektYtelse) {
        this.annenpartInntektYtelse = inntektYtelse;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "<template, " + templateNavn + ", id=" + id + ">";
    }
}
