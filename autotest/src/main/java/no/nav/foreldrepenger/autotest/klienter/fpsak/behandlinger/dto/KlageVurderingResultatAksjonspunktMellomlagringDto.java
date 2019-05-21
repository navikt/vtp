package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Aksjonspunkt;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.KlageVurderingResultat;

public class KlageVurderingResultatAksjonspunktMellomlagringDto {

    protected String kode;
    protected String begrunnelse;
    protected int behandlingId;
    protected String fritekstTilBrev;
    protected String klageMedholdArsak;
    protected String klageVurdering;
    protected String klageVurderingOmgjoer;
    
    
    public KlageVurderingResultatAksjonspunktMellomlagringDto(String kode, String begrunnelse, int behandlingId,
            String fritekstTilBrev, String klageMedholdArsak, String klageVurdering, String klageVurderingOmgjoer) {
        super();
        this.kode = kode;
        this.begrunnelse = begrunnelse;
        this.behandlingId = behandlingId;
        this.fritekstTilBrev = fritekstTilBrev;
        this.klageMedholdArsak = klageMedholdArsak;
        this.klageVurdering = klageVurdering;
        this.klageVurderingOmgjoer = klageVurderingOmgjoer;
    }

    public KlageVurderingResultatAksjonspunktMellomlagringDto(int behandlingId, KlageVurderingResultat resultat, Aksjonspunkt aksjonspunkt) {
        this(aksjonspunkt.getDefinisjon().kode, resultat.getBegrunnelse(), behandlingId, resultat.getFritekstTilBrev(), resultat.getKlageMedholdArsak(), resultat.getKlageVurdering(), resultat.getKlageVurderingOmgjoer());
    }
    
    public KlageVurderingResultatAksjonspunktMellomlagringDto(Behandling behandling, Aksjonspunkt aksjonspunkt) {
        this(behandling.id, behandling.getKlagevurdering().getKlageVurderingResultatNFP(), aksjonspunkt); //lage for hvert av dem?
    }
}
