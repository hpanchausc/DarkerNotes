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

@WebServlet("/RetrieveNotification")
public class RetrieveNotification extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//From previous page, extract parameters
		String notificationId = request.getParameter("notificationId");
		
		//Begin database access
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
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
			String rawData = rs.getString("rawData");
			String fromName = rs.getString("fromName");
			String fileName = rs.getString("fileName");
			
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