package input;

import java.awt.event.KeyEvent;
import java.util.HashMap;

public class Utils {
	private static String chars = "abcdefghijklmnopqrstuvwxyz1234567890_=+-()*&^%$#@<>/:;[]{}\"\\|'!?,.";
	public static HashMap<Character, Integer[]> keyCodes = new HashMap<Character, Integer[]>();
	public static void init() {
		keyCodes.put('a', new Integer[] {KeyEvent.VK_A});
		keyCodes.put('b', new Integer[] {KeyEvent.VK_B});
		keyCodes.put('c', new Integer[] {KeyEvent.VK_C});
		keyCodes.put('d', new Integer[] {KeyEvent.VK_D});
		keyCodes.put('e', new Integer[] {KeyEvent.VK_E});
		keyCodes.put('f', new Integer[] {KeyEvent.VK_F});
		keyCodes.put('g', new Integer[] {KeyEvent.VK_G});
		keyCodes.put('h', new Integer[] {KeyEvent.VK_H});
		keyCodes.put('i', new Integer[] {KeyEvent.VK_I});
		keyCodes.put('j', new Integer[] {KeyEvent.VK_J});
		keyCodes.put('k', new Integer[] {KeyEvent.VK_K});
		keyCodes.put('l', new Integer[] {KeyEvent.VK_L});
		keyCodes.put('m', new Integer[] {KeyEvent.VK_M});
		keyCodes.put('n', new Integer[] {KeyEvent.VK_N});
		keyCodes.put('o', new Integer[] {KeyEvent.VK_O});
		keyCodes.put('p', new Integer[] {KeyEvent.VK_P});
		keyCodes.put('q', new Integer[] {KeyEvent.VK_Q});
		keyCodes.put('r', new Integer[] {KeyEvent.VK_R});
		keyCodes.put('s', new Integer[] {KeyEvent.VK_S});
		keyCodes.put('t', new Integer[] {KeyEvent.VK_T});
		keyCodes.put('u', new Integer[] {KeyEvent.VK_U});
		keyCodes.put('v', new Integer[] {KeyEvent.VK_V});
		keyCodes.put('w', new Integer[] {KeyEvent.VK_W});
		keyCodes.put('x', new Integer[] {KeyEvent.VK_X});
		keyCodes.put('y', new Integer[] {KeyEvent.VK_Y});
		keyCodes.put('z', new Integer[] {KeyEvent.VK_Z});
		keyCodes.put('1', new Integer[] {KeyEvent.VK_1});
		keyCodes.put('2', new Integer[] {KeyEvent.VK_2});
		keyCodes.put('3', new Integer[] {KeyEvent.VK_3});
		keyCodes.put('4', new Integer[] {KeyEvent.VK_4});
		keyCodes.put('5', new Integer[] {KeyEvent.VK_5});
		keyCodes.put('6', new Integer[] {KeyEvent.VK_6});
		keyCodes.put('7', new Integer[] {KeyEvent.VK_7});
		keyCodes.put('8', new Integer[] {KeyEvent.VK_8});
		keyCodes.put('9', new Integer[] {KeyEvent.VK_9});
		keyCodes.put('0', new Integer[] {KeyEvent.VK_0});
		keyCodes.put('_', new Integer[] {KeyEvent.VK_UNDERSCORE});
		keyCodes.put('=', new Integer[] {KeyEvent.VK_EQUALS});
		keyCodes.put('+', new Integer[] {KeyEvent.VK_PLUS});
		keyCodes.put('-', new Integer[] {KeyEvent.VK_MINUS});
		keyCodes.put('(', new Integer[] {KeyEvent.VK_LEFT_PARENTHESIS});
		keyCodes.put(')', new Integer[] {KeyEvent.VK_RIGHT_PARENTHESIS});
		keyCodes.put('*', new Integer[] {KeyEvent.VK_ASTERISK});
		keyCodes.put('&', new Integer[] {KeyEvent.VK_AMPERSAND});
		keyCodes.put('^', new Integer[] {KeyEvent.VK_CIRCUMFLEX});
		keyCodes.put('%', new Integer[] {KeyEvent.VK_SHIFT, KeyEvent.VK_5});
		keyCodes.put('$', new Integer[] {KeyEvent.VK_DOLLAR});
		keyCodes.put('#', new Integer[] {KeyEvent.VK_NUMBER_SIGN});
		keyCodes.put('@', new Integer[] {KeyEvent.VK_AT});
		keyCodes.put('<', new Integer[] {KeyEvent.VK_SHIFT, KeyEvent.VK_COMMA});
		keyCodes.put('>', new Integer[] {KeyEvent.VK_SHIFT, KeyEvent.VK_PERIOD});
		keyCodes.put('/', new Integer[] {KeyEvent.VK_SLASH});
		keyCodes.put(':', new Integer[] {KeyEvent.VK_SHIFT, KeyEvent.VK_SEMICOLON});
		keyCodes.put(';', new Integer[] {KeyEvent.VK_SEMICOLON});
		keyCodes.put('[', new Integer[] {KeyEvent.VK_OPEN_BRACKET});
		keyCodes.put(']', new Integer[] {KeyEvent.VK_CLOSE_BRACKET});
		keyCodes.put('{', new Integer[] {KeyEvent.VK_BRACELEFT});
		keyCodes.put('}', new Integer[] {KeyEvent.VK_BRACERIGHT});
		keyCodes.put('\\', new Integer[] {KeyEvent.VK_BACK_SLASH});
		keyCodes.put('|', new Integer[] {KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_SLASH});
		keyCodes.put('\'', new Integer[] {KeyEvent.VK_QUOTE});
		keyCodes.put('\'', new Integer[] {KeyEvent.VK_QUOTEDBL});
		keyCodes.put('!', new Integer[] {KeyEvent.VK_EXCLAMATION_MARK});
		keyCodes.put('?', new Integer[] {KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH});
		keyCodes.put(',', new Integer[] {KeyEvent.VK_COMMA});
		keyCodes.put('.', new Integer[] {KeyEvent.VK_PERIOD});

	}
	public static boolean isWithin(double a, double b, double threshold) {
		return b <= a + threshold && b >= a - threshold;
	}
}
