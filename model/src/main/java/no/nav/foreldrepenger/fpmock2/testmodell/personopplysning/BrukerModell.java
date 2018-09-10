package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import no.nav.foreldrepenger.fpmock2.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.util.VariabelContainer;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY)
@JsonSubTypes({ @Type(BarnModell.class), @Type(SøkerModell.class), @Type(AnnenPartModell.class), @Type(BrukerIdent.class) })
public abstract class BrukerModell {

    /** Ident referanse, hver unik referanse vil erstattes av en syntetisk men 'gyldig' ident (FNR). */
    @JsonIgnore
    private String lokalIdent;

    /** Deler VirksomhetIndeks for et helt scenario for å veksle lokale identer inn i fnr el. */
    @JacksonInject
    private LokalIdentIndeks identer;

    /** Variabler delt i scenario. */
    @JacksonInject
    private VariabelContainer vars;

    public BrukerModell() {
        // default ctor.
    }

    public BrukerModell(String lokalIdent) {
        this.lokalIdent = lokalIdent;
    }

    @JsonSetter("ident")
    void setLokalIdent(String lokalIdent) {
        this.lokalIdent = lokalIdent;
        String ident = getIdent();
        if (ident != null && !Objects.equals(lokalIdent, ident)) {
            this.vars.computeIfAbsent(lokalIdent, n -> ident);
        }
    }

    protected LokalIdentIndeks getIdenter() {
        return identer;
    }

    protected String getLokalIdent() {
        return lokalIdent;
    }

    @JsonGetter("ident")
    public abstract String getIdent();

    public enum Kjønn {
        M, K;
    }

    public void setIdenter(LokalIdentIndeks identer) {
        if (this.identer != null && identer != this.identer /* merk System.identy brukes her med vilje */) { // NOSONAR
            throw new IllegalStateException("identer allerede satt");
        }
        this.identer = identer;
    }

    public String getAktørIdent() {
        // aktørId er pt. 13 siffer. bruker FNR som utgangspunkt som er 11 slik at det er enkelt å spore
        String ident = getIdent();
        return ident == null ? null : "99" + ident;
    }

}
