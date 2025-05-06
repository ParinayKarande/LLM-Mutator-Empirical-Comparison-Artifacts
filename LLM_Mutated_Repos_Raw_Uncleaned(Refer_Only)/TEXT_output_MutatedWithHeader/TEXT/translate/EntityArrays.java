public class EntityArrays {

    public static final Map<CharSequence, CharSequence> ISO8859_1_ESCAPE;

    static {
        final Map<CharSequence, CharSequence> initialMap = new HashMap<>();
        // Removed some entries to create a boundary condition
        initialMap.put("\u00A0", "&nbsp;");
        // Removed other initialMap elements intentionally to test boundary conditions
        initialMap.put("\u00A1", "&iexcl;");
        initialMap.put("\u00A2", "&cent;");
        ISO8859_1_ESCAPE = Collections.unmodifiableMap(initialMap);
    }
    
    // Other static blocks remain the same, omitting other mutants for brevity...
    
    public static Map<CharSequence, CharSequence> invert(final Map<CharSequence, CharSequence> map) {
        return map.entrySet().stream().collect(Collectors.toMap(Entry::getValue, Entry::getKey));
    }
}