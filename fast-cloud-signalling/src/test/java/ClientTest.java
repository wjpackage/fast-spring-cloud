import com.sendi.signalling.client.SignallingClientDemo;
import org.junit.Test;

import java.io.IOException;

/**
 * @author fiendo
 * @version 1.0 (2019/7/13)
 */
public class ClientTest {

    private static final String IP = "192.168.2.50";
    private static final int PORT = 21123;

    @Test
    public void singleClientTest() throws IOException {
        SignallingClientDemo signallingClientDemo = new SignallingClientDemo();
        signallingClientDemo.run(IP,PORT);
    }

    @Test
    public void fiveClientTest() throws IOException {
        for (int i = 0; i < 5; i++) {
            SignallingClientDemo signallingClientDemo = new SignallingClientDemo();
            signallingClientDemo.run(IP,PORT);
        }
    }

    @Test
    public void oneHundredClientTest() throws IOException {
        for (int i = 0; i < 100; i++) {
            SignallingClientDemo signallingClientDemo = new SignallingClientDemo();
            signallingClientDemo.run(IP,PORT);
        }
    }

}
