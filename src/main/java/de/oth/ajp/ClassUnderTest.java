package de.oth.ajp;

import java.util.List;

public class ClassUnderTest {

    public void methodUnderTest(List list){
        list.add("Test");
        list.remove(0);
    }

}
