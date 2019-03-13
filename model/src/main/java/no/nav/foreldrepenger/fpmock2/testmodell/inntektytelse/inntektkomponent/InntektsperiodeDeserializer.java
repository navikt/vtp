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

public class InntektsperiodeDeserializer extends JsonDeserializer<List<Inntektsperiode>> {

    @Override
    public List<Inntektsperiode> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        List<Inntektsperiode> inntektsperioder = new ArrayList<>();
        for (JsonNode nodeElement : node){
            Inntektsperiode inntektsperiodeIkkeMånedlig = oc.treeToValue(nodeElement, Inntektsperiode.class);
            inntektsperioder.addAll(splittInntektsperioderTilMånedligeIntervall(inntektsperiodeIkkeMånedlig));
        }
        return inntektsperioder;
    }

    private static List<Inntektsperiode> splittInntektsperioderTilMånedligeIntervall(Inntektsperiode ip) {
        List<Inntektsperiode> inntektsperioderPaaMaaned = new ArrayList<>();
        LocalDate tomDato = (ip.getTom() != null) ? ip.getTom() : LocalDate.now();
        Stream.iterate(ip.getFom(), d -> d.plusMonths(1))
                .limit(ChronoUnit.MONTHS.between(ip.getFom(), tomDato) + 1).forEach(p -> {
            LocalDate init = LocalDate.of(p.getYear(), p.getMonth(), p.getDayOfMonth());
            inntektsperioderPaaMaaned.add(new Inntektsperiode(init.withDayOfMonth(1)
                    , init.withDayOfMonth(init.lengthOfMonth()),
                    ip.getBeløp(), ip.getOrgnr(), ip.getType(), ip.getFordel(), ip.getBeskrivelse(), ip.getSkatteOgAvgiftsregel(),
                    ip.getInngaarIGrunnlagForTrekk(), ip.getUtloeserArbeidsgiveravgift(), ip.getPersonligArbeidsgiver()));

        });
        return inntektsperioderPaaMaaned;
    }
}
