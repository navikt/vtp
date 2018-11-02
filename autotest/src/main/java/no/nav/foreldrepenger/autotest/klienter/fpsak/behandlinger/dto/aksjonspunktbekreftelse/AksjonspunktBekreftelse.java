package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.reflections.Reflections;

import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Aksjonspunkt;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;


public abstract class AksjonspunktBekreftelse {

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
        Reflections reflections = new Reflections("no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse");
                
        for (Class<? extends AksjonspunktBekreftelse> klasse : reflections.getSubTypesOf(AksjonspunktBekreftelse.class)) {
            
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
}
