package no.nav.system.os.eksponering;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.soap.Addressing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.system.os.eksponering.simulerfpservicewsbinding.SendInnOppdragFeilUnderBehandling;
import no.nav.system.os.eksponering.simulerfpservicewsbinding.SimulerBeregningFeilUnderBehandling;
import no.nav.system.os.eksponering.simulerfpservicewsbinding.SimulerFpService;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SendInnOppdragRequest;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SendInnOppdragResponse;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SimulerBeregningRequest;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SimulerBeregningResponse;


@Addressing
@WebService(name = "simulerFpService", targetNamespace = "http://nav.no/system/os/tjenester/simulerFpService/simulerFpServiceGrensesnitt")
@HandlerChain(file = "Handler-chain.xml")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class SimulerFpServiceMockImpl implements SimulerFpService {

    private static Logger LOG = LoggerFactory.getLogger(SimulerFpServiceMockImpl.class);


    @Override
    @WebMethod
    @WebResult(name = "sendInnOppdragResponse", targetNamespace = "http://nav.no/system/os/tjenester/simulerFpService/simulerFpServiceGrensesnitt", partName = "parameters")
    public SendInnOppdragResponse sendInnOppdrag(SendInnOppdragRequest sendInnOppdragRequest) throws SendInnOppdragFeilUnderBehandling {
        throw new UnsupportedOperationException();
    }

    @Override
    @WebMethod
    @WebResult(name = "simulerBeregningResponse", targetNamespace = "http://nav.no/system/os/tjenester/simulerFpService/simulerFpServiceGrensesnitt", partName = "parameters")
    public SimulerBeregningResponse simulerBeregning(SimulerBeregningRequest simulerBeregningRequest) throws SimulerBeregningFeilUnderBehandling {
        LOG.info("Simulerer beregning.");
        SimuleringGenerator simuleringGenerator = new SimuleringGenerator();
        return simuleringGenerator.opprettSimuleringsResultat(simulerBeregningRequest);
    }
}
