package projet.digitalpharmacy;

import java.sql.*;

public class QuickConnectionTest {
    public static void main(String[] args) {
        System.out.println("🧪 Testing Database Connection...");

        String url = "jdbc:postgresql://localhost:5432/digital_pharmacy";
        String user = "pharmacy_user";
        String password = "pharmacy_pass";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ SUCCESS: Java can connect to database!");

            // Test if we can create tables
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1 as test");
            if (rs.next()) {
                System.out.println("✅ SUCCESS: Can execute queries");
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("❌ FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
}