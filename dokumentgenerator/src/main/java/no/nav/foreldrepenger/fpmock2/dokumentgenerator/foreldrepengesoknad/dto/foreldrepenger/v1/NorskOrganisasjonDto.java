package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1;

import javax.validation.constraints.NotNull;

public class NorskOrganisasjonDto extends EgenNaeringDto {
   @NotNull
   protected String organisasjonsnummer;
   @NotNull
   protected String navn;

   public String getOrganisasjonsnummer() {
      return this.organisasjonsnummer;
   }

   public void setOrganisasjonsnummer(String value) {
      this.organisasjonsnummer = value;
   }

   public String getNavn() {
      return this.navn;
   }

   public void setNavn(String value) {
      this.navn = value;
   }
}
