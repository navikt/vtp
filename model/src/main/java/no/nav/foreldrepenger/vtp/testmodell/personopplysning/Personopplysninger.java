package no.nav.foreldrepenger.vtp.testmodell.personopplysning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.vtp.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell.Rolle;
import no.nav.foreldrepenger.vtp.testmodell.util.VariabelContainer;

public class Personopplysninger {

    @JsonProperty("søker")
    private SøkerModell søker;

    @JsonProperty("annenPart")
    private AnnenPartModell annenPart;

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("familierelasjoner")
    private List<FamilierelasjonModell> familierelasjoner;

    @JsonProperty("familierelasjonerAnnenPart")
    private List<FamilierelasjonModell> familierelasjonerAnnenPart;

    @JsonProperty("familierelasjonerBarn")
    private List<FamilierelasjonModell> familierelasjonerBarn;

    /**
     * identity cache for dette scenario. Medfører at identer kan genereres dynamisk basert på lokal id referanse i scenarioet.
     * Deler VirksomhetIndeks for et helt scenario for å veksle lokale identer inn i fnr el.
     */
    @JacksonInject
    private LokalIdentIndeks identer;

    @JacksonInject
    private VariabelContainer vars;

    public Personopplysninger() {
    }

    public Personopplysninger(SøkerModell søker,
                              AnnenPartModell annenPart,
                              List<FamilierelasjonModell> familierelasjoner,
                              List<FamilierelasjonModell> familierelasjonerAnnenPart,
                              List<FamilierelasjonModell> familierelasjonerBarn) {
        this.søker = søker;
        this.annenPart = annenPart;
        this.familierelasjoner = Optional.ofNullable(familierelasjoner).orElse(new ArrayList<>());
        this.familierelasjonerAnnenPart = Optional.ofNullable(familierelasjonerAnnenPart).orElse(new ArrayList<>());
        this.familierelasjonerBarn = Optional.ofNullable(familierelasjonerBarn).orElse(new ArrayList<>());
    }

    public SøkerModell getSøker() {
        return søker;
    }

    public AnnenPartModell getAnnenPart() {
        return annenPart;
    }

    public Collection<FamilierelasjonModell> getFamilierelasjoner() {
        return Collections.unmodifiableList(familierelasjoner);
    }

    public Collection<FamilierelasjonModell> getFamilierelasjonerForAnnenPart() {
        return Collections.unmodifiableList(familierelasjonerAnnenPart);
    }

    public Collection<FamilierelasjonModell> getFamilierelasjonerForBarnet() {
        return Collections.unmodifiableList(familierelasjonerBarn);
    }

    public Stream<FamilierelasjonModell> getFamilierelasjoner(Rolle rolle) {
        return getFamilierelasjoner().stream().filter(f -> rolle.equals(f.getRolle()));
    }

    public LokalIdentIndeks getIdenter() {
        return identer;
    }

    public VariabelContainer getVars() {
        return vars;
    }

    public void leggTil(FamilierelasjonModell rel) {
        Objects.requireNonNull(this.identer, "identer er ikke satt");
        rel.getTil().setIdenter(identer);
        this.familierelasjoner.add(rel);
    }

    public String leggTilDødfødsel(BarnModell barn) {
        Objects.requireNonNull(this.identer, "identer er ikke satt");

        // Lager ident og oppdaterer vars
        var lokalIdent = henterUtUniktVariabelnavn();
        var barnIdent = identer.getIdentForDødfødsel(lokalIdent, barn.getFødselsdato());
        getVars().putVar(lokalIdent, barnIdent);

        leggTilBarnIFamilierelasjonsModeller(barn, lokalIdent);
        return  barnIdent;
    }

    public String leggTilBarn(BarnModell barn) {
        Objects.requireNonNull(this.identer, "identer er ikke satt");

        // Lager ident og oppdaterer vars
        var lokalIdent = henterUtUniktVariabelnavn();
        var barnIdent = identer.getBarnIdentForLokalIdent(lokalIdent);
        getVars().putVar(lokalIdent, barnIdent);

        leggTilBarnIFamilierelasjonsModeller(barn, lokalIdent);
        return barnIdent;
    }

    public void setIdenter(LokalIdentIndeks identer) {
        this.identer = identer;
        this.søker.setIdenter(identer);
        if (this.annenPart != null) {
            this.annenPart.setIdenter(identer);
        }
        Stream.concat(this.familierelasjoner.stream(), Stream.concat(this.familierelasjonerAnnenPart.stream(), this.familierelasjonerBarn.stream()))
                    .forEach(f -> f.getTil().setIdenter(identer));
    }

    public void setVars(VariabelContainer vars) {
        this.vars = vars;
        this.søker.setVars(vars);
        if (this.annenPart != null) {
            this.annenPart.setVars(vars);
        }
        Stream.concat(this.familierelasjoner.stream(), Stream.concat(this.familierelasjonerAnnenPart.stream(), this.familierelasjonerBarn.stream()))
                .forEach(f -> f.getTil().setVars(vars));
    }

    public void setAdresseIndeks(AdresseIndeks adresseIndeks) {
        this.søker.setAdresseIndeks(adresseIndeks);
        if (this.annenPart != null) {
            this.annenPart.setAdresseIndeks(adresseIndeks);
        }
    }


    private void leggTilBarnIFamilierelasjonsModeller(BarnModell barn, String lokalIdent) {
        barn.setVars(vars);
        barn.setIdenter(identer);
        barn.setLokalIdent(lokalIdent);
        barn.setAdresseIndeks(søker.getAdresseIndeks());
        familierelasjoner.add(new FamilierelasjonModell(FamilierelasjonModell.Rolle.BARN, barn));
        familierelasjonerAnnenPart.add(new FamilierelasjonModell(FamilierelasjonModell.Rolle.BARN, barn));

        if (familierelasjonerBarn.isEmpty()) {
            var søkerRolle = søker.getKjønn().erKvinne() ? Rolle.MORA : Rolle.FARA;
            familierelasjonerBarn.add(new FamilierelasjonModell(søkerRolle, søker));
            if (annenPart != null) {
                var annenpartRolle = annenPart.getKjønn().erKvinne() ? Rolle.MMOR : Rolle.FARA;
                familierelasjonerBarn.add(new FamilierelasjonModell(annenpartRolle, annenPart));
            }
        }
    }

    private String henterUtUniktVariabelnavn() {
        var i = 1;
        while (getVars().getVar("barn" + i) != null) {
            i++;
        }
        return "${barn" + i + "}";
    }
}
