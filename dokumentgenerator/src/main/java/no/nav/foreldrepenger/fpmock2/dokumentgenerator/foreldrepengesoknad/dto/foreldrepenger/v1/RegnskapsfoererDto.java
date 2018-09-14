package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1;

import javax.validation.constraints.NotNull;

public class RegnskapsfoererDto {
   @NotNull
   protected String navn;
   @NotNull
   protected String telefon;

   public String getNavn() {
      return this.navn;
   }

   public void setNavn(String value) {
      this.navn = value;
   }

   public String getTelefon() {
      return this.telefon;
   }

   public void setTelefon(String value) {
      this.telefon = value;
   }
}
