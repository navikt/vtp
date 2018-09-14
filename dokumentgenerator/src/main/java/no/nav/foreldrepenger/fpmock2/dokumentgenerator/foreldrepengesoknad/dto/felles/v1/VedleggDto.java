package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import javax.validation.constraints.NotNull;

public class VedleggDto {
   @NotNull
   protected String id;

   @NotNull
   protected InnsendingstypeDto innsendingstype;
   protected String skjemanummer;

   protected boolean vedlagt;

   @NotNull
   protected String tilleggsinformasjon;

   public String getId() {
      return this.id;
   }

   public void setId(String value) {
      this.id = value;
   }

   public InnsendingstypeDto getInnsendingstype() {
      return this.innsendingstype;
   }

   public void setInnsendingstype(InnsendingstypeDto value) {
      this.innsendingstype = value;
   }

   public String getSkjemanummer() {
      return this.skjemanummer;
   }

   public void setSkjemanummer(String value) {
      this.skjemanummer = value;
   }

   public String getTilleggsinformasjon() {
      return this.tilleggsinformasjon;
   }

   public void setTilleggsinformasjon(String value) {
      this.tilleggsinformasjon = value;
   }

   public boolean isVedlagt() {
      return vedlagt;
   }

   public void setVedlagt(boolean vedlagt) {
      this.vedlagt = vedlagt;
   }
}
