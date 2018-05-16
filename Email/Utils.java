package com.allconnect.filemerge.common.utils;

import java.util.Collection;

public class Utils {
	
	 public static boolean isEmpty(Collection col)
	  {
	    return col == null || col.isEmpty() == true;
	  }
	 
	 public static boolean isEmpty(Object[] arr)
	  {
	    return arr == null || arr.length == 0;
	  }
	 
	 public static boolean isBlank(String str)
	  {
	    return str == null || str.trim().length() == 0;
	  }

}
