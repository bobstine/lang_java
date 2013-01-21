class NewRoot {
    public static void main(String[] args) {
        int n = 100;
        if(args.length > 0) {
            n = Integer.parseInt(args[0]);
        }
        System.out.println( "The square root of " + n + " is " + Math.sqrt(n));
    }
}