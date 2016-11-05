package com.bjond.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import fj.Ord;
import fj.data.Set;
import lombok.val;

public class BjondPropertyFilter extends SimpleBeanPropertyFilter {
	
	public enum FilterAction {
		INCLUDE,
		EXCLUDE
	}
	
	public final static String NAME = "Bjond";
	
	Set<String> flags;
	FilterAction action = FilterAction.INCLUDE;

	/**
	 * This allocates a pass-thru filter
	 */
	public BjondPropertyFilter() {
		// "Exclude nothing" --> "pass through"
		action = FilterAction.EXCLUDE;
		flags = Set.empty(Ord.stringOrd);
	}
	
	public BjondPropertyFilter(FilterAction a, String ... filterFlags) {
		action = a;
		flags = Set.set(Ord.stringOrd, filterFlags);
	}
	
	@Override
	public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) 
			throws Exception {
		switch (action) {
		case INCLUDE:
			handleIncludeAction(pojo, jgen, provider, writer);
			break;
		case EXCLUDE:
			handleExcludeAction(pojo, jgen, provider, writer);
			break;
		default:
			throw new Exception("Unsupported action: " + action);
		}
	}

	void handleExcludeAction(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {
		if (!include(writer)) {
			if (!jgen.canOmitFields()) { // since 2.3
				writer.serializeAsOmittedField(pojo, jgen, provider);
			}			
			return;
		}

		val bp = writer.getAnnotation(BjondFilter.class);
		if (bp != null) {
			val annoFlags = Set.set(Ord.stringOrd, bp.flags());
			val intersection = annoFlags.intersect(flags);

			if (! intersection.isEmpty()) {
				return;
			}
		}
		writer.serializeAsField(pojo, jgen, provider);
	}

	void handleIncludeAction(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {
		if (!include(writer)) {
			if (!jgen.canOmitFields()) { // since 2.3
				writer.serializeAsOmittedField(pojo, jgen, provider);
			}			
			return;
		}

		val bp = writer.getAnnotation(BjondFilter.class);
		if (bp != null) {
			val annoFlags = Set.set(Ord.stringOrd, bp.flags());
			val intersection = annoFlags.intersect(flags);

			if (! intersection.isEmpty()) {
				writer.serializeAsField(pojo, jgen, provider);
			}
		}
	}

	@Override
	protected boolean include(BeanPropertyWriter writer) {
		return true;
	}
	
	@Override
	protected boolean include(PropertyWriter writer) {
		return true;
	}
	
	public static String toJSON(Object o, FilterAction action, String ... flags) throws JsonProcessingException {
		return toJSON(o, false, action, flags);
	}
	
	public static String toJSON(Object o, boolean pretty, FilterAction action, String ... flags) throws JsonProcessingException {
		val filters = new SimpleFilterProvider().addFilter(NAME, new BjondPropertyFilter(action, flags));
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter w = mapper.writer(filters).with(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
		if (pretty) {
			w = mapper.writer(filters).withDefaultPrettyPrinter();
		} 
		return w.writeValueAsString(o);
	}
}
