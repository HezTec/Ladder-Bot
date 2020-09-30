package sqlClasses;
import net.dv8tion.jda.core.entities.User;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import coolGroup.LadderBot.App;

/**
 * this class contains methods that insert values into the ladder
 * 
 * @author 17col
 *
 */
public class LadderInsert{
	
	private File jarFile = new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	private File file = new File(jarFile.getParentFile().getParent());
	private User objUser;
	private String memberID;
		/**
		 * 
		 * @param memberID requires a discord user id number as a string, this can be done by using the .conversion()
		 * method in UserConvertID and the toString() method together [User.toString().conversion();]
		 */
	public LadderInsert(String memberID, User objUser) {
		super();
		this.memberID = memberID;
		this.objUser = objUser;
	}
		/**
		 * Using the discord user id number, this method inserts the user id into the ladder along with its other needed
		 * information
		 * 
		 * note that in the setscore slot it is by default set to -1 because the set scores will always be either
		 * 1 or 0 in a best of 3 game so to avoid errors of 0 getting through it is set to -1 may be changed to null
		 */
	public void insertToLadder() {
		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+ file +"\\LADDER.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully, inserted into ladder");
			stmt = c.createStatement();
			String sql = "INSERT INTO LADDER (ID,NAME,POINTS,ISFIGHTING, CLAIMWIN, CLAIMLOSS, WINS, LOSSES,"
					+ " ISBANNED, BANNEDSERVERS,SETSCORE ,GLICKORATING, CHALLENGER) "
					+ "VALUES ("+memberID+",'" + objUser.getName() + "', 1000, 0, 0, 0, 0, 0, 0,'',3,'1500, 350, 0.06D', 'none');";
			stmt.executeUpdate(sql);

			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.exit(0);
		}

	}
}
