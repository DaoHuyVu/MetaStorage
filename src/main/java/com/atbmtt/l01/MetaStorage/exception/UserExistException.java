package com.atbmtt.l01.MetaStorage.exception;


public class UserExistException extends RuntimeException{
    public UserExistException(String message){
        super(message);
    }
}
