package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class PeriodeDto {
   @NotNull
   protected LocalDate fom;

   protected LocalDate tom;

   public LocalDate getFom() {
      return this.fom;
   }

   public void setFom(LocalDate value) {
      this.fom = value;
   }

   public LocalDate getTom() {
      return this.tom;
   }

   public void setTom(LocalDate value) {
      this.tom = value;
   }
}
