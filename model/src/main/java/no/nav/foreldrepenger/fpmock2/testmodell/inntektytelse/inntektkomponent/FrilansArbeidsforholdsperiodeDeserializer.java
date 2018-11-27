package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.fpmock2.testmodell.util.JsonMapper;

public class FrilansArbeidsforholdsperiodeDeserializer extends JsonDeserializer {


    @Override
    public List<FrilansArbeidsforholdsperiode> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        List<FrilansArbeidsforholdsperiode> frilansArbeidsforholdsperioder = new ArrayList<>();
        JsonMapper jm = new JsonMapper();
        ObjectMapper objectMapper = jm.lagObjectMapper();

        for (JsonNode nodeElement : node) {
            FrilansArbeidsforholdsperiode frilansArbeidsforholdsperiode = objectMapper.treeToValue(nodeElement, FrilansArbeidsforholdsperiode.class);
            frilansArbeidsforholdsperioder.addAll(splittFrilansArbeidsforholdTilMånedligeIntervall(frilansArbeidsforholdsperiode));
        }
        return frilansArbeidsforholdsperioder;
    }

    private static List<FrilansArbeidsforholdsperiode> splittFrilansArbeidsforholdTilMånedligeIntervall(FrilansArbeidsforholdsperiode fap) {
        List<FrilansArbeidsforholdsperiode> frilansArbeidsforholdsperioderPerMåned = new ArrayList<>();
        Stream.iterate(fap.getFrilansFom(), d -> d.plusMonths(1))
                .limit(ChronoUnit.MONTHS.between(fap.getFrilansFom(), fap.getFrilansTom()) + 1).forEach(p -> {
            LocalDate init = LocalDate.of(p.getYear(), p.getMonth(), p.getDayOfMonth());
            frilansArbeidsforholdsperioderPerMåned.add(new FrilansArbeidsforholdsperiode(init.withDayOfMonth(1).atStartOfDay(),
                    init.withDayOfMonth(init.lengthOfMonth()).atStartOfDay(),
                    fap.getOrgnr(), fap.getStillingsprosent()));
        });
        return frilansArbeidsforholdsperioderPerMåned;
    }
}