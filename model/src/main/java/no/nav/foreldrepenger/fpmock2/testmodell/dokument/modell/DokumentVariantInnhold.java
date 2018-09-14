package no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell;

public class DokumentVariantInnhold {

    private String filType;
    private byte[] dokumentInnhold;
    private String variantFormat;

    public String getFilType() {
        return filType;
    }

    public void setFilType(String filType) {
        this.filType = filType;
    }

    public byte[] getDokumentInnhold() {
        return dokumentInnhold;
    }

    public void setDokumentInnhold(byte[] dokumentInnhold) {
        this.dokumentInnhold = dokumentInnhold;
    }

    public String getVariantFormat() {
        return variantFormat;
    }

    public void setVariantFormat(String variantFormat) {
        this.variantFormat = variantFormat;
    }
}
