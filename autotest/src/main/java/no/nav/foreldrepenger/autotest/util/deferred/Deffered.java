package no.nav.foreldrepenger.autotest.util.deferred;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

public class Deffered<T> {

    private static final ThreadLocal<ExecutorService> DEFER_SERVICE = ThreadLocal.withInitial(() -> Executors.newFixedThreadPool(4));

    private final Supplier<T> result;

    private Deffered(Supplier<T> result) {
        this.result = result;
    }

    public T get() throws ExecutionException, InterruptedException {
        return result.get();
    }

    public static <T> Deffered<T> deffered(Callable<T> function) {
        Future<T> future = DEFER_SERVICE.get().submit(function);
        return new Deffered<T>(() -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    public static <T> Deffered<T> defferedLazy(Callable<T> function) {
        return new Deffered<T>(() -> {
            try {
                return function.call();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
    }

}
