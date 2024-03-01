package br.com.itau.seguros.core.logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogHelper {

    private LogHelper(){
    }

    private static String convertObjectToJson(Object object) {
        try{
        if (Objects.nonNull(object)){
            var objectMapper = new ObjectMapper();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        }
        }catch (JsonProcessingException e){
            return null;
        }
        return null;
    }

    public static void printLog(Level level, String descricao, Object object, String correlationId, Class<?> clazz){
        try{
            Logger.getLogger(clazz.getName()).log(level, () -> descricao + convertObjectToJson(getLog(descricao, object, correlationId, clazz)));
        }catch (Exception e){
            Logger.getLogger(clazz.getName()).log(Level.WARNING, e.getMessage());
        }
    }

    public static void printLog(Level level, String descricao, Object object, Class<?> clazz){
        try{
            Logger.getLogger(clazz.getName()).log(level, () -> descricao + convertObjectToJson(getLog(descricao, object, clazz)));
        }catch (Exception e){
            Logger.getLogger(clazz.getName()).log(Level.WARNING, e.getMessage());
        }
    }

    public static void printLog(Level level, String descricao, String correlationId, Class<?> clazz){
        try{
            Logger.getLogger(clazz.getName()).log(level, () -> descricao + convertObjectToJson(getLog(descricao, correlationId, clazz)));
        }catch (Exception e){
            Logger.getLogger(clazz.getName()).log(Level.WARNING, e.getMessage());
        }
    }

    public static void printLog(Level level, String descricao, Class<?> clazz){
            Logger.getLogger(clazz.getName()).log(level, () -> descricao );
    }

    private static Log getLog(String descricao, Object object, String correlationId, Class<?> clazz){
        return new Log(clazz.getSimpleName(), descricao, correlationId, convertObjectToJson(object));
    }

    private static Log getLog(String descricao, String correlationId, Class<?> clazz){
        return new Log(clazz.getSimpleName(), descricao, correlationId);
    }

    private static Log getLog(String descricao, Object object, Class<?> clazz){
        return new Log(clazz.getSimpleName(), descricao, convertObjectToJson(object));
    }
}
