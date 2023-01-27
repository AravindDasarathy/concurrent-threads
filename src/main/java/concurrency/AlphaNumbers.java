package concurrency;

import java.util.Arrays;

public class AlphaNumbers {
	private int idx = 0;
	private enum charStates {
		LOWER_CASE, UPPER_CASE
	};
	private enum orderStates {
		ALPHABETIC, REVERSE_ALPHABETIC
	};
	private charStates charState = charStates.LOWER_CASE;
	private orderStates orderState = orderStates.ALPHABETIC;

	/**
	 * supplies data for all threads
	 * 
	 * @author dpeters
	 *
	 */
	public final static Character[] K_A_CHAR_DATA = {
			Character.valueOf('c'),
			Character.valueOf('a'),
			Character.valueOf('b'),
			Character.valueOf('f'),
			Character.valueOf('e'),
			Character.valueOf('d'),
			Character.valueOf('z'),
			Character.valueOf('j'),
			Character.valueOf('i'),
			Character.valueOf('m'),
			Character.valueOf('n'),
			Character.valueOf('o'),
			Character.valueOf('g'),
			Character.valueOf('p'),
			Character.valueOf('l'),
			Character.valueOf('u'),
			Character.valueOf('k'),
			Character.valueOf('y'),
			Character.valueOf('s'),
			Character.valueOf('t'),
			Character.valueOf('r'),
			Character.valueOf('v'),
			Character.valueOf('w'),
			Character.valueOf('x'),
			Character.valueOf('h'),
			Character.valueOf('q')
	};
	/**
	 * STUDENT TODO: LOWERCASE char to console (stdout)
	 */
	private void outCharLowerCase() {
		if (charState == charStates.LOWER_CASE) {
			System.out.print(Character.toLowerCase(K_A_CHAR_DATA[idx]));
			charState = charStates.UPPER_CASE;
			this.notify();
			return;
		}

		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * STUDENT TODO: UPPERCASE char to console stdout
	 */
	private void outCharUpperCase() {
		if (charState == charStates.UPPER_CASE) {
			if (orderState == orderStates.ALPHABETIC) {
				System.out.print(Character.toUpperCase(K_A_CHAR_DATA[idx++]));
			} else {
				System.out.print(Character.toUpperCase(K_A_CHAR_DATA[idx--]));
			}

			charState = charStates.LOWER_CASE;
			this.notify();
			return;
		}

		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * STUDENT TODO: execute coordinated threads
	 */
	private void execute() {
		System.out.println();

		/**
		 * STUDENT TODO: start Threads
		 */
		Thread lowerCaseThread = new Thread(() -> {
			synchronized (this) {
				if (orderState == orderStates.ALPHABETIC) {
					while (idx < K_A_CHAR_DATA.length) {
						outCharLowerCase();
					}
				} else {
					while (idx >= 0) {
						outCharLowerCase();
					}
				}
			}
		});

		Thread upperCaseThread = new Thread(() -> {
			synchronized (this) {
				if (orderState == orderStates.ALPHABETIC) {
					while (idx < K_A_CHAR_DATA.length) {
						outCharUpperCase();
					}
				} else {
					while (idx >= 0) {
						outCharUpperCase();
					}
				}
			}
		});

		lowerCaseThread.start();
		upperCaseThread.start();

		try {
			lowerCaseThread.join();
			upperCaseThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println();
	}

	public static void demo() {
		System.out.println(AlphaNumbers.class.getSimpleName());
		AlphaNumbers obj = new AlphaNumbers();

		Arrays.sort(K_A_CHAR_DATA);
		
		/**
		 * STUDENT TODO: output in alphabetical order
		 */
		obj.execute();

		/**
		 * STUDENT TODO: output in reverse alphabetical order
		 */
		obj.idx = K_A_CHAR_DATA.length-1;
		obj.orderState = orderStates.REVERSE_ALPHABETIC;
		obj.charState = charStates.LOWER_CASE;
		obj.execute();

		/**
		 * STUDENT TODO: output in alphabetical order again
		 */
		obj.idx = 0;
		obj.orderState = orderStates.ALPHABETIC;
		obj.charState = charStates.LOWER_CASE;
		obj.execute();
	}
}
