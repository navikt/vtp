package no.nav.foreldrepenger.vtp.testmodell.arbeidsgiver;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class SakModell {
    private final UUID id;
    private final String grupperingsid;
    private final String merkelapp;
    private final String virksomhetsnummer;
    private final String tittel;
    private final String lenke;
    private SakStatus status;
    private String overstyrtStatustekst;
    private String overstyrtTillegsinformasjon;
    private final LocalDateTime opprettetTid;
    private LocalDateTime endretTid;

    public SakModell(UUID id,
                     String grupperingsid,
                     String merkelapp,
                     String virksomhetsnummer,
                     String tittel,
                     String lenke,
                     SakStatus status,
                     String overstyrtStatustekst,
                     String overstyrtTillegsinformasjon,
                     LocalDateTime opprettetTid,
                     LocalDateTime endretTid) {
        this.id = id;
        this.grupperingsid = grupperingsid;
        this.merkelapp = merkelapp;
        this.virksomhetsnummer = virksomhetsnummer;
        this.tittel = tittel;
        this.lenke = lenke;
        this.status = status;
        this.overstyrtStatustekst = overstyrtStatustekst;
        this.overstyrtTillegsinformasjon = overstyrtTillegsinformasjon;
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

    public String virksomhetsnummer() {
        return virksomhetsnummer;
    }

    public String tittel() {
        return tittel;
    }

    public String lenke() {
        return lenke;
    }

    public SakStatus status() {
        return status;
    }

    public String overstyrtStatustekst() {
        return overstyrtStatustekst;
    }

    public String overstyrtTillegsinformasjon() {
        return overstyrtTillegsinformasjon;
    }

    public LocalDateTime opprettetTid() {
        return opprettetTid;
    }

    public LocalDateTime endretTid() {
        return endretTid;
    }

    public void setStatus(SakStatus status) {
        this.status = status;
    }

    public void setOverstyrtStatustekst(String overstyrtStatustekst) {
        this.overstyrtStatustekst = overstyrtStatustekst;
    }

    public void setEndretTid(LocalDateTime endretTid) {
        this.endretTid = endretTid;
    }

    public void setOverstyrtTillegsinformasjon(String overstyrtTillegsinformasjon) {
        this.overstyrtTillegsinformasjon = overstyrtTillegsinformasjon;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (SakModell) obj;
        return Objects.equals(this.id, that.id) && Objects.equals(this.grupperingsid, that.grupperingsid) && Objects.equals(this.merkelapp,
                that.merkelapp) && Objects.equals(this.virksomhetsnummer, that.virksomhetsnummer) && Objects.equals(this.tittel, that.tittel) && Objects.equals(this.lenke,
                that.lenke) && Objects.equals(this.status, that.status) && Objects.equals(this.overstyrtStatustekst, that.overstyrtStatustekst) && Objects.equals(
                this.overstyrtTillegsinformasjon, that.overstyrtTillegsinformasjon) && Objects.equals(this.opprettetTid, that.opprettetTid) && Objects.equals(this.endretTid,
                that.endretTid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, grupperingsid, merkelapp, virksomhetsnummer, tittel, lenke, status, overstyrtStatustekst,
                overstyrtTillegsinformasjon, opprettetTid, endretTid);
    }

    @Override
    public String toString() {
        return "SakModell[" + "id=" + id + ", " + "grupperingsid=" + grupperingsid + ", " + "merkelapp=" + merkelapp + ", "
                + "virksomhetsnummer=" + virksomhetsnummer + ", " + "tittel=" + tittel + ", " + "lenke=" + lenke + ", " + "status="
                + status + ", " + "overstyrtStatustekst=" + overstyrtStatustekst + ", " + "overstyrtTillegsinformasjon="
                + overstyrtTillegsinformasjon + ", " + "opprettetTid=" + opprettetTid + ", " + "endretTid=" + endretTid + ']';
    }


    public enum SakStatus {
        MOTTATT,
        UNDER_BEHANDLING,
        FERDIG
    }

}
