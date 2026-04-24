package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.MessageDTO;
import mx.uv.spp.dataAcces.config.DatabaseConfig;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MessageDAOImplementationTest {

    private static final Logger LOGGER = Logger.getLogger(MessageDAOImplementationTest.class.getName());
    private MessageDAOImplementation messageDAO;
    private Connection connection;
    private int senderId;
    private int receiverId;

    @BeforeEach
    public void setUp() {
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            messageDAO = new MessageDAOImplementation();

            senderId = insertDummyUser("SenderTest");
            receiverId = insertDummyUser("ReceiverTest");
        } catch (SQLException | DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Error setting up test database", e);
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            if (connection != null && !connection.isClosed()) {
                try (PreparedStatement statement = connection.prepareStatement("DELETE FROM Mensaje")) {
                    statement.executeUpdate();
                }
                try (PreparedStatement statement = connection.prepareStatement("DELETE FROM Usuario WHERE nombre IN ('SenderTest', 'ReceiverTest')")) {
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error cleaning up test database", e);
        }
    }

    @Test
    public void saveMessage_NormalFlow_ReturnsTrue() throws DataAccessException {
        MessageDTO message = createBaseMessage();
        boolean result = messageDAO.saveMessage(message);
        assertTrue(result);
    }

    @Test
    public void saveMessage_AlternateFlow_InvalidSenderFk_ThrowsException() {
        MessageDTO message = createBaseMessage();
        message.setSenderId(999999);

        assertThrows(DataAccessException.class, () -> {
            messageDAO.saveMessage(message);
        });
    }

    @Test
    public void saveMessage_ExceptionFlow_NullSubject_ThrowsException() {
        MessageDTO message = createBaseMessage();
        message.setSubject(null);

        assertThrows(DataAccessException.class, () -> {
            messageDAO.saveMessage(message);
        });
    }

    @Test
    public void findMessageById_NormalFlow_ReturnsMessage() throws Exception {
        int messageId = insertDirectMessageAndGetId();
        MessageDTO result = messageDAO.findMessageById(messageId);

        assertNotNull(result);
        assertEquals("Test Subject", result.getSubject());
    }

    @Test
    public void findMessageById_AlternateFlow_NonExistentId_ReturnsNull() throws DataAccessException {
        MessageDTO result = messageDAO.findMessageById(999999);
        assertNull(result);
    }

    @Test
    public void findAllMessages_NormalFlow_ReturnsList() throws Exception {
        insertDirectMessageAndGetId();
        insertDirectMessageAndGetId();

        List<MessageDTO> list = messageDAO.findAllMessages();

        assertFalse(list.isEmpty());
        assertTrue(list.size() >= 2);
    }

    @Test
    public void findAllMessages_AlternateFlow_EmptyDB_ReturnsEmptyList() throws DataAccessException {
        List<MessageDTO> list = messageDAO.findAllMessages();
        assertTrue(list.isEmpty());
    }

    @Test
    public void updateMessage_NormalFlow_ReturnsTrue() throws Exception {
        int messageId = insertDirectMessageAndGetId();
        MessageDTO message = messageDAO.findMessageById(messageId);
        message.setSubject("Updated Subject");

        boolean result = messageDAO.updateMessage(message);
        assertTrue(result);
    }

    @Test
    public void updateMessage_AlternateFlow_NonExistentId_ReturnsFalse() throws DataAccessException {
        MessageDTO message = createBaseMessage();
        message.setMessageId(999999);

        boolean result = messageDAO.updateMessage(message);
        assertFalse(result);
    }

    @Test
    public void updateMessage_ExceptionFlow_NullBody_ThrowsException() throws Exception {
        int messageId = insertDirectMessageAndGetId();
        MessageDTO message = messageDAO.findMessageById(messageId);
        message.setBody(null);

        assertThrows(DataAccessException.class, () -> {
            messageDAO.updateMessage(message);
        });
    }

    @Test
    public void deleteMessageById_NormalFlow_ReturnsTrue() throws Exception {
        int messageId = insertDirectMessageAndGetId();
        boolean result = messageDAO.deleteMessageById(messageId);
        assertTrue(result);
    }

    @Test
    public void deleteMessageById_AlternateFlow_NonExistentId_ReturnsFalse() throws DataAccessException {
        boolean result = messageDAO.deleteMessageById(999999);
        assertFalse(result);
    }

    private MessageDTO createBaseMessage() {
        MessageDTO message = new MessageDTO();
        message.setSubject("Test Subject");
        message.setBody("Test Body content");
        message.setSendDate(LocalDateTime.now());
        message.setDeleted(false);
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        return message;
    }

    private int insertDummyUser(String name) throws SQLException {
        String sql = "INSERT INTO Usuario (nombre, contrasena, estado, rol) VALUES (?, 'pass123', 'Activo', 'Profesor')";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    private int insertDirectMessageAndGetId() throws SQLException {
        String sql = "INSERT INTO Mensaje (asunto, cuerpo, fecha_envio, eliminado, id_remitente, id_destinatario) VALUES ('Test Subject', 'Body', ?, 0, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
            stmt.setInt(2, senderId);
            stmt.setInt(3, receiverId);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }
}