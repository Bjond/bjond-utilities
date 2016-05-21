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

// Jackson Engine
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.validation.constraints.NotNull;

import lombok.val;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
// Log4j Apache

/** <p> Contains all XML related utilities and abstracts the
    underlying XML engine implementation </p>

 *
 * <a href="mailto:Stephen.Agneta@bjondinc.com">Steve 'Cräsh' Agneta</a>
 *
 */

public class XMLUtils {
    private final static XmlMapper mapper;


    
    // Never change the XmlMapper configuration outside of this static block. 
    static {
        // JAX RS XmlMapper. Tell it that any field of any visibility (private, protected, whatever) is accessable.
        mapper = new XmlMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        // Don't get confused by empty lists.
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Null values are expected and are ok. Don't freak out about this either.
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // no more null-valued properties

    }

	/**
	 *  <code>toXML</code> method will extract the OBJ passed as a parameter and return the JSON String
	 *  representation. 
	 * 
	 * NOTE: Written for perfomance thus NULL checks are not performed.
	 *
	 * @param obj an <code>Object</code> value
	 * @return a <code>String</code> value
	 * @exception IOException if an error occurs
	 */
	public static @NotNull(message="obj must not be null.") String toXML(final Object obj) throws IOException{
        val writer = new StringWriter();
	    constructJackson().writeValue(writer, obj);
	
	    return writer.toString();
	}

	/**
	 *  <code>fromXML</code> method will accept an XML string and Class template
	 *  and will deserialize the XML to that Class. 
	 *
	 * NOTE: Written for perfomance thus NULL checks are not performed.
	 *
	 * @param xml a <code>String</code> value
	 * @param c a <code>Class<T></code> value
	 * @return a <code><T></code> value
	 * @exception IOException if an error occurs
	 */
	public static <T> T fromXML(final String xml, final Class<T> c) throws IOException {
	    return constructJackson().readValue(xml, c);
	}

	public static <T> T fromXML(final InputStream xml, final Class<T> c) throws IOException {
	    return constructJackson().readValue(xml, c);
	}

	/**
	 *  <code>constructJackson</code> method will return the JaxRS XmlMapper.
	 *  It is configured to NOT fail on unknown properties as it gets mighty confused around empty List<T> 
	 *  types in hibernate beans.
	 *
	 * @return an <code>XmlMapper</code> value
	 */
	public static @NotNull(message="return must not be null.") XmlMapper constructJackson() {
	    return mapper;
	}
    

}
