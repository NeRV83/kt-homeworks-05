import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class WallServiceTest {

    @Before
    fun clearBeforeTest() {
        WallService.clear()
    }

    @Test
    fun testAddPostIdNotZero() {
        val post = Post(
            id = 0,
            toID = 1,
            fromID = 2,
            date = 1234567890,
            text = "Тестовый пост",
            postType = "post",
            canEdit = true,
            canDelete = true,
            canPin = true,
            isPinned = false,
            isFavorite = false,
            likes = Likes(0, false, true, true),
            comments = Comment(0, false, false, false, false)
        )

        val result = WallService.add(post)
        assertNotEquals("ID поста должен быть не равен 0 после добавления", 0, result.id)
    }

    @Test
    fun testUpdateExistingPostReturnsTrue() {
        val post = Post(
            id = 0,
            toID = 1,
            fromID = 2,
            date = 1234567890,
            text = "Тестовый пост для обновления",
            postType = "post",
            canEdit = true,
            canDelete = true,
            canPin = true,
            isPinned = false,
            isFavorite = false,
            likes = Likes(0, false, true, true),
            comments = Comment(0, false, false, false, false)
        )

        val addedPost = WallService.add(post)

        val updatedPost = addedPost.copy(
            text = "Обновленный текст",
            isPinned = true
        )

        val result = WallService.update(updatedPost)

        assertTrue("При обновлении существующего поста должен возвращаться true", result)
    }

    @Test
    fun testUpdateNonExistingPostReturnsFalse() {

        val nonExistingPost = Post(
            id = 999,
            toID = 1,
            fromID = 2,
            date = 1234567890,
            text = "Несуществующий пост",
            postType = "post",
            canEdit = true,
            canDelete = true,
            canPin = true,
            isPinned = false,
            isFavorite = false,
            likes = Likes(0, false, true, true),
            comments = Comment(0, false, false, false, false)
        )

        val result = WallService.update(nonExistingPost)

        assertFalse("При обновлении несуществующего поста должен возвращаться false", result)
    }
}