
// Исключения

class ChatNotFoundException(message: String) : RuntimeException(message)
class MessageNotFoundException(message: String) : RuntimeException(message)
class ChatAlreadyExistsException(message: String) : RuntimeException(message)
class PostNotFoundException(message: String) : RuntimeException(message)
class NoteNotFoundException(message: String) : RuntimeException(message)
class NoteDeletedException(message: String) : RuntimeException(message)
class CommentNotFoundException(message: String) : RuntimeException(message)
class CommentDeletedException(message: String) : RuntimeException(message)

// Модели данных

data class Message(
    val id: Int,
    val chatId: Int,
    val senderId: Int,
    var text: String,
    val timestamp: Long = System.currentTimeMillis(),
    var isRead: Boolean = false,
    var isDeleted: Boolean = false
)

data class Chat(
    val id: Int,
    val participantId: Int,
    val messages: MutableList<Message> = mutableListOf()
)

data class Notes(
    val id: Int = 0,
    val ownerId: Int = 0,
    val title: String = "",
    val text: String = "",
    val date: Long = System.currentTimeMillis(),
    val comments: Int = 0
)

data class NoteComment(
    val id: Int,
    val userId: Int,
    val noteId: Int,
    val ownerId: Int,
    val date: Long = System.currentTimeMillis(),
    val message: String,
    var isDeleted: Boolean = false
)

data class Likes(
    val count: Int = 0,
    val userLikes: Boolean = true,
    val canLike: Boolean = true,
    val canPublish: Boolean = true
)

//data class Comment(
//    val count: Int = 0,
//    val canPost: Boolean = false,
//    val groupsCanPost: Boolean = false,
//    val canClose: Boolean = false,
//    val canOpen: Boolean = false
//)

data class Comment(
    val id: Int = 0,
    val fromID: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val text: String = "",
    val replyToUser: Int = 0,
    val replyToComment: Int = 0,
    val attachments: Array<Attachment> = emptyArray()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Comment

        if (id != other.id) return false
        if (fromID != other.fromID) return false
        if (date != other.date) return false
        if (replyToUser != other.replyToUser) return false
        if (replyToComment != other.replyToComment) return false
        if (text != other.text) return false
        if (!attachments.contentEquals(other.attachments)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + fromID
        result = 31 * result + date.hashCode()
        result = 31 * result + replyToUser
        result = 31 * result + replyToComment
        result = 31 * result + text.hashCode()
        result = 31 * result + attachments.contentHashCode()
        return result
    }
}

interface Attachment {
    val type: String
}

data class Photo(
    val id: Int,
    val albumId: Int,
    val ownerId: Int,
    val userId: Int,
    val text: String = "",
    val date: Long,
    val width: Int,
    val height: Int,
    val url: String,
    val tags: List<String> = emptyList()
)

data class PhotoAttachment(
    val photo: Photo
) : Attachment {
    override val type: String = "photo"
}

data class Video(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val description: String = "",
    val duration: Int, // в секундах
    val date: Long,
    val views: Int = 0,
    val comments: Int = 0,
    val playerUrl: String,
    val accessKey: String = ""
)

data class VideoAttachment(
    val video: Video
) : Attachment {
    override val type: String = "video"
}

data class Audio(
    val id: Int,
    val ownerId: Int,
    val artist: String,
    val title: String,
    val duration: Int, // в секундах
    val url: String = "",
    val albumId: Int? = null,
    val date: Long,
    val genreId: Int? = null
)

data class AudioAttachment(
    val audio: Audio
) : Attachment {
    override val type: String = "audio"
}

data class Document(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val size: Long, // в байтах
    val ext: String, // расширение файла
    val url: String,
    val date: Long,
    val type: Int = 0, // тип документа
    val preview: DocPreview? = null
)

data class DocPreview(
    val photo: Photo? = null,
    val video: Video? = null
)

data class DocumentAttachment(
    val document: Document
) : Attachment {
    override val type: String = "doc"
}

data class Link(
    val url: String,
    val title: String,
    val caption: String? = null,
    val description: String = "",
    val photo: Photo? = null
)

data class LinkAttachment(
    val link: Link
) : Attachment {
    override val type: String = "link"
}

data class Post(
    val id: Int = 0,
    val toID: Int = 0,
    val fromID: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val text: String = "",
    val postType: String = "post",
    val canEdit: Boolean = false,
    val canDelete: Boolean = false,
    val canPin: Boolean = false,
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
    val likes: Likes,
    val comments: Array<Comment> = emptyArray(),
    val attachments: Array<Attachment> = emptyArray()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Post

        if (id != other.id) return false
        if (toID != other.toID) return false
        if (fromID != other.fromID) return false
        if (date != other.date) return false
        if (canEdit != other.canEdit) return false
        if (canDelete != other.canDelete) return false
        if (canPin != other.canPin) return false
        if (isPinned != other.isPinned) return false
        if (isFavorite != other.isFavorite) return false
        if (text != other.text) return false
        if (postType != other.postType) return false
        if (likes != other.likes) return false
        if (comments != other.comments) return false
        if (!attachments.contentEquals(other.attachments)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + toID
        result = 31 * result + fromID
        result = 31 * result + date.hashCode()
        result = 31 * result + canEdit.hashCode()
        result = 31 * result + canDelete.hashCode()
        result = 31 * result + canPin.hashCode()
        result = 31 * result + isPinned.hashCode()
        result = 31 * result + isFavorite.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + postType.hashCode()
        result = 31 * result + likes.hashCode()
        result = 31 * result + (comments?.hashCode() ?: 0)
        result = 31 * result + attachments.contentHashCode()
        return result
    }
}

// Сервисы

object WallService {
    private var posts = emptyArray<Post>()
    private var currentPostId = 1
    private var currentCommentId = 1

    fun createComment(postId: Int, comment: Comment): Comment {
        val post = findById(postId)
        val newComment = comment.copy(id = currentCommentId++)
        val updatedPost = post.copy(
            comments = post.comments + newComment
        )
        val postIndex = posts.indexOfFirst { it.id == postId }
        posts[postIndex] = updatedPost
        return newComment
    }

    fun findById(id: Int): Post =
        posts.find { it.id == id } ?: throw PostNotFoundException("Post with id $id not found")

    fun add(post: Post): Post {
        val newPost = post.copy(id = currentPostId++)
        posts += newPost
        return newPost
    }

    fun update(post: Post): Boolean {
        for ((index, existingPost) in posts.withIndex()) {
            if (existingPost.id == post.id) {
                posts[index] = post.copy(id = existingPost.id)
                return true
            }
        }
        return false
    }

    //fun removeById(id: Int): Boolean = posts.removeIf { it.id == id } ?: throw PostNotFoundException("Post with id $id not found")

    fun clear() {
        posts = emptyArray()
        currentPostId = 1
    }
}

object NoteService {
    private var notes = mutableMapOf<Int, Notes>()
    private var comments = mutableMapOf<Int, NoteComment>()
    private var nextNoteId = 1
    private var nextCommentId = 1

    // Generic метод для проверки существования
    private fun <T> checkExists(id: Int, map: Map<Int, T>, entityName: String): T {
        return map[id] ?: throw NotFoundException("$entityName with id $id not found")
    }

    class NotFoundException(message: String) : RuntimeException(message)

    fun add(title: String, text: String): Int {
        val note = Notes(
            id = nextNoteId++,
            title = title,
            text = text
        )
        notes[note.id] = note
        return note.id
    }

    fun createComment(noteId: Int, ownerId: Int, message: String, commentId: Int? = null): Int {
        val note = checkExists(noteId, notes, "Note")

        val comment = NoteComment(
            id = commentId ?: nextCommentId++,
            userId = ownerId,
            noteId = noteId,
            ownerId = ownerId,
            message = message,
            isDeleted = false
        )
        comments[comment.id] = comment

        val updatedNote = note.copy(comments = note.comments + 1)
        notes[noteId] = updatedNote

        return comment.id
    }

    fun delete(noteId: Int): Boolean {
        val note = checkExists(noteId, notes, "Note")

        // Помечаем все комментарии заметки как удаленные
        comments.values
            .filter { it.noteId == noteId }
            .forEach { it.isDeleted = true }

        notes.remove(noteId)
        return true
    }

    fun deleteComment(commentId: Int, ownerId: Int): Boolean {
        val comment = comments[commentId]
            ?: throw CommentNotFoundException("Comment with id $commentId not found")

        if (comment.ownerId != ownerId) {
            throw IllegalArgumentException("User $ownerId is not the owner of comment $commentId")
        }

        if (comment.isDeleted) {
            throw CommentDeletedException("Comment $commentId is already deleted")
        }

        comment.isDeleted = true
        return true
    }

    fun edit(noteId: Int, title: String, text: String): Boolean {
        val note = checkExists(noteId, notes, "Note")

        val updatedNote = note.copy(
            title = title,
            text = text
        )
        notes[noteId] = updatedNote
        return true
    }

    fun editComment(commentId: Int, ownerId: Int, message: String): Boolean {
        val comment = comments[commentId]
            ?: throw CommentNotFoundException("Comment with id $commentId not found")

        if (comment.ownerId != ownerId) {
            throw IllegalArgumentException("User $ownerId is not the owner of comment $commentId")
        }

        if (comment.isDeleted) {
            throw CommentDeletedException("Cannot edit deleted comment $commentId")
        }

        val updatedComment = comment.copy(message = message)
        comments[commentId] = updatedComment
        return true
    }

    fun getById(noteId: Int, ownerId: Int): Notes {
        val note = checkExists(noteId, notes, "Note")

        if (note.ownerId != ownerId) {
            throw IllegalArgumentException("User $ownerId is not the owner of note $noteId")
        }

        return note
    }

    fun getComments(noteId: Int, ownerId: Int, sort: Int = 0): Array<NoteComment> {
        val note = checkExists(noteId, notes, "Note")

        if (note.ownerId != ownerId) {
            throw IllegalArgumentException("User $ownerId is not the owner of note $noteId")
        }

        val noteComments = comments.values
            .filter { it.noteId == noteId && !it.isDeleted }
            .toList()

        return when (sort) {
            0 -> noteComments.sortedBy { it.date }.toTypedArray()
            1 -> noteComments.sortedByDescending { it.date }.toTypedArray()
            else -> noteComments.toTypedArray()
        }
    }

    fun get(ownerId: Int, sort: Int = 0): List<Notes> {
        val userNotes = notes.values
            .filter { it.ownerId == ownerId }
            .toList()

        return when (sort) {
            0 -> userNotes.sortedBy { it.date }
            1 -> userNotes.sortedByDescending { it.date }
            else -> userNotes
        }
    }

    fun restoreComment(commentId: Int, ownerId: Int): Boolean {
        val comment = comments[commentId]
            ?: throw CommentNotFoundException("Comment with id $commentId not found")

        if (comment.ownerId != ownerId) {
            throw IllegalArgumentException("User $ownerId is not the owner of comment $commentId")
        }

        if (!comment.isDeleted) {
            throw CommentDeletedException("Comment $commentId is not deleted")
        }

        if (!notes.containsKey(comment.noteId)) {
            throw NoteDeletedException("Cannot restore comment - note ${comment.noteId} is deleted")
        }

        comment.isDeleted = false
        return true
    }

    fun clear() {
        notes = mutableMapOf()
        comments = mutableMapOf()
        nextNoteId = 1
        nextCommentId = 1
    }
}

object ChatService {
    private var chats = mutableMapOf<Int, Chat>() // key: chatId
    private var messages = mutableMapOf<Int, Message>() // key: messageId
    private var nextChatId = 1
    private var nextMessageId = 1
    private val currentUserId = 1 // Текущий пользователь системы

    // Вспомогательные extension функции
    private fun <T> MutableMap<Int, T>.findByParticipant(participantId: Int): Chat? =
        this.values.firstOrNull { (it as Chat).participantId == participantId } as Chat?

    private fun List<Message>.unreadCount(): Int = this.count { !it.isRead && !it.isDeleted }

    private fun List<Message>.lastNonDeletedMessage(): String =
        this.filterNot { it.isDeleted }
            .maxByOrNull { it.timestamp }
            ?.text
            ?: "нет сообщений"

    // Получить количество непрочитанных чатов
    fun getUnreadChatsCount(): Int = chats.values
        .map { it.messages }
        .count { messages -> messages.any { !it.isRead && !it.isDeleted } }

    // Получить список чатов
    fun getChats(): List<Chat> = chats.values.toList()

    // Получить список последних сообщений из чатов
    fun getLastMessages(): List<String> = chats.values
        .map { it.messages }
        .map { messages -> messages.lastNonDeletedMessage() }

    // Получить список сообщений из чата с автоматическим прочтением
    fun getMessages(participantId: Int, count: Int): List<Message> {
        val chat = chats.findByParticipant(participantId)
            ?: throw ChatNotFoundException("Chat with participant $participantId not found")

        val messages = chat.messages
            .asSequence()
            .filterNot { it.isDeleted }
            .sortedByDescending { it.timestamp }
            .take(count)
            .onEach { it.isRead = true } // Отмечаем как прочитанные
            .toList()

        return messages
    }

    // Создать новое сообщение (и чат при необходимости)
    fun sendMessage(participantId: Int, text: String): Message {
        val chat = chats.findByParticipant(participantId) ?: run {
            // Создаем новый чат
            val newChat = Chat(
                id = nextChatId++,
                participantId = participantId
            )
            chats[newChat.id] = newChat
            newChat
        }

        val message = Message(
            id = nextMessageId++,
            chatId = chat.id,
            senderId = currentUserId,
            text = text,
            timestamp = System.currentTimeMillis()
        )

        messages[message.id] = message
        chat.messages.add(message)
        return message
    }

    // Удалить сообщение (мягкое удаление)
    fun deleteMessage(messageId: Int): Boolean {
        val message = messages[messageId]
            ?: throw MessageNotFoundException("Message with id $messageId not found")

        message.isDeleted = true
        return true
    }

    // Редактировать сообщение
    fun editMessage(messageId: Int, newText: String): Boolean {
        val message = messages[messageId]
            ?: throw MessageNotFoundException("Message with id $messageId not found")

        if (message.isDeleted) {
            throw MessageNotFoundException("Cannot edit deleted message")
        }

        message.text = newText
        return true
    }

    // Удалить чат полностью
    fun deleteChat(participantId: Int): Boolean {
        val chat = chats.findByParticipant(participantId)
            ?: throw ChatNotFoundException("Chat with participant $participantId not found")

        // Удаляем все сообщения чата
        messages.values
            .filter { it.chatId == chat.id }
            .forEach { messages.remove(it.id) }

        chats.remove(chat.id)
        return true
    }

    // Проверить существование чата
    fun hasChat(participantId: Int): Boolean = chats.findByParticipant(participantId) != null

    // Получить непрочитанные сообщения из конкретного чата
    fun getUnreadMessages(participantId: Int): List<Message> {
        val chat = chats.findByParticipant(participantId)
            ?: throw ChatNotFoundException("Chat with participant $participantId not found")

        return chat.messages
            .filter { !it.isRead && !it.isDeleted }
    }

    fun clear() {
        chats.clear()
        messages.clear()
        nextChatId = 1
        nextMessageId = 1
    }
}

fun main() {
}











