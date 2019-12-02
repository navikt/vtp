package no.nav.aktoerregister.rest.api.v1;

class Identinfo {

    private String ident;
    private String identgruppe;
    private Boolean gjeldende;

    public Identinfo() {

    }

    public Identinfo(String ident, String identgruppe, Boolean gjeldende) {
        this.ident = ident;
        this.identgruppe = identgruppe;
        this.gjeldende = gjeldende;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getIdentgruppe() {
        return identgruppe;
    }

    public void setIdentgruppe(String identgruppe) {
        this.identgruppe = identgruppe;
    }

    public Boolean getGjeldende() {
        return gjeldende;
    }

    public void setGjeldende(Boolean gjeldende) {
        this.gjeldende = gjeldende;
    }

    @Override
    public String toString() {
        return "Identinfo{" + "ident=" + ident +
                ", identgruppe=" + identgruppe +
                ", gjeldende=" + gjeldende +
                '}';
    }
}
