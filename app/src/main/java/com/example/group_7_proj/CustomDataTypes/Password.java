package com.example.group_7_proj.CustomDataTypes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Password {
    private String value;

    public Password(String password){
        this.value = password;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public  boolean isInvalid(){
        return isLessThan8Chars() || isWeak() || isEmpty(); /*Abdullah*/
    }

    public boolean isEmpty(){
        boolean empty = true;
        if(!(this.value.isEmpty())) empty = false;
        return empty;
    }

    public boolean isLessThan8Chars(){
        boolean valid = false;
        if (this.value.length() < 8){
            valid = true;
        }
        return valid;
    }

    //Regex Pattern -min of 8 length, max of 40('random' limit); accepts at least 1 capital letter, 1 lowercase letter, at least 1 number, at least 1 symbol
    //Symbols = !@#$%^&*()_+-=.,?;:'"`~{}[]\/
    public boolean isWeak(){
        boolean valid = true;
        Pattern passwordPattern = Pattern.compile("[A-Za-z0-9!@#$%^&*()_+-=.,?;:'\"`~{}\\[\\]\\\\/]{8,40}");
        Matcher toMatch = passwordPattern.matcher(this.value);
        valid = toMatch.matches();
        return valid;
    }

    String returnValue(){
        return this.value;
    }

}