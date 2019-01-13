import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import javax.websocket.*; // for space
import javax.websocket.server.ServerEndpoint;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@ServerEndpoint(value = "/ws")
public class ServerSocket {
	private static Vector<Session> sessionVector = new Vector<Session>();
	private static HashMap<String, Vector<Session>> hm = new HashMap<String, Vector<Session>>();
	
	@OnOpen
	public void open(Session session) {
		System.out.println("Connection made!");
		sessionVector.add(session);
	}
	@OnMessage
	public void onMessage(String message, Session session) {
		//called when the frontend makes a request
		//message is a json w/ action, email, fileID, rawFileData
		//Here 
		//Using GSON
		JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();

		String action = jsonObject.get("action").getAsString();
		if (action.equals("addUtoMap")) {
			String email = jsonObject.get("email").getAsString();
			if (hm.get(email) != null) {
				hm.get(email).add(session);
			}
			else {
				hm.put(email, new Vector<Session>());
				hm.get(email).add(session);
			}
		}
		else if (action.equals("removeUFromMap")) {
			String email = jsonObject.get("email").getAsString();
			if (hm.get(email) != null) {
				hm.get(email).remove(session);
			}
		}
		else if (action.equals("SendFile")) {
			//add a new notification for the user
			String emailTo = jsonObject.get("emailTo").getAsString();
			String email = jsonObject.get("email").getAsString();
			String fileID = jsonObject.get("fileID").getAsString();
			
			//Begin database access
			if  (hm.get(emailTo) != null) {
				Vector<Session> sendToThese = hm.get(emailTo);
				for (Session s : sendToThese) {
					try {
						if (sessionVector.contains(s)) {
							s.getBasicRemote().sendText("You have a new notification");
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
	}
	@OnClose
	public void close(Session session) {
		System.out.println("Disconnecting!");
		sessionVector.remove(session);
		
		//^ Replace with map remove
		
	}
	@OnError
	public void error(Throwable error) {
		System.out.println("Error!");
	}
	
}
	