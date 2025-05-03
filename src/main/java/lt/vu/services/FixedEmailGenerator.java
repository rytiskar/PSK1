package lt.vu.services;

import lt.vu.interfaces.IEmailGeneratorStrategy;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class FixedEmailGenerator implements Serializable, IEmailGeneratorStrategy {

    public CompletableFuture<String> generateEmail() {

        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "fixed@example.com";
        });
    }
}
