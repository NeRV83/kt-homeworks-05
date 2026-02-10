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

    @Test
    fun addShouldWorkWithPhotoAttachment() {
        val photo = Photo(
            id = 1,
            albumId = 1,
            ownerId = 100,
            userId = 100,
            date = System.currentTimeMillis(),
            width = 1920,
            height = 1080,
            url = "https://example.com/photo.jpg"
        )
        val photoAttachment = PhotoAttachment(photo)

        val likes = Likes(count = 3)
        val post = Post(
            text = "Пост с фото",
            likes = likes,
            attachments = arrayOf(photoAttachment)
        )

        // Выполнение
        val result = WallService.add(post)

        // Проверка
        assertEquals(1, result.id)
        assertEquals("Пост с фото", result.text)
        assertEquals(1, result.attachments.size)
        assertEquals("photo", result.attachments[0].type)
        assertTrue(result.attachments[0] is PhotoAttachment)
    }

    @Test
    fun `add should work with multiple attachments`() {
        // Подготовка данных с несколькими вложениями
        val photo = Photo(
            id = 1,
            albumId = 1,
            ownerId = 100,
            userId = 100,
            date = System.currentTimeMillis(),
            width = 800,
            height = 600,
            url = "https://example.com/photo1.jpg"
        )
        val video = Video(
            id = 1,
            ownerId = 100,
            title = "Мое видео",
            duration = 120,
            date = System.currentTimeMillis(),
            playerUrl = "https://example.com/video.mp4"
        )

        val photoAttachment = PhotoAttachment(photo)
        val videoAttachment = VideoAttachment(video)

        val likes = Likes(count = 10)
        val post = Post(
            text = "Пост с несколькими вложениями",
            likes = likes,
            attachments = arrayOf(photoAttachment, videoAttachment)
        )

        // Выполнение
        val result = WallService.add(post)

        // Проверка
        assertEquals(1, result.id)
        assertEquals(2, result.attachments.size)
        assertEquals("photo", result.attachments[0].type)
        assertEquals("video", result.attachments[1].type)
    }

    @Test
    fun `add should work with different attachment types`() {
        // Подготовка данных с разными типами вложений
        val audio = Audio(
            id = 1,
            ownerId = 100,
            artist = "Исполнитель",
            title = "Песня",
            duration = 240,
            date = System.currentTimeMillis()
        )
        val document = Document(
            id = 1,
            ownerId = 100,
            title = "Документ.pdf",
            size = 1024,
            ext = "pdf",
            url = "https://example.com/doc.pdf",
            date = System.currentTimeMillis()
        )

        val audioAttachment = AudioAttachment(audio)
        val documentAttachment = DocumentAttachment(document)

        val likes = Likes(count = 5)
        val post = Post(
            text = "Пост с разными вложениями",
            likes = likes,
            attachments = arrayOf(audioAttachment, documentAttachment)
        )

        // Выполнение
        val result = WallService.add(post)

        // Проверка
        assertEquals(1, result.id)
        assertEquals(2, result.attachments.size)
        assertEquals("audio", result.attachments[0].type)
        assertEquals("doc", result.attachments[1].type)
    }
}