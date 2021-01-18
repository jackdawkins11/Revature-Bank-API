import java.sql.Connection;
import static java.util.Map.entry;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.revature.model.Account;
import com.revature.model.AccountStatus;
import com.revature.model.AccountType;
import com.revature.model.Role;
import com.revature.model.User;

public class Main {

	/*
	 * Tests some JDBC classes to make sure it is working and so I can practice with
	 * JDBC classes
	 */
	private static void runSimpleJDBC() {
		try {
			Connection conn = DriverManager.getConnection(System.getenv("postgres_URL_test"), System.getenv("postgres_user"),
					System.getenv("postgres_pass"));
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from teacher");
			while (rs.next()) {
				System.out.println("Teacher name " + rs.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void runJDBC() {
		try {
			Connection conn = DriverManager.getConnection(System.getenv("postgres_URL_test"), System.getenv("postgres_user"),
					System.getenv("postgres_pass"));
			PreparedStatement stmt = conn.prepareStatement(
					"insert into student (name, pay) values (?, ?) ",
					Statement.RETURN_GENERATED_KEYS
			);
			stmt.setString(1, "Ellen Anderson");
			stmt.setInt(2, 23);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			System.out.println("Genereated key " + rs.getInt(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	static void thrower1() throws SpecificException1, SpecificException2 {
		throw new SpecificException1();
	}
	
	static void thrower2() throws SpecificException1, SpecificException2 {
		throw new SpecificException2();
	}
	
	static void exceptionFunc() throws SpecificException1, SpecificException2 {
		try {
			thrower1();
		}catch(SpecificException2 e) {
			System.out.println("Caught SpecificException2 in exceptionFunc");
		}
	}
	
	static void exceptionTest() {
		try {
			exceptionFunc();
		}catch(SpecificException1 e) {
			System.out.println("Caught SpecificException1 in exceptionTest");
		}catch(SpecificException2 e) {
			System.out.println("Caught SpecificException2 in exceptionTest");
		}
	}

	/*
	 * 
	 */
	private static void testModel() {
		Role role = new Role(1, "Customer");
		User user = new User(1, "jackd1974", "password", "John", "Dawkins", "jackdawkins1974@gmail.com", role);
		System.out.println(user);
		AccountStatus status = new AccountStatus(1, "Open");
		AccountType type = new AccountType(1, "Standard");
		Account account = new Account(1, 10.2, type, status);
		System.out.println(account);
	}
	
	private static void regTest() {
		System.out.println("passwordA!".matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=]).{8,100}$"));
	}
	
	private static void gsonTest() {
		Gson gson = new Gson();
		
		//String json = "{\"name\": 100, \"nested\": { \"num\": 100 } }";
		String json = "{\"nested\": { \"num\": 100 } }";
		Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
        map.forEach((x, y) -> {
        	System.out.print("key : " + x + " , value : " );
        	Map<String, Object> m = (Map<String, Object>) y;
        	System.out.println(m.get("num"));
        });
	}
	
	static void gsonTest2() {
		Gson gson = new Gson();
		
		System.out.println(gson.toJson(Map.ofEntries(entry("message", "Invalid credentials"))));
	}
	
	static void stringTest() {
		String x = "1234";
		System.out.println("234".matches("1?234") );
	}

	static void nullTest(){
		Double x = null;
		System.out.println(x == null);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		nullTest();

	}

}


class SpecificException1 extends Exception{
	
}

class SpecificException2 extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}

