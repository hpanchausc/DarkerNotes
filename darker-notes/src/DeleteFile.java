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

@WebServlet("/DeleteFile")
public class DeleteFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//From previous page, extract parameters
		String email = request.getParameter("email");
		String fileID = request.getParameter("fileId");
		
		//Begin database access
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps5 = null;
		PreparedStatement ps6 = null;
		ResultSet rs = null;
		ResultSet rs5 = null;
		ResultSet rs6 = null;
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		//If we didn't have null input, go into main database access
		
		try {
			//success = false;
			Class.forName("com.mysql.jdbc.Driver");
			//conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db?user=root&password=password&useSSL=false");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/db?user=root&password=password&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
			
			//Check if email already exists in our database
			ps5 = conn.prepareStatement("SELECT * FROM Users WHERE email=?");
			ps5.setString(1, email);
			rs5 = ps5.executeQuery();
			rs5.next();
			int uID = rs5.getInt("userID");
			
			//use uID to remove access to fileID in Access table
			//delete row where uID = uID and fileID = fileID
			
			//DELETE FROM Access WHERE (fileID=? AND userID=?);
			ps = conn.prepareStatement("DELETE FROM Access WHERE (fileID=? AND userID=?);");
			ps.setString(1, fileID);
			ps.setString(2, Integer.toString(uID));
			ps.executeUpdate();
			
			//Set up a JSON return
			String objectToReturn =
					  "{\n"
						+ "\"success\": \"" + "true" + "\",\n"
						+ "\"data\": {\n"
							+ "\"errorMsg\": \"" + "" + "\",\n"
							+ "\"fileID\": \"" + fileID + "\",\n"
							+ "\"email\": \"" + email + "\",\n"
							+ "\"newFileId\": \"" + "-1" + "\"\n"
						+ "}\n" 
					+ "}";
			
			out.print(objectToReturn);
		} catch(SQLException sqle) {
			System.out.println("sqle in deletefile.java: " + sqle.getMessage());
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
	
}
