package no.nav.okonomi.tilbakekrevingservice;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.google.common.collect.Lists;

import no.nav.tilbakekreving.kravgrunnlag.detalj.v1.DetaljertKravgrunnlagBelopDto;
import no.nav.tilbakekreving.kravgrunnlag.detalj.v1.DetaljertKravgrunnlagDto;
import no.nav.tilbakekreving.kravgrunnlag.detalj.v1.DetaljertKravgrunnlagPeriodeDto;
import no.nav.tilbakekreving.kravgrunnlag.detalj.v1.HentKravgrunnlagDetaljDto;
import no.nav.tilbakekreving.typer.v1.PeriodeDto;
import no.nav.tilbakekreving.typer.v1.TypeGjelderDto;
import no.nav.tilbakekreving.typer.v1.TypeKlasseDto;

class KravgrunnlagGenerator {

    public static DetaljertKravgrunnlagDto hentGrunnlag(HentKravgrunnlagDetaljDto hentKravgrunnlagDetaljDto) {
        DetaljertKravgrunnlagDto detaljertKravgrunnlag = new DetaljertKravgrunnlagDto();
        detaljertKravgrunnlag.setVedtakId(BigInteger.valueOf(207406));
        detaljertKravgrunnlag.setKravgrunnlagId(hentKravgrunnlagDetaljDto.getKravgrunnlagId());
        detaljertKravgrunnlag.setEnhetAnsvarlig(hentKravgrunnlagDetaljDto.getEnhetAnsvarlig());
        detaljertKravgrunnlag.setFagsystemId(TilbakekrevingKonsistensTjeneste.getSisteSaksnummer() + "100");
        detaljertKravgrunnlag.setKodeFagomraade("FP");
        detaljertKravgrunnlag.setKontrollfelt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.ss.SSSSSS")));
        detaljertKravgrunnlag.setReferanse(TilbakekrevingKonsistensTjeneste.getSisteHenvisning());
        detaljertKravgrunnlag.setSaksbehId(hentKravgrunnlagDetaljDto.getSaksbehId());
        detaljertKravgrunnlag.setVedtakGjelderId("910909088"); //mock verdi
        detaljertKravgrunnlag.setUtbetalesTilId("910909088"); //mock verdi
        detaljertKravgrunnlag.setEnhetBehandl(hentKravgrunnlagDetaljDto.getEnhetAnsvarlig());
        detaljertKravgrunnlag.setEnhetBosted(hentKravgrunnlagDetaljDto.getEnhetAnsvarlig());
        detaljertKravgrunnlag.setKodeStatusKrav("NY");
        detaljertKravgrunnlag.setTypeGjelderId(TypeGjelderDto.ORGANISASJON);
        detaljertKravgrunnlag.setTypeUtbetId(TypeGjelderDto.ORGANISASJON);
        detaljertKravgrunnlag.getTilbakekrevingsPeriode().addAll(hentPerioder());

        return detaljertKravgrunnlag;
    }

    private static List<DetaljertKravgrunnlagPeriodeDto> hentPerioder() {
        DetaljertKravgrunnlagPeriodeDto kravgrunnlagPeriode1 = new DetaljertKravgrunnlagPeriodeDto();
        PeriodeDto periode = new PeriodeDto();
        periode.setFom(konvertDato(LocalDate.of(2020, 3, 1)));
        periode.setTom(konvertDato(LocalDate.of(2020, 3, 31)));
        kravgrunnlagPeriode1.setPeriode(periode);
        kravgrunnlagPeriode1.getTilbakekrevingsBelop().add(hentBeløp(BigDecimal.valueOf(6000.00), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, TypeKlasseDto.FEIL, 30));
        kravgrunnlagPeriode1.getTilbakekrevingsBelop().add(hentBeløp(BigDecimal.ZERO, BigDecimal.valueOf(6000.00), BigDecimal.valueOf(6000.00), BigDecimal.ZERO, TypeKlasseDto.YTEL, 30));
        kravgrunnlagPeriode1.setBelopSkattMnd(new BigDecimal(2700));

        DetaljertKravgrunnlagPeriodeDto kravgrunnlagPeriode2 = new DetaljertKravgrunnlagPeriodeDto();
        periode = new PeriodeDto();
        periode.setFom(konvertDato(LocalDate.of(2020, 4, 1)));
        periode.setTom(konvertDato(LocalDate.of(2020, 4, 30)));
        kravgrunnlagPeriode2.setPeriode(periode);
        kravgrunnlagPeriode2.getTilbakekrevingsBelop().add(hentBeløp(BigDecimal.valueOf(21000.00), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, TypeKlasseDto.FEIL, 30));
        kravgrunnlagPeriode2.getTilbakekrevingsBelop().add(hentBeløp(BigDecimal.ZERO, BigDecimal.valueOf(21000.00), BigDecimal.valueOf(21000.00), BigDecimal.ZERO, TypeKlasseDto.YTEL, 30));
        kravgrunnlagPeriode2.setBelopSkattMnd(new BigDecimal(6300));


        return Lists.newArrayList(kravgrunnlagPeriode1, kravgrunnlagPeriode2);
    }

    private static DetaljertKravgrunnlagBelopDto hentBeløp(BigDecimal nyBeløp,
                                                           BigDecimal tilbakekrevesBeløp,
                                                           BigDecimal opprUtbetBeløp,
                                                           BigDecimal uInnkrevdBeløp,
                                                           TypeKlasseDto typeKlasse,
                                                           int skatteprosent) {
        DetaljertKravgrunnlagBelopDto detaljertKravgrunnlagBelop = new DetaljertKravgrunnlagBelopDto();
        detaljertKravgrunnlagBelop.setTypeKlasse(typeKlasse);
        detaljertKravgrunnlagBelop.setBelopNy(nyBeløp);
        detaljertKravgrunnlagBelop.setBelopOpprUtbet(opprUtbetBeløp);
        detaljertKravgrunnlagBelop.setBelopTilbakekreves(tilbakekrevesBeløp);
        detaljertKravgrunnlagBelop.setBelopUinnkrevd(uInnkrevdBeløp);
        detaljertKravgrunnlagBelop.setKodeKlasse("FPATAL");
        detaljertKravgrunnlagBelop.setSkattProsent(BigDecimal.valueOf(skatteprosent));

        return detaljertKravgrunnlagBelop;
    }

    private static XMLGregorianCalendar konvertDato(LocalDate localDate) {
        return DatatypeFactory.newDefaultInstance().newXMLGregorianCalendar(localDate.toString());
    }
}
