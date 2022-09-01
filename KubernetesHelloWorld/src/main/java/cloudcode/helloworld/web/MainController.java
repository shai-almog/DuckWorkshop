
package cloudcode.helloworld.web;

import cloudcode.helloworld.service.PrimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Defines a controller to handle HTTP requests.
 */
@Controller
public final class MainController {
    private final PrimeService primeService;

    public MainController(PrimeService primeService) {
        this.primeService = primeService;
    }

    /**
     * Create an endpoint for the landing page
     * @return the index view template with a simple message
     */
    @GetMapping("/")
    public String helloWorld(Model model) {
        String message = "Hello World!";
        model.addAttribute("message", message);
        return "index";
    }

    private long untilNotNull(Long until) {
        return until == null ? (long)Math.pow(10, 8) : until;
    }

    @GetMapping("/primes")
    public DeferredResult<ResponseEntity<String>> countPrimes(Long until) {
        DeferredResult<ResponseEntity<String>> result = new DeferredResult<>();
        primeService.countPrimes(untilNotNull(until), result);
        return result;
    }

    @GetMapping("/primesLambda")
    public DeferredResult<ResponseEntity<String>> countPrimesLambda(Long until) {
        DeferredResult<ResponseEntity<String>> result = new DeferredResult<>();
        primeService.countPrimesLambda((int)untilNotNull(until), result);
        return result;
    }
}
