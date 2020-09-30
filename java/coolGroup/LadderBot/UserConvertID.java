package coolGroup.LadderBot;

/**
 * the object class for the UserConvertID objects, this object takes out
 * unusable data from Discord user objects leaving only the users ID number
 * 
 * 
 * @author 17col
 *
 */

public class UserConvertID {

	private String userId;

	/**
	 * 
	 * @param userId is user object that has been converted to a string using the
	 *               .toString() method
	 */
	public UserConvertID(String userId) {
		super();
		this.userId = userId;
	}

	public UserConvertID() {

	}

	/**
	 * This method allows you to trim everything but the user ID number off of a
	 * user object and returns a string
	 * 
	 * 
	 * @return returns the discord ID of the user object as a string of numbers
	 */
	public String conversion() {
		int beginning = 0;
		int end = userId.length();

		for (int i = 0; i < userId.length(); i++) {
			if (userId.substring(i, i + 1).equals("(")) {
				beginning = i;
			}

		}

		userId = userId.substring(beginning, end).replaceAll("\\D", "");
		return userId;
	}

	/**
	 * This method allows you to trim everything but the user ID number off of a
	 * user object and returns a string
	 * 
	 * @param userId is a discord user object taken as a a string using the
	 *               .toString() method
	 * @return returns the discord ID of the user object as a string of numbers
	 */
	public String conversion(String userId) {
		int beginning = 0;
		int end = userId.length();

		for (int i = 0; i < userId.length(); i++) {
			if (userId.substring(i, i + 1).equals("(")) {
				beginning = i;
			}

		}

		userId = userId.substring(beginning, end).replaceAll("\\D", "");
		return userId;

	}

	public String getName(String userId) {
		int beginning = 0;
		int end = 0;

		for (int i = 0; i < userId.length(); i++) {
			if (userId.substring(i, i + 1).equals(":")) {
				beginning = i + 1;
				break;
			}

		}
		for (int i = 0; i < userId.length(); i++) {
			if (userId.substring(i, i + 1).equals("(")) {
				end = i;
				break;
			}
		}

		userId = userId.substring(beginning, end);
		return userId;

	}
}
