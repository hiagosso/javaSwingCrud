import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataBase {

    private String creatTable = "CREATE TABLE IF NOT EXISTS Usuarios(id INTEGER PRIMARY KEY,"+ 
    "name text NOT NULL UNIQUE)";
    private String listTable = "select * from usuarios";
    private String inserTable = "INSERT OR IGNORE INTO Usuarios(name) values(?)";
    private String deleteById = "DELETE FROM Usuarios WHERE id = ?";
    private String updateByid = "UPDATE Usuarios SET name = ? WHERE id = ?";

    public void creaTable() {
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:dados.db")) {
            Statement smt = con.createStatement();
            smt.execute(creatTable);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public List<String[]> readTable() {
        List<String[]> dados = new ArrayList<>();
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:dados.db")) {
            Statement smt = con.createStatement();
            ResultSet rs = smt.executeQuery(listTable);
            while (rs.next()) {
                String id = rs.getString("id");
                String nome = rs.getString("name");
                dados.add(new String[] { id, nome });
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return dados;
    }

    public void insertTable(String name) {
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:dados.db")) {
            PreparedStatement psmt = con.prepareStatement(inserTable);
            psmt.setString(1, name);
            psmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void deleteById(int id) {
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:dados.db")) {
            PreparedStatement psmt = con.prepareStatement(deleteById);
            psmt.setInt(1, id);
            psmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void updateById(int id, String name) {
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:dados.db")) {
            PreparedStatement psmt = con.prepareStatement(updateByid);
            psmt.setString(1,name);
            psmt.setInt(2, id);
            psmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

}
