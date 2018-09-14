package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.LandDto;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class UtenlandskOrganisasjonDto extends EgenNaeringDto {
   @NotNull
   protected String navn;
   @NotNull
   protected LandDto registrertILand;
   @NotNull
   protected BigInteger stillingsprosent;

   public String getNavn() {
      return this.navn;
   }

   public void setNavn(String value) {
      this.navn = value;
   }

   public LandDto getRegistrertILand() {
      return this.registrertILand;
   }

   public void setRegistrertILand(LandDto value) {
      this.registrertILand = value;
   }

   public BigInteger getStillingsprosent() {
      return this.stillingsprosent;
   }

   public void setStillingsprosent(BigInteger value) {
      this.stillingsprosent = value;
   }
}
