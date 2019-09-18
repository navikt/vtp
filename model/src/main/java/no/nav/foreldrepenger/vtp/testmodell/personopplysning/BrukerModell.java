package no.nav.foreldrepenger.vtp.testmodell.personopplysning;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.Nulls;

import no.nav.foreldrepenger.vtp.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.vtp.testmodell.util.VariabelContainer;

@JsonInclude(Include.NON_NULL)
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY)
@JsonSubTypes({ @Type(BarnModell.class), @Type(SøkerModell.class), @Type(AnnenPartModell.class), @Type(BrukerIdent.class), @Type(PersonArbeidsgiver.class) })
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

    private String aktørIdent;

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

    @JsonGetter()
    public String getAktørIdent() {
        if (aktørIdent != null) {
            return aktørIdent;
        }
        // aktørId er pt. 13 siffer. bruker FNR som utgangspunkt som er 11 slik at det er enkelt å spore
        String ident = getIdent();
        return ident == null ? null : "99" + ident;
    }

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    public void setAktørIdent(String aktørIdent) {
        this.aktørIdent = aktørIdent;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }
        BrukerModell bm = (BrukerModell) obj;
        String ident = getIdent();
        String ident2 = bm.getIdent();
        return ident != null && ident2!=null && Objects.equals(ident, ident2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdent());
    }

}
