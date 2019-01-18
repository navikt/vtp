package no.nav.foreldrepenger.autotest.util.deferred;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class Deffered<T> extends CompletableFuture<T>{

    public Deffered(Callable<T> function){
        
        Executors.newCachedThreadPool().submit(() ->{
            try {
                this.complete(function.call());
            } catch (Exception e) {
                this.completeExceptionally(e);
            }
        });
        
    }
    
}
