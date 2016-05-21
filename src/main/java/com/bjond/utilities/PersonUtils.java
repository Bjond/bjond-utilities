/*  Copyright (c) 2015
 *  by Bjönd, Inc., Boston, MA
 *
 *  This software is furnished under a license and may be used only in
 *  accordance with the terms of such license.  This software may not be
 *  provided or otherwise made available to any other party.  No title to
 *  nor ownership of the software is hereby transferred.
 *
 *  This software is the intellectual property of Bjönd, Inc.,
 *  and is protected by the copyright laws of the United States of America.
 *  All rights reserved internationally.
 *
 */


package com.bjond.utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.val;

public class PersonUtils {
	
	@Value
	@NoArgsConstructor
	@EqualsAndHashCode(callSuper=false)
	static public class SetDiffResult {
		List<String> added = new ArrayList<String>();
		List<String> removed = new ArrayList<String>();

        /**
		 *  Return TRUE if there are no changes within the diff.
		 * 
		 * @return
		 */
        public boolean isEmpty() { return added.isEmpty() && removed.isEmpty();}
	}
	
	public static SetDiffResult takeDifference(Set<String> s1, Set<String> s2) {
		
		if (s1 == null)
			s1 = new HashSet<String>();
		
		if (s2 == null)
			s2 = new HashSet<String>();
		
		val added = new ArrayList<String>();
		added.addAll(s2);
		added.removeAll(s1);
		
		val removed = new ArrayList<String>();
		removed.addAll(s1);
		removed.removeAll(s2);
		
		val result = new SetDiffResult();
		result.getAdded().addAll(added);
		result.getRemoved().addAll(removed);
		
		return result;
	}
}
