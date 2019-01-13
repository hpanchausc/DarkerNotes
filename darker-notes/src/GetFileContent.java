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

@WebServlet("/GetFileContent")
public class GetFileContent extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//From previous page, extract parameters
		String fileID = request.getParameter("file");
		String data="";
		String fileName ="";
		
		//Set up variables to store return value
		boolean success = true;
		String errorMsg = "";
	
		
		//Check for null input
		if (fileID == null) {
			success = false;
			errorMsg += "The file doesn't exist! ";
		}
		
		
		//Begin database access
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		if (success) {
			try {
				success = false;
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db?user=root&password=password&useSSL=false&allowPublicKeyRetrieval=true");
				
				//Validate email and password with server
				ps = conn.prepareStatement("SELECT * FROM Files WHERE fileID=?");
				ps.setString(1,fileID);
				rs = ps.executeQuery();
				
				if (rs.next()) {
					data = rs.getString("rawData");
					fileName = rs.getString("fileName");
					success=true;
				}
				else {
					success = false;
					errorMsg = "File does not exist!";
				}
				
				
				
			
				
				data = data.replace("\\", "\\\\");
				
				data = data.replaceAll("\"","\\\\\"");
				
				
				
				//Set up a JSON return
				String objectToReturn =
						  "{\n"
							+ "\"success\": \"" + success + "\",\n"
							+ "\"data\": {\n"
								+ "\"errorMsg\": \"" + errorMsg + "\",\n"
								+ "\"rawData\": \"" + data + "\",\n"
								+ "\"fileName\": \"" + fileName + "\"\n"
							+ "}\n" 
						+ "}";
				
				out.print(objectToReturn);
				
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
							+ "\"rawData\": \"\"\n"
						+ "}\n" 
					+ "}";
			out.print(objectToReturn);
		}
	}
}