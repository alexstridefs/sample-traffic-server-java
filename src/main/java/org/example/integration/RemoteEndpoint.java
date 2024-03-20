package org.example.integration;

import java.security.SecureRandom;

public class RemoteEndpoint {

    private static final SecureRandom RNG = new SecureRandom();

    public record EndpointCallResult(int statusCode) {
    }

    public static EndpointCallResult call(String url) {
        // Dummy implementation simulating behaviour of remote service
        try {
            if (RNG.nextInt(100) == 0) {
                Thread.sleep(RNG.nextInt(1000));
                return new EndpointCallResult(500);
            } else {
                Thread.sleep(RNG.nextInt(20));
                return new EndpointCallResult(200);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
