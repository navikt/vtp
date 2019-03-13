package no.nav.foreldrepenger.autotest.util.deferred;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Deffered<T> {

    public static final java.util.concurrent.ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(20);

    private final Future<T> future;
    private Deffered(Future<T> future){
        this.future = future;
    }

    public T get() throws ExecutionException, InterruptedException {
        return future.get();
    }

    public static <T> Deffered<T> deffered(Callable<T> function) {
        Future<T> future = EXECUTOR_SERVICE.submit(function);
        return new Deffered<T>(future);
    }


}
