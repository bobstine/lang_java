import java.util.*;

public class StringLister {
    String[] names = { "bob", "george", "ringo", "dave", "sharon", "susie"};
    
    public StringLister(String[] moreNames) {
        Vector<String> list=new Vector<String>();
        for (int i=0; i< names.length; ++i) {
            list.add(names[i]);
        }
        for (int i=0; i<moreNames.length; ++i){
            list.add(moreNames[i]);
        }
        Collections.sort(list);
        for (String name:list){
            System.out.print( name + " ");
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        StringLister lister = new StringLister(args);
    }
}