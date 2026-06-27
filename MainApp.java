import java.util.*;

/**
 * ============================================================
 * Modul 3: Antarmuka Pengguna & Entry Point Aplikasi
 * ============================================================
 * Berisi logika menu interaktif, utils print, dan data seeder.
 * ============================================================
 */
public class MainApp {
    private final CatalogManager catalog = new CatalogManager();
    private final PlaybackManager playback = new PlaybackManager();
    private final Scanner scanner = new Scanner(System.in);
    private boolean isRunning = true;

    public static void main(String[] args) {
        MainApp app = new MainApp();
        app.seedData();
        app.runUI();
    }

    // ── MAIN LOOP UI ───────────────────────────────────────────────
    public void runUI() {
        clearScreen();
        System.out.println("Memuat Sistem...");
        sleep(500);

        while (isRunning) {
            printMainMenu();
            String choice = prompt("Pilih menu").trim();
            System.out.println();
            
            switch (choice) {
                case "1": menuBrowseCatalog(); break;
                case "2": menuSearch(); break;
                case "3": menuAddToQueue(); break;
                case "4": menuPlayNext(); break;
                case "5": menuUndo(); break;
                case "6": menuViewQueue(); break;
                case "7": menuViewHistory(); break;
                case "0": exitApp(); break;
                default: System.out.println("[WARN] Pilihan tidak valid!"); sleep(800);
            }
        }
    }

    // ── LOGIKA MENU ────────────────────────────────────────────────
    private void printMainMenu() {
        System.out.println("\n============================================================");
        System.out.println("                 [#] MUSIQ PLAYER [#]");
        System.out.println("  Katalog: " + catalog.getTotalSongs() + " lagu | Antrean: " + playback.getQueueSize() + " | Riwayat: " + playback.getHistorySize());
        System.out.println("============================================================");
        
        Song current = playback.getCurrentSong();
        if (current != null) {
            System.out.println(" >> NOW PLAYING: " + current.getTitle() + " - " + current.getArtist());
        } else {
            System.out.println(" [!] Belum ada lagu yang diputar.");
        }
        System.out.println("------------------------------------------------------------");
        System.out.println(" [1] Browse Katalog Hierarki (Tree)");
        System.out.println(" [2] Cari Lagu & Tambah Antrean");
        System.out.println(" [3] Tambah Lagu via ID (Enqueue)");
        System.out.println(" [4] >> Putar Berikutnya (Dequeue)");
        System.out.println(" [5] << Kembali ke Sebelumnya (Undo/Pop)");
        System.out.println(" [6] Lihat Daftar Antrean (Queue)");
        System.out.println(" [7] Lihat Riwayat Putar (Stack)");
        System.out.println(" [0] Keluar Aplikasi");
    }

    private void menuBrowseCatalog() {
        System.out.println("=== BROWSE KATALOG (GENRE) ===");
        List<String> genres = new ArrayList<>(catalog.getGenres());
        for (int i = 0; i < genres.size(); i++) System.out.println(" [" + (i+1) + "] " + genres.get(i));
        System.out.println(" [0] Kembali");
        
        int gIdx = parseInt(prompt("Pilih Genre"), genres.size());
        if (gIdx < 0) return;
        String selectedGenre = genres.get(gIdx);

        System.out.println("\n=== ARTIS DALAM GENRE " + selectedGenre + " ===");
        List<String> artists = new ArrayList<>(catalog.getRoot().children.get(selectedGenre).children.keySet());
        for (int i = 0; i < artists.size(); i++) System.out.println(" [" + (i+1) + "] " + artists.get(i));
        
        int aIdx = parseInt(prompt("Pilih Artis"), artists.size());
        if (aIdx < 0) return;
        String selectedArtist = artists.get(aIdx);

        List<Song> songs = catalog.getSongsByArtist(selectedGenre, selectedArtist);
        printSongTable(songs, "Lagu dari " + selectedArtist);
        promptEnter();
    }

    private void menuSearch() {
        String kw = prompt("Masukkan kata kunci (judul/artis)");
        List<Song> results = catalog.searchByKeyword(kw);
        if (results.isEmpty()) {
            System.out.println("[X] Tidak ditemukan lagu dengan kata kunci tersebut.");
        } else {
            printSongTable(results, "Hasil Pencarian");
            String addToQ = prompt("Tambah semua hasil ke antrean? (y/n)");
            if (addToQ.equalsIgnoreCase("y")) {
                for (Song s : results) playback.enqueue(s);
                System.out.println("[OK] " + results.size() + " lagu ditambahkan ke antrean.");
            }
        }
        promptEnter();
    }

    private void menuAddToQueue() {
        System.out.println("Ketik 'list' untuk melihat semua ID lagu.");
        String input = prompt("Masukkan ID Lagu");
        if (input.equalsIgnoreCase("list")) {
            printSongTable(new ArrayList<>(catalog.getAllSongs()), "Semua Lagu di Katalog");
            input = prompt("Masukkan ID Lagu");
        }
        
        Song song = catalog.findById(input);
        if (song != null) {
            playback.enqueue(song);
            System.out.println("[OK] Ditambahkan ke antrean: " + song.getTitle());
        } else {
            System.out.println("[X] ID Lagu tidak valid.");
        }
        promptEnter();
    }

    private void menuPlayNext() {
        if (playback.isQueueEmpty()) {
            System.out.println("[INFO] Antrean kosong.");
        } else {
            loadingAnim("Memproses pemutaran");
            Song next = playback.playNext();
            System.out.println("\n>> SEDANG DIPUTAR: " + next.getTitle() + " - " + next.getArtist());
            System.out.println("   Durasi: " + next.getFormattedDuration());
        }
        promptEnter();
    }

    private void menuUndo() {
        if (playback.getHistorySize() == 0) {
            System.out.println("[INFO] Riwayat kosong, tidak ada lagu untuk di-undo.");
        } else {
            loadingAnim("Mengembalikan lagu");
            Song prev = playback.undoBack();
            System.out.println("\n<< BERHASIL KEMBALI KE: " + prev.getTitle());
        }
        promptEnter();
    }

    private void menuViewQueue() {
        System.out.println("=== ANTREAN PUTAR (FIFO) ===");
        Song[] q = playback.getQueueSnapshot();
        if (q.length == 0) System.out.println(" [Kosong]");
        for (int i = 0; i < q.length; i++) {
            System.out.println(" " + (i+1) + ". " + q[i].getTitle() + " (" + q[i].getArtist() + ")");
        }
        promptEnter();
    }

    private void menuViewHistory() {
        System.out.println("=== RIWAYAT PUTAR (LIFO) ===");
        Song[] h = playback.getHistorySnapshot();
        if (h.length == 0) System.out.println(" [Kosong]");
        for (int i = 0; i < h.length; i++) {
            System.out.println(" - " + h[i].getTitle() + " (" + h[i].getArtist() + ")");
        }
        promptEnter();
    }

    private void exitApp() {
        System.out.println("Mematikan sistem... Terima kasih telah menggunakan MUSIQ Player!");
        isRunning = false;
    }

    // ── UTILS & SEEDER ─────────────────────────────────────────────
    private String prompt(String text) {
        System.out.print("\n > " + text + ": ");
        return scanner.nextLine();
    }

    private void promptEnter() {
        System.out.print("\n[Tekan Enter untuk kembali ke Menu]");
        scanner.nextLine();
        clearScreen();
    }

    private int parseInt(String input, int max) {
        try { int n = Integer.parseInt(input); if (n >= 1 && n <= max) return n - 1; } 
        catch (NumberFormatException ignored) {}
        return -1;
    }

    private void clearScreen() {
        try { System.out.print("\033[H\033[2J"); System.out.flush(); } 
        catch (Exception e) { for(int i=0; i<30; i++) System.out.println(); }
    }

    private void loadingAnim(String text) {
        System.out.print(" " + text);
        for(int i=0; i<3; i++) { System.out.print("."); sleep(300); }
        System.out.println();
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (Exception ignored) {}
    }

    private void printSongTable(List<Song> songs, String title) {
        System.out.println("\n--- " + title.toUpperCase() + " ---");
        System.out.println("ID   | Judul                          | Artis");
        System.out.println("---------------------------------------------------------");
        for (Song s : songs) {
            System.out.printf("%-4s | %-30s | %s%n", s.getId(), s.getTitle(), s.getArtist());
        }
        System.out.println("Total: " + songs.size() + " lagu.");
    }

    private void seedData() {
        catalog.addSong(new Song("001", "Shape of You", "Ed Sheeran", "Divide", "Pop", 234));
        catalog.addSong(new Song("002", "Bad Guy", "Billie Eilish", "When We All Fall", "Pop", 194));
        catalog.addSong(new Song("003", "Hotel California", "Eagles", "Hotel California", "Rock", 391));
        catalog.addSong(new Song("004", "Take Five", "Dave Brubeck", "Time Out", "Jazz", 325));
        catalog.addSong(new Song("005", "No One", "Alicia Keys", "As I Am", "R&B", 245));
        catalog.addSong(new Song("006", "Strobe", "Deadmau5", "4x4=12", "Electronic", 632));
        catalog.addSong(new Song("007", "Mr. Brightside", "The Killers", "Hot Fuss", "Indie", 222));
    }
}