data class Likes(
    val count: Int = 0,
    val userLikes: Boolean = true,
    val canLike: Boolean = true,
    val canPublish: Boolean = true
)

data class Comment(
    val count: Int = 0,
    val canPost: Boolean = false,
    val groupsCanPost: Boolean = false,
    val canClose: Boolean = false,
    val canOpen: Boolean = false
)

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
    val date: Long = 1767214800,
    val text: String = "",
    val postType: String = "post",
    val canEdit: Boolean = false,
    val canDelete: Boolean = false,
    val canPin: Boolean = false,
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
    val likes: Likes,
    val comments: Comment? = null,
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

object WallService {
    private var posts = emptyArray<Post>()
    private var currentId = 1

    fun add(post: Post): Post {
        val newPost = post.copy(id = currentId++)
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

    fun clear() {
        posts = emptyArray()
        currentId = 1
    }
}

fun main() {
}











