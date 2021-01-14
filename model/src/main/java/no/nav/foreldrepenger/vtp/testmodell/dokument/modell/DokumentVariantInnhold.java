package no.nav.foreldrepenger.vtp.testmodell.dokument.modell;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Arkivfiltype;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Variantformat;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.ANY, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DokumentVariantInnhold {

    private Arkivfiltype filType;
    private byte[] dokumentInnhold;
    private Variantformat variantFormat;

    public DokumentVariantInnhold(@JsonProperty("arkivfiltype") Arkivfiltype arkivfiltype,
                                  @JsonProperty("variantformat") Variantformat variantformat,
                                  @JsonProperty("dokumentInnhold") byte[] dokumentInnhold){
        this.dokumentInnhold = dokumentInnhold;
        this.variantFormat = variantformat;
        this.filType = arkivfiltype;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DokumentVariantInnhold that = (DokumentVariantInnhold) o;
        return Objects.equals(filType, that.filType) && Arrays.equals(dokumentInnhold, that.dokumentInnhold) && Objects.equals(variantFormat, that.variantFormat);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(filType, variantFormat);
        result = 31 * result + Arrays.hashCode(dokumentInnhold);
        return result;
    }

    @Override
    public String toString() {
        return "DokumentVariantInnhold{" +
                "filType=" + filType +
                ", dokumentInnhold=" + Arrays.toString(dokumentInnhold) +
                ", variantFormat=" + variantFormat +
                '}';
    }
}
