/*  Copyright (c) 2016
 *  by Bjönd Health, Inc., Boston, MA
 *
 *  This software is furnished under a license and may be used only in
 *  accordance with the terms of such license.  This software may not be
 *  provided or otherwise made available to any other party.  No title to
 *  nor ownership of the software is hereby transferred.
 *
 *  This software is the intellectual property of Bjönd Health, Inc.,
 *  and is protected by the copyright laws of the United States of America.
 *  All rights reserved internationally.
 *
 */

package com.bjond.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.bjond.utilities.MiscUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

/** JUnit Test Suite TestBrigid
 *
 * @version 0.001 10/16/15mport scala.ScalaHelloWorld;
 * @author Stephen Agneta
 * @since Build 1.000
 *
 */

public class TestUtils {
 
    /////////////////////////////////////////////////////////////////////////
    //                      Unit Tests below this point                    //
    /////////////////////////////////////////////////////////////////////////

	static public class MyBean {
		@Getter @Setter String prop1;
		@Getter @Setter String prop2;
	}

     
    
    @Test
    public void sanityCheck() throws Exception {
        Assert.assertTrue("I ran ok!", true);
        System.out.println("This is a test"); // You should see this in the html report in stdout.
    }

	@Test
	public void testGetNullProperties() throws Exception {
		
		val o = new MyBean();
		o.setProp1("The secret of life is honesty and fair dealing. If you can fake that, you've got it made.");
		o.setProp2("Man does not control his own fate. The women in his life do that for him.");

		Set<String> nullOnes = MiscUtils.getNullProperties(o);
		
		assertEquals(0, nullOnes.size());
		
		o.setProp2(null);
		nullOnes = MiscUtils.getNullProperties(o);
		
		assertEquals(1, nullOnes.size());
	}
	
	@Test 
	public void testNonNullProperties() throws Exception {
		
		val o = new MyBean();
		o.setProp1("The secret of life is honesty and fair dealing. If you can fake that, you've got it made.");
		o.setProp2("Man does not control his own fate. The women in his life do that for him.");
		
		val props = MiscUtils.getNonNullProperties(o);

		// We filter out "Object.getClass()".
		assertFalse(props.containsKey("class"));
		assertEquals(2, props.keySet().size());
	}


}

