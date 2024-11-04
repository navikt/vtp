package no.nav.foreldrepenger.vtp.testmodell.arbeidsgiver;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class OppgaveModell {
    private final UUID id;
    private final String grupperingsid;
    private final String merkelapp;
    private final String eksternId;
    private final String virksomhetsnummer;
    private final String tekst;
    private final String lenke;
    private Tilstand tilstand;
    private final LocalDateTime opprettetTid;
    private LocalDateTime endretTid;

    public OppgaveModell(UUID id,
                         String grupperingsid,
                         String merkelapp,
                         String eksternId,
                         String virksomhetsnummer,
                         String tekst,
                         String lenke,
                         Tilstand tilstand,
                         LocalDateTime opprettetTid,
                         LocalDateTime endretTid) {
        this.id = id;
        this.grupperingsid = grupperingsid;
        this.merkelapp = merkelapp;
        this.eksternId = eksternId;
        this.virksomhetsnummer = virksomhetsnummer;
        this.tekst = tekst;
        this.lenke = lenke;
        this.tilstand = tilstand;
        this.opprettetTid = opprettetTid;
        this.endretTid = endretTid;
    }

    public UUID id() {
        return id;
    }

    public String grupperingsid() {
        return grupperingsid;
    }

    public String merkelapp() {
        return merkelapp;
    }

    public String eksternId() {
        return eksternId;
    }

    public String virksomhetsnummer() {
        return virksomhetsnummer;
    }

    public String tekst() {
        return tekst;
    }

    public String lenke() {
        return lenke;
    }

    public Tilstand tilstand() {
        return tilstand;
    }

    public LocalDateTime opprettetTid() {
        return opprettetTid;
    }

    public LocalDateTime endretTid() {
        return endretTid;
    }

    public void setTilstand(Tilstand tilstand) {
        this.tilstand = tilstand;
    }

    public void setEndretTid(LocalDateTime endretTid) {
        this.endretTid = endretTid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (OppgaveModell) obj;
        return Objects.equals(this.id, that.id) && Objects.equals(this.grupperingsid, that.grupperingsid) && Objects.equals(this.merkelapp,
                that.merkelapp) && Objects.equals(this.eksternId, that.eksternId) && Objects.equals(this.virksomhetsnummer, that.virksomhetsnummer) && Objects.equals(this.tekst,
                that.tekst) && Objects.equals(this.lenke, that.lenke) && Objects.equals(this.tilstand, that.tilstand) && Objects.equals(this.opprettetTid,
                that.opprettetTid) && Objects.equals(this.endretTid, that.endretTid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, grupperingsid, merkelapp, eksternId, virksomhetsnummer, tekst, lenke, tilstand, opprettetTid, endretTid);
    }

    @Override
    public String toString() {
        return "OppgaveModell[" + "id=" + id + ", " + "grupperingsid=" + grupperingsid + ", " + "merkelapp=" + merkelapp + ", "
                + "eksternId=" + eksternId + ", " + "virksomhetsnummer=" + virksomhetsnummer + ", " + "tekst=" + tekst + ", "
                + "lenke=" + lenke + ", " + "tilstand=" + tilstand + ", " + "opprettetTid=" + opprettetTid + ", " + "endretTid="
                + endretTid + ']';
    }


    public enum Tilstand {
        NY,
        UTFOERT,
        UTGAATT
    }


}
