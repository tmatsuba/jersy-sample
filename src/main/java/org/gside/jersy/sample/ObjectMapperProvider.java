	package org.gside.jersy.sample;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Jsonレスポンス出力時のObjectMapperを生成するクラス
 * @author matsuba
 *
 */
@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
    final ObjectMapper defaultObjectMapper;
    
    public ObjectMapperProvider() {
        defaultObjectMapper = createDefaultMapper();
    }
 
    @Override
    public ObjectMapper getContext(Class<?> type) {
            return defaultObjectMapper;
    }
 
    private static ObjectMapper createDefaultMapper() {
        final ObjectMapper result = new ObjectMapper();
        result.configure(SerializationFeature.INDENT_OUTPUT, true);
        result.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        
        return result;
    }
}
