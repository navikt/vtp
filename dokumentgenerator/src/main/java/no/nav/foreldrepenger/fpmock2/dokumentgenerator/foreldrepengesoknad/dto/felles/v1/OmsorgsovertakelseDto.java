package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;

public class OmsorgsovertakelseDto extends SoekersRelasjonTilBarnetDto {
   @NotNull
   protected LocalDate omsorgsovertakelsesdato;

   protected ArrayList<LocalDate> foedselsdato;
   protected String beskrivelse;

   @NotNull
   protected Omsorgsovertakelseaarsaker omsorgsovertakelseaarsak;

   public LocalDate getOmsorgsovertakelsesdato() {
      return this.omsorgsovertakelsesdato;
   }

   public void setOmsorgsovertakelsesdato(LocalDate value) {
      this.omsorgsovertakelsesdato = value;
   }

   public ArrayList<LocalDate> getFoedselsdato() {
      if (this.foedselsdato == null) {
         this.foedselsdato = new ArrayList();
      }

      return this.foedselsdato;
   }

   public String getBeskrivelse() {
      return this.beskrivelse;
   }

   public void setBeskrivelse(String value) {
      this.beskrivelse = value;
   }

   public Omsorgsovertakelseaarsaker getOmsorgsovertakelseaarsak() {
      return this.omsorgsovertakelseaarsak;
   }

   public void setOmsorgsovertakelseaarsak(Omsorgsovertakelseaarsaker value) {
      this.omsorgsovertakelseaarsak = value;
   }
}
