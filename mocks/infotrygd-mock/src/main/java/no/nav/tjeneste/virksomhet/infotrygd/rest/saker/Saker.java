package no.nav.tjeneste.virksomhet.infotrygd.rest.saker;

import java.util.List;

public record Saker(String info,
                    List<Sak>saker,
                    List<LøpendeSak> løpendeSaker,
                    AvsluttedeSaker avsluttedeSaker,
                    List<IkkeStartetSak> ikkeStartedeSaker) {
    public Saker() {
        this(null, List.of(), List.of(), new AvsluttedeSaker(), List.of());
    }
}
