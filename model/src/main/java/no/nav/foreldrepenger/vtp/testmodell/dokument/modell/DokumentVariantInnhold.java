package no.nav.foreldrepenger.vtp.testmodell.dokument.modell;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Arkivfiltype;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Variantformat;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility= JsonAutoDetect.Visibility.ANY, setterVisibility = JsonAutoDetect.Visibility.ANY, fieldVisibility = JsonAutoDetect.Visibility.ANY)
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
