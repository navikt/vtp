package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.*;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1.FordelingDto;

public class ForeldrepengerDto extends YtelseDto {
   protected AnnenForelderDto annenForelder;
   protected RettigheterDto rettigheter;
   protected SoekersRelasjonTilBarnetDto relasjonTilBarnet;
   protected MedlemskapDto medlemskap;
   protected OpptjeningDto opptjening;
   protected FordelingDto fordeling;
   protected DekningsgradDto dekningsgrad;

   public AnnenForelderDto getAnnenForelder() {
      return this.annenForelder;
   }

   public void setAnnenForelder(AnnenForelderDto value) {
      this.annenForelder = value;
   }

   public RettigheterDto getRettigheter() {
      return this.rettigheter;
   }

   public void setRettigheter(RettigheterDto value) {
      this.rettigheter = value;
   }

   public SoekersRelasjonTilBarnetDto getRelasjonTilBarnet() {
      return this.relasjonTilBarnet;
   }

   public void setRelasjonTilBarnet(SoekersRelasjonTilBarnetDto value) {
      this.relasjonTilBarnet = value;
   }

   public MedlemskapDto getMedlemskap() {
      return this.medlemskap;
   }

   public void setMedlemskap(MedlemskapDto value) {
      this.medlemskap = value;
   }

   public OpptjeningDto getOpptjening() {
      return this.opptjening;
   }

   public void setOpptjening(OpptjeningDto value) {
      this.opptjening = value;
   }

   public FordelingDto getFordeling() {
      return this.fordeling;
   }

   public void setFordeling(FordelingDto value) {
      this.fordeling = value;
   }

   public DekningsgradDto getDekningsgrad() {
      return this.dekningsgrad;
   }

   public void setDekningsgrad(DekningsgradDto value) {
      this.dekningsgrad = value;
   }
}
