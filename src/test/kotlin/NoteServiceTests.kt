import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.IllegalArgumentException

class NoteServiceTest {

    @Before
    fun clearBeforeTest() {
        NoteService.clear()
    }

    @Test
    fun testAdd() {
        val noteId = NoteService.add("Test Title", "Test Content")

        assertEquals(1, noteId)

        val note = NoteService.getById(noteId, 0)
        assertEquals("Test Title", note.title)
        assertEquals("Test Content", note.text)
        assertEquals(0, note.ownerId)
        assertEquals(0, note.comments)
    }

    @Test
    fun testAddMultipleNotes() {
        val noteId1 = NoteService.add("Title 1", "Content 1")
        val noteId2 = NoteService.add("Title 2", "Content 2")

        assertEquals(1, noteId1)
        assertEquals(2, noteId2)

        val notes = NoteService.get(0)
        assertEquals(2, notes.size)
        assertEquals("Title 1", notes[0].title)
        assertEquals("Title 2", notes[1].title)
    }

    @Test
    fun testCreateComment() {
        val noteId = NoteService.add("Test Note", "Test Content")
        val commentId = NoteService.createComment(noteId, 1, "Test Comment")

        assertEquals(1, commentId)

        val comments = NoteService.getComments(noteId, 0)
        assertEquals(1, comments.size)
        assertEquals("Test Comment", comments[0].message)
        assertEquals(1, comments[0].userId)
        assertEquals(noteId, comments[0].noteId)
    }

    @Test
    fun testCreateMultipleComments() {
        val noteId = NoteService.add("Test Note", "Test Content")

        val commentId1 = NoteService.createComment(noteId, 1, "Comment 1")
        val commentId2 = NoteService.createComment(noteId, 1, "Comment 2")

        assertEquals(1, commentId1)
        assertEquals(2, commentId2)

        val comments = NoteService.getComments(noteId, 0)
        assertEquals(2, comments.size)
        assertEquals("Comment 1", comments[0].message)
        assertEquals("Comment 2", comments[1].message)
    }

    @Test(expected = NoteService.NotFoundException::class)
    fun testCreateCommentOnNonExistentNote() {
        NoteService.createComment(999, 1, "Test Comment")
    }

    @Test
    fun testDeleteNote() {
        val noteId = NoteService.add("Test Note", "Test Content")

        val result = NoteService.delete(noteId)
        assertTrue(result)
    }

    @Test(expected = NoteService.NotFoundException::class)
    fun testGetDeletedNote() {
        val noteId = NoteService.add("Test Note", "Test Content")
        NoteService.delete(noteId)
        NoteService.getById(noteId, 0)
    }

    @Test
    fun testDeleteNoteWithComments() {
        val noteId = NoteService.add("Test Note", "Test Content")
        val commentId1 = NoteService.createComment(noteId, 1, "Comment 1")
        val commentId2 = NoteService.createComment(noteId, 1, "Comment 2")

        NoteService.delete(noteId)
    }

    @Test(expected = NoteService.NotFoundException::class)
    fun testDeleteNonExistentNote() {
        NoteService.delete(999)
    }

    @Test(expected = CommentNotFoundException::class)
    fun testDeleteNonExistentComment() {
        NoteService.deleteComment(999, 1)
    }

    @Test
    fun testEditNote() {
        val noteId = NoteService.add("Original Title", "Original Content")

        val result = NoteService.edit(noteId, "Updated Title", "Updated Content")
        assertTrue(result)

        val note = NoteService.getById(noteId, 0)
        assertEquals("Updated Title", note.title)
        assertEquals("Updated Content", note.text)
    }

    @Test(expected = NoteService.NotFoundException::class)
    fun testEditNonExistentNote() {
        NoteService.edit(999, "New Title", "New Content")
    }

    @Test
    fun testEditComment() {
        val noteId = NoteService.add("Test Note", "Test Content")
        val commentId = NoteService.createComment(noteId, 1, "Original Comment")

        val result = NoteService.editComment(commentId, 1, "Updated Comment")
        assertTrue(result)

        val comments = NoteService.getComments(noteId, 0)
        assertEquals(1, comments.size)
        assertEquals("Updated Comment", comments[0].message)
    }


    @Test(expected = CommentNotFoundException::class)
    fun testEditNonExistentComment() {
        NoteService.editComment(999, 1, "Updated Comment")
    }

    @Test
    fun testGetById() {
        val noteId = NoteService.add("Test Note", "Test Content")

        val note = NoteService.getById(noteId, 0)
        assertEquals(noteId, note.id)
        assertEquals("Test Note", note.title)
        assertEquals("Test Content", note.text)
    }

    @Test(expected = NoteService.NotFoundException::class)
    fun testGetByIdNonExistentNote() {
        NoteService.getById(999, 0)
    }

    @Test
    fun testGetComments() {
        val noteId = NoteService.add("Test Note", "Test Content")

        NoteService.createComment(noteId, 1, "Comment 1")
        NoteService.createComment(noteId, 1, "Comment 2")
        NoteService.createComment(noteId, 2, "Comment 3")

        val comments = NoteService.getComments(noteId, 0)
        assertEquals(3, comments.size)
    }

    @Test
    fun testGetCommentsSortedAscending() {
        val noteId = NoteService.add("Test Note", "Test Content")

        NoteService.createComment(noteId, 1, "Comment 1")
        NoteService.createComment(noteId, 1, "Comment 2")
        NoteService.createComment(noteId, 1, "Comment 3")

        val comments = NoteService.getComments(noteId, 0, 0)
        assertEquals(3, comments.size)
        assertEquals("Comment 1", comments[0].message)
        assertEquals("Comment 2", comments[1].message)
        assertEquals("Comment 3", comments[2].message)
    }

    @Test
    fun testGet() {
        NoteService.add("Note 1", "Content 1")
        NoteService.add("Note 2", "Content 2")

        val userNotes = NoteService.get(0)
        assertEquals(2, userNotes.size)
        assertEquals("Note 1", userNotes[0].title)
        assertEquals("Note 2", userNotes[1].title)
    }

    @Test
    fun testGetSortedAscending() {
        NoteService.add("Note 2", "Content 2")
        NoteService.add("Note 1", "Content 1")
        NoteService.add("Note 3", "Content 3")

        val notes = NoteService.get(0, 0)
        assertEquals(3, notes.size)
        assertEquals("Note 2", notes[0].title)
        assertEquals("Note 1", notes[1].title)
        assertEquals("Note 3", notes[2].title)
    }

    @Test(expected = CommentNotFoundException::class)
    fun testRestoreNonExistentComment() {
        NoteService.restoreComment(999, 1)
    }

    @Test
    fun testCommentCounterIncrement() {
        val noteId = NoteService.add("Test Note", "Test Content")

        var note = NoteService.getById(noteId, 0)
        assertEquals(0, note.comments)

        NoteService.createComment(noteId, 1, "Comment 1")
        note = NoteService.getById(noteId, 0)
        assertEquals(1, note.comments)

        NoteService.createComment(noteId, 1, "Comment 2")
        note = NoteService.getById(noteId, 0)
        assertEquals(2, note.comments)
    }

    @Test(expected = NoteService.NotFoundException::class)
    fun testGetCommentsForNonExistentNote() {
        NoteService.getComments(999, 0)
    }

    @Test
    fun testDeleteComment() {
        val noteId = NoteService.add("Test Note", "Test Content")
        val commentId = NoteService.createComment(noteId, 1, "Test Comment")

        val result = NoteService.deleteComment(commentId, 1)
        assertTrue(result)

        val comments = NoteService.getComments(noteId, 0)
        assertEquals(0, comments.size)
    }

    @Test(expected = CommentDeletedException::class)
    fun testDeleteCommentTwice() {
        val noteId = NoteService.add("Test Note", "Test Content")
        val commentId = NoteService.createComment(noteId, 1, "Test Comment")

        NoteService.deleteComment(commentId, 1)
        NoteService.deleteComment(commentId, 1)
    }

    @Test(expected = CommentDeletedException::class)
    fun testEditDeletedComment() {
        val noteId = NoteService.add("Test Note", "Test Content")
        val commentId = NoteService.createComment(noteId, 1, "Original Comment")

        NoteService.deleteComment(commentId, 1)
        NoteService.editComment(commentId, 1, "Updated Comment")
    }

    @Test
    fun testRestoreComment() {
        val noteId = NoteService.add("Test Note", "Test Content")
        val commentId = NoteService.createComment(noteId, 1, "Test Comment")

        NoteService.deleteComment(commentId, 1)

        val result = NoteService.restoreComment(commentId, 1)
        assertTrue(result)

        val comments = NoteService.getComments(noteId, 0)
        assertEquals(1, comments.size)
        assertEquals(commentId, comments[0].id)
        assertFalse(comments[0].isDeleted)
    }

    @Test(expected = CommentDeletedException::class)
    fun testRestoreNonDeletedComment() {
        val noteId = NoteService.add("Test Note", "Test Content")
        val commentId = NoteService.createComment(noteId, 1, "Test Comment")

        NoteService.restoreComment(commentId, 1)
    }

    @Test(expected = NoteDeletedException::class)
    fun testRestoreCommentWhenNoteDeleted() {
        val noteId = NoteService.add("Test Note", "Test Content")
        val commentId = NoteService.createComment(noteId, 1, "Test Comment")

        NoteService.deleteComment(commentId, 1)
        NoteService.delete(noteId)

        NoteService.restoreComment(commentId, 1)
    }

    @Test
    fun testGetCommentsExcludeDeleted() {
        val noteId = NoteService.add("Test Note", "Test Content")

        val commentId1 = NoteService.createComment(noteId, 1, "Comment 1")
        val commentId2 = NoteService.createComment(noteId, 1, "Comment 2")
        val commentId3 = NoteService.createComment(noteId, 1, "Comment 3")

        NoteService.deleteComment(commentId2, 1)

        val comments = NoteService.getComments(noteId, 0)
        assertEquals(2, comments.size)
        assertEquals(commentId1, comments[0].id)
        assertEquals(commentId3, comments[1].id)
    }

    @Test
    fun testCommentCounterWithDelete() {
        val noteId = NoteService.add("Test Note", "Test Content")

        val commentId = NoteService.createComment(noteId, 1, "Comment 1")
        var note = NoteService.getById(noteId, 0)
        assertEquals(1, note.comments)

        NoteService.deleteComment(commentId, 1)

        note = NoteService.getById(noteId, 0)
        assertEquals(1, note.comments)
    }

    @Test
    fun testGetCommentsSortedDescending() {
        val noteId = NoteService.add("Test Note", "Test Content")

        val commentId1 = NoteService.createComment(noteId, 1, "Comment 1")
        Thread.sleep(100)
        val commentId2 = NoteService.createComment(noteId, 1, "Comment 2")
        Thread.sleep(100)
        val commentId3 = NoteService.createComment(noteId, 1, "Comment 3")

        val comments = NoteService.getComments(noteId, 0, 1)

        assertEquals(3, comments.size)

        assertEquals(commentId3, comments[0].id)
        assertEquals("Comment 3", comments[0].message)
        assertEquals(commentId2, comments[1].id)
        assertEquals("Comment 2", comments[1].message)
        assertEquals(commentId1, comments[2].id)
        assertEquals("Comment 1", comments[2].message)
    }

    @Test
    fun testGetSortedDescending() {

        val noteId1 = NoteService.add("Note 2", "Content 2")
        Thread.sleep(100)
        val noteId2 = NoteService.add("Note 1", "Content 1")
        Thread.sleep(100)
        val noteId3 = NoteService.add("Note 3", "Content 3")

        val notes = NoteService.get(0, 1)

        assertEquals(3, notes.size)

//        assertEquals(noteId3, notes[0].id)
        assertEquals("Note 3", notes[0].title)
//        assertEquals(noteId2, notes[1].id)
        assertEquals("Note 1", notes[1].title)
//        assertEquals(noteId1, notes[2].id)
        assertEquals("Note 2", notes[2].title)
    }
}
