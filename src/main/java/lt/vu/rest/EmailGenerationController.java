package lt.vu.rest;

import lt.vu.interfaces.IEmailGeneratorStrategy;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
@Path("/email")
public class EmailGenerationController implements Serializable {

    @Inject
    private IEmailGeneratorStrategy emailGeneratorService;

    private CompletableFuture<String> emailGenerationTask;

    @POST
    @Path("/start")
    public Response startGeneration() {
        emailGenerationTask = emailGeneratorService.generateEmail();
        return Response.accepted("Email generation started").build();
    }

    @GET
    @Path("/status")
    @Produces("text/plain")
    public String getStatus() throws ExecutionException, InterruptedException {
        if (emailGenerationTask == null) {
            return "No task started";
        } else if (emailGenerationTask.isDone()) {
            return "Email generated: " + emailGenerationTask.get();
        } else {
            return "Email generation in progress...";
        }
    }
}
