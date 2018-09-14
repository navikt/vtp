package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.LukketPeriodeDto;

import javax.validation.constraints.NotNull;

public class UtsettelsesperiodeDto extends LukketPeriodeDto {
   @NotNull
   protected UtsettelsesaarsakerDto aarsak;

   @NotNull
   protected UttaksperiodetyperDto utsettelseAv;
   protected boolean erArbeidstaker;
   protected String virksomhetsnummer;

   public UtsettelsesaarsakerDto getAarsak() {
      return this.aarsak;
   }

   public void setAarsak(UtsettelsesaarsakerDto value) {
      this.aarsak = value;
   }

   public UttaksperiodetyperDto getUtsettelseAv() {
      return this.utsettelseAv;
   }

   public void setUtsettelseAv(UttaksperiodetyperDto value) {
      this.utsettelseAv = value;
   }

   public boolean isErArbeidstaker() {
      return this.erArbeidstaker;
   }

   public void setErArbeidstaker(boolean value) {
      this.erArbeidstaker = value;
   }

   public String getVirksomhetsnummer() {
      return this.virksomhetsnummer;
   }

   public void setVirksomhetsnummer(String value) {
      this.virksomhetsnummer = value;
   }
}
