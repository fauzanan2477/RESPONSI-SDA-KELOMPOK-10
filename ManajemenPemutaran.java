import java.util.*;

public class ManajemenPemutaran {
    private Deque<Lagu> antreanLagu;    // Menggunakan Deque untuk antrean FIFO (Queue)
    private Deque<Lagu> riwayatLagu;    // Menggunakan Deque dengan membatasi penambahan di atas untuk riwayat LIFO (Stack)
    private Lagu laguYangSedangDiputar;

    public ManajemenPemutaran() {
        antreanLagu = new LinkedList<>();
        riwayatLagu = new ArrayDeque<>();
        laguYangSedangDiputar = null;
    }

    /**
     * Fitur Queue (Antrean) - Memasukkan data ke akhir antrean (Enqueue).
     * Analisis Kompleksitas Waktu: O(1) Konstan. 
     * Penjelasan: Karena struktur Deque (Double Ended Queue) memiliki pointer yang mengarah 
     * ke elemen paling belakang, penambahan data (addLast) tidak memerlukan pergeseran array.
     */
    public void tambahKeAntrean(Lagu lagu) {
        antreanLagu.addLast(lagu);
    }

    /**
     * Fitur Queue (Antrean) - Mengambil data terdepan dan memindah lagu lama ke riwayat.
     * Analisis Kompleksitas Waktu: O(1) Konstan.
     * Penjelasan: Operasi push() ke Stack dan pollFirst() dari Queue sama-sama langsung 
     * menargetkan ujung struktur data secara presisi tanpa perlu perulangan (looping).
     */
    public Lagu putarSelanjutnya() {
        if (laguYangSedangDiputar == null && antreanLagu.isEmpty()) {
            return null;
        }

        // Simpan (Push LIFO)
        if (laguYangSedangDiputar != null) {
            riwayatLagu.push(laguYangSedangDiputar); 
        }

        // Ambil dari depan (Dequeue FIFO)
        if (!antreanLagu.isEmpty()) {
            laguYangSedangDiputar = antreanLagu.pollFirst(); 
        } else {
            laguYangSedangDiputar = null; 
        }

        return laguYangSedangDiputar;
    }

    /**
     * Fitur Stack (Riwayat) - Memulihkan data terakhir yang masuk (Undo / Pop).
     * Analisis Kompleksitas Waktu: O(1) Konstan. 
     * Penjelasan: Fitur Undo sangat cocok menggunakan prinsip Last In First Out (LIFO).
     * Operasi mengambil elemen teratas dari Stack (pop) dieksekusi secara instan.
     */
    public Lagu kembaliKeSebelumnya() {
        if (riwayatLagu.isEmpty()) {
            return null;
        }

        if (laguYangSedangDiputar != null) {
            antreanLagu.addFirst(laguYangSedangDiputar); 
        }

        laguYangSedangDiputar = riwayatLagu.pop(); 
        return laguYangSedangDiputar;
    }

    public Deque<Lagu> getAntreanLagu() { return antreanLagu; }
    public Deque<Lagu> getRiwayatLagu() { return riwayatLagu; }
    public Lagu getLaguYangSedangDiputar() { return laguYangSedangDiputar; }
}