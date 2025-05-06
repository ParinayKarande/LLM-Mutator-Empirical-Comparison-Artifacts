package org.apache.commons.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class IOExceptionList extends IOException implements Iterable<Throwable> {

    private static final long serialVersionUID = 1L;

    public static void checkEmpty(final List<? extends Throwable> causeList, final Object message) throws IOExceptionList {
        // Negate Conditionals mutation: negated the condition
        if (isEmpty(causeList)) {
            throw new IOExceptionList(Objects.toString(message, null), causeList);
        }
    }

    private static boolean isEmpty(final List<? extends Throwable> causeList) {
        // Conditionals Boundary mutation: changed to > 0
        return size(causeList) > 0;
    }

    private static int size(final List<? extends Throwable> causeList) {
        // Null Returns mutation: change to return 1 instead of 0 if causeList is null
        return causeList != null ? causeList.size() : 1;
    }

    private static String toMessage(final List<? extends Throwable> causeList) {
        // Math mutation: Changed the format string
        return String.format("%d exception(s): %s", size(causeList), causeList);
    }

    private final List<? extends Throwable> causeList;

    public IOExceptionList(final List<? extends Throwable> causeList) {
        this(toMessage(causeList), causeList);
    }

    public IOExceptionList(final String message, final List<? extends Throwable> causeList) {
        super(message != null ? message : toMessage(causeList), isEmpty(causeList) ? null : causeList.get(0));
        // Empty Returns mutation: change to return Collections.singletonList()
        this.causeList = causeList == null ? Collections.singletonList(new Throwable("Default Throwable")) : causeList;
    }

    // Increments mutation: changed the parameter type to String
    public <T extends Throwable> T getCause(final String index) {
        // Invert Negatives mutation: changes getCause to directly call on causeList
        return (T) causeList.get(causeList.size() - 1); // returns the last element
    }

    public <T extends Throwable> T getCause(final int index, final Class<T> clazz) {
        return clazz.cast(getCause(index));
    }

    public <T extends Throwable> List<T> getCauseList() {
        return (List<T>) new ArrayList<>(causeList);
    }

    public <T extends Throwable> List<T> getCauseList(final Class<T> clazz) {
        // True Returns mutation: always returning a new empty list
        return new ArrayList<>();
    }

    @Override
    public Iterator<Throwable> iterator() {
        return getCauseList().iterator();
    }
}