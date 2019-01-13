import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;

public class saveThread implements Callable<Integer> {
	
	String email;
	String fileID;
	String fileContent;
	String fileName;
	
	public saveThread(String email, String fileID, String fileContent, String fileName) {
		this.email = email;
		this.fileID = fileID;
		this.fileContent = fileContent;
		this.fileName = fileName;
	}
	
	public Integer call() {
		//Begin database access
		int newFileId = -1;
		
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		PreparedStatement ps5 = null;
		PreparedStatement ps6 = null;
		ResultSet rs = null;
		ResultSet rs5 = null;
		ResultSet rs6 = null;
		try {
			//Set up Connection
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/db?user=root&password=password&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
			
			//Do all database queries here, make sure to lock/monitor these queries
			
			//First check if this file already exists in the database, otherwise we need to add a new file
			ps = conn.prepareStatement("SELECT * FROM Access WHERE fileID=?");
			ps.setString(1, fileID);
			
			synchronized(this) {
				rs = ps.executeQuery();
			}
			
			
			
			if (rs.next()) {
				
				
			
				//This file already exists
				//connect email and currentFileID and update the row for that file with textBoxContent
				//UPDATE `db`.`Files` SET `rawData` = 'This is a test!' WHERE (`fileID` = '1');
				ps2 = conn.prepareStatement("UPDATE Files SET rawData=? WHERE (fileID = ?);");
				ps6 = conn.prepareStatement("UPDATE Files SET fileName=? WHERE (fileID = ?);");
				ps2.setString(1, fileContent);
				ps2.setString(2, fileID);
				ps6.setString(1, fileName);
				ps6.setString(2, fileID);
				
				synchronized(this) {
					ps2.executeUpdate();
					ps6.executeUpdate();
				}
				
				return Integer.parseInt(fileID);
				
			}
			else {
				
				//add a new file
				//ADD HRIDAY's CODE HERE
				//Begin database access
				System.out.println("Adding a new file w/ name : " + fileName);
				ps3 = conn.prepareStatement("INSERT INTO Files (rawData,fileName) VALUES (?,?);", Statement.RETURN_GENERATED_KEYS);
				ps3.setString(1, fileContent);
				ps3.setString(2, fileName);
				synchronized(this) {
					ps3.executeUpdate();
					rs6 = ps3.getGeneratedKeys();
				}

				if(rs6.next()) {
					newFileId=rs6.getInt(1);
					System.out.println("NewFileID: " + newFileId);
				}
				
				ps5 = conn.prepareStatement("SELECT * FROM Users WHERE email=?");
				System.out.println("Email = " + email);
				ps5.setString(1, email);
				synchronized(this) {
					rs5 = ps5.executeQuery();
				}
				rs5.next();
				int uID = rs5.getInt("userID");
				
				ps4 = conn.prepareStatement("INSERT INTO Access (userID, fileID) VALUES (?,?);");
				ps4.setString(1, Integer.toString(uID));
				ps4.setString(2, Integer.toString(newFileId));
				synchronized(this) {
					ps4.executeUpdate();
				}
				
				
				return newFileId;
				
			
			}	
		} catch(SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch(ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			try {
				if(conn!=null) {
					conn.close();
				}
				if(ps!=null) {
					ps.close();
				}
				if(rs!=null) {
					rs.close();
				}
				if(rs5!=null) {
					rs5.close();
				}
				if(ps2!=null) {
					ps2.close();
				}
				if(ps3!=null) {
					ps3.close();
				}
				if(ps4!=null) {
					ps4.close();
				}
				if(ps5!=null) {
					ps5.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle closing stream:-" + sqle.getMessage());
			}
		}
		return newFileId;
		
	}
}
