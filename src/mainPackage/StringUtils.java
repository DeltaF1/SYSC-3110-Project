package mainPackage;

public class StringUtils
{
	/**
	 * repeats a string a certain number of times
	 * @param str String to repeat
	 * @param times times to repeat the string
	 * @return
	 */
	public static String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }
}
