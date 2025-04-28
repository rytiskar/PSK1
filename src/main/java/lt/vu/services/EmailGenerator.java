package lt.vu.services;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
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
            return "generated."+this.toString()+"@example.com";
        });
    }
}
