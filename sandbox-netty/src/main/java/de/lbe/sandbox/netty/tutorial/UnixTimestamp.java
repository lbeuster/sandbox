package de.lbe.sandbox.netty.tutorial;
import java.util.Date;

public class UnixTimestamp {

    private final long value;
    
    public UnixTimestamp() {
        this(System.currentTimeMillis() / 1000L + 2208988800L);
    }
    
    public UnixTimestamp(long value) {
        this.value = value;
    }
        
    public long value() {
        return value;
    }
        
    @Override
    public String toString() {
        return new Date((value() - 2208988800L) * 1000L).toString();
    }
}