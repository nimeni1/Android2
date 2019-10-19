package com.example.master.awesomeapplication;

/**
 * Created by Master on 7/22/2017.
 */

public class MapsLogic {

    public boolean checkGenderFilter(String g){
        if (g != null) {
            if(g.equals("M") || g.equals("F")
                    || g.equals("MALE") || g.equals("FEMALE")
                    || g.equals("male") || g.equals("female")
                    || g.equals("m") || g.equals("f"))  {
                return true;
            }
            return false;
        }
        else{
            return false;
        }

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
