package no.nav.dokarkiv.dto;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Dokumentvariant(String variantformat, String filtype, byte[] fysiskDokument) {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o instanceof Dokumentvariant that && Objects.equals(variantformat, that.variantformat)
                && Objects.equals(filtype, that.filtype) && Arrays.equals(fysiskDokument, that.fysiskDokument);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(variantformat, filtype);
        result = 31 * result + Arrays.hashCode(fysiskDokument);
        return result;
    }

    @Override
    public String toString() {
        return "Dokumentvariant{" + "variantformat=" + variantformat + ", filtype=" + filtype + '}';
    }
}
