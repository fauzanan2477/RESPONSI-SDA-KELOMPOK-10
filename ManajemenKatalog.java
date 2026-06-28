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
    private String tingkatan; 
    private Map<String, KategoriNode> subKategori; 
    private List<Lagu> daftarLagu; 

    public KategoriNode(String namaKategori, String tingkatan) {
        this.namaKategori = namaKategori;
        this.tingkatan = tingkatan;
        this.subKategori = new LinkedHashMap<>();
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
    
    // Menggunakan HashMap sebagai indeks bayangan agar pencarian lagu berkecepatan O(1)
    private HashMap<String, Lagu> indeksLagu; 

    public ManajemenKatalog() {
        rootKatalog = new KategoriNode("Perpustakaan Musik", "ROOT");
        indeksLagu = new HashMap<>();
        isiDataAwal();
    }

    /**
     * Helper untuk merapikan teks (Contoh: "rock" -> "Rock").
     * Analisis Kompleksitas Waktu: O(K) dimana K adalah panjang string.
     */
    private String formatTeks(String teks) {
        if (teks == null || teks.trim().isEmpty()) return "";
        String[] kata = teks.trim().split("\\s+");
        StringBuilder hasil = new StringBuilder();
        for (String k : kata) {
            if (k.length() > 0) {
                hasil.append(Character.toUpperCase(k.charAt(0)))
                     .append(k.substring(1).toLowerCase()).append(" ");
            }
        }
        return hasil.toString().trim();
    }

    /**
     * Memasukkan lagu baru ke dalam Tree secara dinamis DAN menyimpannya ke HashMap.
     * Analisis Kompleksitas Waktu: O(1) rata-rata. 
     * Penjelasan: Mengakses Map (get/put) pada setiap tingkatan kategori (Genre, Artis, Album) 
     * berjalan secara instan O(1) berkat fungsi hashing internal Java.
     */
    public void tambahLaguKeKatalog(String namaGenre, String namaArtis, String namaAlbum, Lagu laguBaru) {
        namaGenre = formatTeks(namaGenre);
        namaArtis = formatTeks(namaArtis);
        namaAlbum = formatTeks(namaAlbum);

        KategoriNode genreNode = rootKatalog.getSubKategori().get(namaGenre);
        if (genreNode == null) {
            genreNode = new KategoriNode(namaGenre, "GENRE");
            rootKatalog.tambahSubKategori(genreNode);
        }

        KategoriNode artisNode = genreNode.getSubKategori().get(namaArtis);
        if (artisNode == null) {
            artisNode = new KategoriNode(namaArtis, "ARTIS");
            genreNode.tambahSubKategori(artisNode);
        }

        KategoriNode albumNode = artisNode.getSubKategori().get(namaAlbum);
        if (albumNode == null) {
            albumNode = new KategoriNode(namaAlbum, "ALBUM");
            artisNode.tambahSubKategori(albumNode);
        }

        albumNode.tambahLagu(laguBaru);
        indeksLagu.put(laguBaru.getJudul().toLowerCase(), laguBaru); 
    }

    private void isiDataAwal() {
        tambahLaguKeKatalog("Pop", "Tulus", "Manusia", new Lagu("Hati-Hati di Jalan", 242));
        tambahLaguKeKatalog("Pop", "Tulus", "Manusia", new Lagu("Diri", 220));
        tambahLaguKeKatalog("Pop", "Billie Eilish", "Happier Than Ever", new Lagu("Happier Than Ever", 298));
        tambahLaguKeKatalog("Pop", "Billie Eilish", "Happier Than Ever", new Lagu("Halley's Comet", 234));
        
        tambahLaguKeKatalog("Rock", "Dewa 19", "Bintang Lima", new Lagu("Roman Picisan", 247));
        tambahLaguKeKatalog("Rock", "Dewa 19", "Bintang Lima", new Lagu("Dua Sejoli", 274));
        tambahLaguKeKatalog("Rock", "Queen", "A Night at the Opera", new Lagu("Bohemian Rhapsody", 354));
        
        tambahLaguKeKatalog("Indie", "Arctic Monkeys", "AM", new Lagu("Do I Wanna Know?", 272));
        tambahLaguKeKatalog("Indie", "Arctic Monkeys", "AM", new Lagu("R U Mine?", 200));
    }

    /**
     * Menampilkan isi Tree menggunakan metode Pre-order Traversal.
     * Analisis Kompleksitas Waktu: O(N) di mana N adalah total seluruh node dan daun (lagu).
     * Penjelasan: Program harus menelusuri setiap cabang (DFS) satu per satu tanpa ada yang terlewat.
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
     * Mengambil daftar semua lagu.
     * Analisis Kompleksitas Waktu: O(N).
     */
    public void dapatkanSemuaLagu(List<Lagu> wadahLagu) {
        wadahLagu.addAll(indeksLagu.values());
    }

    /**
     * Mencari lagu menggunakan HashMap Indexing.
     * Analisis Kompleksitas Waktu: O(1) Konstan.
     * Penjelasan: Alih-alih melakukan pencarian O(N) menyusuri Tree, kami menggunakan HashMap 
     * untuk mendapatkan objek lagu secara instan berdasarkan key (judul).
     */
    public Lagu cariLaguBerdasarkanJudul(String targetJudul) {
        return indeksLagu.get(targetJudul.toLowerCase());
    }

    public KategoriNode getRootKatalog() { return rootKatalog; }
}