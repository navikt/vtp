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

public class InntektsperiodeDeserializer extends JsonDeserializer {

    @Override
    public List<Inntektsperiode> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        List<Inntektsperiode> inntektsperioder = new ArrayList<>();
        JsonMapper jm = new JsonMapper();
        ObjectMapper objectMapper = jm.lagObjectMapper();

        for (JsonNode nodeElement : node){
            Inntektsperiode inntektsperiodeIkkeMånedlig = objectMapper.treeToValue(nodeElement, Inntektsperiode.class);
            inntektsperioder.addAll(getSplitInntektsperiodeToMonthly(inntektsperiodeIkkeMånedlig));
        }
        return inntektsperioder;
    }

    private static List<Inntektsperiode> getSplitInntektsperiodeToMonthly(Inntektsperiode ip) {
        List<Inntektsperiode> inntektsperioderPaaMaaned = new ArrayList<>();
        Stream.iterate(ip.getFom(), d -> d.plusMonths(1))
                .limit(ChronoUnit.MONTHS.between(ip.getFom(), ip.getTom()) + 1).forEach(p -> {
            LocalDate init = LocalDate.of(p.getYear(), p.getMonth(), p.getDayOfMonth());
            inntektsperioderPaaMaaned.add(new Inntektsperiode(init.withDayOfMonth(1).atStartOfDay()
                    , init.withDayOfMonth(init.lengthOfMonth()).atStartOfDay(),
                    ip.getBeløp(), ip.getOrgnr(), ip.getType(), ip.getFordel(), ip.getBeskrivelse()));

        });
        return inntektsperioderPaaMaaned;
    }
}
