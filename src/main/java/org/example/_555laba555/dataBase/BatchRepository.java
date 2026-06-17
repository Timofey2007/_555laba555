package org.example._555laba555.dataBase;

import org.example._555laba555.domain.ReagentBatch;
import org.example._555laba555.domain.BatchUnit;
import org.example._555laba555.domain.BatchStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BatchRepository {

    public void insert(ReagentBatch batch) throws SQLException {
        String sql = "INSERT INTO reagent_batches (reagent_id, label, quantity_current, unit, " +
                "location, expires_at, status, owner_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = org.example._555laba555.dataBase.ConnectionToData.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, batch.getReagentId());
            stmt.setString(2, batch.getLabel());
            stmt.setDouble(3, batch.getQuantityCurrent());
            stmt.setString(4, batch.getUnit().name());
            stmt.setString(5, batch.getLocation());
            stmt.setTimestamp(6, batch.getExpiresAt() != null ? Timestamp.from(batch.getExpiresAt()) : null);
            stmt.setString(7, batch.getStatus().name());
            stmt.setLong(8, batch.getOwnerId());

            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                batch.setId(keys.getLong(1));
            }
        }
    }

    public List<ReagentBatch> findAll() throws SQLException {
        List<ReagentBatch> batches = new ArrayList<>();
        String sql = "SELECT b.*, u.login as owner_name FROM reagent_batches b " +
                "LEFT JOIN users u ON b.owner_id = u.id";

        try (Connection conn = ConnectionToData.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ReagentBatch b = new ReagentBatch();
                b.setId(rs.getLong("id"));
                b.setReagentId(rs.getLong("reagent_id"));
                b.setLabel(rs.getString("label"));
                b.setQuantityCurrent(rs.getDouble("quantity_current"));
                b.setUnit(BatchUnit.valueOf(rs.getString("unit")));
                b.setLocation(rs.getString("location"));
                Timestamp ts = rs.getTimestamp("expires_at");
                if (ts != null) b.setExpiresAt(ts.toInstant());
                b.setStatus(BatchStatus.valueOf(rs.getString("status")));
                b.setOwnerId(rs.getLong("owner_id"));
                b.setOwnerName(rs.getString("owner_name"));
                batches.add(b);
            }
        }
        return batches;
    }

    public void update(ReagentBatch batch) throws SQLException {
        String sql = "UPDATE reagent_batches SET label = ?, quantity_current = ?, " +
                "location = ?, expires_at = ?, status = ? WHERE id = ?";
        try (Connection conn = ConnectionToData.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, batch.getLabel());
            stmt.setDouble(2, batch.getQuantityCurrent());
            stmt.setString(3, batch.getLocation());
            stmt.setTimestamp(4, batch.getExpiresAt() != null ? Timestamp.from(batch.getExpiresAt()) : null);
            stmt.setString(5, batch.getStatus().name());
            stmt.setLong(6, batch.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM reagent_batches WHERE id = ?";
        try (Connection conn = ConnectionToData.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}