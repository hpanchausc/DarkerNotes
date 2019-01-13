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

@WebServlet("/AddFile")
public class AddFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//From previous page, extract parameters
		String email = request.getParameter("email");
		String fileName = request.getParameter("file");
		String rawData= request.getParameter("data");
		
		//Set up variables to hold response
		boolean success = true;
		String errorMsg = "";
		
		//no need to check for null input?
		
		//Error checking for empty fileName in servlet?
		//How are we passing in rawData?
		
		//Begin database access
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps4 = null;
		PreparedStatement ps5 = null;
		PreparedStatement ps6 = null;
		ResultSet rs = null;
		ResultSet rs5 = null;
		ResultSet rs6 = null;
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		int newFileId = -1;
		
		//If we didn't have null input, go into main database access
		if (success) {
			try {
				//success = false;
				Class.forName("com.mysql.jdbc.Driver");
				//conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db?user=root&password=password&useSSL=false");
				conn = DriverManager.getConnection("jdbc:mysql://localhost/db?user=root&password=password&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
				
				ps = conn.prepareStatement("INSERT INTO Files (rawData,fileName) VALUES (?,?);", Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, rawData);
				ps.setString(2, fileName);
				ps.executeUpdate();
				rs6 = ps.getGeneratedKeys();
				
				if(rs6.next()) {
					newFileId=rs6.getInt(1);
					
				}
				
		
				
				//Check if email already exists in our database
				ps5 = conn.prepareStatement("SELECT * FROM Users WHERE email=?");
				ps5.setString(1, email);
				rs5 = ps5.executeQuery();
				rs5.next();
				int uID = rs5.getInt("userID");
				
				
				
				if (newFileId != -1) {
					ps4 = conn.prepareStatement("INSERT INTO Access (userID, fileID) VALUES (?,?);");
					ps4.setString(1, Integer.toString(uID));
					ps4.setString(2, Integer.toString(newFileId));
					ps4.executeUpdate();
				}
				
				
				
				
				//Set up a JSON return
				String objectToReturn =
						  "{\n"
							+ "\"success\": \"" + success + "\",\n"
							+ "\"data\": {\n"
								+ "\"errorMsg\": \"" + errorMsg + "\",\n"
								+ "\"fileName\": \"" + fileName + "\",\n"
								+ "\"email\": \"" + email + "\",\n"
								+ "\"newFileId\": \"" + newFileId + "\"\n"
							+ "}\n" 
						+ "}";
				
				out.print(objectToReturn);
			} catch(SQLException sqle) {
				System.out.println("sqle in ADDFILE.java: " + sqle.getMessage());
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
					if(ps5!=null) {ps5.close();}
					if(ps6!=null) {ps6.close();}
					if(rs5!=null) {rs5.close();}
					if(rs6!=null) {rs6.close();}
				} catch (SQLException sqle) {
					System.out.println("sqle closing stream:-" + sqle.getMessage());
				}
			}
		}
		else {
			String objectToReturn =
					  "{\n"
						+ "\"success\": \"" + success + "\",\n"
						+ "\"data\": {\n"
							+ "\"errorMsg\": \"" + errorMsg + "\",\n"
							+ "\"fileName\": \"\",\n"
							+ "\"email\": \"\"\n"
						+ "}\n" 
					+ "}";
			out.print(objectToReturn);
		}
		
	}
}