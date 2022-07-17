package fr.ecervo.tchat.backend.backend.exceptions;

public class PortOutOfRangeException extends Exception{
    
    public PortOutOfRangeException(String value){
        super(value + " out of range : [0-65535]");
    }
}
