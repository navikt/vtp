package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import javax.validation.constraints.NotNull;


public class OppholdUtlandetDto {
   @NotNull
   protected PeriodeDto periode;
   @NotNull
   protected LandDto land;

   public PeriodeDto getPeriode() {
      return this.periode;
   }

   public void setPeriode(PeriodeDto value) {
      this.periode = value;
   }

   public LandDto getLand() {
      return this.land;
   }

   public void setLand(LandDto value) {
      this.land = value;
   }
}
