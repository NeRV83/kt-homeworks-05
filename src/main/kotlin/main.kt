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
    val comments: Comment?
)

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











