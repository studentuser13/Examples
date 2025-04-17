/** DBManager class
 * Manages operations on a SQLite of player names and high scores
 * Reference
 * https://www.tutorialspoint.com/sqlite/sqlite_java.htm
 */

import javafx.util.Pair;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBManager {

    /** public insertHighScore method
     * inserts a given score and name into the database
     * uses private insertScore and delete methods to so this */
    public static void insertHighScore(String name, int score) {
        insertScore(name, score, delete());
    }

    /** private insertScore method
     * inserts a new entry into the database given its primary key id,
     * name and score */
    private static void insertScore(String name, int score, int id) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:scores.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();

            // Insert new entry
            String sql = "INSERT INTO SCORES (ID,NAME,SCORE)" +
                    "VALUES (" + id + ", '" + name + "', " + score + ");";
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }

    /** public getScores
     * returns arrays of the database's names and scores as a Pair */
    public static Pair<String[], int[]> getScores() {

        Connection c = null;
        Statement stmt = null;
        String[] names = new String[5];
        int[] scores = new int[5];
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:scores.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();

            // Read the first five entries ordered by score
            ResultSet rs = stmt.executeQuery("SELECT * FROM SCORES ORDER BY SCORE DESC LIMIT 5;");
            int i = 0;
            while (rs.next()) {
                names[i] = rs.getString("name");
                scores[i] = rs.getInt("score");
                i++;
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return new Pair<>(names, scores);
    }

    /** private delete method
     * removes the last entry from the database and returns
     * its primary key */
    private static int delete() {
        Connection c = null;
        Statement stmt = null;
        int id = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:scores.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();

            // Read the id of the lowest score
            ResultSet rs = stmt.executeQuery("SELECT * FROM SCORES ORDER BY SCORE ASC LIMIT 1;");

            while (rs.next()) {
                id = rs.getInt("id");
                System.out.println("ID = " + id);
            }
            rs.close();

            // Remove the entry with that primary key;
            String sql = "DELETE from SCORES where ID=" + id + ";";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return id; // Return the primary key
    }

    /** private createDB
     * creates the database if it does not already exist */
    private static void createDB() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:scores.db");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE SCORES " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " SCORE          INT     NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

    /** private showDB
     * outputs the entries in the the database to the console*/
    private static void showDB() {

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:scores.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();

            // Read the first five entries ordered by score
            ResultSet rs = stmt.executeQuery("SELECT * FROM SCORES ORDER BY SCORE DESC LIMIT 5;");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("score");

                System.out.println("ID = " + id);
                System.out.println("NAME = " + name);
                System.out.println("SCORE = " + age);
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }
}
