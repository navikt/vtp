package no.nav.tjeneste.virksomhet.journal.v2.modell;

//import org.hibernate.annotationsNamedQueries;
//import org.hibernate.annotations.NamedQuery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
//import javax.persistence.Transient;
//import java.io.FileNotFoundException;


/*
@NamedQueries({
        */
/*@NamedQuery(name="JournalDokument.findByDocumentId",
                query="SELECT t FROM MOCK_JOARK_JOURNALDOKUMENTER t WHERE t.dokument_id = ':dokument_id'"),
        @NamedQuery(name="JournalDokument.findByJournalpostId",
                query="SELECT t FROM MOCK_JOARK_JOURNALDOKUMENTER t WHERE t.journalpost_id = :'journalpost_id'"),*//*


})
*/

@Entity(name = "JournalDokument")
@Table(name = "MOCK_JOARK_JOURNALDOKUMENTER")

public class JournalDokument {

    @Id
    @Column(name = "ID", nullable = false)
    long id;

    @Column(name = "DOKUMENT_ID")
    String dokumentId;

    @Column(name = "JORUNALPOST_ID")
    String journalpostId;

    @Column(name = "SAK_ID")
    String sakId;

    @Column(name = "DOKUMENT", nullable = false)
    byte[] dokument;

    @Column(name = "DOKUMENTTYPE")
    String dokumentType;

    @Column(name = "FILTYPE")
    String filType;

    JournalDokument(){
    }

    public JournalDokument(long id, String dokumentId,  String journalpostId, byte[] dokument)
    {
        this.id=id;
        if (dokumentId != null)
        {
            this.dokumentId=dokumentId;
        }
        if (journalpostId != null)
        {
            this.journalpostId=journalpostId;
        }
        if(dokument != null) {
          this.dokument=dokument;
        }
        //this.filType = filType;

    }

    public byte[] getDokument() {
        return dokument;
    }
}
