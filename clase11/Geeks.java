import java.util.*;

class Geeks {
    public static void main(String[] args)
    {
        int a[] = { 1, 13, 4, 1, 41, 31, 31, 4, 13, 2 };

        // put all elements in arraylist
        ArrayList<Integer> al = new ArrayList();
        for (int i = 0; i < a.length; i++) {
            al.add(a[i]);
        }

        HashMap<Integer, Integer> hm = new HashMap();

        // counting occurrence of numbers
        for (int i = 0; i < al.size(); i++) {
            hm.putIfAbsent(al.get(i), Collections.frequency(
                                         al, al.get(i)));
        }
        System.out.println(hm);
    }
}