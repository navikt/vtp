package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
   private static final QName _LukketPeriodeVedlegg_QNAME = new QName("", "vedlegg");

   public VergeDto createVerge() {
      return new VergeDto();
   }

   public OppholdUtlandetDto createOppholdUtlandet() {
      return new OppholdUtlandetDto();
   }

   public AdopsjonDto createAdopsjon() {
      return new AdopsjonDto();
   }

   public FoedselDto createFoedsel() {
      return new FoedselDto();
   }

   public OmsorgsovertakelseDto createOmsorgsovertakelse() {
      return new OmsorgsovertakelseDto();
   }

   public Omsorgsovertakelseaarsaker createOmsorgsovertakelseaarsaker() {
      return new Omsorgsovertakelseaarsaker();
   }

   public RettigheterDto createRettigheter() {
      return new RettigheterDto();
   }

   public TerminDto createTermin() {
      return new TerminDto();
   }

   public MedlemskapDto createMedlemskap() {
      return new MedlemskapDto();
   }

   public OppholdNorgeDto createOppholdNorge() {
      return new OppholdNorgeDto();
   }

   public BrukerDto createBruker() {
      return new BrukerDto();
   }

   public BrukerrollerDto createBrukerroller() {
      return new BrukerrollerDto();
   }

   public FullmektigDto createFullmektig() {
      return new FullmektigDto();
   }

   public AnnenForelderMedNorskIdentDto createAnnenForelderMedNorskIdent() {
      return new AnnenForelderMedNorskIdentDto();
   }

   public AnnenForelderUtenNorskIdentDto createAnnenForelderUtenNorskIdent() {
      return new AnnenForelderUtenNorskIdentDto();
   }

   public UkjentForelderDto createUkjentForelder() {
      return new UkjentForelderDto();
   }

   public VedleggDto createVedlegg() {
      return new VedleggDto();
   }

   public LandDto createLand() {
      return new LandDto();
   }

   public PeriodeDto createPeriode() {
      return new PeriodeDto();
   }

   public InnsendingstypeDto createInnsendingstype() {
      return new InnsendingstypeDto();
   }

   public KodeverkDto createKodeverk() {
      return new KodeverkDto();
   }

   public VergetypeDto createVergetype() {
      return new VergetypeDto();
   }

   @XmlElementDecl(
      namespace = "",
      name = "vedlegg",
      scope = LukketPeriodeDto.class
   )
   @XmlIDREF
   public JAXBElement createLukketPeriodeVedlegg(Object value) {
      return new JAXBElement(_LukketPeriodeVedlegg_QNAME, Object.class, LukketPeriodeDto.class, value);
   }

   @XmlElementDecl(
      namespace = "",
      name = "vedlegg",
      scope = SoekersRelasjonTilBarnetDto.class
   )
   @XmlIDREF
   public JAXBElement createSoekersRelasjonTilBarnetVedlegg(Object value) {
      return new JAXBElement(_LukketPeriodeVedlegg_QNAME, Object.class, SoekersRelasjonTilBarnetDto.class, value);
   }
}
