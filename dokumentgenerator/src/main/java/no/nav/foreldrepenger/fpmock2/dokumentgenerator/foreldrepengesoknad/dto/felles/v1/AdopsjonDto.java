package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;

public class AdopsjonDto extends SoekersRelasjonTilBarnetDto {
   @NotNull
   protected LocalDate omsorgsovertakelsesdato;

   protected LocalDate ankomstdato;

   protected ArrayList<LocalDate> foedselsdato;
   protected boolean adopsjonAvEktefellesBarn;

   public LocalDate getOmsorgsovertakelsesdato() {
      return this.omsorgsovertakelsesdato;
   }

   public void setOmsorgsovertakelsesdato(LocalDate value) {
      this.omsorgsovertakelsesdato = value;
   }

   public LocalDate getAnkomstdato() {
      return this.ankomstdato;
   }

   public void setAnkomstdato(LocalDate value) {
      this.ankomstdato = value;
   }

   public ArrayList<LocalDate> getFoedselsdato() {
      if (this.foedselsdato == null) {
         this.foedselsdato = new ArrayList();
      }

      return this.foedselsdato;
   }

   public boolean isAdopsjonAvEktefellesBarn() {
      return this.adopsjonAvEktefellesBarn;
   }

   public void setAdopsjonAvEktefellesBarn(boolean value) {
      this.adopsjonAvEktefellesBarn = value;
   }

   public void setFoedselsdato(ArrayList<LocalDate> foedselsdato) {
      this.foedselsdato = foedselsdato;
   }
}
