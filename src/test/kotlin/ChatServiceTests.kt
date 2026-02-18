import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.IllegalArgumentException

class ChatServiceTest {

    @Before
    fun clearBeforeTest() {
        ChatService.clear()
    }

    @Test
    fun sendFirstMessageShouldCreateNewChat() {
        val message = ChatService.sendMessage(2, "Привет!")

        assertTrue(ChatService.hasChat(2))
        assertEquals(1, ChatService.getChats().size)
        assertEquals("Привет!", message.text)
    }

    @Test
    fun sendMessageShouldAddMessageToExistingChat() {
        ChatService.sendMessage(2, "Первое сообщение")

        val secondMessage = ChatService.sendMessage(2, "Второе сообщение")

        assertEquals(1, ChatService.getChats().size)
        val chat = ChatService.getChats().first()
        assertEquals(2, chat.messages.size)
        assertEquals("Второе сообщение", secondMessage.text)
    }

    @Test
    fun getUnreadChatsCountShouldReturnCorrectCountWhenChatsHaveUnreadMessages() {
        ChatService.sendMessage(2, "Сообщение пользователю 2")
        ChatService.sendMessage(3, "Сообщение пользователю 3")
        ChatService.sendMessage(4, "Сообщение пользователю 4")

        val unreadCount = ChatService.getUnreadChatsCount()

        assertEquals(3, unreadCount)
    }

    @Test
    fun getUnreadChatsCountShouldReturnZeroWhenAllMessagesAreRead() {
        ChatService.sendMessage(2, "Сообщение пользователю 2")
        ChatService.sendMessage(3, "Сообщение пользователю 3")
        ChatService.getMessages(2, 10)
        ChatService.getMessages(3, 10)

        val unreadCount = ChatService.getUnreadChatsCount()

        assertEquals(0, unreadCount)
    }

    @Test
    fun getChatsShouldReturnAllExistingChats() {
        ChatService.sendMessage(2, "Сообщение 1")
        ChatService.sendMessage(3, "Сообщение 2")
        ChatService.sendMessage(4, "Сообщение 3")

        val chats = ChatService.getChats()

        assertEquals(3, chats.size)
        assertTrue(chats.any { it.participantId == 2 })
        assertTrue(chats.any { it.participantId == 3 })
        assertTrue(chats.any { it.participantId == 4 })
    }

    @Test
    fun getLastMessagesShouldReturnCorrectLastMessagesForEachChat() {
        ChatService.sendMessage(2, "Первое сообщение пользователю 2")
        Thread.sleep(10)
        ChatService.sendMessage(2, "Второе сообщение пользователю 2")
        Thread.sleep(10)
        ChatService.sendMessage(3, "Единственное сообщение пользователю 3")

        val lastMessages = ChatService.getLastMessages()

        assertEquals(2, lastMessages.size)
        assertTrue(lastMessages.any { it == "Второе сообщение пользователю 2" })
        assertTrue(lastMessages.any { it == "Единственное сообщение пользователю 3" })
    }

    @Test
    fun getLastMessagesShouldReturnNoMessagesTextForChatWithAllMessagesDeleted() {
        val message1 = ChatService.sendMessage(2, "Сообщение 1")
        val message2 = ChatService.sendMessage(2, "Сообщение 2")
        ChatService.deleteMessage(message1.id)
        ChatService.deleteMessage(message2.id)

        val lastMessages = ChatService.getLastMessages()

        assertEquals(1, lastMessages.size)
        assertEquals("нет сообщений", lastMessages.first())
    }

    @Test
    fun getMessagesShouldReturnRequestedNumberOfMessages() {
        ChatService.sendMessage(2, "Сообщение 1")
        ChatService.sendMessage(2, "Сообщение 2")
        ChatService.sendMessage(2, "Сообщение 3")
        ChatService.sendMessage(2, "Сообщение 4")

        val messages = ChatService.getMessages(2, 2)

        assertEquals(2, messages.size)
    }

    @Test
    fun getMessagesShouldMarkMessagesAsRead() {
        ChatService.sendMessage(2, "Сообщение 1")
        ChatService.sendMessage(2, "Сообщение 2")

        val messages = ChatService.getMessages(2, 2)

        assertTrue(messages.all { it.isRead })
    }

    @Test(expected = ChatNotFoundException::class)
    fun getMessagesShouldThrowExceptionWhenChatNotFound() {
        ChatService.getMessages(99, 10)
    }

    @Test
    fun deleteMessageShouldMarkMessageAsDeleted() {
        val message = ChatService.sendMessage(2, "Сообщение для удаления")

        ChatService.deleteMessage(message.id)

        val chat = ChatService.getChats().first()
        val deletedMessage = chat.messages.first { it.id == message.id }
        assertTrue(deletedMessage.isDeleted)
    }

    @Test(expected = MessageNotFoundException::class)
    fun deleteMessageShouldThrowExceptionWhenMessageNotFound() {
        ChatService.deleteMessage(999)
    }

    @Test
    fun editMessageShouldUpdateMessageText() {
        val message = ChatService.sendMessage(2, "Оригинальный текст")
        val newText = "Обновленный текст"

        ChatService.editMessage(message.id, newText)

        val chat = ChatService.getChats().first()
        val editedMessage = chat.messages.first { it.id == message.id }
        assertEquals(newText, editedMessage.text)
    }

    @Test(expected = MessageNotFoundException::class)
    fun editMessageShouldThrowExceptionWhenMessageNotFound() {
        ChatService.editMessage(999, "Новый текст")
    }

    @Test(expected = MessageNotFoundException::class)
    fun editMessageShouldThrowExceptionWhenMessageIsDeleted() {
        val message = ChatService.sendMessage(2, "Сообщение для удаления")
        ChatService.deleteMessage(message.id)

        ChatService.editMessage(message.id, "Попытка редактировать удаленное")
    }

    @Test
    fun deleteChatShouldRemoveChatAndAllItsMessages() {
        ChatService.sendMessage(2, "Сообщение 1")
        ChatService.sendMessage(2, "Сообщение 2")
        ChatService.sendMessage(3, "Сообщение 3")

        ChatService.deleteChat(2)

        val chats = ChatService.getChats()
        assertEquals(1, chats.size)
        assertEquals(3, chats.first().participantId)
    }

    @Test(expected = ChatNotFoundException::class)
    fun deleteChatShouldThrowExceptionWhenChatNotFound() {
        ChatService.deleteChat(999)
    }

    @Test
    fun getUnreadMessagesShouldReturnOnlyUnreadMessages() {
        val message1 = ChatService.sendMessage(2, "Непрочитанное 1")
        val message2 = ChatService.sendMessage(2, "Непрочитанное 2")
        ChatService.getMessages(2, 1)

        val unreadMessages = ChatService.getUnreadMessages(2)

        assertEquals(1, unreadMessages.size)
        assertEquals(message2.id, unreadMessages.first().id)
    }

    @Test(expected = ChatNotFoundException::class)
    fun getUnreadMessagesShouldThrowExceptionWhenChatNotFound() {
        ChatService.getUnreadMessages(999)
    }

    @Test
    fun hasChatShouldReturnTrueWhenChatExists() {
        ChatService.sendMessage(2, "Тестовое сообщение")

        assertTrue(ChatService.hasChat(2))
    }

    @Test
    fun hasChatShouldReturnFalseWhenChatDoesNotExist() {
        assertFalse(ChatService.hasChat(99))
    }

    @Test
    fun getMessagesShouldReturnOnlyNonDeletedMessages() {
        ChatService.sendMessage(2, "Сообщение 1")
        val message2 = ChatService.sendMessage(2, "Сообщение 2")
        ChatService.sendMessage(2, "Сообщение 3")
        ChatService.deleteMessage(message2.id)

        val messages = ChatService.getMessages(2, 10)

        assertEquals(2, messages.size)
        assertTrue(messages.none { it.id == message2.id })
    }

    @Test
    fun sendMessageShouldIncrementMessageIdCorrectly() {
        val message1 = ChatService.sendMessage(2, "Сообщение 1")
        val message2 = ChatService.sendMessage(2, "Сообщение 2")
        val message3 = ChatService.sendMessage(3, "Сообщение 3")

        assertEquals(1, message1.id)
        assertEquals(2, message2.id)
        assertEquals(3, message3.id)
    }

    @Test
    fun sendMessageShouldIncrementChatIdCorrectly() {
        val message1 = ChatService.sendMessage(2, "Сообщение 1")
        val message2 = ChatService.sendMessage(3, "Сообщение 2")
        val message3 = ChatService.sendMessage(4, "Сообщение 3")

        assertEquals(1, message1.chatId)
        assertEquals(2, message2.chatId)
        assertEquals(3, message3.chatId)
    }

    @Test
    fun clearShouldResetAllData() {
        ChatService.sendMessage(2, "Сообщение 1")
        ChatService.sendMessage(3, "Сообщение 2")

        ChatService.clear()

        assertEquals(0, ChatService.getChats().size)
        assertEquals(0, ChatService.getUnreadChatsCount())
        assertFalse(ChatService.hasChat(2))
    }

    @Test
    fun getMessagesShouldReturnMessagesInReverseChronologicalOrder() {
        val message1 = ChatService.sendMessage(2, "Сообщение 1")
        Thread.sleep(10)
        val message2 = ChatService.sendMessage(2, "Сообщение 2")
        Thread.sleep(10)
        val message3 = ChatService.sendMessage(2, "Сообщение 3")

        val messages = ChatService.getMessages(2, 3)

        assertEquals(message3.id, messages[0].id)
        assertEquals(message2.id, messages[1].id)
        assertEquals(message1.id, messages[2].id)
    }
}