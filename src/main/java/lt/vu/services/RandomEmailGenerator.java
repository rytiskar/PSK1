package lt.vu.services;

import lt.vu.interfaces.IEmailGeneratorStrategy;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Alternative
@ApplicationScoped
public class RandomEmailGenerator implements Serializable, IEmailGeneratorStrategy {

    public CompletableFuture<String> generateEmail() {

        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "random@example.com";
        });
    }
}
