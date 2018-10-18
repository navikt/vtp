package no.nav.foreldrepenger.autotest.klienter.fpsak.brev.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BestillBrev {

    protected int behandlingId;
    protected String mottaker;
    protected String brevmalkode;
    protected String fritekst;
    protected String arsakskode;
    protected String årsakskode;
    
    public BestillBrev(int behandlingId, String mottaker, String brevmalkode, String fritekst) {
        super();
        this.behandlingId = behandlingId;
        this.mottaker = mottaker;
        this.brevmalkode = brevmalkode;
        this.fritekst = fritekst;
    }
}
