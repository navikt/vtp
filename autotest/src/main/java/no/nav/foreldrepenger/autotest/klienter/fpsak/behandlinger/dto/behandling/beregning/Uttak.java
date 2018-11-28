package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning;

public class Uttak {

    protected int trekkdager;
    protected String stonadskontoType;
    protected String periodeResultatType;
    protected boolean gradering;

    public int getTrekkdager() {
        return trekkdager;
    }

    public String getStonadskontoType() {
        return stonadskontoType;
    }

    public String getPeriodeResultatType() {
        return periodeResultatType;
    }

    public boolean isGradering() {
        return gradering;
    }

}
