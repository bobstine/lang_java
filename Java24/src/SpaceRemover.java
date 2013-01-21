class SpaceRemover {
    public static void main(String[] args)
    {
        String mostFamous = "Rudolf the red nosed reindeer";
        char[] mfl = mostFamous.toCharArray();
        for(int i=0; i<mfl.length; ++i)
        {
            char current = mfl[i];
            if(current != ' '){
                System.out.print(current);
            } else {
                System.out.print('.');
            }
        }
        System.out.println();
    }
}
