package no.nav.system.os.eksponering;

import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.system.os.eksponering.simulerfpservicewsbinding.SendInnOppdragFeilUnderBehandling;
import no.nav.system.os.eksponering.simulerfpservicewsbinding.SimulerFpService;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SendInnOppdragRequest;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SendInnOppdragResponse;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SimulerBeregningRequest;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SimulerBeregningResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.soap.Addressing;


@Addressing
@WebService(name = "simulerFpService", targetNamespace = "http://nav.no/system/os/tjenester/simulerFpService/simulerFpServiceGrensesnitt")
@HandlerChain(file = "Handler-chain.xml")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class SimulerFpServiceMockImpl implements SimulerFpService {

    private static Logger LOG = LoggerFactory.getLogger(SimulerFpServiceMockImpl.class);

    private TestscenarioBuilderRepository scenarioRepository;

    public SimulerFpServiceMockImpl(TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    public SimulerFpServiceMockImpl() {}

    @Override
    @WebMethod
    @WebResult(name = "sendInnOppdragResponse", targetNamespace = "http://nav.no/system/os/tjenester/simulerFpService/simulerFpServiceGrensesnitt", partName = "parameters")
    public SendInnOppdragResponse sendInnOppdrag(SendInnOppdragRequest sendInnOppdragRequest) throws SendInnOppdragFeilUnderBehandling {
        throw new UnsupportedOperationException();
    }

    @Override
    @WebMethod
    @WebResult(name = "simulerBeregningResponse", targetNamespace = "http://nav.no/system/os/tjenester/simulerFpService/simulerFpServiceGrensesnitt", partName = "parameters")
    public SimulerBeregningResponse simulerBeregning(SimulerBeregningRequest simulerBeregningRequest) {

        SimuleringGenerator simuleringGenerator = new SimuleringGenerator();
        return simuleringGenerator.opprettSimuleringsResultat(simulerBeregningRequest);
    }
}
