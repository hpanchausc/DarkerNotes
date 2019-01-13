import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/getNotifications")
public class getNotifications extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//From previous page, extract parameters
		String email = request.getParameter("email");
		String rawHTML = "";
		
		//Begin database access
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		try {
			
			
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/db?user=root&password=password&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
			
			//Check if email already exists in our database
			
	
			
			ps = conn.prepareStatement("SELECT * FROM Users WHERE email=?");
			ps.setString(1, email);
			rs = ps.executeQuery();
			
			rs.next();
			
			
			
			int uID = rs.getInt("userID");
			
			//now we have uID and email
			//need to retrieve notifications and make a table for the notifications popdown(1 column)
			
			//First, retrieve notificationIDs for user from Access table
			ps2 = conn.prepareStatement("SELECT * FROM Notifications WHERE userID=? AND isRead=?");
			ps2.setString(1,  Integer.toString(uID));
			ps2.setString(2, "0");
			rs2 = ps2.executeQuery();
			
			//Declare lists to hold notification data we want to display
			ArrayList<Integer> notificationID = new ArrayList<Integer>();
			ArrayList<String> fromNames = new ArrayList<String>();
			ArrayList<String> fileNames = new ArrayList<String>();
			
			
			
			
			while (rs2.next()) {
				notificationID.add(rs2.getInt("notificationID"));
				fromNames.add(rs2.getString("fromName"));
				fileNames.add(rs2.getString("fileName"));
			}
			
			
			if (notificationID.size() != 0) {
				//rawHTML+=("<table class=\"table table-bordered table-dark\"><tbody>");
				rawHTML+=("<table class=\"table table-bordered table-hover\"><tbody>");
				for (int j = 0; j < notificationID.size(); j++) {
	        		//names
		         	rawHTML+=("<tr>");
		         	rawHTML+=("<td onclick=\"handleNotification('" + notificationID.get(j) + "')\" style=\"cursor: pointer;\">"  + fileNames.get(j) + " from " + fromNames.get(j) +  "</td>");
		         	rawHTML+=("</tr>");
				}
				rawHTML+=("</tbody></table>");
			}
			else {
				//rawHTML+="<table class=\"table table-bordered table-dark\"><tbody>";
	         	rawHTML+=("<em>No new files</em>");
			}
			
			
			out.print(rawHTML);
			
		} catch(SQLException sqle) {
			System.out.println("sqle IN GETNOTIFICATIONs: " + sqle.getMessage());
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