package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.LukketPeriodeDto;

import javax.validation.constraints.NotNull;

public class OppholdsperiodeDto extends LukketPeriodeDto {
   @NotNull
   protected OppholdsaarsakerDto aarsak;

   public OppholdsaarsakerDto getAarsak() {
      return this.aarsak;
   }

   public void setAarsak(OppholdsaarsakerDto value) {
      this.aarsak = value;
   }
}
