import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AcceptNotification")
public class AcceptNotification extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//From previous page, extract parameters
		String notificationId = request.getParameter("notificationId");
		String rawData = request.getParameter("rawData");
		String fileName = request.getParameter("fileName");
		
		//Begin database access
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/db?user=root&password=password&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
			
			ps = conn.prepareStatement("SELECT * FROM Notifications WHERE notificationID=?");
			ps.setString(1, notificationId);
			rs = ps.executeQuery();
			rs.next();
			int userID = rs.getInt("userID");
			String fromName = rs.getString("fromName");
			
			//set notification isRead to 1
			
			//UPDATE Notifications SET isRead = '1' WHERE (notificationID=?);
			ps4 = conn.prepareStatement("UPDATE Notifications SET isRead = '1' WHERE (notificationID=?);");
			ps4.setString(1, notificationId);
			ps4.executeUpdate();
			
			
			//generate new file in Files
			ps2 = conn.prepareStatement("INSERT INTO Files (rawData,fileName) VALUES (?,?);", Statement.RETURN_GENERATED_KEYS);
			ps2.setString(1, rawData);
			ps2.setString(2, fileName);
			ps2.executeUpdate();
			rs2 = ps2.getGeneratedKeys();
			
			int newFileId = -1;
			
			if(rs2.next()) {
				newFileId=rs2.getInt(1);
				
			}
			
			//generate new row in Access for specified user
			ps3 = conn.prepareStatement("INSERT INTO Access (userID, fileID) VALUES (?,?);");
			ps3.setString(1, Integer.toString(userID));
			ps3.setString(2, Integer.toString(newFileId));
			ps3.executeUpdate();
			
			rawData = rawData.replace("\\", "\\\\");
			rawData = rawData.replaceAll("\"","\\\\\"");
			
			//Set up a JSON return
			String objectToReturn =
					  "{\n"
						+ "\"success\": \"" + "true" + "\",\n"
						+ "\"data\": {\n"
							+ "\"rawData\": \"" + rawData + "\",\n"
							+ "\"fromName\": \"" + fromName + "\",\n"
							+ "\"fileName\": \"" + fileName + "\"\n"
						+ "}\n" 
					+ "}";
			out.print(objectToReturn);
		} catch(SQLException sqle) {
			System.out.println("sqle IN GETFILES: " + sqle.getMessage());
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
				if(ps2!=null) {
					ps2.close();
				}
				if(ps3!=null) {
					ps3.close();
				}
				if(ps4!=null) {
					ps4.close();
				}
				if(rs!=null) {
					rs.close();
				}
				if(rs2!=null) {
					rs2.close();
				}
				if(rs3!=null) {
					rs3.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle closing stream:-" + sqle.getMessage());
			}
		}
	}
}