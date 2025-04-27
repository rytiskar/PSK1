package lt.vu.services;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class EmailGenerator implements Serializable {

    public CompletableFuture<String> generateEmail() {

        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "generated.email@example.com";
        });
    }
}
