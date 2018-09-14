package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.BrukerDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.VedleggDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.YtelseDto;


import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SoeknadDto {
   @NotNull
   protected LocalDate mottattDato;
   protected List<VedleggDto> paakrevdeVedlegg;
   protected String begrunnelseForSenSoeknad;
   protected String tilleggsopplysninger;
   @NotNull
   protected YtelseDto omYtelse;
   @NotNull
   protected BrukerDto soeker;
   protected List<VedleggDto> andreVedlegg;

   public LocalDate getMottattDato() {
      return this.mottattDato;
   }

   public void setMottattDato(LocalDate value) {
      this.mottattDato = value;
   }

   public List getPaakrevdeVedlegg() {
      if (this.paakrevdeVedlegg == null) {
         this.paakrevdeVedlegg = new ArrayList();
      }

      return this.paakrevdeVedlegg;
   }

   public String getBegrunnelseForSenSoeknad() {
      return this.begrunnelseForSenSoeknad;
   }

   public void setBegrunnelseForSenSoeknad(String value) {
      this.begrunnelseForSenSoeknad = value;
   }

   public String getTilleggsopplysninger() {
      return this.tilleggsopplysninger;
   }

   public void setTilleggsopplysninger(String value) {
      this.tilleggsopplysninger = value;
   }

   public YtelseDto getOmYtelse() {
      return this.omYtelse;
   }

   public void setOmYtelse(YtelseDto value) {
      this.omYtelse = value;
   }

   public BrukerDto getSoeker() {
      return this.soeker;
   }

   public void setSoeker(BrukerDto value) {
      this.soeker = value;
   }

   public List getAndreVedlegg() {
      if (this.andreVedlegg == null) {
         this.andreVedlegg = new ArrayList();
      }

      return this.andreVedlegg;
   }

}
