package no.nav.navansatt;

import no.nav.foreldrepenger.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.foreldrepenger.vtp.testmodell.ansatt.NAVAnsatt;
import no.nav.foreldrepenger.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.vtp.testmodell.enheter.Norg2Modell;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.inf.psak.navansatt.*;
import no.nav.lib.pen.psakpselv.asbo.ASBOPenFagomrade;
import no.nav.lib.pen.psakpselv.asbo.ASBOPenFagomradeListe;
import no.nav.lib.pen.psakpselv.asbo.navansatt.ASBOPenNAVAnsatt;
import no.nav.lib.pen.psakpselv.asbo.navansatt.ASBOPenNAVAnsattListe;
import no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenNAVEnhet;
import no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenNAVEnhetListe;

import javax.jws.*;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Addressing
@WebService(name = "PSAKNAVAnsatt", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf")
@HandlerChain(file = "Handler-chain.xml")
public class NavAnsattServiceMockImpl implements PSAKNAVAnsatt {

    @WebMethod
    @RequestWrapper(localName = "hentNAVAnsattEnhetListe", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattEnhetListe")
    @ResponseWrapper(localName = "hentNAVAnsattEnhetListeResponse", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattEnhetListeResponse")
    @WebResult(name = "hentNAVAnsattEnhetListeResponse", targetNamespace = "")
    @Override
    public ASBOPenNAVEnhetListe hentNAVAnsattEnhetListe(@WebParam(name = "hentNAVAnsattEnhetListeRequest",targetNamespace = "") ASBOPenNAVAnsatt asboPenNAVAnsatt) throws HentNAVAnsattEnhetListeFaultPenGeneriskMsg, HentNAVAnsattEnhetListeFaultPenNAVAnsattIkkeFunnetMsg {
        try {
            AnsatteIndeks ansatteIndeks = BasisdataProviderFileImpl.getInstance().getAnsatteIndeks();
            EnheterIndeks enheterIndeks = BasisdataProviderFileImpl.getInstance().getEnheterIndeks();
            NAVAnsatt ansatt = ansatteIndeks.hentNAVAnsatt(asboPenNAVAnsatt.getAnsattId()).orElseThrow(() -> new HentNAVAnsattEnhetListeFaultPenNAVAnsattIkkeFunnetMsg("NAV-ansatt '" + asboPenNAVAnsatt.getAnsattId() + "' ikke funnet."));

            List<ASBOPenNAVEnhet> enheter = new ArrayList<>();
            for (String enhetId: ansatt.enheter) {
                Norg2Modell enhet = enheterIndeks.finnByEnhetId(enhetId).orElseThrow(() -> new HentNAVAnsattEnhetListeFaultPenGeneriskMsg("Enhet ikke funnet: " + enhetId));
                ASBOPenNAVEnhet asboEnhet = new ASBOPenNAVEnhet();
                asboEnhet.setEnhetsId(enhet.getEnhetId());
                asboEnhet.setEnhetsNavn(enhet.getNavn());
                enheter.add(asboEnhet);
            }

            ASBOPenNAVEnhetListe response = new ASBOPenNAVEnhetListe();
            response.setNAVEnheter(enheter.toArray(ASBOPenNAVEnhet[]::new));
            return response;
        } catch (IOException e) {
            throw new HentNAVAnsattEnhetListeFaultPenGeneriskMsg(e.getMessage());
        }
    }

    @WebMethod
    @RequestWrapper(localName = "hentNAVAnsattFagomradeListe", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattFagomradeListe")
    @ResponseWrapper(localName = "hentNAVAnsattFagomradeListeResponse", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattFagomradeListeResponse")
    @WebResult(name = "hentNAVAnsattFagomradeListeResponse", targetNamespace = "")
    @Override
    public ASBOPenFagomradeListe hentNAVAnsattFagomradeListe(@WebParam(name = "hentNAVAnsattFagomradeListeRequest",targetNamespace = "") ASBOPenNAVAnsatt asboPenNAVAnsatt) throws HentNAVAnsattFagomradeListeFaultPenGenerisksMsg, HentNAVAnsattFagomradeListeFaultPenNAVAnsattIkkeFunnetMsg {
        ASBOPenFagomradeListe liste = new ASBOPenFagomradeListe();
        ASBOPenFagomrade omrade = new ASBOPenFagomrade();
        omrade.setFagomradeBeskrivelse("Spesialkompetanse for pepperkakebaking");
        omrade.setFagomradeKode("424242");
        omrade.setGyldig(true);
        omrade.setTrekkgruppeKode("567890");
        liste.setFagomrader(new ASBOPenFagomrade[] { omrade });
        return liste;
    }

    @WebMethod
    @RequestWrapper(localName = "hentNAVAnsattListe", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattListe")
    @ResponseWrapper(localName = "hentNAVAnsattListeResponse", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattListeResponse"
    )
    @WebResult(name = "hentNAVAnsattListeResponse", targetNamespace = "")
    @Override
    public ASBOPenNAVAnsattListe hentNAVAnsattListe(@WebParam(name = "hentNAVAnsattListeRequest",targetNamespace = "") ASBOPenNAVEnhet enhet) throws HentNAVAnsattListeFaultPenNAVEnhetIkkeFunnetMsg, HentNAVAnsattListeFaultPenGenerisksMsg {
        try {
            ASBOPenNAVAnsattListe liste = new ASBOPenNAVAnsattListe();
            liste.setNAVAnsatte(BasisdataProviderFileImpl.getInstance().getAnsatteIndeks().hentAlleAnsatte()
                    .stream()
                    .filter(ansatt -> ansatt.enheter.contains(enhet.getEnhetsId()))
                    .map(ansatt -> {
                ASBOPenNAVAnsatt asboAnsatt = new ASBOPenNAVAnsatt();
                asboAnsatt.setAnsattId(ansatt.cn);
                asboAnsatt.setAnsattNavn(ansatt.displayName);
                asboAnsatt.setFornavn(ansatt.givenname);
                asboAnsatt.setEtternavn(ansatt.sn);
                return asboAnsatt;
            }).toArray(ASBOPenNAVAnsatt[]::new));
            return liste;
        } catch (IOException e) {
            e.printStackTrace();
            throw new HentNAVAnsattListeFaultPenGenerisksMsg(e.getMessage());
        }
    }

    @WebMethod
    @RequestWrapper(localName = "hentNAVAnsatt", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsatt")
    @ResponseWrapper(localName = "hentNAVAnsattResponse", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattResponse")
    @WebResult(name = "hentNAVAnsattResponse", targetNamespace = "")
    @Override
    public ASBOPenNAVAnsatt hentNAVAnsatt(@WebParam(name = "hentNAVAnsattRequest",targetNamespace = "") ASBOPenNAVAnsatt asboPenNAVAnsatt) throws HentNAVAnsattFaultPenGeneriskMsg, HentNAVAnsattFaultPenNAVAnsattIkkeFunnetMsg {
        try {
            AnsatteIndeks indeks = BasisdataProviderFileImpl.getInstance().getAnsatteIndeks();
            NAVAnsatt ansatt = indeks.hentNAVAnsatt(asboPenNAVAnsatt.getAnsattId()).orElseThrow(() -> new HentNAVAnsattFaultPenNAVAnsattIkkeFunnetMsg("NAV-ansatt '" + asboPenNAVAnsatt.getAnsattId() + "' ikke funnet."));
            asboPenNAVAnsatt.setAnsattNavn(ansatt.displayName);
            asboPenNAVAnsatt.setFornavn(ansatt.givenname);
            asboPenNAVAnsatt.setEtternavn(ansatt.sn);
            return asboPenNAVAnsatt;
        } catch (IOException e) {
            e.printStackTrace();
            throw new HentNAVAnsattFaultPenGeneriskMsg(e.getMessage());
        }
    }
}
