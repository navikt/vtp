package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import javax.validation.constraints.NotNull;

public class AnnenForelderMedNorskIdentDto extends AnnenForelderDto {
   @NotNull
   protected String aktoerId;

   public String getAktoerId() {
      return this.aktoerId;
   }

   public void setAktoerId(String value) {
      this.aktoerId = value;
   }
}
