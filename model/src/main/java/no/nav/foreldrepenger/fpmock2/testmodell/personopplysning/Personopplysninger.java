package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import no.nav.foreldrepenger.fpmock2.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.FamilierelasjonModell.Rolle;
import no.nav.foreldrepenger.fpmock2.testmodell.util.VariabelContainer;

public class Personopplysninger {

    @JsonProperty("søker")
    private SøkerModell søker;

    @JsonProperty("annenPart")
    private AnnenPartModell annenPart;
    
    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("familierelasjoner")
    private List<FamilierelasjonModell> familierelasjoner = new ArrayList<>();

    /**
     * identity cache for dette scenario. Medfører at identer kan genereres dynamisk basert på lokal id referanse i scenarioet.
     * Deler VirksomhetIndeks for et helt scenario for å veksle lokale identer inn i fnr el.
     */
    @JacksonInject
    private LokalIdentIndeks identer;
    
    @JacksonInject
    private VariabelContainer vars;

    public Personopplysninger(SøkerModell søker) {
        this.søker = søker;
    }

    public Personopplysninger(SøkerModell søker, AnnenPartModell annenPart) {
        this.søker = søker;
        this.annenPart = annenPart;
    }

    Personopplysninger() {
    }


    public AnnenPartModell getAnnenPart() {
        return annenPart;
    }

    public Collection<FamilierelasjonModell> getFamilierelasjoner() {
        return Collections.unmodifiableList(familierelasjoner);
    }

    public Stream<FamilierelasjonModell> getFamilierelasjoner(Rolle rolle) {
        return getFamilierelasjoner().stream().filter(f -> rolle.equals(f.getRolle()));
    }

    public SøkerModell getSøker() {
        return søker;
    }

    public void leggTil(FamilierelasjonModell rel) {
        Objects.requireNonNull(this.identer, "identer er ikke satt");
        rel.getTil().setIdenter(identer);
        this.familierelasjoner.add(rel);
    }

    public void leggTilBarn(BarnModell barn) {
        Objects.requireNonNull(this.identer, "identer er ikke satt");
        barn.setIdenter(identer);
        this.familierelasjoner.add(new FamilierelasjonModell(FamilierelasjonModell.Rolle.BARN, barn));
    }

    public void setIdenter(LokalIdentIndeks identer) {
        this.identer = identer;
        this.søker.setIdenter(identer);
        if (this.annenPart != null) {
            this.annenPart.setIdenter(identer);
        }
    }

}
