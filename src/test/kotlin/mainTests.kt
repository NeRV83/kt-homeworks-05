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
        val service = WallService

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
            comments = emptyArray(), // Исправлено: comments - это Array<Comment>
            attachments = emptyArray()
        )

        val result = service.add(post)

        assertNotEquals("ID поста должен быть не равен 0 после добавления", 0, result.id)
        assertEquals(1, result.id) // Проверяем, что ID стал 1
    }

    @Test
    fun testUpdateExistingPostReturnsTrue() {
        val service = WallService

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
            comments = emptyArray(),
            attachments = emptyArray()
        )

        val addedPost = service.add(post)

        val updatedPost = addedPost.copy(
            text = "Обновленный текст",
            isPinned = true
        )

        val result = service.update(updatedPost)

        assertTrue("При обновлении существующего поста должен возвращаться true", result)
        val postAfterUpdate = service.findById(addedPost.id)
        assertEquals("Обновленный текст", postAfterUpdate.text)
        assertTrue(postAfterUpdate.isPinned)
    }

    @Test
    fun testUpdateNonExistingPostReturnsFalse() {
        val service = WallService

        val existingPost = Post(
            id = 0,
            toID = 1,
            fromID = 2,
            date = 1234567890,
            text = "Существующий пост",
            likes = Likes(0, false, true, true),
            comments = emptyArray(),
            attachments = emptyArray()
        )
        service.add(existingPost)

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
            comments = emptyArray(),
            attachments = emptyArray()
        )

        val result = service.update(nonExistingPost)
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

    @Test
    fun createComment_existingPost() {

        val service = WallService
        service.clear()

        val post = Post(
            id = 0,
            fromID = 1,
            text = "Тестовый пост",
            likes = Likes(count = 0)
        )
        val addedPost = service.add(post)

        val comment = Comment(
            fromID = 2,
            text = "Тестовый комментарий",
            date = 1767214800
        )

        val result = service.createComment(addedPost.id, comment)

        assertNotNull(result)
        assertEquals(1, result.id) // первый комментарий должен иметь id = 1
        assertEquals(2, result.fromID)
        assertEquals("Тестовый комментарий", result.text)

        val updatedPost = service.findById(addedPost.id)
        assertEquals(1, updatedPost.comments.size)
        assertEquals(result, updatedPost.comments[0])
    }

    @Test(expected = PostNotFoundException::class)
    fun createComment_nonExistingPost() {

        val service = WallService
        service.clear()

        val comment = Comment(
            fromID = 2,
            text = "Комментарий к несуществующему посту"
        )

        service.createComment(999, comment)
    }

    @Test
    fun createCommentNonExistingPostTryCatch() {

        val service = WallService
        service.clear()

        val comment = Comment(
            fromID = 2,
            text = "Комментарий к несуществующему посту"
        )

        try {
            service.createComment(999, comment)
            fail("Expected PostNotFoundException was not thrown")
        } catch (e: PostNotFoundException) {
            assertTrue(e.message!!.contains("999"))
        }
    }
}