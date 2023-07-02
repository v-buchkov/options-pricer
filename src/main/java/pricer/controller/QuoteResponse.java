package pricer.controller;

import org.springframework.lang.Nullable;

import java.util.UUID;

public class QuoteResponse {
    public final UUID quotationRequestId;
    public final String timestamp;
    public final @Nullable double bidQuote;
    public final @Nullable double askQuote;
    public final double sportRef;

    public QuoteResponse(UUID quotationRequestId, String timestamp, double bidQuote, double askQuote, double sportRef) {
        this.quotationRequestId = quotationRequestId;
        this.timestamp = timestamp;
        this.bidQuote = bidQuote;
        this.askQuote = askQuote;
        this.sportRef = sportRef;
    }
}
