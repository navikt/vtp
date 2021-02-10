package no.nav.tjeneste.virksomhet.sak.v1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class GsakRepo {

    private static final String SAKSBEHANDLER_IDENT = "MinSaksbehandler";

    private Map<Long, ObjectNode> jsonBySakId;
    private AtomicInteger sakIder;

    public GsakRepo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Mdkm");
        int initvalue =  Integer.parseInt(LocalDateTime.now().format(formatter)) * 100;
        while (initvalue > 150000000)
            initvalue = initvalue -  50000000;
        sakIder = new AtomicInteger(initvalue);
        jsonBySakId = new ConcurrentHashMap<>();
    }

    public ObjectNode leggTilSak(ObjectNode input) {
        Long sakId = (long) sakIder.incrementAndGet();
        input.put("id", sakId);
        jsonBySakId.put(sakId, input);
        return input;
    }

    public ObjectNode hentSak(Long id) {
        return jsonBySakId.get(id);
    }

    public Map<Long, ObjectNode> alleSaker() {
        return jsonBySakId;
    }

}
