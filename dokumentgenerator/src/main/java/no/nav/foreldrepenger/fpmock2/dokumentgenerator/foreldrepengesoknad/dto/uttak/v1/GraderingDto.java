package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1;

public class GraderingDto extends UttaksperiodeDto {
   protected double arbeidtidProsent;
   protected boolean erArbeidstaker;
   protected Object virksomhetsnummer;
   protected boolean arbeidsforholdSomSkalGraderes;

   public double getArbeidtidProsent() {
      return this.arbeidtidProsent;
   }

   public void setArbeidtidProsent(double value) {
      this.arbeidtidProsent = value;
   }

   public boolean isErArbeidstaker() {
      return this.erArbeidstaker;
   }

   public void setErArbeidstaker(boolean value) {
      this.erArbeidstaker = value;
   }

   public Object getVirksomhetsnummer() {
      return this.virksomhetsnummer;
   }

   public void setVirksomhetsnummer(Object value) {
      this.virksomhetsnummer = value;
   }

   public boolean isArbeidsforholdSomSkalGraderes() {
      return this.arbeidsforholdSomSkalGraderes;
   }

   public void setArbeidsforholdSomSkalGraderes(boolean value) {
      this.arbeidsforholdSomSkalGraderes = value;
   }
}
