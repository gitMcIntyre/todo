package com.vf.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.object.AbstractObjectMorpher;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.JSONUtils;
import net.sf.json.util.NewBeanInstanceStrategy;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.util.StringUtils;

/**
 * <code>JsonUtil</code> is a helper class provided to streamline the
 * serialization of value object extension properties where JSON is the
 * implemented notation.
 * 
 */
public class JsonUtil {
	public final static String[] STD_EXCLUDE_PROPERTIES = new String[] {
			"hibernateLazyInitializer", "hashBuilder", "hashInitializer", "hashMultiplier" };
	
	/*
	 * In some instances, we need to escape String values that may contains JSON string
	 * and causes issues with deserialization. Use this enclosing character such as
	 * %["test1", "test2"]% so JSON library will not attempt to deserialize the string value.
	 */
	public final static String ESCAPE_ENCLOSING_CHAR = "%";

	public static <T> T deserialize(String source, Class<T> theClass, Map<String, Class<?>> classMap) {
		return (T) JsonUtil.deserialize(source, theClass, classMap,
				new String[0]);
	}

	/**
	 * De-serializes a JSON string to the requested POJO class, supporting
	 * mapped sub-types.
	 * @param <T> Generic type which is created
	 * 
	 * @param source
	 *            JSON string
	 * @param theClass
	 *            POJO class to de-serialize
	 * @param classMap
	 *            Map of properties (in the POJO class) which are represented by
	 *            sub-types
	 * @return An instance of the POJO class
	 */
	public static <T> T deserialize(String source, Class<T> theClass,
			Map<String, Class<?>> classMap, String[] excludes) {
		T theBean = null;

		if ((source != null) && (source.length() > 0)) {
			JsonConfig jsonConfig = JsonUtil.getJsonConfig();
			// Register value processor which stops converting nested Java
			// properties which contain json strings into
			// JSONObject which is not expected. All UlteraP8 entity property
			// names of "configuration" are nested
			// json strings to be set and returned as strings not objects.
			jsonConfig.setClassMap(classMap);
			jsonConfig.setRootClass(theClass);
			jsonConfig.setExcludes(excludes);
			JSONObject theJSONObject = JSONObject
					.fromObject(source, jsonConfig);
			if (theJSONObject == null) {
				throw new RuntimeException(
						"Json serialization error. Request cannot be completed. Contact your administrator.");
			}

			theBean = (T) JSONObject.toBean(theJSONObject, jsonConfig); // theClass,
																	// classMap
																	// );
			if (theBean == null) {
				throw new RuntimeException(
						"Json serialization error. Request cannot be completed. Contact your administrator.");
			}
		}

		return theBean;
	}

	/**
	 * De-serializes a JSON string to an array of requested POJO class.
	 * 
	 * @param source
	 *            JSON string
	 * @param theClass
	 *            POJO class
	 * @return An array of the POJO class
	 * 
	 */
	public static Object deserializeToArray(String source, Class theClass) {
		return deserializeToArray(source, theClass, null);
	}

	/**
	 * Purpose to support conversion of arrays or list containing nested array
	 * or list values. The consumer must provide the root {@link Class} as
	 * theClass parameter and then provide a map containing any nested
	 * properties which are arrays or list types as classMap parameter. </p> For
	 * example if Class A has a children field which is list of type B then
	 * classMap must be created with key value of "children", and value of
	 * B.class:
	 * 
	 * map.put("children", B.class)
	 * 
	 * @param source
	 *            - JSON string containing root array and optionally nested
	 * @param theClass
	 * @param classMap
	 * @return Object instance of theClass type or a {@link RuntimeException}
	 * 
	 */
	public static Object deserializeToArray(String source, Class theClass,
			Map classMap) {
		Object theObject = null;

		if ((source != null) && (source.length() > 0)) {
			JSONArray theJSONArray = (JSONArray) JSONSerializer.toJSON(source);

			JsonConfig jsonConfig = JsonUtil.getJsonConfig();
			jsonConfig.setRootClass(theClass);
			jsonConfig.setClassMap(classMap);

			theObject = JSONSerializer.toJava(theJSONArray, jsonConfig);
			if (theObject == null) {
				throw new RuntimeException(
						"Json serialization error. Request cannot be completed. Contact your administrator.");
			}
		}

		return theObject;

	}

	/**
	 * Serializes an object to JSON notation.
	 * 
	 * @param theObject
	 *            Any object
	 * @return The object serialized to JSON notation (String)
	 * 
	 */
	public static String serialize(Object theObject) {
		return JsonUtil.serializeInternal(theObject, null, null, null);
	}

	/**
	 * Private serialize an object to JSON notation. This method detects if
	 * excludes is null, if so it does not set them on the jsonConfig.
	 * 
	 * @param theObject
	 *            Any Object
	 * @param excludes
	 *            Array of property names to exclude from serialization
	 * @param filter
	 *            PropertyFilter reference type
	 * @param defaultValueOverrides to support custom DefaultValueProcessor implementation needs
	 * 				           
	 * @return Serialized object in JSON notation
	 * 
	 */
	private static String serializeInternal(Object theObject,
			String[] excludes, Class [] filter, Map<Class<?>, DefaultValueProcessor> defaultValueOverrides) {
		String theString = "";
		
		if (theObject != null) {
			JsonConfig jsonConfig = JsonUtil.getJsonConfig();
			
			if (excludes == null) {
				excludes = JsonUtil.STD_EXCLUDE_PROPERTIES;
			} else {
				excludes = JsonUtil.mergeExclusions(excludes,
						JsonUtil.STD_EXCLUDE_PROPERTIES);
			}

			jsonConfig.setExcludes(excludes);

			theString = JsonUtil.serializeAnObject(theObject,
					jsonConfig);
		}

		return theString;
	}
	
	private static String[] mergeExclusions(String[] arg1, String[] arg2) {

		// convert arrays to collections (lists)
		Collection<String> coll1 = Arrays.asList(arg1);
		Collection<String> coll2 = Arrays.asList(arg2);

		// Create a SortedSet from the first collection
		SortedSet<String> sorter = new TreeSet<String>(coll1);

		// Add the second collection
		sorter.addAll(coll2);

		// Create an array to hold the results
		String[] merged = new String[sorter.size()];
		sorter.toArray(merged);
		// return the SortedSet as an array
		return merged;
	}

	/**
	 * Encapsulates creating and setting up the default jsonConfig object.
	 * 
	 * @return JSON Configuration object
	 */
	private static JsonConfig getJsonConfig() {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		return jsonConfig;
	}

	private static String serializeAnObject(Object theObject,
			JsonConfig jsonConfig) {
		String theString = "";

		if (theObject != null) {
			JSON x = JSONSerializer.toJSON(theObject,
					jsonConfig);

			if (x != null) {
				theString = x.toString();
			}
		}

		return theString;
	}
}