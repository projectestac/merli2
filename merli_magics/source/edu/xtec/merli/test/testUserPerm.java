package edu.xtec.merli.test;

import java.util.ArrayList;

import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.basedades.UserBD;
import junit.framework.Assert;
import junit.framework.TestCase;

public class testUserPerm extends TestCase {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(testUserPerm.class);
	}

	public static final void testGetLlistat() throws MerliDBException{
		ArrayList list = new ArrayList();
		UserBD ubd = new UserBD();
		list = ubd.getLlistatPermisos();
		Assert.assertTrue(list.size()>=0);
	}
}
