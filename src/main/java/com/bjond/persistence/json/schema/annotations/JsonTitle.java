/*  Copyright (c) 2016
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
package com.bjond.persistence.json.schema.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This simple annotation allows us to set the title of a given field. The
 * title will be serialized into the JSON schema as a property, and is the label on
 * the field in the generated UI.
 * 
 * @author Benjamin Flynn
 *
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface JsonTitle {
	public String title() default "";
}
