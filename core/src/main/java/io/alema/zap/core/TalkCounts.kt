package io.alema.zap.core

/**
 * Who you talk to most, for VisualContext (07/11/2026). Only counts per person per day, for 30 days:
 * never the text. Groups never enter. Pure Kotlin, so it is tested on the JVM.
 */
class TalkCounts(private val days: MutableMap<String, MutableMap<Long, Int>> = mutableMapOf()) {
    fun add(name: String, timeMs: Long, n: Int = 1) {
        val who = name.trim()
        if (who.isEmpty() || n <= 0) return
        val day = timeMs / DAY_MS
        val m = days.getOrPut(who) { mutableMapOf() }
        m[day] = (m[day] ?: 0) + n
    }

    /** Drops what is older than 30 days before [nowMs]. */
    fun prune(nowMs: Long) {
        val first = nowMs / DAY_MS - WINDOW_DAYS
        days.values.forEach { m -> m.keys.removeAll { it < first } }
        days.entries.removeAll { it.value.isEmpty() }
    }

    /** Messages in the last 30 days, per person, biggest first. */
    fun top(nowMs: Long): List<Pair<String, Int>> {
        val first = nowMs / DAY_MS - WINDOW_DAYS
        return days.map { (who, m) -> who to m.filterKeys { it >= first }.values.sum() }.filter { it.second > 0 }.sortedByDescending { it.second }
    }

    /** "name\tday\tcount" lines: small, readable, no message content possible. */
    fun encode(): String = buildString { days.forEach { (who, m) -> m.forEach { (d, c) -> append(who.replace('\t', ' ').replace('\n', ' ')).append('\t').append(d).append('\t').append(c).append('\n') } } }

    val isEmpty get() = days.isEmpty()

    companion object {
        const val WINDOW_DAYS = 30
        private const val DAY_MS = 86_400_000L
        fun decode(s: String): TalkCounts {
            val t = TalkCounts()
            s.lineSequence().forEach { l ->
                val p = l.split('\t'); if (p.size != 3) return@forEach
                val d = p[1].toLongOrNull() ?: return@forEach; val c = p[2].toIntOrNull() ?: return@forEach
                t.days.getOrPut(p[0]) { mutableMapOf() }[d] = c
            }
            return t
        }

        /** First fill from the chats already kept (the last days): only one-to-one chats. */
        fun fromChats(chats: List<ZapConversation>): TalkCounts {
            val t = TalkCounts()
            chats.filter { !it.isGroup }.forEach { c -> c.messages.forEach { m -> t.add(c.title, m.timeMs) } }
            return t
        }
    }
}
