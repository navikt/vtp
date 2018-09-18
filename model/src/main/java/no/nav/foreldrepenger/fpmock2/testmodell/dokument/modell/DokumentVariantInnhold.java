package no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell;

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Arkivfiltype;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Variantformat;

public class DokumentVariantInnhold {

    private Arkivfiltype filType;
    private byte[] dokumentInnhold;
    private Variantformat variantFormat;

    public Arkivfiltype getFilType() {
        return filType;
    }

    public void setFilType(Arkivfiltype filType) {
        this.filType = filType;
    }

    public byte[] getDokumentInnhold() {
        return dokumentInnhold;
    }

    public void setDokumentInnhold(byte[] dokumentInnhold) {
        this.dokumentInnhold = dokumentInnhold;
    }

    public Variantformat getVariantFormat() {
        return variantFormat;
    }

    public void setVariantFormat(Variantformat variantFormat) {
        this.variantFormat = variantFormat;
    }

    public DokumentVariantInnhold(Arkivfiltype arkivfiltype, Variantformat variantformat, byte[] dokumentInnhold){
        this.dokumentInnhold = dokumentInnhold;
        this.variantFormat = variantformat;
        this.filType = arkivfiltype;
    }

}
