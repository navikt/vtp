package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class FoedselDto extends SoekersRelasjonTilBarnetDto {
   @NotNull
   protected LocalDate foedselsdato;

   public LocalDate getFoedselsdato() {
      return this.foedselsdato;
   }

   public void setFoedselsdato(LocalDate value) {
      this.foedselsdato = value;
   }
}
