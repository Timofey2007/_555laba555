package org.example._555laba555.dataBase;

import org.example._555laba555.domain.Reagent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReagentRepository {

    public void insert(Reagent reagent) throws SQLException {
        String sql = "INSERT INTO reagents (name, formula, cas, hazard_class, owner_id, owner_name) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionToData.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, reagent.getName());
            stmt.setString(2, reagent.getFormula());
            stmt.setString(3, reagent.getCas());
            stmt.setString(4, reagent.getHazardClass());
            stmt.setLong(5, reagent.getOwnerId());
            stmt.setString(6, reagent.getOwnerName());

            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                reagent.setId(keys.getLong(1));
            }
        }
    }

    public List<Reagent> findAll() throws SQLException {
        List<Reagent> reagents = new ArrayList<>();
        String sql = "SELECT r.*, u.login as owner_name FROM reagents r " +
                "LEFT JOIN users u ON r.owner_id = u.id";

        try (Connection conn = ConnectionToData.getConnection();
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
                reagents.add(r);
            }
        }
        return reagents;
    }

    public void update(Reagent reagent) throws SQLException {
        String sql = "UPDATE reagents SET name = ?, formula = ?, cas = ?, hazard_class = ?, owner_name = ? WHERE id = ?";
        try (Connection conn = ConnectionToData.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reagent.getName());
            stmt.setString(2, reagent.getFormula());
            stmt.setString(3, reagent.getCas());
            stmt.setString(4, reagent.getHazardClass());
            stmt.setString(5, reagent.getOwnerName());
            stmt.setLong(6, reagent.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM reagents WHERE id = ?";
        try (Connection conn = ConnectionToData.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}