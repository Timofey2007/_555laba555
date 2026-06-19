package org.example._555laba555.dataBase;

import org.example._555laba555.domain.Reagent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReagentRepository {
    public long save(Reagent reagent) {
        String sql = "INSERT INTO reagents (name, formula, cas, hazard_class, owner_id) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, reagent.getName());
            pstmt.setString(2, reagent.getFormula());
            pstmt.setString(3, reagent.getCas());
            pstmt.setString(4, reagent.getHazardClass());
            pstmt.setLong(5, reagent.getOwnerId());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка БД при сохранении реактива: " + e.getMessage());
        }
        return -1;
    }

    public List<Reagent> findAll() {
        List<Reagent> list = new ArrayList<>();
        String sql = "SELECT r.*, u.login as owner_name FROM reagents r LEFT JOIN users u ON r.owner_id = u.id";
        try (Connection conn = DataBaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Reagent r = new Reagent();
                r.setId(rs.getLong("id"));
                r.setName(rs.getString("name"));
                r.setFormula(rs.getString("formula"));
                r.setCas(rs.getString("cas"));
                r.setHazardClass(rs.getString("hazard_class"));
                r.setOwnerId(rs.getLong("owner_id"));
                r.setOwnerName(rs.getString("owner_name"));
                list.add(r);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка чтения списка реактивов");
        }
        return list;
    }

    public void delete(long id) {
        String sql = "DELETE FROM reagents WHERE id = ?";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления реактива");
        }
    }
}