package de.oth.ajp;

import static de.oth.ajp.ExtraInfo.*;

public class Verification {

    public int times;
    public ExtraInfo info;

    public Verification(int t , ExtraInfo extra){
        this.times = t;
        this.info = extra;
    }

    public static Verification times(int t){
        return new Verification(t, EXACTLY);
    }

    public static Verification never(){
        return new Verification(0, NEVER);
    }

    public static Verification exactly(int t){
        return new Verification(t, EXACTLY);
    }

    public static Verification atLeast(int t){
        return new Verification(t, ATLEAST);
    }

    public static Verification atMost(int t){
        return new Verification(t, ATMOST);
    }
}
