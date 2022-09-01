package cloudcode.helloworld.service;

import java.util.stream.IntStream;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

@Service
public class PrimeService {
    class Stats {
        long even;
        long primes;
        long counted;

        @Override
        public String toString() {
            return "Even = " + even +
                    ", Primes = " + primes +
                    ", Counted = " + counted;
        }
    }

    private static boolean isPrime(Stats stats, long num) {
        stats.counted++;
        if (num == 2) {
            stats.even++;
            stats.primes++;
            return true;
        }
        if (num < 2 || num % 2 == 0) {
            stats.even++;
            return false;
        }
        for (int i = 3; i * i <= num; i += 2) {
            if (num % i == 0) {
                return false;
            }
        }
        stats.primes++;
        return true;
    }

    @Async
    public void countPrimes(long until, DeferredResult<ResponseEntity<String>> response) {
        Stats stats = new Stats();
        for (long i = 2; i < until ; ++i) {
            isPrime(stats, i);
        }
        response.setResult(ResponseEntity.ok(stats.toString()));
    }

    private static boolean isPrime(int num) {
        if (num == 2) {
            return true;
        }
        if (num < 2 || num % 2 == 0) {
            return false;
        }
        for (int i = 3; i * i <= num; i += 2) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Async
    public void countPrimesLambda(int until, DeferredResult<ResponseEntity<String>> response) {
        long primeCount = IntStream.iterate(1, n -> n + 1)
                .limit(until)
                .filter(PrimeService::isPrime)
                .count();
        response.setResult(ResponseEntity.ok("Primes: " + primeCount));
    }
}
