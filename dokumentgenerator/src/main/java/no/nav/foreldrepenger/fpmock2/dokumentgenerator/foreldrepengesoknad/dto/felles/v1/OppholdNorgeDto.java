package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import javax.validation.constraints.NotNull;

public class OppholdNorgeDto {
   @NotNull
   protected PeriodeDto periode;

   public PeriodeDto getPeriode() {
      return this.periode;
   }

   public void setPeriode(PeriodeDto value) {
      this.periode = value;
   }
}
