package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
   public GraderingDto createGradering() {
      return new GraderingDto();
   }

   public UttaksperiodeDto createUttaksperiode() {
      return new UttaksperiodeDto();
   }

   public UttaksperiodetyperDto createUttaksperiodetyper() {
      return new UttaksperiodetyperDto();
   }

   public UtsettelsesperiodeDto createUtsettelsesperiode() {
      return new UtsettelsesperiodeDto();
   }

   public UtsettelsesaarsakerDto createUtsettelsesaarsaker() {
      return new UtsettelsesaarsakerDto();
   }

   public OverfoeringsperiodeDto createOverfoeringsperiode() {
      return new OverfoeringsperiodeDto();
   }

   public OverfoeringsaarsakerDto createOverfoeringsaarsaker() {
      return new OverfoeringsaarsakerDto();
   }

   public FordelingDto createFordeling() {
      return new FordelingDto();
   }

   public OppholdsperiodeDto createOppholdsperiode() {
      return new OppholdsperiodeDto();
   }

   public OppholdsaarsakerDto createOppholdsaarsaker() {
      return new OppholdsaarsakerDto();
   }
}
