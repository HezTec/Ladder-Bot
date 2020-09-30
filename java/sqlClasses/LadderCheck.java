package sqlClasses;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;

import coolGroup.LadderBot.App;

/**
 * this class holds methods that have to do with checking the ladder database
 * 
 * @author 17col
 *
 */
public class LadderCheck {

	private File jarFile = new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	private File file = new File(jarFile.getParentFile().getParent());
	private String id;
	private boolean check;

	/**
	 * opens the laddercheck objects, this object holds many methods for checking
	 * various parts of the ladder
	 * 
	 * @param id is user object that has been converted to a string using the
	 *           .toString() method to only hold the users discord ID numbers
	 */
	public LadderCheck(String id) {
		super();
		this.id = id;
	}

	/**
	 * opens the laddercheck objects, this object holds many methods for checking
	 * various parts of the ladder
	 * 
	 */
	public LadderCheck() {

	}

	/**
	 * Methods checks if a certain id is in the Ladder or not
	 * 
	 * @param Id id is user object that has been converted to a string using the
	 *           .toString() method to only hold the users discord ID numbers
	 * @return either true if it finds your id in the ladder or false if it doesn't
	 */
	public boolean checkIfInLadder(String Id) {

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully check if in ladder Id");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER;");

			while (rs.next()) {
				String DBid = rs.getString("id");
				if (DBid.equals(Id)) {
					this.check = true;
					break;
				} else {
					this.check = false;
				}
			}

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		return check;
	}

	/**
	 * Methods checks if a certain id is in the Ladder or not
	 * 
	 * @return either true if it finds your id in the ladder or false if it doesn't
	 */
	public boolean checkIfInLadder() {

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully check if in ladder");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER;");

			while (rs.next() && check == false) {
				String DBid = rs.getString("id");
				if (DBid.equals(id)) {
					this.check = true;
				} else {
					this.check = false;
				}
			}

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		return check;
	}

	/**
	 * Method checks if the challenger and the defender both have each other in
	 * their respective challenger columns
	 * 
	 * @param challenger takes in the discord user ID number as a string of the
	 *                   challenger
	 * @param defender   takes in the discord user ID number as a string of the
	 *                   defender
	 * @return true if they have each other in their respective challenger columns
	 *         and false if they don't
	 */
	public boolean checkIfChallenged(String challenger, String defender) {

		boolean checkIfChall = false;

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully, check if challenged");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + challenger + ";");
			String checkChallenger = null;
			while (rs.next()) {
				checkChallenger = rs.getString("challenger");
			}

			ResultSet rs2 = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + defender + ";");
			String checkDefender = null;
			while (rs2.next()) {
				checkDefender = rs2.getString("challenger");
			}

			if (challenger.equals(checkDefender) && defender.equals(checkChallenger)) {
				checkIfChall = true;
			}
			rs2.close();
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		return checkIfChall;
	}

	/**
	 * 
	 * @param id is user object that has been converted to a string using the
	 *           .toString() method to only hold the users discord ID numbers
	 * @return if the user is fighting it returns true else if they aren't fighting
	 *         it returns false
	 */
	public boolean checkIfFighting(String id2) {

		int isFighting = 0;
		boolean fightCheck = false;
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully, check if fighting id");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER;");

			while (rs.next() && check == false) {
				String DBid = rs.getString("id");
				if (DBid.equals(id2)) {
					isFighting = rs.getInt("isfighting");
				}

			}
			if (isFighting == 1) {
				fightCheck = true;
			} else {
				fightCheck = false;
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		return fightCheck;
	}

	public boolean checkIfFighting() {

		int isFighting = 0;
		boolean fightCheck = false;
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully, check if fighting");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER;");

			while (rs.next() && check == false) {
				String DBid = rs.getString("id");
				if (DBid.equals(id)) {
					isFighting = rs.getInt("isfighting");
				}

			}
			if (isFighting == 1) {
				fightCheck = true;
			} else {
				fightCheck = false;
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		return fightCheck;
	}

	/**
	 * Thiis method check to see if a match between two players was created
	 * 
	 * @param challenger takes in the discord user ID number as a string of the
	 *                   challenger
	 * @param defender   takes in the discord user ID number as a string of the
	 *                   defender
	 * @return returns true if match was created and false if not
	 */
	public boolean checkMatchCreated(String challenger, String defender) {

		if (challenger.equals("none") || defender.equals("none")) {
			return false;
		}

		LadderCheck matchCheck = new LadderCheck();
		boolean ifFighthing1 = matchCheck.checkIfFighting(challenger);
		boolean ifFighthing2 = matchCheck.checkIfFighting(defender);
		boolean ifChallenged = matchCheck.checkIfChallenged(challenger, defender);
		boolean matchChecker = false;

		if (ifFighthing1 && ifFighthing2 && ifChallenged) {
			matchChecker = true;
		} else {
			matchChecker = false;
		}
		return matchChecker;
	}

	/**
	 * Grabs the challenger value of the inputed user
	 * 
	 * @param challenger a string that represtend the user id as a string of
	 *                   numberss
	 * @return returns the challenger value of the inputed user as a string
	 */
	public String grabChallenger(String challenger) {

		String checkChallenger = null;

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully, challenger grabbed");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + challenger + ";");

			while (rs.next()) {
				checkChallenger = rs.getString("challenger");
			}

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		return checkChallenger;
	}
	
	
	/*
	 * found the problem with the set score, for some reason the null valuse in the DB never changes
	 * so when i grab it it is set to 0 when checking the claims.
	 */
	public boolean checkClaims(String challenger, String defender) {

		boolean checkIfChall = false;

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully, Check win claims");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + challenger + ";");
			int checkChallengerWin = 2;
			int checkChallengerLoss = 3;
			int checkChallengerSetScore = 4;
			while (rs.next()) {
				checkChallengerWin = rs.getInt("claimwin");
				checkChallengerLoss = rs.getInt("claimloss");
				checkChallengerSetScore = rs.getInt("setscore");
			}

			ResultSet rs2 = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + defender + ";");
			int checkDefenderLoss = 5;
			int checkDefenderWin = 6;
			int checkDefenderSetScore = 7;
			while (rs2.next()) {
				checkDefenderLoss = rs2.getInt("claimloss");
				checkDefenderWin = rs2.getInt("claimwin");
				checkDefenderSetScore = rs2.getInt("setscore");
			}
			
			if (checkDefenderWin == checkChallengerWin || checkChallengerLoss == checkDefenderLoss ||
					checkChallengerSetScore != checkDefenderSetScore) {
				
				checkIfChall = false;
			} else {
				checkIfChall = true;
				System.out.println(checkDefenderSetScore);
				System.out.println(checkChallengerSetScore);
			}
			rs2.close();
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		return checkIfChall;
	}

	/**
	 * checks if the values for the two inputed discord id's was reset to default
	 * values
	 * 
	 * @param challenger discord user id number
	 * @param defender   discord user id number
	 * @return true if default values are set and false if they aren't
	 */

	public boolean checkIfReset(String challenger, String defender) {

		boolean checkIfReset = false;

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully, Check if reset");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + challenger + ";");
			String checkChallenger = null;
			int checkChallClaimWin = 3;
			int checkChallClaimLoss = 3;
			int checkChallIfFighting = 3;
			while (rs.next()) {
				checkChallenger = rs.getString("challenger");
				checkChallClaimWin = rs.getInt("claimwin");
				checkChallClaimLoss = rs.getInt("claimloss");
				checkChallIfFighting = rs.getInt("isfighting");
			}

			ResultSet rs2 = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + defender + ";");
			String checkDefender = null;
			int checkDefClaimWin = 2;
			int checkDefClaimLoss = 2;
			int checkDefIfFighting = 2;
			while (rs2.next()) {
				checkDefender = rs2.getString("challenger");
				checkDefClaimWin = rs.getInt("claimwin");
				checkDefClaimLoss = rs.getInt("claimloss");
				checkDefIfFighting = rs.getInt("isfighting");
			}

			if (checkChallenger.equals("none") && checkDefender.equals("none") && checkChallClaimWin == 0
					&& checkDefClaimWin == 0 && checkChallClaimLoss == 0 && checkDefClaimLoss == 0
					&& checkChallIfFighting == 0 && checkDefIfFighting == 0) {
				checkIfReset = true;
			}
			rs2.close();
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		return checkIfReset;
	}

	public boolean checkIfBanned() {

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully, check if banned");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER;");

			while (rs.next() && check == false) {
				String isBanned = rs.getString("isbanned");
				if (isBanned.equals(id)) {
					this.check = true;
				} else {
					this.check = false;
				}
			}

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		return check;
	}

	public String grabStats(String challenger) {

		DecimalFormat df2 = new DecimalFormat(".##");

		int grabWins = 0;
		int grabLosses = 0;
		int grabPoints = 0;
		double winRatio = 0;
		String grabName = null;
		String stats = null;

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully, grab stats");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + challenger + ";");

			while (rs.next()) {
				grabWins = rs.getInt("wins");
				grabLosses = rs.getInt("losses");
				grabPoints = rs.getInt("Points");
				grabName = rs.getString("name");
			}
			if (grabWins + grabLosses == 0) {
				winRatio = 0;
			} else {
				winRatio = (double) grabWins / (grabWins + grabLosses);
			}

			stats = grabName + "'s stats:\n               Wins: " + grabWins + "\n             Losses: " + grabLosses
					+ "\n       Win Ratio: " + df2.format(winRatio) + "\n             Raiting: " + grabPoints;

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		return stats;
	}
	/**
	 * checks if the user is banned from using ladder commands
	 * 
	 * @param Id
	 * @return true if banned and false if not banned
	 */
	public boolean checkIfBanned(String Id) {

		boolean check = false;

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully, check if banned Id");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + Id + ";");

			while (rs.next()) {
				int isBanned = rs.getInt("isbanned");
				if (isBanned == 1) {
					check = true;
					break;
				} else {
					check = false;
				}
			}

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		return check;
	}
	
	public boolean checkIfserverBanned(String Id, String serverId) {

		boolean check = false;
		String banList = "";

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully, check if sevrer banned Id");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + Id + ";");

			while (rs.next()) {
				banList = rs.getString("bannedservers");
			}
			String[] servers = banList.split(",");

			
			for(int i = 0; i < servers.length; i++) {
				if(servers[i].equals(serverId)) {
					check = true;
				}
			}

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		return check;
	}

}
