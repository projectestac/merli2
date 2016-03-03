package edu.xtec.merli.segur.login.test;



import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.xtec.merli.segur.login.LoginBean;
import edu.xtec.merli.utils.Utility;
import junit.framework.TestCase;

public class TestLogin extends TestCase {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(TestLogin.class);
	}

	public final void testValidation(){
		LoginBean lb = new LoginBean();
		String user = "acanals5";
		String pass = "ac4263";
		boolean b = false;
		
		b = lb.validateUser(user,pass);
		
		System.out.println("\nUser valid:"+user+ "("+pass+") ->"+b);
	}
	/*
	public final void testQuery(){
		String condicio = "idrecurs = 2 AND (nom='patata' OR edat= 32)";
		StringBuffer query = new StringBuffer();
		String con  = condicio;
		String aux;
		ArrayList cond = new ArrayList();
		if (condicio.length() > 2){ 
			query.append(" WHERE ");
			while (con.length() > 1){
				con = con.substring(con.indexOf("=")+1);
				con.trim();
				if (con.charAt(0) == '\'')
					aux = "'";
				else
					aux= " ";
				aux = con.substring(0,con.indexOf(aux));
				cond.add(aux);
			}
			query.append(condicio);
		}
	}
	*/
}
