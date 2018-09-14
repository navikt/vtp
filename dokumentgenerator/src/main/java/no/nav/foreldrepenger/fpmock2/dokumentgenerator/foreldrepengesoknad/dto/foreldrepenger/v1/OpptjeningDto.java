package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1;

import java.util.ArrayList;
import java.util.List;

public class OpptjeningDto {
   protected List<UtenlandskArbeidsforholdDto> arbeidsforhold;
   protected List egenNaering;
   protected List<AnnenOpptjeningDto> annenOpptjening;

   public List<UtenlandskArbeidsforholdDto> getUtenlandskArbeidsforhold() {
      if (this.arbeidsforhold == null) {
         this.arbeidsforhold = new ArrayList();
      }

      return this.arbeidsforhold;
   }

   public List getEgenNaering() {
      if (this.egenNaering == null) {
         this.egenNaering = new ArrayList();
      }

      return this.egenNaering;
   }

   public List<AnnenOpptjeningDto> getAnnenOpptjening() {
      if (this.annenOpptjening == null) {
         this.annenOpptjening = new ArrayList();
      }

      return this.annenOpptjening;
   }
}
