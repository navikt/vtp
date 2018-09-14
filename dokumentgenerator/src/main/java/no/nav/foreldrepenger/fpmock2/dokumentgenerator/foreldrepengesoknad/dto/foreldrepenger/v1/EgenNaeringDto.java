package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.LandDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.PeriodeDto;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "egenNaeringDtoType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NorskOrganisasjonDto.class),
        @JsonSubTypes.Type(value = UtenlandskOrganisasjonDto.class)
})
public abstract class EgenNaeringDto {
   protected LandDto arbeidsland;
   protected RegnskapsfoererDto regnskapsfoerer;
   protected List virksomhetstype;
   protected PeriodeDto periode;

   protected List vedlegg;
   protected String beskrivelseAvNaerRelasjon;
   protected boolean erNyoppstartet;
   protected boolean erVarigEndring;
   protected BigInteger naeringsinntektBrutto;

   protected LocalDate endringsDato;
   protected String beskrivelseAvEndring;

   public LandDto getArbeidsland() {
      return this.arbeidsland;
   }

   public void setArbeidsland(LandDto value) {
      this.arbeidsland = value;
   }

   public RegnskapsfoererDto getRegnskapsfoerer() {
      return this.regnskapsfoerer;
   }

   public void setRegnskapsfoerer(RegnskapsfoererDto value) {
      this.regnskapsfoerer = value;
   }

   public List getVirksomhetstype() {
      if (this.virksomhetstype == null) {
         this.virksomhetstype = new ArrayList();
      }

      return this.virksomhetstype;
   }

   public PeriodeDto getPeriode() {
      return this.periode;
   }

   public void setPeriode(PeriodeDto value) {
      this.periode = value;
   }

   public List getVedlegg() {
      if (this.vedlegg == null) {
         this.vedlegg = new ArrayList();
      }

      return this.vedlegg;
   }

   public String getBeskrivelseAvNaerRelasjon() {
      return this.beskrivelseAvNaerRelasjon;
   }

   public void setBeskrivelseAvNaerRelasjon(String value) {
      this.beskrivelseAvNaerRelasjon = value;
   }

   public boolean isErNyoppstartet() {
      return this.erNyoppstartet;
   }

   public void setErNyoppstartet(boolean value) {
      this.erNyoppstartet = value;
   }

   public boolean isErVarigEndring() {
      return this.erVarigEndring;
   }

   public void setErVarigEndring(boolean value) {
      this.erVarigEndring = value;
   }

   public BigInteger getNaeringsinntektBrutto() {
      return this.naeringsinntektBrutto;
   }

   public void setNaeringsinntektBrutto(BigInteger value) {
      this.naeringsinntektBrutto = value;
   }

   public LocalDate getEndringsDato() {
      return this.endringsDato;
   }

   public void setEndringsDato(LocalDate value) {
      this.endringsDato = value;
   }

   public String getBeskrivelseAvEndring() {
      return this.beskrivelseAvEndring;
   }

   public void setBeskrivelseAvEndring(String value) {
      this.beskrivelseAvEndring = value;
   }
}
