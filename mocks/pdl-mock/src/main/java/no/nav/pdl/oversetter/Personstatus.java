package no.nav.pdl.oversetter;

public enum Personstatus {
    BOSA("bosattEtterFolkeregisterloven", "bosatt"),
    DØD("doedIFolkeregisteret", "doed"),
    DØDD("doedIFolkeregisteret", "doed"),
    UTPE("opphoert", "opphoert"),
    ADNR("dNummer", "inaktiv"),
    FOSV("forsvunnet", "forsvunnet"),
    UTVA("ikkeBosatt", "utflyttet"),
    UREG("ikkeBosatt", "ikkeBosatt"),
    UTAN("ikkeBosatt", "ikkeBosatt"),
    FØDR("ikkeBosatt", "foedselsregistrert");

    private final String forenkletStatus;
    private final String status;

    Personstatus(String forenkletStatus, String status) {
        this.forenkletStatus = forenkletStatus;
        this.status = status;
    }

    public String getForenkletStatus() {
        return forenkletStatus;
    }

    public String getStatus() {
        return status;
    }
}
