package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.LukketPeriodeDto;

import javax.validation.constraints.NotNull;

public class OverfoeringsperiodeDto extends LukketPeriodeDto {
   @NotNull
   protected OverfoeringsaarsakerDto aarsak;
   @NotNull
   protected UttaksperiodetyperDto overfoeringAv;

   public OverfoeringsaarsakerDto getAarsak() {
      return this.aarsak;
   }

   public void setAarsak(OverfoeringsaarsakerDto value) {
      this.aarsak = value;
   }

   public UttaksperiodetyperDto getOverfoeringAv() {
      return this.overfoeringAv;
   }

   public void setOverfoeringAv(UttaksperiodetyperDto value) {
      this.overfoeringAv = value;
   }
}
