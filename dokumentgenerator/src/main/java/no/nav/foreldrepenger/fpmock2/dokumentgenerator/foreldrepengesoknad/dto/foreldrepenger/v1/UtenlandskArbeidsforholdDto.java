package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.LandDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.PeriodeDto;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class UtenlandskArbeidsforholdDto {
   @NotNull
   protected LandDto arbeidsland;
   protected boolean harHattInntektIPerioden;
   protected PeriodeDto periode;
   protected String arbeidsgiversnavn;
   protected List<ObjectFactory> vedlegg;

   public LandDto getArbeidsland() {
      return this.arbeidsland;
   }

   public void setArbeidsland(LandDto value) {
      this.arbeidsland = value;
   }

   public boolean isHarHattInntektIPerioden() {
      return this.harHattInntektIPerioden;
   }

   public void setHarHattInntektIPerioden(boolean value) {
      this.harHattInntektIPerioden = value;
   }

   public PeriodeDto getPeriode() {
      return periode;
   }

   public void setPeriode(PeriodeDto periode) {
      this.periode = periode;
   }

   public String getArbeidsgiversnavn() {
      return arbeidsgiversnavn;
   }

   public void setArbeidsgiversnavn(String arbeidsgiversnavn) {
      this.arbeidsgiversnavn = arbeidsgiversnavn;
   }

   public List getVedlegg() {
      if (this.vedlegg == null) {
         this.vedlegg = new ArrayList();
      }

      return this.vedlegg;
   }

   public void setVedlegg(List<ObjectFactory> vedlegg) {
      this.vedlegg = vedlegg;
   }
}
