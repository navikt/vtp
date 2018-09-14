package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import javax.validation.constraints.NotNull;

public class AnnenForelderUtenNorskIdentDto extends AnnenForelderDto {
   @NotNull
   protected String utenlandskPersonidentifikator;

   @NotNull
   protected LandDto land;

   public String getUtenlandskPersonidentifikator() {
      return this.utenlandskPersonidentifikator;
   }

   public void setUtenlandskPersonidentifikator(String value) {
      this.utenlandskPersonidentifikator = value;
   }

   public LandDto getLand() {
      return this.land;
   }

   public void setLand(LandDto value) {
      this.land = value;
   }
}
