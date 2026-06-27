import java.util.*;

/**
 * Kelas model untuk merepresentasikan lagu.
 */
class Song {
    private final String id, title, artist, album, genre;
    private final int durationSeconds;

    public Song(String id, String title, String artist, String album, String genre, int durationSeconds) {
        this.id = id; this.title = title; this.artist = artist;
        this.album = album; this.genre = genre; this.durationSeconds = durationSeconds;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public String getGenre() { return genre; }
    public int getDurationSeconds() { return durationSeconds; }
    
    public String getFormattedDuration() {
        return String.format("%02d:%02d", durationSeconds / 60, durationSeconds % 60);
    }
}

/**
 * ============================================================
 * Modul 1: Manajemen Katalog Hierarkis
 * ============================================================
 * Struktur Data : N-Ary Tree
 * Hierarki      : Genre -> Artis -> Album -> Lagu
 * ============================================================
 */
public class CatalogManager {

    // Node untuk General Tree
    public static class TreeNode {
        String name;
        LinkedHashMap<String, TreeNode> children = new LinkedHashMap<>();
        List<Song> songs = new ArrayList<>(); // Hanya terisi di level album
        TreeNode(String name) { this.name = name; }
    }

    private final TreeNode root = new TreeNode("ROOT");
    private final HashMap<String, Song> songIndex = new HashMap<>();

    /**
     * Menambahkan lagu dan membangun hierarki secara otomatis.
     * Analisis Kompleksitas: O(1) rata-rata karena menggunakan HashMap lookup
     */
    public void addSong(Song song) {
        root.children.putIfAbsent(song.getGenre(), new TreeNode(song.getGenre()));
        TreeNode genreNode = root.children.get(song.getGenre());
        
        genreNode.children.putIfAbsent(song.getArtist(), new TreeNode(song.getArtist()));
        TreeNode artistNode = genreNode.children.get(song.getArtist());
        
        artistNode.children.putIfAbsent(song.getAlbum(), new TreeNode(song.getAlbum()));
        TreeNode albumNode = artistNode.children.get(song.getAlbum());
        
        albumNode.songs.add(song);
        songIndex.put(song.getId(), song);
    }

    /** Pencarian lagu berdasarkan ID. Kompleksitas: O(1) */
    public Song findById(String id) { 
        return songIndex.get(id); 
    }

    /** Pencarian keyword (Judul/Artis/Album). Kompleksitas: O(n) */
    public List<Song> searchByKeyword(String keyword) {
        List<Song> results = new ArrayList<>();
        String kw = keyword.toLowerCase();
        for (Song s : songIndex.values()) {
            if (s.getTitle().toLowerCase().contains(kw) || 
                s.getArtist().toLowerCase().contains(kw) || 
                s.getAlbum().toLowerCase().contains(kw)) {
                results.add(s);
            }
        }
        return results;
    }

    public TreeNode getRoot() { return root; }
    public Collection<Song> getAllSongs() { return songIndex.values(); }
    public int getTotalSongs() { return songIndex.size(); }
    public Set<String> getGenres() { return root.children.keySet(); }

    /** Mengambil semua lagu dari artis tertentu. Kompleksitas: O(m) */
    public List<Song> getSongsByArtist(String genre, String artist) {
        List<Song> result = new ArrayList<>();
        TreeNode g = root.children.get(genre); 
        if (g == null) return result;
        TreeNode a = g.children.get(artist);   
        if (a == null) return result;
        
        for (TreeNode album : a.children.values()) {
            result.addAll(album.songs);
        }
        return result;
    }

    /** Mengambil lagu spesifik dari sebuah album */
    public List<Song> getSongsByAlbum(String genre, String artist, String album) {
        TreeNode g = root.children.get(genre);   if (g == null) return new ArrayList<>();
        TreeNode a = g.children.get(artist);     if (a == null) return new ArrayList<>();
        TreeNode al = a.children.get(album);     if (al == null) return new ArrayList<>();
        return al.songs;
    }
}