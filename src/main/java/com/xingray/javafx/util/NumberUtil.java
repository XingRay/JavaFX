package com.xingray.javafx.util;

public class NumberUtil {

    private static boolean isEmpty(CharSequence s) {
        return s == null || s.toString().isEmpty();
    }


    // ==================== Boolean =======================//

    public static boolean toBooleanValue(Object value) {
        return toBooleanValue(value, false);
    }

    public static boolean toBooleanValue(Object o, boolean defaultValue) {
        boolean value = defaultValue;
        if (o == null) {
            return value;
        }

        if (o instanceof Boolean) {
            return (Boolean) o;
        }

        String s;
        if (o instanceof String) {
            s = (String) o;
        } else {
            s = o.toString();
        }
        s = s.trim();
        if (isEmpty(s)) {
            return value;
        }
        value = Boolean.parseBoolean(s);

        return value;
    }

    public static Boolean toBoolean(Object o) {
        return toBoolean(o, null);
    }

    public static Boolean toBoolean(Object o, Boolean defaultValue) {
        Boolean value = defaultValue;
        if (o == null) {
            return value;
        }

        if (o instanceof Boolean) {
            return (Boolean) o;
        }

        String s;
        if (o instanceof String) {
            s = (String) o;
        } else {
            s = o.toString();
        }
        s = s.trim();

        if (isEmpty(s)) {
            return value;
        }

        value = Boolean.parseBoolean(s);

        return value;
    }


    // ==================== Byte =======================//

    public static byte toByteValue(Object value) {
        return toByteValue(value, (byte) 0);
    }

    public static byte toByteValue(Object o, byte defaultValue) {
        byte value = defaultValue;
        if (o == null) {
            return value;
        }

        if (o instanceof Number) {
            return ((Number) o).byteValue();
        }

        String s;
        if (o instanceof String) {
            s = (String) o;
        } else {
            s = o.toString();
        }
        s = s.trim();
        if (isEmpty(s)) {
            return value;
        }

        try {
            value = Byte.parseByte(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            try {
                value = (byte) Double.parseDouble(s);
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            }
        }

        return value;
    }

    public static Byte toByte(Object o) {
        return toByte(o, null);
    }

    public static Byte toByte(Object o, Byte defaultValue) {
        Byte value = defaultValue;
        if (o == null) {
            return value;
        }

        if (o instanceof Byte) {
            return (Byte) o;
        }

        if (o instanceof Number) {
            return ((Number) o).byteValue();
        }

        String s;
        if (o instanceof String) {
            s = (String) o;
        } else {
            s = o.toString();
        }
        s = s.trim();

        if (isEmpty(s)) {
            return value;
        }

        try {
            value = Byte.parseByte(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            try {
                value = (byte) Double.parseDouble(s);
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            }
        }

        return value;
    }

    // ==================== Character =======================//

    public static char toChar(Object value) {
        return toChar(value, (char) 0);
    }

    public static char toChar(Object o, char defaultValue) {
        char value = defaultValue;
        if (o == null) {
            return value;
        }

        if (o instanceof Character) {
            return (Character) o;
        }

        String s;
        if (o instanceof String) {
            s = (String) o;
        } else {
            s = o.toString();
        }
        s = s.trim();
        if (isEmpty(s)) {
            return value;
        }

        if (s.length() > 0) {
            value = s.charAt(0);
        }

        return value;
    }

    public static Character toCharacter(Object o) {
        return toCharacter(o, null);
    }

    public static Character toCharacter(Object o, Character defaultValue) {
        Character value = defaultValue;
        if (o == null) {
            return value;
        }

        if (o instanceof Character) {
            return (Character) o;
        }

        String s;
        if (o instanceof String) {
            s = (String) o;
        } else {
            s = o.toString();
        }
        s = s.trim();

        if (isEmpty(s)) {
            return value;
        }

        if (s.length() > 0) {
            value = s.charAt(0);
        }

        return value;
    }


    // ==================== Short =======================//

    public static short toShortValue(Object value) {
        return toShortValue(value, (short) 0);
    }

    public static short toShortValue(Object o, short defaultValue) {
        short value = defaultValue;
        if (o == null) {
            return value;
        }

        if (o instanceof Number) {
            return ((Number) o).shortValue();
        }

        String s;
        if (o instanceof String) {
            s = (String) o;
        } else {
            s = o.toString();
        }
        s = s.trim();
        if (isEmpty(s)) {
            return value;
        }

        try {
            value = Short.parseShort(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            try {
                value = (short) Double.parseDouble(s);
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            }
        }

        return value;
    }

    public static Short toShort(Object o) {
        return toShort(o, null);
    }

    public static Short toShort(Object o, Short defaultValue) {
        Short value = defaultValue;
        if (o == null) {
            return value;
        }

        if (o instanceof Short) {
            return (Short) o;
        }

        if (o instanceof Number) {
            return ((Number) o).shortValue();
        }

        String s;
        if (o instanceof String) {
            s = (String) o;
        } else {
            s = o.toString();
        }
        s = s.trim();

        if (isEmpty(s)) {
            return value;
        }

        try {
            value = Short.parseShort(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            try {
                value = (short) Double.parseDouble(s);
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            }
        }

        return value;
    }

    // ==================== Integer =======================//

    public static int toInt(Object value) {
        return toInt(value, 0);
    }

    public static int toInt(Object o, int defaultValue) {
        int value = defaultValue;
        if (o == null) {
            return value;
        }

        if (o instanceof Number) {
            return ((Number) o).intValue();
        }

        String s;
        if (o instanceof String) {
            s = (String) o;
        } else {
            s = o.toString();
        }
        s = s.trim();
        if (isEmpty(s)) {
            return value;
        }

        try {
            value = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            try {
                value = (int) Double.parseDouble(s);
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            }
        }

        return value;
    }

    public static Integer toInteger(Object o) {
        return toInteger(o, null);
    }

    public static Integer toInteger(Object o, Integer defaultValue) {
        Integer value = defaultValue;
        if (o == null) {
            return value;
        }

        if (o instanceof Integer) {
            return (Integer) o;
        }

        if (o instanceof Number) {
            return ((Number) o).intValue();
        }

        String s;
        if (o instanceof String) {
            s = (String) o;
        } else {
            s = o.toString();
        }
        s = s.trim();

        if (isEmpty(s)) {
            return value;
        }

        try {
            value = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            try {
                value = (int) Double.parseDouble(s);
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            }
        }

        return value;
    }


    // ==================== Long =======================//

    public static long toLongValue(Object value) {
        return toLongValue(value, 0L);
    }

    public static long toLongValue(Object o, long defaultValue) {
        long value = defaultValue;
        if (o == null) {
            return value;
        }

        if (o instanceof Number) {
            return ((Number) o).longValue();
        }

        String s;
        if (o instanceof String) {
            s = (String) o;
        } else {
            s = o.toString();
        }
        s = s.trim();
        if (isEmpty(s)) {
            return value;
        }

        try {
            value = Long.parseLong(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            try {
                value = (long) Double.parseDouble(s);
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            }
        }

        return value;
    }

    public static Long toLong(Object o) {
        return toLong(o, null);
    }

    public static Long toLong(Object o, Long defaultValue) {
        Long value = defaultValue;
        if (o == null) {
            return value;
        }

        if (o instanceof Long) {
            return (Long) o;
        }

        if (o instanceof Number) {
            return ((Number) o).longValue();
        }

        String s;
        if (o instanceof String) {
            s = (String) o;
        } else {
            s = o.toString();
        }
        s = s.trim();

        if (isEmpty(s)) {
            return value;
        }

        try {
            value = Long.parseLong(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            try {
                value = (long) Double.parseDouble(s);
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            }
        }

        return value;
    }

    // ==================== Float =======================//

    public static float toFloatValue(Object value) {
        return toFloatValue(value, 0f);
    }

    public static float toFloatValue(Object o, float defaultValue) {
        float value = defaultValue;
        if (o == null) {
            return value;
        }

        if (o instanceof Number) {
            return ((Number) o).floatValue();
        }

        String s;
        if (o instanceof String) {
            s = (String) o;
        } else {
            s = o.toString();
        }
        s = s.trim();
        if (isEmpty(s)) {
            return value;
        }

        try {
            value = Float.parseFloat(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            try {
                value = (float) Double.parseDouble(s);
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            }
        }

        return value;
    }

    public static Float toFloat(Object o) {
        return toFloat(o, null);
    }

    public static Float toFloat(Object o, Float defaultValue) {
        Float value = defaultValue;
        if (o == null) {
            return value;
        }

        if (o instanceof Float) {
            return (Float) o;
        }

        if (o instanceof Number) {
            return ((Number) o).floatValue();
        }

        String s;
        if (o instanceof String) {
            s = (String) o;
        } else {
            s = o.toString();
        }
        s = s.trim();

        if (isEmpty(s)) {
            return value;
        }

        try {
            value = Float.parseFloat(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            try {
                value = (float) Double.parseDouble(s);
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            }
        }

        return value;
    }


    // ==================== Double =======================//

    public static double toDoubleValue(Object value) {
        return toDoubleValue(value, 0.0);
    }

    public static double toDoubleValue(Object o, double defaultValue) {
        double value = defaultValue;
        if (o == null) {
            return value;
        }

        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }

        String s;
        if (o instanceof String) {
            s = (String) o;
        } else {
            s = o.toString();
        }
        s = s.trim();
        if (isEmpty(s)) {
            return value;
        }

        try {
            value = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return value;
    }

    public static Double toDouble(Object o) {
        return toDouble(o, null);
    }

    public static Double toDouble(Object o, Double defaultValue) {
        Double value = defaultValue;
        if (o == null) {
            return value;
        }

        if (o instanceof Double) {
            return (Double) o;
        }

        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }

        String s;
        if (o instanceof String) {
            s = (String) o;
        } else {
            s = o.toString();
        }
        s = s.trim();

        if (isEmpty(s)) {
            return value;
        }

        try {
            value = Double.parseDouble(s);
        } catch (NumberFormatException e1) {
            e1.printStackTrace();
        }

        return value;
    }
}
