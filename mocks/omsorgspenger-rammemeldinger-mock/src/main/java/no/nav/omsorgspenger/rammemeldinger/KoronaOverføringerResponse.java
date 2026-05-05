package no.nav.omsorgspenger.rammemeldinger;

import java.util.List;

public record KoronaOverføringerResponse(List<String> gitt, List<String> fått) {
}
