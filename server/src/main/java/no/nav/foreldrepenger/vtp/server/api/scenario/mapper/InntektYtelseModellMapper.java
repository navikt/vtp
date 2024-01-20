package no.nav.foreldrepenger.vtp.server.api.scenario.mapper;

import static no.nav.foreldrepenger.fpwsproxy.UtilKlasse.safeStream;
import static no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun.SigrunModell.SIGRUN_OPPFØRING_SKATTEOPPGJØR;
import static no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun.SigrunModell.SIGRUN_OPPFØRING_TEKNISK_NAVN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import no.nav.foreldrepenger.vtp.kontrakter.v2.AaregDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.ArbeidsavtaleDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.ArbeidsforholdDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.Arbeidsforholdstype;
import no.nav.foreldrepenger.vtp.kontrakter.v2.Arbeidsgiver;
import no.nav.foreldrepenger.vtp.kontrakter.v2.ArenaDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.ArenaSakerDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.ArenaVedtakDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.GrunnlagDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.InfotrygdDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.InntektYtelseModellDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.InntektkomponentDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.InntektsperiodeDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.OrganisasjonDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.PesysDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.PrivatArbeidsgiver;
import no.nav.foreldrepenger.vtp.kontrakter.v2.SigrunDto;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.PesysModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsavtale;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.ArbeidsforholdModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Avlønningstype;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.ArenaMeldekort;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.ArenaModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.ArenaSak;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.ArenaVedtak;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.RelatertYtelseTema;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.SakStatus;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.VedtakStatus;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektFordel;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektType;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.Inntektsperiode;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun.Inntektsår;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun.Oppføring;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun.SigrunModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Arbeidskategori;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.ArbeidskategoriKode;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Behandlingstema;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.BehandlingstemaKode;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Grunnlag;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Periode;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Prosent;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Status;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.StatusKode;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.TRexModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Tema;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.TemaKode;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Vedtak;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonArbeidsgiver;

public class InntektYtelseModellMapper {

    private static final String STATUS_TERMNAVN = "status";

    private InntektYtelseModellMapper() {
        // Bare statiske metoder
    }

    private static final String DUMMY_SAKSNUMMER = "999";
    private static final String INNTEKTPERIODE_BESKRIVELSE = "fastloenn";
    private static final Arbeidskategori GRUNNLAG_KATEGORI = new Arbeidskategori(ArbeidskategoriKode.K01, "kategori");

    public static InntektYtelseModell tilInntektytelseModell(InntektYtelseModellDto i, Map<UUID, BrukerModell> allePersoner) {
        if (i == null) {
            return null;
        }
        ArenaModell arena = tilArena(i.arena());
        TRexModell trex = tilTrex(i.infotrygd());
        InntektskomponentModell inntektskomponenten = tilInntektkomponenten(i.inntektskomponent(), allePersoner);
        ArbeidsforholdModell arbeidsforhold = tilArbeidsforholdModell(i.aareg(), allePersoner);
        SigrunModell sigrun = tilSigrun(i.sigrun());
        PesysModell pesysModell = tilPesys(i.pesys());
        return new InntektYtelseModell(arena, null, trex, inntektskomponenten, arbeidsforhold, sigrun, null, pesysModell);
    }


    private static PesysModell tilPesys(PesysDto pesys) {
        if (pesys == null) {
            return new PesysModell();
        }
        return new PesysModell(pesys.harUføretrygd());

    }

    private static SigrunModell tilSigrun(SigrunDto sigrun) {
        if (sigrun == null) {
            return new SigrunModell();
        }
        return new SigrunModell(tilInntektår(sigrun.inntektår()));
    }

    private static List<Inntektsår> tilInntektår(List<SigrunDto.InntektsårDto> inntektår) {
        return safeStream(inntektår)
                .map(InntektYtelseModellMapper::tilInntektår)
                .toList();
    }

    private static Inntektsår tilInntektår(SigrunDto.InntektsårDto inntektår) {
        return new Inntektsår(inntektår.år().toString(), List.of(new Oppføring(SIGRUN_OPPFØRING_TEKNISK_NAVN, inntektår.beløp().toString()),
                new Oppføring(SIGRUN_OPPFØRING_SKATTEOPPGJØR, LocalDate.of(inntektår.år(), 1, 1).toString())));
    }

    private static ArbeidsforholdModell tilArbeidsforholdModell(AaregDto aareg, Map<UUID, BrukerModell> allePersoner) {
        if (aareg == null) {
            return new ArbeidsforholdModell();
        }
        return new ArbeidsforholdModell(tilArbeidsforhold(aareg.arbeidsforhold(), allePersoner));
    }

    private static List<Arbeidsforhold> tilArbeidsforhold(List<ArbeidsforholdDto> arbeidsforhold, Map<UUID, BrukerModell> allePersoner) {
        return safeStream(arbeidsforhold)
                .map(a -> tilArbeidsforhold(a, allePersoner))
                .toList();
    }

    private static Arbeidsforhold tilArbeidsforhold(ArbeidsforholdDto a, Map<UUID, BrukerModell> allePersoner) {
        return new Arbeidsforhold(
                tilArbeidsavtaler(a.arbeidsavtaler()),
                null,
                a.arbeidsforholdId(),
                null,
                a.ansettelsesperiodeTom(),
                a.ansettelsesperiodeFom(),
                tilArbeidsforholdType(a.arbeidsforholdstype()),
                null,
                tilOrgnummer(a.arbeidsgiver()),
                null,
                null,
                tilPrivatArbeidgiver(a.arbeidsgiver(), allePersoner)
        );
    }

    private static no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsforholdstype tilArbeidsforholdType(
            Arbeidsforholdstype arbeidsforholdstype) {
        return switch (arbeidsforholdstype) {
            case ORDINÆRT_ARBEIDSFORHOLD -> no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsforholdstype.ORDINÆRT_ARBEIDSFORHOLD;
            case MARITIMT_ARBEIDSFORHOLD -> no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsforholdstype.MARITIMT_ARBEIDSFORHOLD;
            case FRILANSER_OPPDRAGSTAKER_MED_MER -> no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsforholdstype.FRILANSER_OPPDRAGSTAKER_MED_MER;
            case FORENKLET_OPPGJØRSORDNING -> no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsforholdstype.FORENKLET_OPPGJØRSORDNING;
        };
    }

    private static List<Arbeidsavtale> tilArbeidsavtaler(List<ArbeidsavtaleDto> arbeidsavtaler) {
        return safeStream(arbeidsavtaler)
                .map(InntektYtelseModellMapper::tilArbeidsavtale)
                .toList();
    }

    private static Arbeidsavtale tilArbeidsavtale(ArbeidsavtaleDto a) {
        return new Arbeidsavtale(
                null,
                Avlønningstype.FASTLØNN,
                a.avtaltArbeidstimerPerUke(),
                a.stillingsprosent(),
                a.beregnetAntallTimerPerUke(),
                a.sisteLønnsendringsdato(),
                a.fomGyldighetsperiode(),
                a.tomGyldighetsperiode()
        );
    }

    private static InntektskomponentModell tilInntektkomponenten(InntektkomponentDto inntektskomponent, Map<UUID, BrukerModell> allePersoner) {
        if (inntektskomponent == null) {
            return new InntektskomponentModell();
        }
        return new InntektskomponentModell(tilInntektsperioder(inntektskomponent.inntektsperioder(), allePersoner), List.of());
    }

    private static List<Inntektsperiode> tilInntektsperioder(List<InntektsperiodeDto> inntektsperioder, Map<UUID, BrukerModell> allePersoner) {
        return safeStream(inntektsperioder)
                .map(i -> tilInntektsperiode(i, allePersoner))
                .toList();
    }

    private static Inntektsperiode tilInntektsperiode(InntektsperiodeDto i, Map<UUID, BrukerModell> allePersoner) {
        return new Inntektsperiode(i.fom(), i.tom(), null, i.beløp(), tilOrgnummer(i.arbeidsgiver()), tilInntektType(i),
                tilInntektFordel(i.inntektFordel()), tilBeskrivelse(i), null, null,
                null,
                tilPrivatArbeidgiver(i.arbeidsgiver(), allePersoner));
    }

    private static String tilOrgnummer(Arbeidsgiver arbeidsgiver) {
        if (arbeidsgiver instanceof OrganisasjonDto org) {
            return org.orgnummer().value();
        }
        return null;
    }

    private static PersonArbeidsgiver tilPrivatArbeidgiver(Arbeidsgiver arbeidsgiver, Map<UUID, BrukerModell> allePersoner) {
        if (arbeidsgiver instanceof PrivatArbeidsgiver privatArbeidsgiver) {
            return (PersonArbeidsgiver) allePersoner.get(privatArbeidsgiver.uuid());
        }
        return null;
    }

    private static InntektFordel tilInntektFordel(InntektsperiodeDto.InntektFordelDto inntektFordelDto) {
        return switch (inntektFordelDto) {
            case KONTANTYTELSE -> InntektFordel.KONTANTYTELSE;
            case UTGIFTSGODTGJØRELSE -> InntektFordel.UTGIFTSGODTGJØRELSE;
            case NATURALYTELSE -> InntektFordel.NATURALYTELSE;
        };
    }

    private static InntektType tilInntektType(InntektsperiodeDto inntektsperiodeDto) {
        if (inntektsperiodeDto.inntektYtelseType() != null) {
            return switch (inntektsperiodeDto.inntektYtelseType().getInntektType()) {
                case LØNNSINNTEKT -> InntektType.LØNNSINNTEKT;
                case NÆRINGSINNTEKT -> InntektType.NÆRINGSINNTEKT;
                case PENSJON_ELLER_TRYGD -> InntektType.PENSJON_ELLER_TRYGD;
                case YTELSE_FRA_OFFENTLIGE -> InntektType.YTELSE_FRA_OFFENTLIGE;
            };
        } else if (inntektsperiodeDto.inntektType() != null) {
            return switch (inntektsperiodeDto.inntektType()) {
                case LØNNSINNTEKT -> InntektType.LØNNSINNTEKT;
                case NÆRINGSINNTEKT -> InntektType.NÆRINGSINNTEKT;
                case PENSJON_ELLER_TRYGD -> InntektType.PENSJON_ELLER_TRYGD;
                case YTELSE_FRA_OFFENTLIGE -> InntektType.YTELSE_FRA_OFFENTLIGE;
            };
        } else {
            return InntektType.LØNNSINNTEKT;
        }
    }

    private static String tilBeskrivelse(InntektsperiodeDto inntektsperiodeDto) {
        if (inntektsperiodeDto.inntektYtelseType() != null) {
            return inntektsperiodeDto.inntektYtelseType().getBeskrivelse();
        } else {
            return INNTEKTPERIODE_BESKRIVELSE;
        }
    }

    private static TRexModell tilTrex(InfotrygdDto infotrygd) {
        if (infotrygd == null) {
            return new TRexModell();
        }
        return new TRexModell(tilSykepengerGrunnlag(infotrygd.ytelser()), tilBarnsykdomGrunnlag(infotrygd.ytelser()));
    }

    private static List<Grunnlag> tilBarnsykdomGrunnlag(List<GrunnlagDto> ytelser) {
        return safeStream(ytelser)
                .filter(grunnlag -> grunnlag.ytelse().equals(GrunnlagDto.Ytelse.BS))
                .map(g -> tilGrunnlag(TemaKode.BS, g))
                .toList();
    }

    private static List<Grunnlag> tilSykepengerGrunnlag(List<GrunnlagDto> ytelser) {
        return safeStream(ytelser)
                .filter(grunnlag -> grunnlag.ytelse().equals(GrunnlagDto.Ytelse.SP))
                .map(g -> tilGrunnlag(TemaKode.SP, g))
                .toList();
    }

    private static Grunnlag tilGrunnlag(TemaKode temakode, GrunnlagDto g) {
        return new Grunnlag(
                tilGrunnlagStatus(g.status()),
                new Tema(temakode, "tema"),
                new Prosent(100),
                g.fødselsdatoBarn(),
                GRUNNLAG_KATEGORI,
                List.of(),
                new Periode(g.fom(), g.tom()),
                new Behandlingstema(tilBehandlingstemaKode(temakode), "behandlingstema"),
                g.fom(),
                g.fom(),
                g.tom(),
                100, // que pasa
                null,
                null,
                "ABC",
                tilGrunnlagVedtak(g.vedtak())
        );
    }

    private static List<Vedtak> tilGrunnlagVedtak(List<GrunnlagDto.Vedtak> vedtak) {
        return safeStream(vedtak)
                .map(InntektYtelseModellMapper::tilGrunnlagVedtak)
                .toList();
    }

    private static Vedtak tilGrunnlagVedtak(GrunnlagDto.Vedtak vedtak) {
        return new Vedtak(new Periode(vedtak.fom(), vedtak.tom()), vedtak.utbetalingsgrad(), null, null, null);
    }

    private static BehandlingstemaKode tilBehandlingstemaKode(TemaKode temakode) {
        return switch (temakode) {
            case FA -> BehandlingstemaKode.FP;
            case SP -> BehandlingstemaKode.SP;
            case BS, UKJENT -> BehandlingstemaKode.UKJENT;
        };
    }

    private static Status tilGrunnlagStatus(GrunnlagDto.Status status) {
        return switch (status) {
            case LØPENDE -> new Status(StatusKode.L, STATUS_TERMNAVN);
            case AVSLUTTET -> new Status(StatusKode.A, STATUS_TERMNAVN);
            case I -> new Status(StatusKode.I, STATUS_TERMNAVN);
        };
    }


    private static ArenaModell tilArena(ArenaDto arena) {
        if (arena == null) {
            return new ArenaModell();
        }
        return new ArenaModell(null, tilArenaSaker(arena.saker()));
    }

    private static List<ArenaSak> tilArenaSaker(List<ArenaSakerDto> saker) {
        return safeStream(saker)
                .map(InntektYtelseModellMapper::tilArenaSak)
                .toList();

    }

    private static ArenaSak tilArenaSak(ArenaSakerDto sak) {
        return new ArenaSak(DUMMY_SAKSNUMMER, tilRelatertYtelseTema(sak.tema()), tilSakStatus(sak.status()), tilArenaVedtak(sak.vedtak()));
    }

    private static List<ArenaVedtak> tilArenaVedtak(List<ArenaVedtakDto> vedtak) {
        return safeStream(vedtak)
                .map(InntektYtelseModellMapper::tilArenaVedtak)
                .toList();
    }

    private static ArenaVedtak tilArenaVedtak(ArenaVedtakDto vedtak) {
        return new ArenaVedtak(vedtak.fom(), vedtak.fom(), vedtak.tom(), vedtak.fom(), tilVedtakStatus(vedtak.status()),
                BigDecimal.valueOf(vedtak.dagsats()), tilMeldekort(vedtak.meldekort()));

    }

    private static List<ArenaMeldekort> tilMeldekort(List<no.nav.foreldrepenger.vtp.kontrakter.v2.ArenaMeldekort> meldekort) {
        return safeStream(meldekort)
                .map(InntektYtelseModellMapper::tilMeldekort)
                .toList();
    }

    private static ArenaMeldekort tilMeldekort(no.nav.foreldrepenger.vtp.kontrakter.v2.ArenaMeldekort m) {
        return new ArenaMeldekort(m.fom(), m.tom(), BigDecimal.valueOf(m.dagsats()), BigDecimal.valueOf(m.beløp()), BigDecimal.valueOf(m.utbetalingsgrad()));
    }

    private static VedtakStatus tilVedtakStatus(ArenaVedtakDto.VedtakStatus status) {
        return switch (status) {
            case IVERK -> VedtakStatus.IVERK;
            case OPPRE -> VedtakStatus.OPPRE;
            case INNST -> VedtakStatus.INNST;
            case REGIS -> VedtakStatus.REGIS;
            case MOTAT -> VedtakStatus.MOTAT;
            case GODKJ -> VedtakStatus.GODKJ;
            case AVSLU -> VedtakStatus.AVSLU;
            case INAKT -> VedtakStatus.INAKT;
            case AKTIV -> VedtakStatus.AKTIV;
        };
    }

    private static SakStatus tilSakStatus(ArenaSakerDto.SakStatus status) {
        return switch (status) {
            case AKTIV -> SakStatus.AKTIV;
            case INAKT -> SakStatus.INAKT;
            case AVSLU -> SakStatus.AVSLU;
        };
    }

    private static RelatertYtelseTema tilRelatertYtelseTema(ArenaSakerDto.YtelseTema tema) {
        return switch (tema) {
            case AAP -> RelatertYtelseTema.AAP;
            case DAG -> RelatertYtelseTema.DAG;
            case FA -> RelatertYtelseTema.FA;
            case EF -> RelatertYtelseTema.EF;
            case SP -> RelatertYtelseTema.SP;
            case BS -> RelatertYtelseTema.BS;
        };
    }
}
