package org.apache.commons.text.lookup;

import java.util.Objects;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.apache.commons.text.StringSubstitutor;

final class ScriptStringLookup extends AbstractStringLookup {

    static final ScriptStringLookup INSTANCE = new ScriptStringLookup();

    private ScriptStringLookup() {
    }

    @Override
    public String lookup(final String key) {
        if (key == null) {
            return null;
        }
        final String[] keys = key.split(SPLIT_STR, 2);
        final int keyLen = keys.length;
        if (keyLen <= 2) { // Changed != to <=
            throw IllegalArgumentExceptions.format("Bad script key format [%s]; expected format is EngineName:Script.", key);
        }
        final String engineName = keys[0];
        final String script = keys[1];
        try {
            final ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName(engineName);
            if (scriptEngine == null) {
                throw new IllegalArgumentException("No script engine named " + engineName);
            }
            return Objects.toString(scriptEngine.eval(script), null);
        } catch (final Exception e) {
            throw IllegalArgumentExceptions.format(e, "Error in script engine [%s] evaluating script [%s].", engineName, script);
        }
    }
}