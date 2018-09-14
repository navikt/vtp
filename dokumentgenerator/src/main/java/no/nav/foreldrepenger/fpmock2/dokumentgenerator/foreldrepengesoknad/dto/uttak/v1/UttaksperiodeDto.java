package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.LukketPeriodeDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1.MorsAktivitetsTyperDto;

import javax.validation.constraints.NotNull;

/*
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "uttaksperiodeDtoType")

@JsonSubTypes({
        @JsonSubTypes.Type(value = GraderingDto.class)
})
*/

public class UttaksperiodeDto extends LukketPeriodeDto {
   @NotNull
   protected UttaksperiodetyperDto type;
   protected Boolean oenskerSamtidigUttak;
   protected MorsAktivitetsTyperDto morsAktivitetIPerioden;

   public UttaksperiodetyperDto getType() {
      return this.type;
   }

   public void setType(UttaksperiodetyperDto value) {
      this.type = value;
   }

   public Boolean isOenskerSamtidigUttak() {
      return this.oenskerSamtidigUttak;
   }

   public void setOenskerSamtidigUttak(Boolean value) {
      this.oenskerSamtidigUttak = value;
   }

   public MorsAktivitetsTyperDto getMorsAktivitetIPerioden() {
      return this.morsAktivitetIPerioden;
   }

   public void setMorsAktivitetIPerioden(MorsAktivitetsTyperDto value) {
      this.morsAktivitetIPerioden = value;
   }
}
