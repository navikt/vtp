package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AktørId {

    public AktørId(String aktørId) {
        this.aktørId = aktørId;
    }

    protected String aktørId;
    protected String indexKey;

    public String getAktørId() {
        return aktørId;
    }

    public String getIndexKey() {
        return indexKey;
    }
}
