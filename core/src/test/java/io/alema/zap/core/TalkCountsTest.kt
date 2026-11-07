package io.alema.zap.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TalkCountsTest {
    private val day = 86_400_000L
    private val now = 100 * day

    @Test fun countsPerPersonInTheWindow() {
        val t = TalkCounts()
        t.add("Bruno", now); t.add("Bruno", now - day); t.add("Ana", now - 2 * day)
        t.add("Bruno", now - 40 * day) // too old
        assertEquals(listOf("Bruno" to 2, "Ana" to 1), t.top(now))
    }

    @Test fun groupsNeverCount() {
        val chats = listOf(
            ZapConversation("1", "Bruno", null, isGroup = false, messages = listOf(ZapMessage("Bruno", "oi", now), ZapMessage(ZapMessage.ME, "e aí", now))),
            ZapConversation("2", "Família", null, isGroup = true, messages = listOf(ZapMessage("Tia", "bom dia", now))),
        )
        assertEquals(listOf("Bruno" to 2), TalkCounts.fromChats(chats).top(now))
    }

    @Test fun encodeKeepsNoText() {
        val t = TalkCounts(); t.add("Bruno", now)
        val e = t.encode()
        assertTrue(e.startsWith("Bruno\t"))
        assertEquals(listOf("Bruno" to 1), TalkCounts.decode(e).top(now))
    }

    @Test fun pruneDropsOld() {
        val t = TalkCounts(); t.add("Bruno", now - 31 * day); t.prune(now)
        assertTrue(t.isEmpty)
    }
}
