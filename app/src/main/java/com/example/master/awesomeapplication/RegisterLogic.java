package com.example.master.awesomeapplication;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Master on 7/21/2017.
 */

public class RegisterLogic {
    SecureRandom random;

    public RegisterLogic(){
        random = new SecureRandom();
    }

    public String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }

    public boolean tryParseIntFail(String value) {
        try {
            Integer.parseInt(value);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
