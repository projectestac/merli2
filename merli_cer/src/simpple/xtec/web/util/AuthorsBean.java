package simpple.xtec.web.util;

import java.util.ArrayList;
import java.util.List;


public class AuthorsBean {
   public List getAllAuthors () {
	   ArrayList myList = new ArrayList();
	   myList.add("author1");
	   myList.add("author2");
	   return myList;
       }
   
   public String getAuthorProfile (String authorName) {
	   if (authorName != null) {
	     if (authorName.equals("author1")) {
	        return "myProfile 1";
	        }
	     if (authorName.equals("author2")) {
	        return "myProfile 2";
	        }
	     }
       return "myProfile";
      }
   }