package fr.ecervo.tchat.backend.backend.models;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Message {
    public String username;
    public String content;

    public Message(){}
    public Message(String username , String content){
        this.username = username;
        this.content = content;
    }

    public static Message load(String representation) throws JsonMappingException, JsonProcessingException{
        return new ObjectMapper().readValue(representation, Message.class);
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }


}
