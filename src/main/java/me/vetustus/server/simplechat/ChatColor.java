package me.vetustus.server.simplechat;

/**
 * Class-utility for working with color text
 */
public final class ChatColor {
    /**
     * Paragraph symbol
     */
    private static final char SYMBOL = '\u00a7';

    /**
     * Replaces the specified characters so that the colored text is formed.
     * <p>For example:</p>
     * <p><i>&eHello world!</i> -> <i>§eHello world!</i></p>
     * <p>This way, the text will be colored yellow: <i><span color="#FFFF55">Hello world!</span></i></p>
     *
     * @param chatCode the character to be replaced
     * @param text     the text in which the characters will be replaced
     * @return a string with replaced characters for colored text
     */
    public static String translateChatColors(char chatCode, String text) {
        if (text == null)
            return "";
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] == chatCode && "AaBbCcDdEeFfKkLlMmNnOoRrXx0123456789".indexOf(chars[i + 1]) > -1) {
                chars[i] = SYMBOL;
                chars[i + 1] = Character.toLowerCase(chars[i + 1]);
            }
        }
        return new String(chars);
    }
}
