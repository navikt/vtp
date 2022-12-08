package no.nav.foreldrepenger.fpwsproxy.tilbakekreving;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.google.common.collect.Lists;

import no.nav.foreldrepenger.kontrakter.tilbakekreving.kravgrunnlag.request.HentKravgrunnlagDetaljDto;
import no.nav.foreldrepenger.kontrakter.tilbakekreving.kravgrunnlag.respons.FagOmrådeKode;
import no.nav.foreldrepenger.kontrakter.tilbakekreving.kravgrunnlag.respons.GjelderType;
import no.nav.foreldrepenger.kontrakter.tilbakekreving.kravgrunnlag.respons.KlasseType;
import no.nav.foreldrepenger.kontrakter.tilbakekreving.kravgrunnlag.respons.KravStatusKode;
import no.nav.foreldrepenger.kontrakter.tilbakekreving.kravgrunnlag.respons.Kravgrunnlag431Dto;
import no.nav.foreldrepenger.kontrakter.tilbakekreving.kravgrunnlag.respons.KravgrunnlagBelop433Dto;
import no.nav.foreldrepenger.kontrakter.tilbakekreving.kravgrunnlag.respons.KravgrunnlagPeriode432Dto;
import no.nav.foreldrepenger.kontrakter.tilbakekreving.kravgrunnlag.respons.Periode;

class Kravgrunnlag431DtoMapper {


    public static Kravgrunnlag431Dto lagGeneriskKravgrunnlag(HentKravgrunnlagDetaljDto hentKravgrunnlagDetaljDto) {
        return new Kravgrunnlag431Dto.Builder()
                .vedtakId(207406L)
                .eksternKravgrunnlagId(String.valueOf(hentKravgrunnlagDetaljDto.kravgrunnlagId()))
                .ansvarligEnhet(hentKravgrunnlagDetaljDto.enhetAnsvarlig())
                .fagSystemId(TilbakekrevingKonsistensTjeneste.getSisteSaksnummer() + "100")
                .fagOmrådeKode(FagOmrådeKode.FP)
                .kontrollFelt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.ss.SSSSSS")))
                .referanse(TilbakekrevingKonsistensTjeneste.getSisteHenvisning())
                .gjelderVedtakId("910909088")
                .utbetalesTilId("910909088")
                .behandlendeEnhet(hentKravgrunnlagDetaljDto.enhetAnsvarlig())
                .bostedEnhet(hentKravgrunnlagDetaljDto.enhetAnsvarlig())
                .kravStatusKode(KravStatusKode.NY)
                .gjelderType(GjelderType.ORGANISASJON)
                .utbetGjelderType(GjelderType.ORGANISASJON)
                .perioder(hentPerioder())
                .build();
    }

    private static List<KravgrunnlagPeriode432Dto> hentPerioder() {
        var kravgrunnlagPeriode1 = new KravgrunnlagPeriode432Dto.Builder()
                .periode(new Periode(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31)))
                .beløpSkattMnd(BigDecimal.valueOf(2700))
                .kravgrunnlagBeloper433(List.of(
                        hentBeløp(BigDecimal.valueOf(6000.00), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, KlasseType.FEIL, 30),
                        hentBeløp(BigDecimal.ZERO, BigDecimal.valueOf(6000.00), BigDecimal.valueOf(6000.00), BigDecimal.ZERO, KlasseType.YTEL, 30)
                ))
                .build();
        var kravgrunnlagPeriode2 = new KravgrunnlagPeriode432Dto.Builder()
                .periode(new Periode(LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 30)))
                .beløpSkattMnd(BigDecimal.valueOf(6300))
                .kravgrunnlagBeloper433(List.of(
                        hentBeløp(BigDecimal.valueOf(21000.00), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, KlasseType.FEIL, 30),
                        hentBeløp(BigDecimal.ZERO, BigDecimal.valueOf(21000.00), BigDecimal.valueOf(21000.00), BigDecimal.ZERO, KlasseType.YTEL, 30)
                ))
                .build();
        return Lists.newArrayList(kravgrunnlagPeriode1, kravgrunnlagPeriode2);
    }

    private static KravgrunnlagBelop433Dto hentBeløp(BigDecimal nyBeløp,
                                                     BigDecimal tilbakekrevesBeløp,
                                                     BigDecimal opprUtbetBeløp,
                                                     BigDecimal uInnkrevdBeløp,
                                                     KlasseType klassetype,
                                                     int skatteprosent) {
        return new KravgrunnlagBelop433Dto.Builder()
                .klasseType(klassetype)
                .nyBelop(nyBeløp)
                .opprUtbetBelop(opprUtbetBeløp)
                .tilbakekrevesBelop(tilbakekrevesBeløp)
                .uinnkrevdBelop(uInnkrevdBeløp)
                .klasseKode("FPATAL")
                .skattProsent(BigDecimal.valueOf(skatteprosent))
                .build();
    }
}
