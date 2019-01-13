

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ShareFile
 */
@WebServlet("/ShareFile")
public class ShareFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//From previous page, extract parameters
		String email = request.getParameter("email");
		
		//add a new notification for the user
		String emailTo = request.getParameter("emailTo");
		String rawData = request.getParameter("rawData");
		String fileName = request.getParameter("fileName");
		
		boolean success = false;
		String errorMsg = "";
		
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		try {
			//Set up Connection
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/db?user=root&password=password&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
			
			ps = conn.prepareStatement("SELECT * FROM Users WHERE email=?");
			ps.setString(1, email);
			rs = ps.executeQuery();
			rs.next();
			String nameFrom = rs.getString("fullName");
			
			ps2 = conn.prepareStatement("SELECT * FROM Users WHERE email=?");
			ps2.setString(1,  emailTo);
			rs2 = ps2.executeQuery();
			
			
			if (rs2.next()) {
				//The user we are emailing exists
				int userID = rs2.getInt("userID");
				//INSERT INTO Notifications (notificationID, userID, fromName, isRead, fileID) VALUES (1, 4, Connor Buckley, 0, 1);
				ps3 = conn.prepareStatement("INSERT INTO Notifications (userID, fromName, isRead, fileName, rawData) VALUES (?, ?, ?, ?, ?);");
				ps3.setString(1, Integer.toString(userID));
				ps3.setString(2, nameFrom);
				ps3.setString(3, "0");
				ps3.setString(4, fileName);
				ps3.setString(5, rawData);
				ps3.executeUpdate();
				
				//Notify other user of new file if they are logged in
				
				//check if they are logged in?
				// check if the session is in sessionVector<>
				
				success = true;
			}
			// if our query for the desired email is false (user with that email DNE)
			else {
				errorMsg = "A user with that email does not exist.";
			}
			
			//Set up a JSON return
			String objectToReturn =
					  "{\n"
						+ "\"success\": \"" + success + "\",\n"
						+ "\"data\": {\n"
							+ "\"errorMsg\": \"" + errorMsg + "\"\n"
						+ "}\n" 
					+ "}";
			out.print(objectToReturn);
			
		} catch(SQLException sqle) {
			System.out.println("sqle SERVERSOCKET.java: " + sqle.getMessage());
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
				if(rs2!=null) {
					rs2.close();
				}
				if(ps2!=null) {
					ps2.close();
				}
				if(ps3!=null) {
					ps3.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle closing stream:-" + sqle.getMessage());
			}
		}
		
	}

}
