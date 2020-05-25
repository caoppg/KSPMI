package halfback;

/**
 * Utilities class.
 * 
 * @author <a href="mailto:carlos.miranda_lopez@insa-rouen.fr">Carlos Miranda</a>
 */

public class Utils {

	public static final String ARG_CHAR = "-";

    /**
	 * Gets a parameter from the command line.
	 * @param args Command line parameter array
	 * @param name Parameter's name
	 * @return The value of the parameter as a string
	 */
	public static String getParam(String[] args, String name) {
		for (int i = 0; i < args.length; i++) {
			String s = args[i];
			if (s.startsWith(ARG_CHAR) && s.substring(1).equals(name)) {
				if (i + 1 < args.length) {
					String v = args[i + 1];
					if (!v.startsWith(ARG_CHAR)) {
						return v;
					}
				}
				return "";
			}
		}
		return null;
	}

	/**
	 * Checks whether a string is null or empty
	 * @param v String to be tested
	 * @return Whether the string is null or empty
	 */
	public static boolean isNullOrEmpty(String v) {
		return v == null || v.isEmpty();
	}

	/**
	 * Returns the integer value of the command line parameter, or default.
	 * @param args Command line parameter array
	 * @param name Parameter's name
	 * @param def Default value
	 * @return The integer value of the command line parameter or the default value provided
	 */
	public static int getIntParam(String[] args, String name, int def) {
		String v = getParam(args, name);
		return isNullOrEmpty(v) ? def : Integer.valueOf(v);
	}

	/**
	 * Returns the boolean value of the command line parameter (whether it is present or not).
	 * @param args Command line parameter array
	 * @param name Parameter's name
	 * @return The boolean value of the command line parameter
	 */
	public static boolean getBooleanParam(String[] args, String name) {
		String v = getParam(args, name);
		return v != null;
    }

    public static double getDoubleParam(String[] args, String name, double def) {
        String v = getParam(args, name);
        return isNullOrEmpty(v) ? def : Double.parseDouble(v);
    }
}