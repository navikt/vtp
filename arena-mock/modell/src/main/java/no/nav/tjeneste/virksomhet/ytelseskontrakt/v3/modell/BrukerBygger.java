package no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.modell;


import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.Bruker;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.Rettighetsgruppe;

public class BrukerBygger {

    protected String rettighetsGruppe;

    public BrukerBygger(ArenaSvar arenaSvar) {
        this.rettighetsGruppe = arenaSvar.getRettighetsGruppe();
    }

    public Bruker byggBruker() {

        Bruker bruker = new Bruker();
        Rettighetsgruppe r = new Rettighetsgruppe();
        r.setRettighetsGruppe(rettighetsGruppe);
        bruker.setRettighetsgruppe(r);

        return bruker;
    }

}
