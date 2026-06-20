package org.example._555laba555.dataBase;

import org.example._555laba555.service.HistoryRecord;
import org.example._555laba555.validation.StorageException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryRepository {

    public HistoryRepository() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS deletion_history (" +
                "id SERIAL PRIMARY KEY, " +
                "object_type VARCHAR(20) NOT NULL, " +
                "object_id BIGINT NOT NULL, " +
                "object_data TEXT NOT NULL, " +
                "deleted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "deleted_by BIGINT REFERENCES users(id))";
        try (Connection conn = DataBaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Не удалось создать таблицу deletion_history: " + e.getMessage());
        }
    }

    public void save(HistoryRecord record) {
        String sql = "INSERT INTO deletion_history (object_type, object_id, object_data, deleted_by) " +
                "VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, record.getObjectType());
            pstmt.setLong(2, record.getObjectId());
            pstmt.setString(3, record.getObjectData());
            pstmt.setLong(4, record.getDeletedBy());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                record.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new StorageException("Ошибка сохранения в историю: " + e.getMessage(), e);
        }
    }

    public List<HistoryRecord> findAll() {
        List<HistoryRecord> list = new ArrayList<>();
        String sql = "SELECT h.*, u.login AS deleted_by_name FROM deletion_history h " +
                "LEFT JOIN users u ON h.deleted_by = u.id ORDER BY h.deleted_at DESC";
        try (Connection conn = DataBaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                HistoryRecord r = new HistoryRecord();
                r.setId(rs.getLong("id"));
                r.setObjectType(rs.getString("object_type"));
                r.setObjectId(rs.getLong("object_id"));
                r.setObjectData(rs.getString("object_data"));
                r.setDeletedBy(rs.getLong("deleted_by"));
                r.setDeletedByName(rs.getString("deleted_by_name"));
                Timestamp ts = rs.getTimestamp("deleted_at");
                if (ts != null) r.setDeletedAt(ts.toLocalDateTime());
                list.add(r);
            }
        } catch (SQLException e) {
            throw new StorageException("Ошибка загрузки истории: " + e.getMessage(), e);
        }
        return list;
    }

    public HistoryRecord findById(long id) {
        String sql = "SELECT * FROM deletion_history WHERE id = ?";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                HistoryRecord r = new HistoryRecord();
                r.setId(rs.getLong("id"));
                r.setObjectType(rs.getString("object_type"));
                r.setObjectId(rs.getLong("object_id"));
                r.setObjectData(rs.getString("object_data"));
                r.setDeletedBy(rs.getLong("deleted_by"));
                Timestamp ts = rs.getTimestamp("deleted_at");
                if (ts != null) r.setDeletedAt(ts.toLocalDateTime());
                return r;
            }
        } catch (SQLException e) {
            throw new StorageException("Ошибка поиска в истории: " + e.getMessage(), e);
        }
        return null;
    }

    public void delete(long id) {
        String sql = "DELETE FROM deletion_history WHERE id = ?";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new StorageException("Ошибка удаления из истории: " + e.getMessage(), e);
        }
    }
}