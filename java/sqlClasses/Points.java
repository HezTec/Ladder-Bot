package sqlClasses;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import coolGroup.LadderBot.App;
import coolGroup.LadderBot.Glicko2;
import coolGroup.LadderBot.Glicko2.Rating;

public class Points {
	private File jarFile = new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	private File file = new File(jarFile.getParentFile().getParent());

	public Points() {

	}

	/**
	 * This method gets the points of a user from the database using a userID
	 * 
	 * @param UserId A discord UserID
	 * @return the number of points stored in the points column as an int
	 */
	public int getPoints(String userId) {

		int points = 0;

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully, getting points");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + userId + ";");

			while (rs.next()) {
				points = rs.getInt("points");
			}

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		return points;
	}
	
	/**
	 * Updates the users point amount
	 * 
	 * @param userId       the discord user id number as a string
	 * @param pointsAmount this number becomes the new amount of points in this
	 *                     users points column
	 */
	public void updatePoints(String userId, int pointsAmount) {
		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully, Update points");

			stmt = c.createStatement();
			String sql = "UPDATE LADDER set POINTS = " + pointsAmount + " where ID=" + userId + ";";
			stmt.executeUpdate(sql);
			c.commit();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		System.out.println("Operation done successfully, points updated");
	}
	/*
	 * this shit is the old shitty way to transfer points before glicko2 so dont use this shit
	 */
	public void transferPoints(String winner, String loser) {
		int winnerPoints = 0;
		int loserPoints = 0;
		int tpt = 10;

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully, transfer points");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + winner + ";");

			while (rs.next()) {
				winnerPoints = rs.getInt("points");
			}

			ResultSet rs2 = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + loser + ";");

			while (rs2.next()) {
				loserPoints = rs.getInt("points");
			}
			int diff = Math.abs(winnerPoints - loserPoints);
			if (winnerPoints > loserPoints && diff >= 500) {
				tpt = 10;
			} else if (winnerPoints <= loserPoints) {

				// the calculation of how many points are transfered
				/**
				 * to edit the amount of points transfered its easiest to edit the loop divison
				 * or the point threshold
				 * 
				 * 
				 */
				diff = Math.abs(winnerPoints - loserPoints);

				int loops = (diff / 20);
				int i = 0;
				while (i < loops) {
					// the number of points added each time the loop runs
					int pointsAdded = 1;
					// the number of points it takes to add one to pointsAdded
					int pointThreshold = 10;
					if (i > pointThreshold) {
						pointsAdded += 1;
						pointThreshold += 10;
					}
					tpt += pointsAdded;
					i++;
				}
			} else if (winnerPoints > loserPoints) {
				// the calculation of how many points are transfered
				diff = Math.abs(winnerPoints - loserPoints);
				int loops = (diff / 20);
				int i = 0;
				while (i < loops) {
					// the number of points added each time the loop runs
					int pointsAdded = 1;
					// the number of points it takes to add one to pointsAdded
					int pointThreshold = 10;
					if (i > pointThreshold) {
						pointsAdded += 1;
						pointThreshold += 10;
					}
					tpt += pointsAdded;
					i++;
				} // changed
				int subtractLoops = (diff / 23);
				i = 0;
				while (i < subtractLoops) {
					// the number of points added each time the loop runs
					int pointsSubtracted = 1;
					// the number of points it takes to add one to pointsAdded
					int pointThreshold = 10;
					if (i > pointThreshold) {
						pointsSubtracted += 1;
						pointThreshold += 10;
					}
					tpt -= pointsSubtracted;
					i++;
				}
				// changed
				if (diff < 200) {
					loserPoints += 5;
				}
			}

			winnerPoints += tpt;
			loserPoints -= tpt;
			String sql = "UPDATE LADDER set POINTS = " + winnerPoints + " where ID=" + winner + ";";
			stmt.executeUpdate(sql);
			sql = "UPDATE LADDER set POINTS = " + loserPoints + " where ID=" + loser + ";";
			stmt.executeUpdate(sql);
			c.commit();

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
	}

	/**
	 * gets the top ten users in the ladder
	 * 
	 * @return a string of the tip ten users in the ladder
	 */
	public String ladderTop10() {
		int points = 0;
		String name = null;
		String ladder = "";

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully, top 10");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER ORDER BY POINTS DESC;");

			int rank = 1;
			while (rs.next() && rank <= 10) {

				points = rs.getInt("points");
				name = rs.getString("name");
				ladder += "\n" + rank + ": " + name + "> " + points;
				rank++;
			}

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		return ladder;
	}

	/**
	 * gets the users rank in the ladder as a string showing the them in the middle
	 * and 5 users above and below them
	 * 
	 * @param userId
	 * @return The users rank in the ladder as a string.
	 */
	public String myLadderRank(String userId) {
		int points = 0;
		String name = null;
		String ladder = "";
		int rankNum = getRankNum(userId);

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully, my ladder rank");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER ORDER BY POINTS DESC;");

			int rank = 0;
			while (rs.next() && rank <= rankNum + 9) {
				if (rank >= (rankNum - 1)) {
					points = rs.getInt("points");
					name = rs.getString("name");
					String id = rs.getString("id");
					if (id.equals(userId)) {
						ladder += "\n" + (rank + 1) + ": (You) " + name + "> " + points;
					} else {
						ladder += "\n" + (rank + 1) + ": " + name + "> " + points;
					}
				}
				rank++;
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}

		return ladder;
	}

	public String displayLadderNum(String userId) {
		int points = 0;
		String name = null;
		String ladder = "";
		int rankNum = getRankNum(userId);

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully, my ladder rank");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER ORDER BY POINTS DESC;");

			int rank = 0;
			while (rs.next() && rank <= rankNum + 9) {
				if (rank >= (rankNum - 1)) {
					points = rs.getInt("points");
					name = rs.getString("name");
					String id = rs.getString("id");
					if (id.equals(userId)) {
						ladder += "\n" + (rank + 1) + ": (You) " + name + "> " + points;
					} else {
						ladder += "\n" + (rank + 1) + ": " + name + "> " + points;
					}
				}
				rank++;
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}

		return ladder;
	}

	/**
	 * gets the number of this user in the ladder ordered from best to worst
	 * 
	 * @param userId
	 * @return the number of the inputed user in the ladder
	 */
	public int getRankNum(String userId) {
		int rank = 0;
		String id = null;
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully, get rank num");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER ORDER BY POINTS DESC;");

			int benchmark = 0;
			while (rs.next() && benchmark != 1) {
				rank++;
				id = rs.getString("id");
				if (id.equals(userId)) {
					benchmark = 1;
				}
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}
		return rank - 5;
	}
	//Attempt to implement glicko2 for the ladder
	/**
	 * things to do:
	 * -Implement a way to ask the players who won which games when they turn in scores
	 *	thats what im doing rn with glicko update but to be sure to fix all of the shit with the
	 *	messed up variable names
	 *
	 *	first of all you need to switch the parameters that GlickoUpdate takes in from
	 *	the strings of the glicko info to the players IDs themselves because this method is just going to 
	 *	change the DB instead of just changing the glicko info.
	 * 
	 */
	
	public void GlickoUpdate(String winner, String loser, int losses) {
		
		
		
		
		String winnerGlickoRating = "";//the winning players current glicko rating info
		String loserGlickoRating = "";//the losing players current glicko rating info
		

		//getting the currnet glicko info from the DB and assigning those strings to the variables
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully, transfer GlickoUpdate");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + winner + ";");

			while (rs.next()) {
				winnerGlickoRating = rs.getString("glickorating");
			}

			ResultSet rs2 = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + loser + ";");

			while (rs2.next()) {
				loserGlickoRating = rs.getString("glickorating");
			}
			
			
			
			//creating the winners rating object from the string in the DB
			String[] wTokens = winnerGlickoRating.split(",[ ]*");
			double winnerRating = Double.parseDouble(wTokens[0]);
			double winnerDeviation = Double.parseDouble(wTokens[1]);
			double winnerVolatility = Double.parseDouble(wTokens[2]);
			
			//the rating object for the winners glicko calculation made form the string
			Rating winnerGlicko = new Rating(winnerRating, winnerDeviation, winnerVolatility);
			
			//creating the losers rating object from the string in the DB
			String[] lTokens = loserGlickoRating.split(",[ ]*");
			double loserRating = Double.parseDouble(lTokens[0]);
			double loserDeviation = Double.parseDouble(lTokens[1]);
			double loserVolatility = Double.parseDouble(lTokens[2]);
			
			//the rating object for the losers glicko calculation made from the string
			Rating loserGlicko = new Rating(loserRating, loserDeviation, loserVolatility);
			
			//if the match ends 2-0 it only calculates the matches played
			if (losses == 0) {
				
				// the winners points are updated
				List<Rating> opponents = Arrays.asList(loserGlicko, loserGlicko);
				List<Double> scores = Arrays.asList(new Double(1.0), new Double(1.0));
				Glicko2.updateRating(winnerGlicko, opponents, scores);

				// the losers points are updated
				List<Rating> opponents2 = Arrays.asList(winnerGlicko, winnerGlicko);
				List<Double> scores2 = Arrays.asList(new Double(0.0), new Double(0.0));
				Glicko2.updateRating(loserGlicko, opponents2, scores2);
				
				// if the match ends 2-1 it calculates all the matches
			} else if (losses == 1) {
				
				// the winners points are updated
				List<Rating> opponents = Arrays.asList(loserGlicko, loserGlicko, loserGlicko);
				List<Double> scores = Arrays.asList(new Double(1.0), new Double(0.0), new Double(1.0));
				Glicko2.updateRating(winnerGlicko, opponents, scores);

				// the losers points are updated
				List<Rating> opponents2 = Arrays.asList(winnerGlicko, winnerGlicko, winnerGlicko);
				List<Double> scores2 = Arrays.asList(new Double(0.0), new Double(1.0), new Double(0.0));
				Glicko2.updateRating(loserGlicko, opponents2, scores2);
			}
			
			System.out.println(winnerGlicko.toString());
			System.out.println(loserGlicko.toString());
			
			
			//assigning the new ratings to the players spots in the database
			String sql = "UPDATE LADDER set GLICKORATING = '" + winnerGlicko.toString() + "' where ID=" + winner + ";";
			stmt.executeUpdate(sql);
			sql = "UPDATE LADDER set POINTS = '" + (int)winnerGlicko.getRating() + "' where ID=" + winner + ";";
			stmt.executeUpdate(sql);
			sql = "UPDATE LADDER set GLICKORATING = '" + loserGlicko.toString() + "' where ID=" + loser + ";";
			stmt.executeUpdate(sql);
			sql = "UPDATE LADDER set POINTS = '" + (int)loserGlicko.getRating() + "' where ID=" + loser + ";";
			stmt.executeUpdate(sql);
			c.commit();

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}

	}
	
public void GlickoUpdateTesting(String winner, String loser, int losses) {
		
		
		
		
		String winnerGlickoRating = winner;//the winning players current glicko rating info
		String loserGlickoRating = loser;//the losing players current glicko rating info
		

		//getting the currnet glicko info from the DB and assigning those strings to the variables
//		Connection c = null;
//		Statement stmt = null;
//		try {
//			Class.forName("org.sqlite.JDBC");
//			c = DriverManager.getConnection("jdbc:sqlite:" + file + "\\LADDER.db");
//			c.setAutoCommit(false);
//			System.out.println("Opened database successfully, transfer GlickoUpdate");
//
//			stmt = c.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + winner + ";");
//
//			while (rs.next()) {
//				winnerGlickoRating = rs.getString("glickorating");
//			}
//
//			ResultSet rs2 = stmt.executeQuery("SELECT * FROM LADDER WHERE ID = " + loser + ";");
//
//			while (rs2.next()) {
//				loserGlickoRating = rs.getString("glickorating");
//			}
			
			
			
			//creating the winners rating object from the string in the DB
			String[] wTokens = winnerGlickoRating.split(",[ ]*");
			double winnerRating = Double.parseDouble(wTokens[0]);
			double winnerDeviation = Double.parseDouble(wTokens[1]);
			double winnerVolatility = Double.parseDouble(wTokens[2]);
			
			//the rating object for the winners glicko calculation made form the string
			Rating winnerGlicko = new Rating(winnerRating, winnerDeviation, winnerVolatility);
			
			//creating the losers rating object from the string in the DB
			String[] lTokens = loserGlickoRating.split(",[ ]*");
			double loserRating = Double.parseDouble(lTokens[0]);
			double loserDeviation = Double.parseDouble(lTokens[1]);
			double loserVolatility = Double.parseDouble(lTokens[2]);
			
			//the rating object for the losers glicko calculation made from the string
			Rating loserGlicko = new Rating(loserRating, loserDeviation, loserVolatility);
			
			//if the match ends 2-0 it only calculates the matches played
			if (losses == 0) {
				
				// the winners points are updated
				List<Rating> opponents = Arrays.asList(loserGlicko, loserGlicko);
				List<Double> scores = Arrays.asList(new Double(1.0), new Double(1.0));
				Glicko2.updateRating(winnerGlicko, opponents, scores);

				// the losers points are updated
				List<Rating> opponents2 = Arrays.asList(winnerGlicko, winnerGlicko);
				List<Double> scores2 = Arrays.asList(new Double(0.0), new Double(0.0));
				Glicko2.updateRating(loserGlicko, opponents2, scores2);
				
				// if the match ends 2-1 it calculates all the matches
			} else if (losses == 1) {
				
				// the winners points are updated
				List<Rating> opponents = Arrays.asList(loserGlicko, loserGlicko, loserGlicko);
				List<Double> scores = Arrays.asList(new Double(1.0), new Double(0.0), new Double(1.0));
				Glicko2.updateRating(winnerGlicko, opponents, scores);

				// the losers points are updated
				List<Rating> opponents2 = Arrays.asList(winnerGlicko, winnerGlicko, winnerGlicko);
				List<Double> scores2 = Arrays.asList(new Double(0.0), new Double(1.0), new Double(0.0));
				Glicko2.updateRating(loserGlicko, opponents2, scores2);
			}
			
			System.out.println(winnerGlicko.toString());
			System.out.println(loserGlicko.toString());
			
			
//			//assigning the new ratings to the players spots in the database
//			String sql = "UPDATE LADDER set GLICKORATING = '" + winnerGlicko.toString() + "' where ID=" + winner + ";";
//			stmt.executeUpdate(sql);
//			sql = "UPDATE LADDER set POINTS = '" + (int)winnerGlicko.getRating() + "' where ID=" + winner + ";";
//			stmt.executeUpdate(sql);
//			sql = "UPDATE LADDER set GLICKORATING = '" + loserGlicko.toString() + "' where ID=" + loser + ";";
//			stmt.executeUpdate(sql);
//			sql = "UPDATE LADDER set POINTS = '" + (int)loserGlicko.getRating() + "' where ID=" + loser + ";";
//			stmt.executeUpdate(sql);
//			c.commit();
//
//			rs.close();
//			stmt.close();
//			c.close();
//		} catch (Exception e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			//System.exit(0);
//		}

	}

}
