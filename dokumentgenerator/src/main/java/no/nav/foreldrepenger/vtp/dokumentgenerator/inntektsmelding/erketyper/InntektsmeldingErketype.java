package no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.builders.ArbeidsforholdBuilder;
import no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.builders.InntektsmeldingBuilder;
import no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.dto.EksempelArbeidsgiver;
import no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.dto.InntektsmeldingDTO;
import no.nav.inntektsmelding.xml.kodeliste._20180702.YtelseKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.ÅrsakInnsendingKodeliste;
import no.seres.xsd.nav.inntektsmelding_m._20181211.*;

@Deprecated
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

    public no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder makeInntektsmeldingFromRequest(InntektsmeldingDTO request) {
        no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder inntektsmeldingBuilder = new no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder(
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

        inntektsmeldingBuilder.setAvsendersystem(no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder.createAvsendersystem(
                "FS32",
                "1.0"));

        if (request.getOrganisasjonsnummer() != null) {
            inntektsmeldingBuilder.setArbeidsgiver(no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder.createArbeidsgiver(
                    request.getOrganisasjonsnummer(),
                    "41925090"
            ));
        }

        if (request.getOrganisasjonsnummer() == null && request.getEksempelArbeidsgiver() != null) {
            inntektsmeldingBuilder.setArbeidsgiver(no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder.createArbeidsgiver(
                    request.getEksempelArbeidsgiver().getVirkNummer(),
                    request.getEksempelArbeidsgiver().getKontaktInfoTLF()));
        }

        List<EndringIRefusjon> endringIRefusjonListe = new ArrayList<>();
        if(request.getInntektsmeldingEndringIRefusjonDTOS() != null){
            request.getInntektsmeldingEndringIRefusjonDTOS().forEach(t ->
                endringIRefusjonListe.add(
                        no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder.createEndringIRefusjon(
                                t.getEndringsdato(),
                                t.getRefusjonsbeloepPrMnd()
                        )
                )
            );
        }

        if (request.getRefusjonBeloepPrMnd() != null) {
            inntektsmeldingBuilder.setRefusjon(no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder.createRefusjon(
                    new BigDecimal(request.getRefusjonBeloepPrMnd()),
                    request.getRefusjonOpphoersdato(),
                    endringIRefusjonListe
            ));
        }

        List<NaturalytelseDetaljer> opphorAvNaturytelseListe = new ArrayList<>();
        if (request.getOpphoerAVNaturalytelseDTOliste() != null) {
            request.getOpphoerAVNaturalytelseDTOliste().forEach(t ->
                    opphorAvNaturytelseListe.add(
                            no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder.createNaturalytelseDetaljer(
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
                            no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder.createNaturalytelseDetaljer(
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
                    arbeidsgiverperiodeListe.add(no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder.createPeriode(t.getFom(),t.getTom()))
            );

            inntektsmeldingBuilder.setSykepengerIArbeidsgiverperioden(
                    no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder.createSykepengerIArbeidsgiverperioden(
                            new BigDecimal(request.getInntektsmeldingSykepengerIArbeidsgiverperiodenDTO().getBruttoUtbetaltSykepenger()),
                            arbeidsgiverperiodeListe,
                            request.getInntektsmeldingSykepengerIArbeidsgiverperiodenDTO().getBegrunnelseForReduksjon()
                    )
            );
        }


        List<UtsettelseAvForeldrepenger> utsettelseAvForeldrepengerListe = new ArrayList<>();
        if (request.getUtsettelseAvForeldrepengerDTOliste() != null) {
            request.getUtsettelseAvForeldrepengerDTOliste().forEach(t ->
                    utsettelseAvForeldrepengerListe.add(no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder.createUtsettelseAvForeldrepenger(
                            t.getUtesettelseAvForeldrepenger(),
                            no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder.createPeriode(t.getFom(), t.getTom())
                    )));
        }

        List<GraderingIForeldrepenger> graderingIForeldrepengerListe = new ArrayList<>();
        if (request.getGraderingForeldrepengerDTOliste() != null) {
            request.getGraderingForeldrepengerDTOliste().forEach(t ->
                    graderingIForeldrepengerListe.add(no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder.createGraderingIForeldrepenger(
                            new BigDecimal(t.getArbeidstidsprosent()),
                            no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder.createPeriode(t.getFom(), t.getTom())
                    ))
            );
        }

        List<Periode> avtaltFerieListe = new ArrayList<>();
        if(request.getInntektsmeldingArbeidsforholdAvtaltFerieDTOS() != null){
            request.getInntektsmeldingArbeidsforholdAvtaltFerieDTOS().forEach(t ->
                    avtaltFerieListe.add(no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder.createPeriode(
                            t.getFom(),
                            t.getTom()
                    ))
            );
        }

        inntektsmeldingBuilder.setArbeidsforhold(no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder.createArbeidsforhold(
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
