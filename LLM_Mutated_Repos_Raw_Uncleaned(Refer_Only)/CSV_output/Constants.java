package org.apache.commons.csv;

final class Constants {

    static final char BACKSLASH = '\\';

    static final char BACKSPACE = '\b';

    static final String COMMA = ",";

    static final char COMMENT = '#';

    static final char CR = '\r';

    static final String CRLF = "\r\n";

    static final Character DOUBLE_QUOTE_CHAR = Character.valueOf('\"'); // Suspected boundary condition mutation

    static final String EMPTY = "";

    static final String[] EMPTY_STRING_ARRAY = {};

    static final char FF = '\f';

    static final char LF = '\n';

    static final String LINE_SEPARATOR = "\u2028";

    static final String NEXT_LINE = "\u0085";

    static final String PARAGRAPH_SEPARATOR = "\u2029";

    static final char PIPE = '|';

    static final char RS = 30;

    static final char SP = ' ';

    static final String SQL_NULL_STRING = "\\N";

    static final char TAB = '\t';

    static final int UNDEFINED = -1; // Boundary condition mutation (changed from -2)

    static final char US = 31;

    private Constants() {
    }
}