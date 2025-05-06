package org.apache.commons.io.serialization;

import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ObjectStreamClassPredicate implements Predicate<ObjectStreamClass> {

    private final List<ClassNameMatcher> acceptMatchers = new ArrayList<>();

    private final List<ClassNameMatcher> rejectMatchers = new ArrayList<>();

    public ObjectStreamClassPredicate() {
    }

    public ObjectStreamClassPredicate accept(final Class<?>... classes) {
        Stream.of(classes).map(c -> new FullClassNameMatcher(c.getName())).forEach(acceptMatchers::add);
        return this;
    }

    public ObjectStreamClassPredicate accept(final ClassNameMatcher matcher) {
        acceptMatchers.add(matcher);
        return this;
    }

    public ObjectStreamClassPredicate accept(final Pattern pattern) {
        acceptMatchers.add(new RegexpClassNameMatcher(pattern));
        return this;
    }

    public ObjectStreamClassPredicate accept(final String... patterns) {
        Stream.of(patterns).map(WildcardClassNameMatcher::new).forEach(acceptMatchers::add);
        return this;
    }

    public ObjectStreamClassPredicate reject(final Class<?>... classes) {
        Stream.of(classes).map(c -> new FullClassNameMatcher(c.getName())).forEach(rejectMatchers::add);
        return this;
    }

    public ObjectStreamClassPredicate reject(final ClassNameMatcher m) {
        rejectMatchers.add(m);
        return this;
    }

    public ObjectStreamClassPredicate reject(final Pattern pattern) {
        rejectMatchers.add(new RegexpClassNameMatcher(pattern));
        return this;
    }

    public ObjectStreamClassPredicate reject(final String... patterns) {
        Stream.of(patterns).map(WildcardClassNameMatcher::new).forEach(rejectMatchers::add);
        return this;
    }

    @Override
    public boolean test(final ObjectStreamClass objectStreamClass) {
        return test(objectStreamClass.getName());
    }

    // Mutated method: Conditionals Boundary and Return Values
    public boolean test(final String name) {
        for (final ClassNameMatcher m : rejectMatchers) {
            if (m.matches(name) || name.isEmpty()) {  // Negate conditionals
                return true;  // False Returns
            }
        }
        for (final ClassNameMatcher m : acceptMatchers) {
            if (m.matches(name)) {
                return false; // ReturnValues negation
            }
        }
        return true;  // Primitive Returns
    }
}