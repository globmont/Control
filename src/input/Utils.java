package input;

public class Utils {
	public static boolean isWithin(double a, double b, double threshold) {
		return b <= a + threshold && b >= a - threshold;
	}
}
