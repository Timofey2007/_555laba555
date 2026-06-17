package org.example._555laba555.dataBase;

import org.example._555laba555.domain.StockMove;
import org.example._555laba555.domain.StockMoveType;
import org.example._555laba555.domain.BatchUnit;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MoveRepository {

    public void insert(StockMove move) throws SQLException {
        String sql = "INSERT INTO stock_moves (batch_id, type, quantity, unit, reason, owner_id, owner_name, moved_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionToData.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, move.getBatchId());
            stmt.setString(2, move.getType().name());
            stmt.setDouble(3, move.getQuantity());
            stmt.setString(4, move.getUnit().name());
            stmt.setString(5, move.getReason());
            stmt.setLong(6, move.getOwnerId());
            stmt.setString(7, move.getOwnerUsername());
            stmt.setTimestamp(8, move.getMovedAt() != null ? Timestamp.from(move.getMovedAt()) : Timestamp.from(Instant.now()));

            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                move.setId(keys.getLong(1));
            }
        }
    }

    public List<StockMove> findAll() throws SQLException {
        List<StockMove> moves = new ArrayList<>();
        String sql = "SELECT m.*, u.login as owner_name FROM stock_moves m " +
                "LEFT JOIN users u ON m.owner_id = u.id";

        try (Connection conn = ConnectionToData.getConnection();
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
                Timestamp ts = rs.getTimestamp("moved_at");
                if (ts != null) m.setMovedAt(ts.toInstant());
                moves.add(m);
            }
        }
        return moves;
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM stock_moves WHERE id = ?";
        try (Connection conn = ConnectionToData.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}