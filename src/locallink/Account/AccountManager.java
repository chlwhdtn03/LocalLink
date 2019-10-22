package locallink.Account;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AccountManager {
	
	private static List<Account> users = new ArrayList<Account>();

	private static File db = new File(System.getenv("appdata") + "/LocalLink/");
	public static void addAccount(String id, String password, String name) {
		users.add(new Account(id, name, password));
	}
	
	public static void removeAccount(String id, String password, String name) {
		users.remove(users.indexOf(new Account(id,name,password)));
	}
	
	public static boolean containsID(String id) {
		for(Account user : users) {
			if(user.id.equals(id))
				return true;
		}
		return false;
	}
	
	public static Account getAccount(String id) {
		if(!containsID(id))
			return null;
		for(Account user : users) {
			if(user.id.equals(id))
				return user;
		}
		return null;
	}
	
	public static boolean login(String id, String password) {
		for(Account user : users) {
			if(user.id.equals(id)) {
				if(user.password.equals(password))
					return true;
				return false;
				}
		}
		return false;
	}
	
	public static void saveUser() {
		if(!db.isDirectory())
			db.mkdirs();
		try {
			
			Connection con = DriverManager.getConnection("jdbc:sqlite:" + db.getPath() + "/Account.db");
			Statement s = con.createStatement();
			s.execute("CREATE TABLE IF NOT EXISTS Account ("
					+ "ID TEXT,"
					+ "Name TEXT,"
					+ "Password TEXT )");
			for(Account user : users) {
				if(s.executeQuery("SELECT * FROM Account WHERE id='"+user.id+ "'").next())
					continue;
				s.execute("INSERT INTO Account (ID, Name, Password) "
						+ "VALUES ('" + user.id + "', '" + user.name + "', '" + user.password + "') ");
			}
			s.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadUser() {
		if(!db.isDirectory())
			db.mkdir();
		try {
			System.out.println(db.getPath());
			Connection con = DriverManager.getConnection("jdbc:sqlite:" + db.getPath() + "/Account.db");
			Statement s = con.createStatement();
			s.execute("CREATE TABLE IF NOT EXISTS Account ("
					+ "ID TEXT,"
					+ "Name TEXT,"
					+ "Password TEXT )");
			ResultSet result = s.executeQuery("SELECT * FROM Account");
			while(result.next()) {
				users.add(new Account(result.getString("ID"), result.getString("Name"), result.getString("Password")));
			}
			s.close();
			con.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
