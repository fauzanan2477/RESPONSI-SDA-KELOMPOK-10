import java.util.*;

/**
 * ============================================================
 * Modul 2: Manajemen Alur Putar Linear
 * ============================================================
 * Menggabungkan fitur Antrean (Queue) dan Riwayat (Stack)
 * dalam satu pengelola yang efisien.
 * ============================================================
 */
public class PlaybackManager {
    
    // Antrean Putar (FIFO) menggunakan LinkedList untuk operasi O(1) di kedua ujung
    private final LinkedList<Song> queue = new LinkedList<>();
    
    // Riwayat Putar (LIFO) menggunakan ArrayDeque untuk push/pop O(1) yang cepat
    private final Deque<Song> history = new ArrayDeque<>();
    
    private Song currentSong = null;
    private static final int MAX_HISTORY = 50; // Batas riwayat agar memori tidak bocor

    /** Menambah lagu ke antrean (Enqueue). Kompleksitas: O(1) */
    public void enqueue(Song song) {
        queue.addLast(song);
    }

    /** Memutar lagu berikutnya (Dequeue + Push History). Kompleksitas: O(1) */
    public Song playNext() {
        if (queue.isEmpty()) return null;
        
        // Simpan lagu saat ini ke history sebelum diganti
        if (currentSong != null) {
            if (history.size() >= MAX_HISTORY) {
                ((ArrayDeque<Song>) history).removeLast(); // Buang yang paling lama
            }
            history.push(currentSong); // Push LIFO
        }
        
        currentSong = queue.removeFirst(); // Dequeue FIFO
        return currentSong;
    }

    /** Memulihkan lagu sebelumnya (Pop History + Enqueue). Kompleksitas: O(1) */
    public Song undoBack() {
        if (history.isEmpty()) return null;
        
        // Kembalikan lagu yang sedang jalan ke antrean depan agar tidak hilang
        if (currentSong != null) {
            queue.addFirst(currentSong);
        }
        
        currentSong = history.pop(); // Ambil dari history LIFO
        return currentSong;
    }

    public Song getCurrentSong() { return currentSong; }
    public Song peekNext() { return queue.peekFirst(); }
    
    public void clearQueue() { queue.clear(); }
    public boolean isQueueEmpty() { return queue.isEmpty(); }
    public int getQueueSize() { return queue.size(); }
    public int getHistorySize() { return history.size(); }

    public Song[] getQueueSnapshot() { return queue.toArray(new Song[0]); }
    public Song[] getHistorySnapshot() { return history.toArray(new Song[0]); }
}