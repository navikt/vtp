package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class TerminDto extends SoekersRelasjonTilBarnetDto {
   @NotNull
   protected LocalDate termindato;

   @NotNull
   protected LocalDate utstedtdato;

   public LocalDate getTermindato() {
      return this.termindato;
   }

   public void setTermindato(LocalDate value) {
      this.termindato = value;
   }

   public LocalDate getUtstedtdato() {
      return this.utstedtdato;
   }

   public void setUtstedtdato(LocalDate value) {
      this.utstedtdato = value;
   }
}
