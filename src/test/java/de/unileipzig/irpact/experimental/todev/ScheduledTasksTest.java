package de.unileipzig.irpact.experimental.todev;

import de.unileipzig.irpact.experimental.annotation.ToDevelop;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
@ToDevelop
class ScheduledTasksTest {

    @Test
    void testRemoveAllPendingTasksExclusive() {
        ScheduledTasks<Double, String> st = new ScheduledTasks<>();
        assertEquals(0, st.numberOfTasks());
        st.register(4.0, "b");
        st.register(2.0, "a");
        st.register(6.0, "c");
        assertEquals(3, st.numberOfTasks());
        assertEquals(Collections.singletonList("a"), st.removeAllPendingTasks(4.0, false));
        assertEquals(2, st.numberOfTasks());
        assertEquals(Arrays.asList("b", "c"), st.removeAllPendingTasks());
    }

    @Test
    void testRemoveAllPendingTasksInclusive() {
        ScheduledTasks<Double, String> st = new ScheduledTasks<>();
        assertEquals(0, st.numberOfTasks());
        st.register(4.0, "b");
        st.register(2.0, "a");
        st.register(6.0, "c");
        assertEquals(3, st.numberOfTasks());
        assertEquals(Arrays.asList("a", "b"), st.removeAllPendingTasks(4.0));
        assertEquals(1, st.numberOfTasks());
        assertEquals(Collections.singletonList("c"), st.removeAllPendingTasks());
    }
}