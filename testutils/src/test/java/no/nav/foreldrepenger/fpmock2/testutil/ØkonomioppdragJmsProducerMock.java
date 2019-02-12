package no.nav.foreldrepenger.fpmock2.testutil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.vedtak.felles.integrasjon.okonomistottejms.ØkonomioppdragJmsProducer;
import no.nav.vedtak.felles.integrasjon.okonomistottejms.consumer.ØkonomioppdragAsyncJmsConsumer;

@ApplicationScoped
@Alternative
@Priority(1)
public class ØkonomioppdragJmsProducerMock extends ØkonomioppdragJmsProducer {
    protected final Logger LOG = LoggerFactory.getLogger(ØkonomioppdragJmsProducer.class);

    private final static Pattern HENVISNING_PATTERN = Pattern.compile("<henvisning>(\\w*)</henvisning>");
    private final static Pattern OPPDRAG_GJELDER_ID_PATTERN = Pattern.compile("<oppdragGjelderId>(\\w*)</oppdragGjelderId>");
    private final static Pattern FAGSYSTEM_PATTERN = Pattern.compile("<fagsystemId>(\\w*)</fagsystemId>");

    private ØkonomioppdragAsyncJmsConsumer økonomioppdragAsyncJmsConsumer;

    @Inject
    public ØkonomioppdragJmsProducerMock(ØkonomioppdragAsyncJmsConsumer økonomioppdragAsyncJmsConsumer) {
        this.økonomioppdragAsyncJmsConsumer = økonomioppdragAsyncJmsConsumer;
    }

    @Override
    public void sendØkonomiOppdrag(String oppdragXML) {
        LOG.info("invoke: sendØkonomiOppdrag - mottar og sender oppdragskvittering");
        try {
            String kvittering = lagInputFraOppdragFraOutputTilOppdrag(getInput("økonomioppdrag-statusOk.xml"), oppdragXML);
            LOG.debug(kvittering);
            økonomioppdragAsyncJmsConsumer.handle(kvittering);
        } catch (IOException | URISyntaxException e){
            LOG.warn("Kunne ikke opprette oppgave mot mottak av økonomimelding.");
        }
    }

    private String getInput(String filename) throws IOException, URISyntaxException {
        return new String(getClass().getClassLoader().getResourceAsStream(filename).readAllBytes());
    }

    private String lagInputFraOppdragFraOutputTilOppdrag(String template, String oppdragXML){
        String fagsystem = hentFørsteTreff(FAGSYSTEM_PATTERN,oppdragXML);
        String oppdragGjelderInfo = hentFørsteTreff(OPPDRAG_GJELDER_ID_PATTERN, oppdragXML);
        String henvisning = hentFørsteTreff(HENVISNING_PATTERN, oppdragXML);
        return String.format(template,fagsystem,oppdragGjelderInfo, henvisning);
    }

    private String hentFørsteTreff(Pattern pattern, String søkestreng) {
        Matcher matcher = pattern.matcher(søkestreng);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new IllegalArgumentException(String.format("Ingen match for pattern: %s", pattern));
        }
    }


}
