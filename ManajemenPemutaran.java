import java.util.*;

public class ManajemenPemutaran {
    private Deque<Lagu> antreanLagu;    // Implementasi FIFO (Queue)
    private Stack<Lagu> riwayatLagu;    // Implementasi LIFO (Stack)
    private Lagu laguYangSedangDiputar;

    public ManajemenPemutaran() {
        antreanLagu = new ArrayDeque<>();
        riwayatLagu = new Stack<>();
        laguYangSedangDiputar = null;
    }

    /**
     * (Fitur Queue) Menambahkan lagu ke antrean belakang.
     * Analisis Kompleksitas Waktu: O(1) konstan, karena operasi addLast() 
     * pada antrean memproses penambahan secara instan tanpa perlu menggeser data lain.
     */
    public void tambahKeAntrean(Lagu lagu) {
        antreanLagu.addLast(lagu);
    }

    /**
     * (Fitur Queue) Memutar lagu dari antrean paling depan (Dequeue).
     * Analisis Kompleksitas Waktu: O(1) karena operasi mengambil dari antrean (pollFirst)
     * dan memasukkan ke riwayat (push) sangat cepat dan langsung ke posisi target.
     */
    public Lagu putarSelanjutnya() {
        if (antreanLagu.isEmpty()) {
            return null;
        }

        if (laguYangSedangDiputar != null) {
            riwayatLagu.push(laguYangSedangDiputar); // Masukkan lagu lama ke stack riwayat (LIFO)
        }

        laguYangSedangDiputar = antreanLagu.pollFirst(); // Ambil lagu baru dari depan antrean (FIFO)
        return laguYangSedangDiputar;
    }

    /**
     * (Fitur Stack) Memulihkan dan memutar lagu sebelumnya (Undo/Back).
     * Analisis Kompleksitas Waktu: O(1) konstan. Mengambil data teratas dari Stack (pop)
     * dieksekusi seketika dalam satu langkah.
     */
    public Lagu kembaliKeSebelumnya() {
        if (riwayatLagu.isEmpty()) {
            return null;
        }

        if (laguYangSedangDiputar != null) {
            antreanLagu.addFirst(laguYangSedangDiputar); // Amankan lagu saat ini ke antrean terdepan
        }

        laguYangSedangDiputar = riwayatLagu.pop(); // Ambil lagu terakhir dari tumpukan riwayat (LIFO)
        return laguYangSedangDiputar;
    }

    public Deque<Lagu> getAntreanLagu() { return antreanLagu; }
    public Stack<Lagu> getRiwayatLagu() { return riwayatLagu; }
    public Lagu getLaguYangSedangDiputar() { return laguYangSedangDiputar; }
}