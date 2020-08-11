package no.nav;

import no.nav.virksomhet.tjenester.arkiv.journal.meldinger.v2.FinnJournalpostResponse;
import no.nav.virksomhet.tjenester.arkiv.journal.meldinger.v2.HentDokumentResponse;
import no.nav.virksomhet.tjenester.arkiv.journal.meldinger.v2.HentDokumentURLResponse;
import no.nav.virksomhet.tjenester.arkiv.journal.meldinger.v2.HentJournalpostResponse;
import no.nav.virksomhet.tjenester.arkiv.journal.meldinger.v2.IdentifiserBrevgruppeResponse;
import no.nav.virksomhet.tjenester.arkiv.journal.meldinger.v2.*;
import no.nav.virksomhet.tjenester.arkiv.journal.v2.ObjectFactory;
import no.nav.virksomhet.tjenester.arkiv.journal.v2.*;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(targetNamespace = "http://nav.no/virksomhet/tjenester/arkiv/journal/v2", name = "Journal")
@XmlSeeAlso({no.nav.virksomhet.tjenester.arkiv.journal.meldinger.v2.ObjectFactory.class, no.nav.virksomhet.tjenester.felles.v1.ObjectFactory.class, ObjectFactory.class, no.nav.virksomhet.gjennomforing.arkiv.journal.v2.ObjectFactory.class, no.nav.virksomhet.tjenester.arkiv.journal.feil.v2.ObjectFactory.class})
@HandlerChain(file = "Handler-chain.xml")
public class JournalMock implements Journal {

    /**
     * @param request
     */
    @Override
    public HentJournalpostResponse hentJournalpost(HentJournalpostRequest request) throws HentJournalpostJournalpostIkkeFunnet {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    /**
     * Operasjon for å identifisere og hente brevgruppeKode for en gitt brevkodeId.
     *
     * @param request
     */
    @Override
    public IdentifiserBrevgruppeResponse identifiserBrevgruppe(IdentifiserBrevgruppeRequest request) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    /**
     * @param request
     */
    @Override
    @WebMethod(action = "http://nav.no/virksomhet/tjenester/arkiv/journal/v2/Journal/finnJournalpostRequest")
    @RequestWrapper(localName = "finnJournalpost", targetNamespace = "http://nav.no/virksomhet/tjenester/arkiv/journal/v2", className = "no.nav.virksomhet.tjenester.arkiv.journal.v2.FinnJournalpost")
    @ResponseWrapper(localName = "finnJournalpostResponse", targetNamespace = "http://nav.no/virksomhet/tjenester/arkiv/journal/v2", className = "no.nav.virksomhet.tjenester.arkiv.journal.v2.FinnJournalpostResponse")
    @WebResult(name = "response", targetNamespace = "")
    public no.nav.virksomhet.tjenester.arkiv.journal.meldinger.v2.FinnJournalpostResponse finnJournalpost(
            @WebParam(name = "request", targetNamespace = "")
                    no.nav.virksomhet.tjenester.arkiv.journal.meldinger.v2.FinnJournalpostRequest request)
    {
        return new FinnJournalpostResponse();
    }

    /**
     * @param request
     */
    @Override
    public HentDokumentURLResponse hentDokumentURL(HentDokumentURLRequest request) throws HentDokumentURLJournalpostIkkeFunnet, HentDokumentURLFilUuidFinnesIkke {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    /**
     * @param request
     */
    @Override
    public HentDokumentResponse hentDokument(HentDokumentRequest request) throws HentDokumentJournalpostIkkeFunnet, HentDokumentFilUuidFinnesIkke {
        throw new UnsupportedOperationException("Ikke implementert");
    }
}
