import java.util.*;

class Name {
    public static void main(String[] args) {
        String names[] = {"moe", "larry", "curly","dean","joe"};
        System.out.println("The original order:");
        for (int i=0; i<names.length; ++i){
            System.out.print(i + ": " + names[i]+ ",  ");
        }
        System.out.println();
        Arrays.sort(names);
        System.out.println("The sorted order:");
        for (int i=0; i<names.length; ++i){
            System.out.print(i + ": " + names[i]+ ",  ");
        }
       System.out.println();        
    }
}