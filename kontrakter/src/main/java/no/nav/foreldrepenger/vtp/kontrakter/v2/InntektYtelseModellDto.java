package no.nav.foreldrepenger.vtp.kontrakter.v2;

public record InntektYtelseModellDto(
        ArenaDto arena,
        InfotrygdDto infotrygd,
        InntektkomponentDto inntektskomponent,
        AaregDto aareg,
        SigrunDto sigrun,
        PesysDto pesys) {

    private InntektYtelseModellDto(Builder b) {
        this(b.arena, b.infotrygd, b.inntektskomponent, b.aareg, b.sigrun, b.pesys);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ArenaDto arena;
        private InfotrygdDto infotrygd;
        private InntektkomponentDto inntektskomponent;
        private AaregDto aareg;
        private SigrunDto sigrun;
        private PesysDto pesys;

        Builder() {
        }

        public ArenaDto arena() {
            return arena;
        }

        public Builder arena(ArenaDto arena) {
            this.arena = arena;
            return this;
        }

        public InfotrygdDto infotrygd() {
            return infotrygd;
        }

        public Builder infotrygd(InfotrygdDto trex) {
            this.infotrygd = trex;
            return this;
        }

        public InntektkomponentDto inntektskomponent() {
            return inntektskomponent;
        }

        public Builder inntektskomponent(InntektkomponentDto inntektskomponent) {
            this.inntektskomponent = inntektskomponent;
            return this;
        }

        public AaregDto aareg() {
            return aareg;
        }

        public Builder aareg(AaregDto aareg) {
            this.aareg = aareg;
            return this;
        }

        public SigrunDto sigrun() {
            return sigrun;
        }

        public Builder sigrun(SigrunDto sigrun) {
            this.sigrun = sigrun;
            return this;
        }

        public PesysDto pesys() {
            return pesys;
        }

        public Builder pesys(PesysDto pesys) {
            this.pesys = pesys;
            return this;
        }

        public InntektYtelseModellDto build() {
            return new InntektYtelseModellDto(this);
        }
    }

}
