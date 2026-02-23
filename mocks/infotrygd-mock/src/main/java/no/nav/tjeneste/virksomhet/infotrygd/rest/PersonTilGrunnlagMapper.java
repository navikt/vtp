package no.nav.tjeneste.virksomhet.infotrygd.rest;

import java.util.List;

import no.nav.vtp.person.Person;
import no.nav.vtp.person.ytelse.Beregningsgrunnlag;
import no.nav.vtp.person.ytelse.Ytelse;
import no.nav.vtp.person.ytelse.YtelseType;

public class PersonTilGrunnlagMapper {

    private PersonTilGrunnlagMapper() {
    }

    public static List<GrunnlagDto> tilSykepengerGrunnlag(Person person) {
        if (person == null || person.ytelser() == null) {
            return List.of();
        }
        return person.ytelser().stream()
                .filter(ytelse -> ytelse.ytelse() == YtelseType.SYKEPENGER)
                .map(ytelse -> tilGrunnlag(TemaKode.SP, ytelse))
                .toList();
    }

    public static List<GrunnlagDto> tilBarnsykdomGrunnlag(Person person) {
        if (person == null || person.ytelser() == null) {
            return List.of();
        }
        return person.ytelser().stream()
                .filter(ytelse -> erBarnsykdom(ytelse.ytelse()))
                .map(ytelse -> tilGrunnlag(TemaKode.BS, ytelse))
                .toList();
    }

    private static boolean erBarnsykdom(YtelseType ytelseType) {
        return ytelseType == YtelseType.PLEIEPENGER
                || ytelseType == YtelseType.OMSORGSPENGER
                || ytelseType == YtelseType.OPPLÆRINGSPENGER;
    }

    private static GrunnlagDto tilGrunnlag(TemaKode temakode, Ytelse ytelse) {
        return new GrunnlagDto(
                new GrunnlagDto.Status(StatusKode.L, "status"),
                new GrunnlagDto.Tema(temakode, "tema"),
                100,
                null, // TODO: Trenger FP denne?
                tilArbeidskategori(ytelse.beregningsgrunnlag()),
                List.of(), // TODO: Trenger K9 denne? Utvid ytelse.s eventuelt
                new GrunnlagDto.Periode(ytelse.fom(), ytelse.tom()),
                new GrunnlagDto.Behandlingstema(tilBehandlingstemaKode(temakode), "behandlingstema"),
                ytelse.fom(),
                ytelse.fom(),
                ytelse.tom(),
                100,
                null,
                null,
                "ABC",
                tilVedtak(ytelse)
        );
    }

    private static GrunnlagDto.Arbeidskategori tilArbeidskategori(List<Beregningsgrunnlag> beregningsgrunnlag) {
        if (beregningsgrunnlag == null || beregningsgrunnlag.isEmpty()) {
            return new GrunnlagDto.Arbeidskategori(ArbeidskategoriKode.K01, "kategori");
        }
        return beregningsgrunnlag.stream().findFirst()
                .map(bg -> new GrunnlagDto.Arbeidskategori(tilArbeidskategori(bg.kategori()), "kategori"))
                .orElseThrow();
    }

    private static ArbeidskategoriKode tilArbeidskategori(Beregningsgrunnlag.Kategori kategori) {
        return switch (kategori) {
            case ARBEIDSTAKER -> ArbeidskategoriKode.K01;
            case DAGPENGER -> ArbeidskategoriKode.K06;
            default ->  throw new IllegalArgumentException("Ukjent : " + kategori);
        };
    }

    private static List<GrunnlagDto.Vedtak> tilVedtak(Ytelse ytelse) {
        if (ytelse.fom() == null || ytelse.tom() == null) {
            return List.of();
        }
        return List.of(new GrunnlagDto.Vedtak(
                new GrunnlagDto.Periode(ytelse.fom(), ytelse.tom()),
                ytelse.utbetalt() != null ? ytelse.utbetalt() : 100,
                null,
                null,
                ytelse.dagsats()
        ));
    }

    private static BehandlingstemaKode tilBehandlingstemaKode(TemaKode temakode) {
        return switch (temakode) {
            case FA -> BehandlingstemaKode.FP;
            case SP -> BehandlingstemaKode.SP;
            case BS, UKJENT -> BehandlingstemaKode.UKJENT;
        };
    }
}

