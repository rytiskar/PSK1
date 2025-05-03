package lt.vu.interfaces;

import java.util.concurrent.CompletableFuture;

public interface IEmailGeneratorStrategy {

    CompletableFuture<String> generateEmail();
}
