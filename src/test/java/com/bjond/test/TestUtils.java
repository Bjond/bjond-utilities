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

import static org.assertj.core.api.Assertions.assertThat;
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


    @Test
	public void test_normalizeToNaturalSortOrder() throws Exception {
        final String resultString1 = "aa000000000000000000000000000000000000000000000000000000000000000000000000011bb000000000000000000000000000000000000000000000000000000000000000000000000022cc000000000000000000000000000000000000000000000000000000000000000000000000033ff";
        final String resultString2 = "000000000000000000000000000000000000000000000000000000000000000000000000011aa000000000000000000000000000000000000000000000000000000000000000000000000022bb000000000000000000000000000000000000000000000000000000000000000000000000033cc000000000000000000000000000000000000000000000000000000000000000000000000044";
        final String resultString3 = "000000000000000000000000000000000000000000000000000000000000000000000000111";
        System.out.println("test_normalizeToNaturalSortOrder() invoked");

        final String result = MiscUtils.normalizeToNaturalSortOrder("aa11bb22cc33ff");
        //System.out.println("RESULT: " + result);
        assertThat(result).isEqualTo(resultString1);

        final String result2 = MiscUtils.normalizeToNaturalSortOrder("11aa22bb33cc44");
        //System.out.println("RESULT: " + result2);
        assertThat(result2).isEqualTo(resultString2);

        final String result3 = MiscUtils.normalizeToNaturalSortOrder("111");
        //System.out.println("RESULT: " + result3);
        assertThat(result3).isEqualTo(resultString3);


        final String result4 = MiscUtils.normalizeToNaturalSortOrder("now is the time for all good men");
        //System.out.println("RESULT: " + result4);
        assertThat(result4).isEqualTo("now is the time for all good men");

        // Some more edge cases
        assertThat(MiscUtils.normalizeToNaturalSortOrder("")).isNullOrEmpty();
        assertThat(MiscUtils.normalizeToNaturalSortOrder(null)).isNullOrEmpty();

    }    

}

