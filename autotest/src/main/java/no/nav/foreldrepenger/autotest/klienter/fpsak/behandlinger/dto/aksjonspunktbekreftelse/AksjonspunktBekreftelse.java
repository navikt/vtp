package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Aksjonspunkt;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;
import no.nav.foreldrepenger.autotest.util.IndexClasses;


public abstract class AksjonspunktBekreftelse {

    private static final List<Class<? extends AksjonspunktBekreftelse>> aksjonspunktBekreftelseClasses;
    static {
        try {
            IndexClasses index = IndexClasses.getIndexFor(AksjonspunktBekreftelse.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            aksjonspunktBekreftelseClasses = Collections.unmodifiableList(index.getSubClassesWithAnnotation(AksjonspunktBekreftelse.class, BekreftelseKode.class));
        } catch (URISyntaxException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @JsonProperty("@type")
    protected String kode;
    protected String begrunnelse;
    
    @SuppressWarnings("unused")
    public AksjonspunktBekreftelse(Fagsak fagsak, Behandling behandling) {
        if(null == this.getClass().getAnnotation(BekreftelseKode.class)) {
            throw new RuntimeException("Kode annotation er ikke satt for " + this.getClass().getTypeName());
        }
        kode = this.getClass().getAnnotation(BekreftelseKode.class).kode();
    }
    
    public static AksjonspunktBekreftelse fromKode(Fagsak fagsak, Behandling behandling, String kode) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
                
        for (Class<? extends AksjonspunktBekreftelse> klasse : aksjonspunktBekreftelseClasses) {
            
            BekreftelseKode annotation = klasse.getDeclaredAnnotation(BekreftelseKode.class);
            
            if(Modifier.isAbstract(klasse.getModifiers())) {
                continue; //trenger trenger ikke skjekke klasser som er abstrakte
            }
            else if(annotation == null) {
                System.out.println("Aksjonspunkt mangler annotasjon" + klasse.getName());
            }
            else if(annotation.kode().equals(kode)) {
                return klasse.getDeclaredConstructor(Fagsak.class, Behandling.class).newInstance(fagsak, behandling);
            }
        }
        
        return null;
    }
    
    public static AksjonspunktBekreftelse fromAksjonspunkt(Fagsak fagsak, Behandling behandling, Aksjonspunkt aksjonspunkt) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        return fromKode(fagsak, behandling, aksjonspunkt.getDefinisjon().kode);
    }
    
    public void setBegrunnelse(String begrunnelse) {
        this.begrunnelse = begrunnelse;
    }

    @Override
    public String toString(){
        return String.format("[%s] %s : %s", this.getClass().getSimpleName(), kode != null ? kode : "", begrunnelse != null ? begrunnelse : "");
    }
}
