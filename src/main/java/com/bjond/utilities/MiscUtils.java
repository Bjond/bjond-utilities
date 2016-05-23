/*  Copyright (c) 2014
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

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jooq.lambda.Unchecked;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.google.common.base.CaseFormat;
import com.google.common.io.Files;

import lombok.val;
import lombok.extern.slf4j.Slf4j;



/** <p> Miscellaneous convenience methods.  </p>

 *
 * <a href="mailto:Stephen.Agneta@bjondinc.com">Steve 'Crash' Agneta</a>
 *
 */

@Slf4j
final public class MiscUtils {
    private final static long DELAY_IN_MILLIS = 3000;

    private static TimeBasedGenerator uuidGenerator;
    static {
        // need to pass Ethernet address; can either use real one (shown here)
        final EthernetAddress nic = EthernetAddress.fromInterface();
        // or bogus which would be gotten with: EthernetAddress.constructMulticastAddress()
        uuidGenerator = Generators.timeBasedGenerator(nic);
        // also: we don't specify synchronizer, getting an intra-JVM syncer; there is
        // also external file-locking-based synchronizer if multiple JVMs run JUG
        // UUID uuid = uuidGenerator.generate();
    }
    
    // Accessors/Mutators

    /**
     *  <code>fromCamelCaseToLowerHyphen</code> method will convert
     *
     * CamelCase to camel-case (lower hyphen). Works only for ASCII equivalents which is 
     * good enough for keywords and internal strings etcetera
     *
     * @param in a <code>String</code> value
     * @return a <code>String</code> value
     */
    
    public static String fromCamelCaseToLowerHyphen(@NotNull(message="in must not be null.") final String in){
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, in);
    }


    /**
     *  <code>isNotNullOrBlank</code> method returns true if the string passed as an argument
     *  is not null or blank. 
     *
     * @param string a <code>String</code> value
     * @return a <code>boolean</code> value
     */
    public static boolean isNotNullOrBlank(final String string)
    {
        //Checks if a CharSequence is whitespace, empty ("") or null.
        return !StringUtils.isBlank(string);
    }

    public static boolean isNullOrBlank(final String string)
    {
        //Checks if a CharSequence is whitespace, empty ("") or null.
        return StringUtils.isBlank(string);
    }

    /**
     *  <code>generateUUID</code> method will generate a random UUID.
     *
     * @return a <code>String</code> value
     */
    public static String generateUUID() {
        //return UUID.randomUUID().toString();
        return uuidGenerator.generate().toString();
    }

    public static UUID generateUUIDObject() {
        //return UUID.randomUUID().toString();
        return uuidGenerator.generate();
    }

    public static UUID generateUUIDObject(final String uuid) {
        return UUID.fromString(uuid);
    }
    
    /**
     * <code>toArray</code> method will convert the collection of T to a corresponding array of T.
     * This is a convenience method which is somewhat simpler and hides some of the details.
     * The syntax of Java never makes this syntax easy. This is the best I can accomplish.
     *
     * example usage: 
            Collection&lt;User&gt; users = identityService.getAllMembers(group);
            Users[] userArray = MiscUtils.&lt;User&gt;toArray(users, User.class)
     * 
     *
     * @param <T> Type of class c returned.
     * @param collection <code>java.util.Collection&lt;T&gt;</code> value
     * @param c <code>Class&lt;T&gt;</code> value
     * @return T <code>T[]</code> value
     */

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<? extends T> collection, Class<T> c){
        return collection.toArray((T[])java.lang.reflect.Array.newInstance(c, collection.size()));
    }

     /**
     *  <code>delay</code> method will put this thread to sleep for some seconds.
     *
     */
    
    public static void delay() {
        try {
            Thread.sleep(DELAY_IN_MILLIS);
        }
        catch(Exception e){
            log.error("Thread.sleep error on delay", e);
        };
    }

    /**
     * The system is in production mode if BJOND_RUNTIME_MODE environment variable is
     * set to "production" (case insensitive).
     * 
     * Use this check for switching the system to use development-time 
     * resources and to protect production resources.
     * 
     * Having written this method, I advise *not* to use it often.  
     * As much as we can, we want our development environment to be identical to
     * production environment.  So it's best to avoid implementing development time
     * specific code.
     *  
     * @return true if running in production runtime mode; false otherwise.
     */
    public static boolean isProductionMode() {
    	return isProductionMode(System.getenv());
    }

    /**
     * I'm keeping this "mode" system intentionally simple for now.  That is, we only check
     * for one mode: either "in production" or not. 
     * 
     * The system is in development mode if BJOND_RUNTIME_MODE environment variable is
     * not set to "production" (case insensitive).
     * 
     * Note, "production" mode means the code is running on OpenShift.  Test, alpha, demo,
     * are all considered to be production.
     * 
     * @param env Usually the return value of <code>System.getenv()</code>.
     * @return TRUE if system running in production mode and false otherwise.
     */
    public static boolean isProductionMode(Map<String, String> env) {
    	final String key = "BJOND_RUNTIME_MODE";
    	final String expected = "production";

    	if (! env.containsKey(key))
    		return false;

    	String val = env.get(key);

    	return expected.equalsIgnoreCase(val);
    }
    
    public static String getOpenshiftAppName() {
    	return getOpenshiftAppName(System.getenv());
    }
    
    public static String getOpenshiftAppName(Map<String, String> env) {
    	final String key = "OPENSHIFT_APP_NAME";
    	return env.get(key);
    }

    /**
     *  <code>isRunningUnderArquillian</code> method will return true if the system is 
     *  running beneath the Arquillian System testing framework. Some system components don't
     *  work in some configurations, for various reasons, with Arquillian thus we need to know.
     *
     * @return a <code>boolean</code> value TRUE if system is running under Arquillian integration test framework.
     */
    
    public static boolean isRunningUnderArquillian() {
        return System.getProperty("ARQUILLIAN") != null;
    }
    
	/**
	 * The file resource must be encoded in UTF-8.   
	 * 
	 * @param myClass Class associated with the resource (resource just have to in the same package as this class, I think).
	 * @param resourceName filename of the resource.
	 * @return Content of the file read in string.
	 * @throws IOException If any IO error occurs reading contents from Resource. Such as the resource not found.
	 */
	public static String readContentsFromResource(@SuppressWarnings("rawtypes") Class myClass, String resourceName) throws IOException {
		val rsc = myClass.getResource(resourceName);
		val path = rsc.getPath();
		val file = new File(path);
		return Files.toString(file, Charset.forName("UTF-8"));
	}
	
 
	/**
	 * Get a MessageDigest based on the algorithm passed.
	 * 
	 * @return the MessageDigest that conforms to MD5 hash.
	 */
	public static MessageDigest getMD5() {
    	MessageDigest digest = null;
    	try{
    		digest = MessageDigest.getInstance("MD5");
    	}
    	catch(NoSuchAlgorithmException e) {
    		log.error("", e);
    	}
    	return digest;
    }
	
	/**
	 * Meant to work like angular.extend():
	 * 
	 * https://docs.angularjs.org/api/ng/function/angular.extend
	 * @param o1 destination object
	 * @param o2 source object
	 * @return returns the 01
	 * @throws Exception if introspection fails.
	 */
	public static Object extend(final Object o1, final Object o2) throws Exception {
		
		if (o2 != null) { 
			val o2Properties = getNonNullProperties(o2);
			BeanUtils.populate(o1, o2Properties);
		}
		
		return o1;
	}
	
	/**
	 * Meant to work like angular.extend():
	 * 
	 * https://docs.angularjs.org/api/ng/function/angular.extend
	 * @param o1 destination
	 * @param objs source
	 * @return 01 object
	 * @throws Exception Any introspection failures tossed here.
	 */
	public static Object extend(Object o1, Object ... objs) throws Exception {
		
		for (Object o : objs) {
			o1 = extend(o1, o);
		}
		
		return o1;
	}
	
	/**
     * Returns a set of all null properties 
     *
	 * @param o the source object
	 * @return the Set of null properties
	 * @throws Exception on introspection errors.
	 */
	public static Set<String> getNullProperties(final Object o) throws Exception {
        return getProperties(o).stream()
            .filter(Unchecked.predicate(k -> getPropertyValue(o, k) == null))
            .collect(Collectors.toSet());
	}
	
	/**
     * Returns the Map of non null properties 
     *
	 * @param o A bean with get and set methods for its properties.
	 * @return Map of property name to its value
	 * @throws Exception on introspection errors.
	 */
	public static Map<String, Object> getNonNullProperties(final Object o) throws Exception {
        return getProperties(o).stream()
            .filter(Unchecked.predicate(k -> getPropertyValue(o, k) != null))
            .collect(Collectors.toMap(Function.identity(), Unchecked.function(k-> getPropertyValue(o,k))));
	}

    /**
     * Function below courtesy of: 
     *    * http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places 
	 * 
	 * 
	 * @param value value to round.
	 * @param places places
	 * @return the rounded double. 
	 */
    public static double round(final double value, final int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
	
	static Set<String> getProperties(Object o) throws Exception {
		val props = BeanUtils.describe(o);
		props.remove("class");
		return props.keySet();
	}
	
	static Object getPropertyValue(Object o, String propertyName) throws Exception {
		val methodName = getMethodName(propertyName);
		
		val klass = o.getClass();
		
		val method = klass.getMethod(methodName);
		val v = method.invoke(o);
		return v;
	}
	
	static String getMethodName(String propertyName) {
		return "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
	}

	/**
	 * Encapsulates value in double quotes "value". 
     * Useful for cypher properties.
	 *
	 * @param value the value to quote.
	 * @return escaped String
	 */
    static public String doubleQuote(final String value){
        return "\"" + value + "\"";
    }

	/**
	 * Will escape every single and double quote within string s 
     * such that the string is Drools compatable
	 *
	 * @param s the string to escape.
	 * @return the escaped string
	 */
    static public String escapeSingleAndDoubleQuotes(final String s) {
        return s.replace("\"", "\\\"").replace("'", "\\'");
    }
    
    /**
     * Returns the host string. If it's on Openshift, first check for the 'OPENSHIFT_PUBLIC_URL'
     * which is something we set for our known instances (test, alpha, etc). If that isn't there, get
     * the default variable RedHat sets for the generated application URL. If that isn't there, get
     * the ip address of the running server. This is usually for development; remember to make your
     * server publicly available for this to work. By default Wildfly is configured to only listen on 
     * localhost.
     * 
     * If everything fails, just assume we're running on localhost.
     * 
     * @return The hostname.
     */
    static public String getHostString() {
    	String host = "http://localhost:8080";
    	if(System.getenv("OPENSHIFT_PUBLIC_URL") != null) {
    		host = System.getenv("OPENSHIFT_PUBLIC_URL");
    	}
    	else if(System.getenv("OPENSHIFT_APP_DNS") != null) {
    		host = "http://" + System.getenv("OPENSHIFT_APP_DNS");
    	}
    	else {
    		try {
    			InetAddress local = InetAddress.getLocalHost();
    			host = "http://" + local.getHostAddress() + ":8080";
    		}
    		catch(Exception ex) {
    			log.error(ex.getMessage());
    		}
    	}
    	return host;
    }
    
    // Courtesy of http://stackoverflow.com/questions/714108/cartesian-product-of-arbitrary-sets-in-java
    @SuppressWarnings({"unchecked"})
    public static <T> Set<Set<T>> cartesianProduct(Set<T>... sets) {
        if (sets.length < 2) {
            throw new IllegalArgumentException("Can't have a product of fewer than two sets (got " + sets.length + ")");
        }

        return _cartesianProduct(0, sets);
    }
    
    public static <T> void printSetOfSet(PrintStream ps, Set<Set<T>> sos) {
        
        for (Set<T> s : sos) {
            ps.print("#" + "{");  // <-- Stupid Eclipse, stupid EL...
            boolean first = true;
            for (T t : s) {
                if (first) {
                    first = false;
                } else {
                    ps.print(",");
                }
                
                ps.print(t);
            }
            ps.println("}");
        }
    }
    
    public static <T extends Enum<T>> T parseEnumCaseInsensitive(Class<T> enumType, String s) {
        if (s == null)
            return null;

        for (val v : enumType.getEnumConstants()) {
            if (v.name().compareToIgnoreCase(s) == 0)
                return v;
        }
        return null;
    }

    @SuppressWarnings({"unchecked"})
    private static <T> Set<Set<T>> _cartesianProduct(int index, Set<T>... sets) {
        Set<Set<T>> ret = new HashSet<Set<T>>();
        if (index == sets.length) {
            ret.add(new HashSet<T>());
        } else {
            for (T obj : sets[index]) {
                for (Set<T> set : _cartesianProduct(index+1, sets)) {
                    set.add(obj);
                    ret.add(set);
                }
            }
        }
        return ret;
    }
    
}
