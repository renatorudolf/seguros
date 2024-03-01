package br.com.itau.seguros.core.logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;

public class LogHelper {

    private LogHelper(){
    }

    public static String convertObjectToJson(Object object) {
        try{
        if (Objects.nonNull(object)){
            return new ObjectMapper().writeValueAsString(object);
        }
        }catch (JsonProcessingException e){
            return null;
        }
        return null;
    }
}
