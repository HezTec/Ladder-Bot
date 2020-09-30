package sqlClasses;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import coolGroup.LadderBot.App;

/**
 * Class that relates to the Challenger field of the database
 * 
 * @author 17col
 *
 */

public class ChallengerUpdate {

	private File jarFile = new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	private File file = new File(jarFile.getParentFile().getParent());

	/**
	 * this object holds methods that relate to the challenger field of the sql
	 * database
	 */
	public ChallengerUpdate() {

	}

	/**
	 * this method places the user ID numbers of both players and inserts them into
	 * eachothers challenger slots on the database
	 * 
	 * @param challenger takes in the discord user ID number as a string of the
	 *                   challenger
	 * @param defender   takes in the discord user ID number as a string of the
	 *                   defender
	 */
	public void updateChallenger(String challenger, String defender) {
		if (challenger.equals(defender)) {

		} else {
			
			Connection c = null;
			Statement stmt = null;

			try {
				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
				c.setAutoCommit(false);
				System.out.println("Opened database successfully");

				stmt = c.createStatement();
				String sql = "UPDATE LADDER set CHALLENGER = " + defender + " where ID=" + challenger + ";";
				stmt.executeUpdate(sql);
				c.commit();

				stmt.close();
				c.close();
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
				//System.exit(0);
			}
			System.out.println("Operation done successfully, challenger updated");
		}
	}

	/**
	 * This method resets both challenger id's for each respective player
	 * 
	 * @param challenger takes in the discord user ID number as a string of the
	 *                   challenger
	 * @param defender   takes in the discord user ID number as a string of the
	 *                   defender
	 */
	public void reset(String challenger, String defender) {

		System.out.println(challenger + " " + defender);
		Connection c = null;
		Statement stmt = null;
		String challOponent1 = null;
		String challOponent2 = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + challenger + ";");
			while (rs.next()) {
				challOponent1 = rs.getString("challenger");
			}

			ResultSet rs2 = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + challenger + ";");
			while (rs.next()) {
				challOponent2 = rs2.getString("challenger");
			}

			// resetting all the values in the database for both the challenger and the
			// defender as well
			// as for the user ID values in the challenger value stored in CHALLENGER
			String sql = "UPDATE LADDER set CHALLENGER = 'none' where ID=" + defender + ";";
			String sqlClaimW = "UPDATE LADDER set CLAIMWIN = 0 where ID=" + defender + ";";
			String sqlClaimL = "UPDATE LADDER set CLAIMLOSS = 0 where ID=" + defender + ";";
			String sqlIsFighting = "UPDATE LADDER set ISFIGHTING = 0 where ID=" + defender + ";";
			String sqlSetScore = "UPDATE LADDER set SETSCORE = NULL where ID=" + defender +";";
			stmt.executeUpdate(sql);
			stmt.executeUpdate(sqlClaimL);
			stmt.executeUpdate(sqlClaimW);
			stmt.executeUpdate(sqlIsFighting);
			stmt.executeUpdate(sqlSetScore);
			sql = "UPDATE LADDER set CHALLENGER = 'none' where ID=" + challOponent1 + ";";
			sqlClaimW = "UPDATE LADDER set CLAIMWIN = 0 where ID=" + challOponent1 + ";";
			sqlClaimL = "UPDATE LADDER set CLAIMLOSS = 0 where ID=" + challOponent1 + ";";
			sqlIsFighting = "UPDATE LADDER set ISFIGHTING = 0 where ID=" + challOponent1 + ";";
			sqlSetScore = "UPDATE LADDER set SETSCORE = NULL where ID=" + challOponent1 +";";
			stmt.executeUpdate(sql);
			stmt.executeUpdate(sqlClaimL);
			stmt.executeUpdate(sqlClaimW);
			stmt.executeUpdate(sqlIsFighting);
			stmt.executeUpdate(sqlSetScore);
			sql = "UPDATE LADDER set CHALLENGER = 'none' where ID=" + challenger + ";";
			sqlClaimW = "UPDATE LADDER set CLAIMWIN = 0 where ID=" + challenger + ";";
			sqlClaimL = "UPDATE LADDER set CLAIMLOSS = 0 where ID=" + challenger + ";";
			sqlIsFighting = "UPDATE LADDER set ISFIGHTING = 0 where ID=" + challenger + ";";
			sqlSetScore = "UPDATE LADDER set SETSCORE = NULL where ID=" + challenger +";";
			stmt.executeUpdate(sql);
			stmt.executeUpdate(sqlClaimL);
			stmt.executeUpdate(sqlClaimW);
			stmt.executeUpdate(sqlIsFighting);
			stmt.executeUpdate(sqlSetScore);
			sql = "UPDATE LADDER set CHALLENGER = 'none' where ID=" + challOponent2 + ";";
			sqlClaimW = "UPDATE LADDER set CLAIMWIN = 0 where ID=" + challOponent2 + ";";
			sqlClaimL = "UPDATE LADDER set CLAIMLOSS = 0 where ID=" + challOponent2 + ";";
			sqlIsFighting = "UPDATE LADDER set ISFIGHTING = 0 where ID=" + challOponent2 + ";";
			sqlSetScore = "UPDATE LADDER set SETSCORE = NULL where ID=" + challOponent2 +";";
			stmt.executeUpdate(sql);
			stmt.executeUpdate(sqlClaimL);
			stmt.executeUpdate(sqlClaimW);
			stmt.executeUpdate(sqlIsFighting);
			stmt.executeUpdate(sqlSetScore);
			c.commit();

			rs2.close();
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		System.out.println("Operation done successfully, challengers reset");
	}

	/**
	 * resets all challenger and isFighting values for each player in the database
	 * 
	 * it is primarily used on bot start up to reset any accidental challenger value
	 * settings to default after close
	 */
	public void resetAll() {
		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			System.out.println("Opened database successfully");

			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER;");

			while (rs.next()) {
				stmt = c.createStatement();
				String sqlChall = "UPDATE LADDER set CHALLENGER = 'none'";
				String sql = "UPDATE LADDER set ISFIGHTING = 0";
				String sqlClaimW = "UPDATE LADDER set CLAIMWIN = 0";
				String sqlClaimL = "UPDATE LADDER set CLAIMLOSS = 0";
				String sqlSetScore = "UPDATE LADDER set SETSCORE = NULL";
				stmt.executeUpdate(sqlClaimL);
				stmt.executeUpdate(sqlClaimW);
				stmt.executeUpdate(sql);
				stmt.executeUpdate(sqlChall);
				stmt.executeUpdate(sqlSetScore);
				c.commit();
			}

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		System.out.println("Operation done successfully, ladder values reset");
	}

	/**
	 * This method updates the is fighting database values for both the challenger
	 * and the defender to be 1 (True)
	 * 
	 * @param challenger takes in the discord user ID number as a string of the
	 *                   challenger
	 * @param defender   takes in the discord user ID number as a string of the
	 *                   defender
	 */
	public void updateIsFightingTrue(String challenger, String defender) {
		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String sql = "UPDATE LADDER set ISFIGHTING = " + 1 + " where ID=" + defender + ";";
			stmt.executeUpdate(sql);
			sql = "UPDATE LADDER set ISFIGHTING = " + 1 + " where ID=" + challenger + ";";
			stmt.executeUpdate(sql);
			c.commit();

			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		System.out.println("Operation done successfully, Is fighting updated");
	}

	/**
	 * This method updates the is fighting database values for both the challenger
	 * and the defender to be 0 (False)
	 * 
	 * @param challenger takes in the discord user ID number as a string of the
	 *                   challenger
	 * @param defender   takes in the discord user ID number as a string of the
	 *                   defender
	 */

	public void updateIsFightingFalse(String challenger, String defender) {
		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String sql = "UPDATE LADDER set ISFIGHTING = " + 0 + " where ID=" + defender + ";";
			stmt.executeUpdate(sql);
			sql = "UPDATE LADDER set ISFIGHTING = " + 0 + " where ID=" + challenger + ";";
			stmt.executeUpdate(sql);
			c.commit();

			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		System.out.println("Operation done successfully, is fighting updated");
	}

	/**
	 * This class sets the claim = to true in the database for the user inputed into
	 * the method
	 * 
	 * @param user is a userId converted to a string using the .conversion() method
	 */
	public void calimWin(String user) {
		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String sql = "UPDATE LADDER set CLAIMWIN = " + 1 + " where ID=" + user + ";";
			stmt.executeUpdate(sql);
			c.commit();

			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		System.out.println("Operation done successfully, claim win updated");
	}

	/**
	 * This class sets the claim = to false in the database for the user inputed
	 * into the method
	 * 
	 * @param user is a userId converted to a string using the .conversion() method
	 */
	public void calimLoss(String user) {
		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String sql = "UPDATE LADDER set CLAIMLOSS = " + 1 + " where ID=" + user + ";";
			stmt.executeUpdate(sql);
			c.commit();

			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		System.out.println("Operation done successfully, claim loss updated");
	}

	/**
	 * Adds a loss to the users loss count
	 * 
	 * @param user
	 */
	public void UpdateLoss(String user) {
		int losses = 0;

		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + user + ";");
			while (rs.next()) {
				losses = rs.getInt("losses");
			}
			losses += 1;
			String sql = "UPDATE LADDER set LOSSES = " + losses + " where ID=" + user + ";";
			stmt.executeUpdate(sql);
			c.commit();

			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		System.out.println("Operation done successfully, update losses");
	}

	/**
	 * adds a win to the users win count
	 * 
	 * @param user
	 */
	public void UpdateWin(String user) {
		int wins = 0;

		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + user + ";");
			while (rs.next()) {
				wins = rs.getInt("wins");
			}
			wins += 1;
			String sql = "UPDATE LADDER set WINS = " + wins + " where ID=" + user + ";";
			stmt.executeUpdate(sql);
			c.commit();

			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		System.out.println("Operation done successfully, update wins");
	}

	/**
	 * adds a server to a players banned servers list
	 * 
	 * @param userId
	 * @param serverId
	 */
	public void updateAddServerBan(String userId, String serverId) {
		Connection c = null;
		Statement stmt = null;
		String servers = "";

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + userId + ";");
			while (rs.next()) {
				servers = rs.getString("bannedservers");
			}

			servers += serverId + ",";

			stmt = c.createStatement();
			String sql = "UPDATE LADDER set BANNEDSERVERS = '" + servers + "' where ID=" + userId + ";";
			stmt.executeUpdate(sql);
			c.commit();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		System.out.println("Operation done successfully, server ban updated");
	}

	/**
	 * removes a server ban from a user's ban list
	 * 
	 * @param userId
	 * @param serverId
	 */
	public void updateRemoveServerBan(String userId, String serverId) {
		Connection c = null;
		Statement stmt = null;
		String servers = "";
		String newServers = "";

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + userId + ";");
			while (rs.next()) {
				servers = rs.getString("bannedservers");
			}
			String[] serverBans = servers.split(",");

			for (int i = 0; i < serverBans.length; i++) {
				if (!serverBans[i].equals(serverId)) {
					newServers += serverBans[i] + ",";
				}
			}

			stmt = c.createStatement();
			String sql = "UPDATE LADDER set BANNEDSERVERS = '" + newServers + "' where ID=" + userId + ";";
			stmt.executeUpdate(sql);
			c.commit();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		System.out.println("Operation done successfully, server unban updated");
	}

	/**
	 * banning a user
	 * 
	 * @param userId
	 */
	public void updateBanTrue(String userId) {
		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String sql = "UPDATE LADDER set ISBANNED = 1 where ID=" + userId + ";";
			stmt.executeUpdate(sql);
			c.commit();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		System.out.println("Operation done successfully, ban updated");
	}

	/**
	 * un banning a user
	 * 
	 * @param userId
	 */
	public void updateBanFalse(String userId) {
		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String sql = "UPDATE LADDER set ISBANNED = 0 where ID=" + userId + ";";
			stmt.executeUpdate(sql);
			c.commit();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		System.out.println("Operation done successfully, unban updated");
	}
	
	public void updateScoreClaim(String player, int setScore) {
		if (setScore == 1 || setScore == 0) {

			Connection c = null;
			Statement stmt = null;

			try {
				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
				c.setAutoCommit(false);
				System.out.println("Opened database successfully");

				stmt = c.createStatement();
				String sql = "UPDATE LADDER set SETSCORE = " + setScore + " where ID=" + player + ";";
				stmt.executeUpdate(sql);
				c.commit();
				System.out.println(setScore + " " + player);

				stmt.close();
				c.close();
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
				//System.exit(0);
			}
			System.out.println("Operation done successfully, setscore updated");
		}
	}

}
