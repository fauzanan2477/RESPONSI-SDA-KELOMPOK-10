import java.util.*;

/**
 * Kelas untuk menyimpan data informasi lagu.
 */
class Lagu {
    private String judul;
    private int durasiDetik;

    public Lagu(String judul, int durasiDetik) {
        this.judul = judul;
        this.durasiDetik = durasiDetik;
    }

    public String getJudul() { return judul; }
    
    @Override
    public String toString() {
        int menit = durasiDetik / 60;
        int detik = durasiDetik % 60;
        return String.format("%s (%02d:%02d)", judul, menit, detik);
    }
}

/**
 * Node untuk struktur General Tree (Katalog Musik Hierarkis).
 * Hierarki: Root -> Genre -> Artis -> Album -> Lagu
 */
class KategoriNode {
    private String namaKategori;
    private String tingkatan; // Contoh: "ROOT", "GENRE", "ARTIS", "ALBUM"
    private Map<String, KategoriNode> subKategori; 
    private List<Lagu> daftarLagu; // List ini hanya diisi jika tingkatan node adalah "ALBUM"

    public KategoriNode(String namaKategori, String tingkatan) {
        this.namaKategori = namaKategori;
        this.tingkatan = tingkatan;
        this.subKategori = new LinkedHashMap<>(); // Menjaga urutan sub-kategori saat dimasukkan
        this.daftarLagu = new ArrayList<>();
    }

    public String getNamaKategori() { return namaKategori; }
    public String getTingkatan() { return tingkatan; }
    public Map<String, KategoriNode> getSubKategori() { return subKategori; }
    public List<Lagu> getDaftarLagu() { return daftarLagu; }

    public void tambahSubKategori(KategoriNode sub) {
        this.subKategori.put(sub.getNamaKategori(), sub);
    }

    public void tambahLagu(Lagu laguBaru) {
        this.daftarLagu.add(laguBaru);
    }
}

public class ManajemenKatalog {
    private KategoriNode rootKatalog;

    public ManajemenKatalog() {
        rootKatalog = new KategoriNode("Perpustakaan Musik", "ROOT");
        isiDataAwal();
    }

    /**
     * Mengisi data dummy ke dalam katalog musik.
     * Analisis Kompleksitas Waktu: O(1) karena datanya konstan dan langsung dimasukkan.
     */
    private void isiDataAwal() {
        KategoriNode pop = new KategoriNode("Pop", "GENRE");
        KategoriNode tulus = new KategoriNode("Tulus", "ARTIS");
        KategoriNode albumManusia = new KategoriNode("Manusia", "ALBUM");
        
        albumManusia.tambahLagu(new Lagu("Hati-Hati di Jalan", 242));
        albumManusia.tambahLagu(new Lagu("Diri", 220));
        tulus.tambahSubKategori(albumManusia);
        pop.tambahSubKategori(tulus);

        KategoriNode rock = new KategoriNode("Rock", "GENRE");
        KategoriNode dewa19 = new KategoriNode("Dewa 19", "ARTIS");
        KategoriNode albumBintangLima = new KategoriNode("Bintang Lima", "ALBUM");
        
        albumBintangLima.tambahLagu(new Lagu("Roman Picisan", 247));
        albumBintangLima.tambahLagu(new Lagu("Dua Sejoli", 274));
        dewa19.tambahSubKategori(albumBintangLima);
        rock.tambahSubKategori(dewa19);

        rootKatalog.tambahSubKategori(pop);
        rootKatalog.tambahSubKategori(rock);
    }

    /**
     * Menampilkan isi Tree menggunakan Pre-order Traversal.
     * Analisis Kompleksitas Waktu: O(N), di mana N adalah total node (kategori & lagu). 
     * Karena program harus mengunjungi semua node satu per satu.
     */
    public void tampilkanKatalog(KategoriNode node, String spasi) {
        if (!node.getTingkatan().equals("ROOT")) {
            System.out.println(spasi + "+-- [" + node.getTingkatan() + "] " + node.getNamaKategori());
        }
        
        if (node.getTingkatan().equals("ALBUM")) {
            for (Lagu lagu : node.getDaftarLagu()) {
                System.out.println(spasi + "    +-- [#] " + lagu);
            }
        }

        for (KategoriNode sub : node.getSubKategori().values()) {
            tampilkanKatalog(sub, spasi + "    ");
        }
    }

    /**
     * Mencari lagu berdasarkan judul menggunakan Depth First Search (DFS).
     * Analisis Kompleksitas Waktu: O(N) (Skenario Terburuk). Karena jika lagu ada di akhir,
     * program harus mengecek seluruh isi Tree terlebih dahulu.
     */
    public Lagu cariLaguBerdasarkanJudul(KategoriNode node, String targetJudul) {
        if (node.getTingkatan().equals("ALBUM")) {
            for (Lagu lagu : node.getDaftarLagu()) {
                if (lagu.getJudul().equalsIgnoreCase(targetJudul)) {
                    return lagu; // Lagu ketemu
                }
            }
        }
        for (KategoriNode sub : node.getSubKategori().values()) {
            Lagu laguDitemukan = cariLaguBerdasarkanJudul(sub, targetJudul);
            if (laguDitemukan != null) return laguDitemukan;
        }
        return null; // Lagu tidak ketemu
    }

    public KategoriNode getRootKatalog() { return rootKatalog; }
}