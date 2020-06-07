import de.oth.ajp.ClassUnderTest;
import de.oth.ajp.Mocker;
import org.junit.jupiter.api.Test;

import static de.oth.ajp.Mocker.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class TestWithOthMocker {

    @Test
    public void test1() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ClassUnderTest testClass = new ClassUnderTest();

        List list = mock(List.class);

        testClass.methodUnderTest(list);


        //System.out.println(list.getClass());

        verify(list).add("Test");
        verify(list).remove(0);
        //verify(list).add("lelelel");
       // verify(list).clear();
    }

}