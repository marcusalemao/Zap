package io.alema.zap

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import io.alema.zap.core.TalkCounts
import io.alema.zap.core.ZapConversation
import java.io.File

/**
 * The bridge to VisualContext (07/11/2026): who you talk to most on WhatsApp, as counts only.
 * Off by default; when off nothing is counted and the provider answers empty. See
 * claude/zap/SPEC-PONTE-VISUALCONTEXT.md.
 */
internal object ZapTalk {
    private const val FILE = "talk_counts.tsv"
    private val lock = Any()

    fun enabled(ctx: Context) = ctx.getSharedPreferences("zap_settings", Context.MODE_PRIVATE).getBoolean(KEY, false)

    fun setEnabled(ctx: Context, on: Boolean) {
        ctx.getSharedPreferences("zap_settings", Context.MODE_PRIVATE).edit().putBoolean(KEY, on).apply()
        synchronized(lock) {
            val f = File(ctx.filesDir, FILE)
            if (!on) { f.delete(); return }
            // First fill from the chats Zap already keeps (the last days), so it is useful at once.
            if (!f.exists()) f.writeText(TalkCounts.fromChats(ZapInbox.list(ctx)).encode())
        }
    }

    /** A new one-to-one message, either way (received or sent). */
    fun count(ctx: Context, chat: ZapConversation?, n: Int, timeMs: Long = System.currentTimeMillis()) {
        if (chat == null || chat.isGroup || n <= 0 || !enabled(ctx)) return
        synchronized(lock) {
            val f = File(ctx.filesDir, FILE)
            val t = if (f.exists()) TalkCounts.decode(f.readText()) else TalkCounts()
            t.add(chat.title, timeMs, n); t.prune(System.currentTimeMillis())
            f.writeText(t.encode())
        }
    }

    fun top(ctx: Context): List<Pair<String, Int>> = synchronized(lock) {
        if (!enabled(ctx)) return emptyList()
        val f = File(ctx.filesDir, FILE)
        if (!f.exists()) emptyList() else TalkCounts.decode(f.readText()).top(System.currentTimeMillis())
    }

    private const val KEY = "share_talk_counts"
}

/** content://io.alema.zap.frequent/people → name, messages_30d. Read-only; empty while the switch is off. */
class ZapFrequentProvider : ContentProvider() {
    override fun onCreate() = true
    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        val c = MatrixCursor(arrayOf("name", "messages_30d"))
        val ctx = context ?: return c
        if (uri.lastPathSegment != "people") return c
        ZapTalk.top(ctx).take(500).forEach { (n, k) -> c.addRow(arrayOf<Any>(n, k)) }
        return c
    }
    override fun getType(uri: Uri): String = "vnd.android.cursor.dir/vnd.io.alema.zap.people"
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?) = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?) = 0
}
