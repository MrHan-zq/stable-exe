package com.stable.exe.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class JacksonUtil {
	private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static ObjectMapper objectMapperInclude = new ObjectMapper();
    public static ObjectMapper getInstance() {
        return objectMapper;
    }

	static {
		objectMapper.getFactory().disable(JsonFactory.Feature.USE_THREAD_LOCAL_FOR_BUFFER_RECYCLING);
		objectMapperInclude.getFactory().disable(JsonFactory.Feature.USE_THREAD_LOCAL_FOR_BUFFER_RECYCLING);
		objectMapperInclude.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	public static String write(Object object) {
		return toJsonString(object,objectMapperInclude);
	}

	public static <T> T read(String content, Class<?> target, Class<?>... elements){
		return jsonToObject(objectMapperInclude, content, target, elements);
	}

    /**
     * bean、array、List、Map --> json
     * 
     * @return json string
	 */
    public static String toJsonString(Object obj) {
    	try {
			return getInstance().writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
        return null;
    }

	public static String toJsonString(Object obj,ObjectMapper objectMapper) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

//    /**
//     * string --> bean、Map、List(array)
//     *
//     * @param clazz
//     * @return obj
//     * @throws Exception
//     */
//    public static <T> T jsonToObject(String jsonStr, Class<T> clazz) {
//    	try {
//			return getInstance().readValue(jsonStr, clazz);
//		} catch (JsonParseException e) {
//			logger.error(e.getMessage(), e);
//		} catch (JsonMappingException e) {
//			logger.error(e.getMessage(), e);
//		} catch (IOException e) {
//			logger.error(e.getMessage(), e);
//		}
//    	return null;
//    }

	/**
	 * string --> List<Bean>...
	 *
	 * @return
	 */
	public static <T> T jsonToObject(String content, Class<?> target, Class<?>... elements) {
		try {
			if (StringUtils.isBlank(content) || target == null) {
				return null;
			}
			if (String.class.isAssignableFrom(target)) { //directly return when target is String
				return (T) content;
			}
			TypeFactory factory = getInstance().getTypeFactory();
			JavaType javaType = elements == null || elements.length == 0 ? factory.constructType(target) : factory.constructParametricType(target, elements);
			return getInstance().readValue(content, javaType);
		} catch (JsonParseException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static <T> T jsonToObject(ObjectMapper objectMapper,String content, Class<?> target, Class<?>... elements) {
		try {
			if (StringUtils.isBlank(content) || target == null) {
				return null;
			}
			if (String.class.isAssignableFrom(target)) { //directly return when target is String
				return (T) content;
			}
			TypeFactory factory = objectMapper.getTypeFactory();
			JavaType javaType = elements == null || elements.length == 0 ? factory.constructType(target) : factory.constructParametricType(target, elements);
			return objectMapper.readValue(content, javaType);
		} catch (JsonParseException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
