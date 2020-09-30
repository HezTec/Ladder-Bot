package sqlClasses;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

import coolGroup.LadderBot.App;

public class CreateLadderDB {
	
	
	
	
	public CreateLadderDB() {
		super();
	}
	/**
	 * creates a database in the file one layer below where the jar file is at
	 */
	public void create() {
		
		File jarFile = new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		File file = new File(jarFile.getParentFile().getParent());
		

		Connection c = null;
		 Statement stmt = null;
      
      try {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:"+ file +"\\LADDER.db");
         System.out.println("Opened database successfully");

         stmt = c.createStatement();
         String sql = "CREATE TABLE LADDER " +
                        "(ID STRING PRIMARY KEY     NOT NULL," +
                        " NAME              TEXT    NOT NULL, " + 
                        " POINTS            INT     NOT NULL, " + 
                        " ISFIGHTING        INT     NOT NULL, " +
                        " CLAIMWIN          INT     NOT NULL, " +
                        " CLAIMLOSS         INT     NOT NULL, " +
                        " WINS              INT     NOT NULL, " +
                        " LOSSES            INT     NOT NULL, " +
                        " ISBANNED          INT     NOT NULL, " +
                        " BANNEDSERVERS     STRING  NOT NULL, " +
                        " SETSCORE          INT     NULL, " +
                        " GLICKORATING      STRING  NOT NULL, " +
                        " CHALLENGER        STRING);";
         stmt.executeUpdate(sql);
         stmt.close();
         c.close();
      } catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      System.out.println("Table created successfully");
   }
   

public static void main( String args[] ) {
	   
	   Path currentRelativePath = Paths.get("");
	   String s = currentRelativePath.toAbsolutePath().toString();
	   
      Connection c = null;
      Statement stmt = null;
      
      try {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:"+ s +"\\LADDER.db");
         System.out.println("Opened database successfully");

         stmt = c.createStatement();
         String sql = "CREATE TABLE LADDER " +
                 "(ID STRING PRIMARY KEY     NOT NULL," +
                 " NAME              TEXT    NOT NULL, " + 
                 " POINTS            INT     NOT NULL, " + 
                 " ISFIGHTING        INT     NOT NULL, " +
                 " CLAIMWIN          INT     NOT NULL, " +
                 " CLAIMLOSS         INT     NOT NULL, " +
                 " WINS              INT     NOT NULL, " +
                 " LOSSES            INT     NOT NULL, " +
                 " ISBANNED          INT     NOT NULL, " +
                 " BANNEDSERVERS     STRING  NOT NULL, " +
                 " SETSCORE          INT     NULL, " +
                 " GLICKORATING      STRING  NOT NULL, " +
                 " CHALLENGER        STRING);";
         stmt.executeUpdate(sql);
         stmt.close();
         c.close();
      } catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      System.out.println("Table created successfully");
   }
}