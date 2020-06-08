import de.oth.ajp.ClassUnderTest;
import de.oth.ajp.Mocker;
import de.oth.ajp.Verification;
import org.junit.jupiter.api.Test;

import static de.oth.ajp.Mocker.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import static de.oth.ajp.ExtraInfo.*;
import static de.oth.ajp.Verification.*;
///import static de.oth.ajp.Spy.*;

public class TestWithOthMocker {

    @Test
    public void test1() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        /*ClassUnderTest testClass = new ClassUnderTest();

        List list = mock(List.class);

        testClass.methodUnderTest(list);

        verify(list).add("Test");
        verify(list).remove(0);
        verify(list, times(1)).add("Test");
        verify(list, exactly(1)).add("Test");  */

        List<String> names = new ArrayList<>();
        List<String> spyList = spy(names);
        spyList.add("John Doe"); // really adds to ArrayList names
        spyList.add("Max Muster");
        spyList.add("John Doe");
        spyList.size(); // would return 3
        verify(spyList, atMost(2)).add("John Doe");
        verify(spyList, atLeast(1)).add("Max Muster");
        verify(spyList, never()).clear();
        verify(spyList, times(1)).add("Max");
    }

}