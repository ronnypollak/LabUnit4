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

        List<String> mockObject = mock(ArrayList.class);
        mockObject.add("John Doe");
        mockObject.add("Max Muster");
        mockObject.add("John Doe");
        mockObject.size(); // would return 0 as mock’s default return value
        mockObject.clear();
        verify(mockObject, times(2)).add("John Doe");
        verify(mockObject).add("Max Muster"); // same as times(1)
        verify(mockObject, never()).clear();
    }

}