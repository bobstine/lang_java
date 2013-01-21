public class ModemTester {
    public static void main(String[] args){
        CableModem surf = new CableModem();
        DSLModem gate = new DSLModem();
        surf.speed = 500;
        gate.speed = 200;
        System.out.println ("Try cable modem:");
        surf.displaySpeed();
        surf.connect();
        System.out.println ("Try DSL modem:");
        gate.displaySpeed();
        gate.connect();
    }
}