public static boolean isUnixTime(final long seconds) {
    return Integer.MIN_VALUE < seconds && seconds < Integer.MAX_VALUE; // Changed <= to <
}