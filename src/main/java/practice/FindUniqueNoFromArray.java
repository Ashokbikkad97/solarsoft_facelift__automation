package practice;

import java.lang.reflect.Array;

public class FindUniqueNoFromArray {
	 public static void main(String[] args) {

	        int result = 0;
	        int[] a = {1, 2, 1, 4, 2};

	        for (int i = 0; i < a.length; i++) {
	            result = result ^ a[i];
	        }

	        System.out.println("Unique number is: " + result);
	    }
}
