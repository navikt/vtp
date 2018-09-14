package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1.AnnenOpptjeningTyperDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1.DekningsgraderDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1.MorsAktivitetsTyperDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1.VirksomhetstyperDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1.OppholdsaarsakerDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1.OverfoeringsaarsakerDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1.UtsettelsesaarsakerDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1.UttaksperiodetyperDto;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "kodeverkDtoType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = UttaksperiodetyperDto.class),
        @JsonSubTypes.Type(value = UtsettelsesaarsakerDto.class),
        @JsonSubTypes.Type(value = OverfoeringsaarsakerDto.class),
        @JsonSubTypes.Type(value = OppholdsaarsakerDto.class),
        @JsonSubTypes.Type(value = VirksomhetstyperDto.class),
        @JsonSubTypes.Type(value = MorsAktivitetsTyperDto.class),
        @JsonSubTypes.Type(value = AnnenOpptjeningTyperDto.class),
        @JsonSubTypes.Type(value = DekningsgraderDto.class),
        @JsonSubTypes.Type(value = Omsorgsovertakelseaarsaker.class),
        @JsonSubTypes.Type(value = BrukerrollerDto.class),
        @JsonSubTypes.Type(value = LandDto.class),
        @JsonSubTypes.Type(value = InnsendingstypeDto.class),
        @JsonSubTypes.Type(value = VergetypeDto.class)
})

public class KodeverkDto {

   protected String value;

   protected String kodeverk;

   protected String kode;

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String getKodeverk() {
      return this.kodeverk;
   }

   public void setKodeverk(String value) {
      this.kodeverk = value;
   }

   public String getKode() {
      return this.kode;
   }

   public void setKode(String value) {
      this.kode = value;
   }
}
