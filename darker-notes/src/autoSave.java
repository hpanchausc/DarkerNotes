

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class autoSave
 */
@WebServlet("/autoSave")
public class autoSave extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		//We have a new autoSave request from a client
		//From previous page, extract parameters
		String email = request.getParameter("email");
		String fileID = request.getParameter("fileID");
		String fileContent = request.getParameter("fileContent");
		String fileName = request.getParameter("fileName");
		
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<Integer> callable = new saveThread(email, fileID, fileContent, fileName);
		Future<Integer> future = executor.submit(callable);
		Integer NewFileId= -1;
		try {
			NewFileId = future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		out.print(NewFileId);
		
	}
}
