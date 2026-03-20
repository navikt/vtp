package no.nav.foreldrepenger.vtp.kontrakter.person.skatt;

public record SkatteopplysningDto(Integer år, Integer beløp) {

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(SkatteopplysningDto existing) {
        return new Builder(existing);
    }

    public static class Builder {
        private Integer år;
        private Integer beløp;

        public Builder() {}

        public Builder(SkatteopplysningDto existing) {
            this.år = existing.år();
            this.beløp = existing.beløp();
        }

        public Builder medÅr(Integer år) { this.år = år; return this; }
        public Builder medBeløp(Integer beløp) { this.beløp = beløp; return this; }

        public SkatteopplysningDto build() {
            return new SkatteopplysningDto(år, beløp);
        }
    }
}

