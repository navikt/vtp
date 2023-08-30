package no.nav.system.os.eksponering;

import jakarta.jws.HandlerChain;
import jakarta.jws.WebMethod;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.soap.Addressing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.system.os.eksponering.simulerfpservicewsbinding.SendInnOppdragFeilUnderBehandling;
import no.nav.system.os.eksponering.simulerfpservicewsbinding.SimulerFpService;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SendInnOppdragRequest;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SendInnOppdragResponse;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SimulerBeregningRequest;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SimulerBeregningResponse;

@Deprecated
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

    @Deprecated
    @Override
    @WebMethod
    @WebResult(name = "simulerBeregningResponse", targetNamespace = "http://nav.no/system/os/tjenester/simulerFpService/simulerFpServiceGrensesnitt", partName = "parameters")
    public SimulerBeregningResponse simulerBeregning(SimulerBeregningRequest simulerBeregningRequest) {

        SimuleringGenerator simuleringGenerator = new SimuleringGenerator();
        return simuleringGenerator.opprettSimuleringsResultat(simulerBeregningRequest);
    }
}
