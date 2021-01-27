package no.nav.foreldrepenger.vtp.testmodell.dokument.modell;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.BrukerType;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.ANY, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JournalpostBruker {
    private String ident;
    private BrukerType brukerType;


    public JournalpostBruker(@JsonProperty("ident") String ident,
                             @JsonProperty("brukerType") BrukerType brukerType) {
        this.ident = ident;
        this.brukerType = brukerType;
    }

    public JournalpostBruker() {}

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public BrukerType getBrukerType() {
        return brukerType;
    }

    public void setBrukerType(BrukerType brukerType) {
        this.brukerType = brukerType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JournalpostBruker that = (JournalpostBruker) o;
        return Objects.equals(ident, that.ident) && Objects.equals(brukerType, that.brukerType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ident, brukerType);
    }

    @Override
    public String toString() {
        return "JournalpostBruker{" +
                "ident='" + ident + '\'' +
                ", brukerType=" + brukerType +
                '}';
    }
}
