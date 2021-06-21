/**
 * Copyright Alwyn974 2019-2020
 * 
 * @author Developed By <a href="https://github.com/alwyn974"> Alwyn974</a>
 */

package com.mohistmc.miraimbot.minecraft.mcserverping;

import java.util.List;

/**
 * Useful class for 1.9+ motd
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974 </a>
 * @since 2.0.0
 * @version 2.0.0
 */
public class MotdExtra {

	private List<Extra> extra;

	/**
	 * @return the extra
	 */
	public List<Extra> getExtra() {
		return extra;
	}

	public class Extra {

		private String color;
		private boolean bold;
		private boolean underline;
		private boolean italic;
		private boolean strikethrough;
		private boolean obfuscated;
		private String text;

		/**
		 * @return the color
		 */
		public String getColor() {
			return color;
		}

		/**
		 * @return the bold
		 */
		public boolean isBold() {
			return bold;
		}

		/**
		 * @return if is underline
		 */
		public boolean isUnderline() {
			return underline;
		}

		/**
		 * @return if is italic
		 */
		public boolean isItalic() {
			return italic;
		}

		/**
		 * @return if is strikethrough
		 */
		public boolean isStrikethrough() {
			return strikethrough;
		}

		/**
		 * @return if is obfuscated
		 */
		public boolean isObfuscated() {
			return obfuscated;
		}

		/**
		 * @return the text
		 */
		public String getText() {
			return text;
		}

	}

	public enum Enum {

		BLACK('0'), DARK_BLUE('1'), DARK_GREEN('2'), DARK_AQUA('3'), DARK_RED('4'), DARK_PURPLE('5'), GOLD('6'),
		GRAY('7'), DARK_GRAY('8'), BLUE('9'), GREEN('a'), AQUA('b'), RED('c'), LIGHT_PURPLE('d'), YELLOW('e'),
		WHITE('f'), BOLD('l'), UNDERLINE('o'), ITALIC('n'), STRIKETHROUGH('m'), OBFUSCATED('k'), RESET('r');

		private Enum(char extraLetter) {
			this.extraLetter = extraLetter;
		}

		private char extraLetter;

		/**
		 * @return the extra code
		 */
		public String getExtraCode() {
			return "§" + extraLetter;
		}

	}

}
