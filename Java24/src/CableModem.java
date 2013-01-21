public class CableModem extends Modem {
    String method = "Cable connection";
    public void connect() {
        System.out.println("Connecting to the Internet...");
        System.out.println("Using a " + method);
    }
}
