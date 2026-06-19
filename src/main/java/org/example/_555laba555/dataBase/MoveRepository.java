package org.example._555laba555.dataBase;

import org.example._555laba555.domain.BatchUnit;
import org.example._555laba555.domain.StockMove;
import org.example._555laba555.domain.StockMoveType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MoveRepository {
    public long save(StockMove move) {
        String sql = "INSERT INTO stock_moves (batch_id, type, quantity, unit, reason, owner_id, moved_at) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, move.getBatchId());
            pstmt.setString(2, move.getType().name());
            pstmt.setDouble(3, move.getQuantity());
            pstmt.setString(4, move.getUnit().name());
            pstmt.setString(5, move.getReason());
            pstmt.setLong(6, move.getOwnerId());
            pstmt.setTimestamp(7, move.getMovedAt() != null ? Timestamp.from(move.getMovedAt()) : new Timestamp(System.currentTimeMillis()));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка БД при записи движения: " + e.getMessage());
        }
        return -1;
    }

    public List<StockMove> findAll() {
        List<StockMove> list = new ArrayList<>();
        String sql = "SELECT m.*, u.login as owner_name FROM stock_moves m LEFT JOIN users u ON m.owner_id = u.id";
        try (Connection conn = DataBaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                StockMove m = new StockMove();
                m.setId(rs.getLong("id"));
                m.setBatchId(rs.getLong("batch_id"));
                m.setType(StockMoveType.valueOf(rs.getString("type")));
                m.setQuantity(rs.getDouble("quantity"));
                m.setUnit(BatchUnit.valueOf(rs.getString("unit")));
                m.setReason(rs.getString("reason"));
                m.setOwnerId(rs.getLong("owner_id"));
                m.setOwnerUsername(rs.getString("owner_name"));
                Timestamp ts = rs.getTimestamp("moved_at");
                if (ts != null) m.setMovedAt(ts.toInstant());
                list.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка чтения истории движений");
        }
        return list;
    }

    public void delete(long id) {
        String sql = "DELETE FROM stock_moves WHERE id = ?";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления движения");
        }
    }
}