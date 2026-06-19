package org.example._555laba555.dataBase;

import org.example._555laba555.domain.BatchStatus;
import org.example._555laba555.domain.BatchUnit;
import org.example._555laba555.domain.ReagentBatch;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BatchRepository {
    public long save(ReagentBatch batch) {
        String sql = "INSERT INTO reagent_batches (reagent_id, label, quantity_current, unit, location, expires_at, status, owner_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, batch.getReagentId());
            pstmt.setString(2, batch.getLabel());
            pstmt.setDouble(3, batch.getQuantityCurrent());
            pstmt.setString(4, batch.getUnit().name());
            pstmt.setString(5, batch.getLocation());
            pstmt.setTimestamp(6, batch.getExpiresAt() != null ? Timestamp.from(batch.getExpiresAt()) : null);
            pstmt.setString(7, batch.getStatus().name());
            pstmt.setLong(8, batch.getOwnerId());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка БД при сохранении партии: " + e.getMessage());
        }
        return -1;
    }

    public List<ReagentBatch> findAll() {
        List<ReagentBatch> list = new ArrayList<>();
        String sql = "SELECT b.*, u.login as owner_name FROM reagent_batches b LEFT JOIN users u ON b.owner_id = u.id";
        try (Connection conn = DataBaseManager.getConnection();
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
                Timestamp expires = rs.getTimestamp("expires_at");
                if (expires != null) b.setExpiresAt(expires.toInstant());
                b.setStatus(BatchStatus.valueOf(rs.getString("status")));
                b.setOwnerId(rs.getLong("owner_id"));
                b.setOwnerUsername(rs.getString("owner_name"));
                list.add(b);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка чтения списка партий");
        }
        return list;
    }

    public void updateQuantity(long id, double newQuantity, Instant updatedAt) {
        String sql = "UPDATE reagent_batches SET quantity_current = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newQuantity);
            pstmt.setTimestamp(2, Timestamp.from(updatedAt));
            pstmt.setLong(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка БД при обновлении количества: " + e.getMessage());
        }
    }

    public void delete(long id) {
        String sql = "DELETE FROM reagent_batches WHERE id = ?";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления партии");
        }
    }
}