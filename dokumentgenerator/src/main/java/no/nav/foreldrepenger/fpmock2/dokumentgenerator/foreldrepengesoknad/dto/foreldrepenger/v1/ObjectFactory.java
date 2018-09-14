package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
   private static final QName _EgenNaeringVedlegg_QNAME = new QName("", "vedlegg");

   public VirksomhetstyperDto createVirksomhetstyper() {
      return new VirksomhetstyperDto();
   }

   public AnnenOpptjeningDto createAnnenOpptjening() {
      return new AnnenOpptjeningDto();
   }

   public ArbeidsforholdtyperDto createArbeidsforholdtyper() {
      return new ArbeidsforholdtyperDto();
   }

   public NorskOrganisasjonDto createNorskOrganisasjon() {
      return new NorskOrganisasjonDto();
   }

   public OpptjeningDto createOpptjening() {
      return new OpptjeningDto();
   }

   public RegnskapsfoererDto createRegnskapsfoerer() {
      return new RegnskapsfoererDto();
   }

   public UtenlandskArbeidsforholdDto createUtenlandskArbeidsforhold() {
      return new UtenlandskArbeidsforholdDto();
   }

   public UtenlandskOrganisasjonDto createUtenlandskOrganisasjon() {
      return new UtenlandskOrganisasjonDto();
   }

   public MorsAktivitetsTyperDto createMorsAktivitetsTyper() {
      return new MorsAktivitetsTyperDto();
   }

   public ForeldrepengerDto createForeldrepenger() {
      return new ForeldrepengerDto();
   }

   public AnnenOpptjeningTyperDto createAnnenOpptjeningTyper() {
      return new AnnenOpptjeningTyperDto();
   }

   public DekningsgraderDto createDekningsgrader() {
      return new DekningsgraderDto();
   }

   public DekningsgradDto createDekningsgrad() {
      return new DekningsgradDto();
   }

   @XmlElementDecl(
      namespace = "",
      name = "vedlegg",
      scope = EgenNaeringDto.class
   )
   @XmlIDREF
   public JAXBElement createEgenNaeringVedlegg(Object value) {
      return new JAXBElement(_EgenNaeringVedlegg_QNAME, Object.class, EgenNaeringDto.class, value);
   }

   @XmlElementDecl(
      namespace = "",
      name = "vedlegg",
      scope = UtenlandskArbeidsforholdDto.class
   )
   @XmlIDREF
   public JAXBElement createArbeidsforholdVedlegg(Object value) {
      return new JAXBElement(_EgenNaeringVedlegg_QNAME, Object.class, UtenlandskArbeidsforholdDto.class, value);
   }

   @XmlElementDecl(
      namespace = "",
      name = "vedlegg",
      scope = AnnenOpptjeningDto.class
   )
   @XmlIDREF
   public JAXBElement createAnnenOpptjeningVedlegg(Object value) {
      return new JAXBElement(_EgenNaeringVedlegg_QNAME, Object.class, AnnenOpptjeningDto.class, value);
   }
}
