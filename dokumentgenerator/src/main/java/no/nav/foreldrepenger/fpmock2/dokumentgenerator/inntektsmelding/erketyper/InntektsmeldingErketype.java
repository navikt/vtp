package no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.dto.EksempelArbeidsgiver;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.dto.InntektsmeldingDTO;
import no.nav.inntektsmelding.xml.kodeliste._20180702.YtelseKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.ÅrsakInnsendingKodeliste;
import no.seres.xsd.nav.inntektsmelding_m._20181211.EndringIRefusjon;
import no.seres.xsd.nav.inntektsmelding_m._20181211.GraderingIForeldrepenger;
import no.seres.xsd.nav.inntektsmelding_m._20181211.NaturalytelseDetaljer;
import no.seres.xsd.nav.inntektsmelding_m._20181211.Periode;
import no.seres.xsd.nav.inntektsmelding_m._20181211.UtsettelseAvForeldrepenger;


public class InntektsmeldingErketype {

    public String standardInntektsmelding(String fnr, String arbeidsforholdId){
        InntektsmeldingDTO inntektsmeldingDTO = new InntektsmeldingDTO();
        inntektsmeldingDTO.setArbeidsforholdId(arbeidsforholdId);
        inntektsmeldingDTO.setArbeidstakerFNR(fnr);
        inntektsmeldingDTO.setBeregnetInntektBelop(35000);
        inntektsmeldingDTO.setInntektsmeldingID(UUID.randomUUID().toString().substring(0, 7));
        inntektsmeldingDTO.setInntektsmeldingType(ÅrsakInnsendingKodeliste.NY);
        inntektsmeldingDTO.setInntektsmeldingYtelse(YtelseKodeliste.FORELDREPENGER);
        inntektsmeldingDTO.setStartDatoForeldrepengePerioden(LocalDate.of(2018, 10, 1));
        inntektsmeldingDTO.setOrganisasjonsnummer(EksempelArbeidsgiver.STATOILSOKKEL.getOrgNummer());
        inntektsmeldingDTO.setEksempelArbeidsgiver(EksempelArbeidsgiver.STATOILSOKKEL);

        return makeInntektsmeldingFromRequestToString(inntektsmeldingDTO);

    }
    
    public InntektsmeldingBuilder makeInntektsmeldingFromRequest(InntektsmeldingDTO request) {
        InntektsmeldingBuilder inntektsmeldingBuilder = new InntektsmeldingBuilder(
                request.getInntektsmeldingID(),
                request.getInntektsmeldingYtelse(),
                request.getInntektsmeldingType(),
                request.getArbeidstakerFNR(),
                request.getStartDatoForeldrepengePerioden());



        if(request.getInntektsmeldingType() != null){
            inntektsmeldingBuilder.setAarsakTilInnsending(request.getInntektsmeldingType());
        }

        if (request.getNaerRelasjon() != null){
            inntektsmeldingBuilder.setNaaerRelasjon(request.getNaerRelasjon());
        }

        inntektsmeldingBuilder.setAvsendersystem(InntektsmeldingBuilder.createAvsendersystem(
                "FS32",
                "1.0"));

        if (request.getOrganisasjonsnummer() != null) {
            inntektsmeldingBuilder.setArbeidsgiver(InntektsmeldingBuilder.createArbeidsgiver(
                    request.getOrganisasjonsnummer(),
                    "41925090"
            ));
        }

        if (request.getOrganisasjonsnummer() == null && request.getEksempelArbeidsgiver() != null) {
            inntektsmeldingBuilder.setArbeidsgiver(InntektsmeldingBuilder.createArbeidsgiver(
                    request.getEksempelArbeidsgiver().getVirkNummer(),
                    request.getEksempelArbeidsgiver().getKontaktInfoTLF()));
        }

        List<EndringIRefusjon> endringIRefusjonListe = new ArrayList<>();
        if(request.getInntektsmeldingEndringIRefusjonDTOS() != null){
            request.getInntektsmeldingEndringIRefusjonDTOS().forEach(t ->
                endringIRefusjonListe.add(
                        InntektsmeldingBuilder.createEndringIRefusjon(
                                t.getEndringsdato(),
                                t.getRefusjonsbeloepPrMnd()
                        )
                )
            );
        }

        if (request.getRefusjonBeloepPrMnd() != null) {
            inntektsmeldingBuilder.setRefusjon(InntektsmeldingBuilder.createRefusjon(
                    new BigDecimal(request.getRefusjonBeloepPrMnd()),
                    request.getRefusjonOpphoersdato(),
                    endringIRefusjonListe
            ));
        }

        List<NaturalytelseDetaljer> opphorAvNaturytelseListe = new ArrayList<>();
        if (request.getOpphoerAVNaturalytelseDTOliste() != null) {
            request.getOpphoerAVNaturalytelseDTOliste().forEach(t ->
                    opphorAvNaturytelseListe.add(
                            InntektsmeldingBuilder.createNaturalytelseDetaljer(
                                    new BigDecimal(t.getBelopPrMnd()),
                                    t.getFom(),
                                    t.getNaturalytelsestype()
                            )
                    ));
            inntektsmeldingBuilder.setOpphoerAvNaturalytelsesList(opphorAvNaturytelseListe);
        }

        List<NaturalytelseDetaljer> gjenopptakelseAvNaturalytelseListe = new ArrayList<>();
        if (request.getGjenopptakelseAvNaturalytelseDTOListe() != null) {
            request.getGjenopptakelseAvNaturalytelseDTOListe().forEach(t ->
                    gjenopptakelseAvNaturalytelseListe.add(
                            InntektsmeldingBuilder.createNaturalytelseDetaljer(
                                    new BigDecimal(t.getBelopPrMnd()),
                                    t.getFom(),
                                    t.getNaturalytelsetype()
                            )
                    ));
            inntektsmeldingBuilder.setGjenopptakelseNaturalytelseList(gjenopptakelseAvNaturalytelseListe);
        }

        if(request.getInntektsmeldingSykepengerIArbeidsgiverperiodenDTO() != null){
            List<Periode> arbeidsgiverperiodeListe = new ArrayList<>();
            request.getInntektsmeldingSykepengerIArbeidsgiverperiodenDTO().getArbeidsgiverperiode().forEach(t ->
                    arbeidsgiverperiodeListe.add(InntektsmeldingBuilder.createPeriode(t.getFom(),t.getTom()))
            );

            inntektsmeldingBuilder.setSykepengerIArbeidsgiverperioden(
                    InntektsmeldingBuilder.createSykepengerIArbeidsgiverperioden(
                            new BigDecimal(request.getInntektsmeldingSykepengerIArbeidsgiverperiodenDTO().getBruttoUtbetaltSykepenger()),
                            arbeidsgiverperiodeListe,
                            request.getInntektsmeldingSykepengerIArbeidsgiverperiodenDTO().getBegrunnelseForReduksjon()
                    )
            );
        }


        List<UtsettelseAvForeldrepenger> utsettelseAvForeldrepengerListe = new ArrayList<>();
        if (request.getUtsettelseAvForeldrepengerDTOliste() != null) {
            request.getUtsettelseAvForeldrepengerDTOliste().forEach(t ->
                    utsettelseAvForeldrepengerListe.add(InntektsmeldingBuilder.createUtsettelseAvForeldrepenger(
                            t.getUtesettelseAvForeldrepenger(),
                            InntektsmeldingBuilder.createPeriode(t.getFom(), t.getTom())
                    )));
        }

        List<GraderingIForeldrepenger> graderingIForeldrepengerListe = new ArrayList<>();
        if (request.getGraderingForeldrepengerDTOliste() != null) {
            request.getGraderingForeldrepengerDTOliste().forEach(t ->
                    graderingIForeldrepengerListe.add(InntektsmeldingBuilder.createGraderingIForeldrepenger(
                            new BigDecimal(t.getArbeidstidsprosent()),
                            InntektsmeldingBuilder.createPeriode(t.getFom(), t.getTom())
                    ))
            );
        }

        List<Periode> avtaltFerieListe = new ArrayList<>();
        if(request.getInntektsmeldingArbeidsforholdAvtaltFerieDTOS() != null){
            request.getInntektsmeldingArbeidsforholdAvtaltFerieDTOS().forEach(t ->
                    avtaltFerieListe.add(InntektsmeldingBuilder.createPeriode(
                            t.getFom(),
                            t.getTom()
                    ))
            );
        }

        inntektsmeldingBuilder.setArbeidsforhold(InntektsmeldingBuilder.createArbeidsforhold(
                request.getArbeidsforholdId(),
                request.getKodelisteArsakEndring(),
                new BigDecimal(request.getBeregnetInntektBelop()),
                utsettelseAvForeldrepengerListe,
                graderingIForeldrepengerListe,
                avtaltFerieListe
        ));

        return inntektsmeldingBuilder;
    }

    public String makeInntektsmeldingFromRequestToString(InntektsmeldingDTO request){
        return makeInntektsmeldingFromRequest(request).createInntektesmeldingXML();
    }
}
