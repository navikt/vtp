package no.nav.tjeneste.virksomhet.journalmodell;

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

    @Column(name = "TILKNYTTET_JP_SOM")
    String tilknJpSom;

    JournalDokument(){
    }

    /*TODO (rune) rm:
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

    }*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDokumentId() {
        return dokumentId;
    }

    public void setDokumentId(String dokumentId) {
        this.dokumentId = dokumentId;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public void setJournalpostId(String journalpostId) {
        this.journalpostId = journalpostId;
    }

    public String getSakId() {
        return sakId;
    }

    public void setSakId(String sakId) {
        this.sakId = sakId;
    }

    public byte[] getDokument() {
        return dokument;
    }

    public void setDokument(byte[] dokument) {
        this.dokument = dokument;
    }

    public String getDokumentType() {
        return dokumentType;
    }

    public void setDokumentType(String dokumentType) {
        this.dokumentType = dokumentType;
    }

    public String getFilType() {
        return filType;
    }

    public void setFilType(String filType) {
        this.filType = filType;
    }

    public String getTilknJpSom() {
        return tilknJpSom;
    }

    public void setTilknJpSom(String tilknJpSom) {
        this.tilknJpSom = tilknJpSom;
    }
}
