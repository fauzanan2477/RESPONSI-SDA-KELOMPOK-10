import java.util.*;

public class AplikasiUtama {
    private ManajemenKatalog katalog;
    private ManajemenPemutaran pemutar;

    public AplikasiUtama() {
        katalog = new ManajemenKatalog();
        pemutar = new ManajemenPemutaran();
    }

    // --- FITUR UI/UX ---
    
    private void bersihkanLayar() {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    private void tekanEnterUntukLanjut(Scanner scanner) {
        System.out.println("\n[ Tekan ENTER untuk kembali ke Menu Utama... ]");
        scanner.nextLine();
        bersihkanLayar(); 
    }

    private void animasiLoading(String teks) {
        System.out.print(teks);
        try {
            for (int i = 0; i < 3; i++) {
                Thread.sleep(300); 
                System.out.print(".");
            }
            System.out.println();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Animasi ASCII Art Kaset untuk antarmuka CLI.
     * Analisis Kompleksitas Waktu: O(1) karena jumlah baris cetakannya konstan.
     */
    private void animasiKasetRetro() {
        String[] kaset = {
            "   ___________________________   ",
            "  |  =======================  |  ",
            "  |  |     MUSIQ PLAYER    |  |  ",
            "  |  |   ___         ___   |  |  ",
            "  |  |  ( + )       ( + )  |  |  ",
            "  |  |__=====_______=====__|  |  ",
            "  |___________________________|  "
        };
        System.out.println();
        for (String baris : kaset) {
            System.out.println(baris);
            try { Thread.sleep(150); } catch (Exception e) {}
        }
        System.out.println();
    }

    /**
     * Menampilkan antrean (Menelusuri Queue).
     * Analisis Kompleksitas Waktu: O(Q) di mana Q adalah jumlah lagu dalam antrean.
     */
    private void lihatAntrean() {
        System.out.println("\n=== DAFTAR ANTREAN (FIFO) ===");
        if (pemutar.getAntreanLagu().isEmpty()) {
            System.out.println("[Kosong]");
            return;
        }
        int nomor = 1;
        for (Lagu lagu : pemutar.getAntreanLagu()) {
            System.out.println(nomor + ". " + lagu.getJudul());
            nomor++;
        }
    }

    /**
     * Menampilkan riwayat (Menelusuri Stack secara mundur).
     * Analisis Kompleksitas Waktu: O(H) di mana H adalah jumlah lagu di riwayat.
     */
    private void lihatRiwayat() {
        System.out.println("\n=== RIWAYAT PUTAR (LIFO) ===");
        if (pemutar.getRiwayatLagu().isEmpty()) {
            System.out.println("[Kosong]");
            return;
        }
        Stack<Lagu> riwayat = pemutar.getRiwayatLagu();
        for (int i = riwayat.size() - 1; i >= 0; i--) {
            System.out.println("- " + riwayat.get(i).getJudul());
        }
    }

    public static void main(String[] args) {
        AplikasiUtama app = new AplikasiUtama();
        Scanner scanner = new Scanner(System.in);
        int pilihan = -1;

        app.bersihkanLayar(); 

        while (pilihan != 8) {
            System.out.println("=================================================");
            System.out.println("             [#] MUSIQ PLAYER [#]                ");
            System.out.println("=================================================");
            
            Lagu laguAktif = app.pemutar.getLaguYangSedangDiputar();
            if (laguAktif != null) {
                System.out.println(">> NOW PLAYING: " + laguAktif);
            } else {
                System.out.println(">> NOW PLAYING: [Tidak ada lagu]");
            }
            
            int jumlahAntrean = app.pemutar.getAntreanLagu().size();
            int jumlahRiwayat = app.pemutar.getRiwayatLagu().size();
            System.out.println("   [Antrean: " + jumlahAntrean + " lagu] | [Riwayat: " + jumlahRiwayat + " lagu]");
            
            System.out.println("-------------------------------------------------");
            System.out.println("1. [DIR] Lihat Katalog Musik (Tree)");
            System.out.println("2. [+]   Tambah Lagu Baru ke Katalog (Demo Dinamis)");
            System.out.println("3. [+]   Pilih & Tambah Lagu ke Antrean (Queue)");
            System.out.println("4. [Q]   Lihat Antrean (FIFO)");
            System.out.println("5. >>    Putar Berikutnya (Play Next)");
            System.out.println("6. [H]   Lihat Riwayat (History Stack)");
            System.out.println("7. <<    Kembali (Undo / Back LIFO)");
            System.out.println("8. [X]   Keluar");
            System.out.print("Pilih menu (1-8): ");

            try {
                pilihan = scanner.nextInt();
                scanner.nextLine(); 

                switch (pilihan) {
                    case 1:
                        app.animasiLoading("\nMemuat Katalog Musik");
                        System.out.println("\n=== KATALOG MUSIK HIERARKIS ===");
                        app.katalog.tampilkanKatalog(app.katalog.getRootKatalog(), "");
                        app.tekanEnterUntukLanjut(scanner);
                        break;
                    case 2:
                        System.out.println("\n=== TAMBAH LAGU BARU (SIMULASI DINAMIS) ===");
                        
                        // Menambahkan validasi ".trim()" untuk mencegah spasi kosong
                        System.out.print("Masukkan nama Genre : "); String inputGenre = scanner.nextLine().trim();
                        System.out.print("Masukkan nama Artis : "); String inputArtis = scanner.nextLine().trim();
                        System.out.print("Masukkan nama Album : "); String inputAlbum = scanner.nextLine().trim();
                        System.out.print("Masukkan Judul Lagu : "); String inputJudulBaru = scanner.nextLine().trim();
                        
                        // EDGE CASE: Mencegah input kosong
                        if (inputGenre.isEmpty() || inputArtis.isEmpty() || inputAlbum.isEmpty() || inputJudulBaru.isEmpty()) {
                            System.out.println("[ERROR] Pengisian gagal! Data tidak boleh ada yang kosong.");
                            app.tekanEnterUntukLanjut(scanner);
                            break; 
                        }

                        System.out.print("Masukkan Durasi (detik) : "); 
                        int inputDurasi = scanner.nextInt();
                        scanner.nextLine(); 

                        // EDGE CASE: Mencegah durasi 0 atau minus
                        if (inputDurasi <= 0) {
                            System.out.println("[ERROR] Durasi tidak valid! Harus lebih dari 0 detik.");
                            app.tekanEnterUntukLanjut(scanner);
                            break;
                        }

                        app.animasiLoading("Menyimpan node ke dalam Tree");
                        app.katalog.tambahLaguKeKatalog(inputGenre, inputArtis, inputAlbum, new Lagu(inputJudulBaru, inputDurasi));
                        System.out.println("[OK] Lagu berhasil masuk ke Katalog. Silakan cek di Menu 1!");
                        app.tekanEnterUntukLanjut(scanner);
                        break;
                    case 3:
                        System.out.println("\n=== DAFTAR SEMUA LAGU DI KATALOG ===");
                        List<Lagu> listSemuaLagu = new ArrayList<>();
                        app.katalog.dapatkanSemuaLagu(listSemuaLagu); 
                        
                        for(int i = 0; i < listSemuaLagu.size(); i++) {
                            System.out.println(" - " + listSemuaLagu.get(i).getJudul());
                        }

                        System.out.print("\nKetik judul lagu dari daftar di atas: ");
                        String inputJudul = scanner.nextLine().trim();
                        
                        if (inputJudul.isEmpty()) {
                            System.out.println("[ERROR] Judul lagu tidak boleh kosong!");
                            app.tekanEnterUntukLanjut(scanner);
                            break;
                        }

                        Lagu laguDitemukan = app.katalog.cariLaguBerdasarkanJudul(inputJudul);
                        if (laguDitemukan != null) {
                            app.pemutar.tambahKeAntrean(laguDitemukan);
                            System.out.println("[OK] '" + laguDitemukan.getJudul() + "' berhasil ditambahkan ke antrean.");
                        } else {
                            System.out.println("[X] Gagal. Lagu '" + inputJudul + "' tidak ditemukan.");
                        }
                        app.tekanEnterUntukLanjut(scanner);
                        break;
                    case 4:
                        app.animasiLoading("\nMembuka daftar antrean");
                        app.lihatAntrean();
                        app.tekanEnterUntukLanjut(scanner);
                        break;
                    case 5:
                        if (app.pemutar.getLaguYangSedangDiputar() == null && app.pemutar.getAntreanLagu().isEmpty()) {
                            System.out.println("\n[INFO] Tidak ada lagu yang sedang diputar maupun di antrean.");
                        } else {
                            app.animasiKasetRetro(); 
                            
                            Lagu laguBaru = app.pemutar.putarSelanjutnya();

                            if (laguBaru != null) {
                                System.out.println(">> SEDANG MEMUTAR: " + laguBaru);
                            } else {
                                System.out.println("[INFO] Antrean habis! Pemutaran dihentikan.");
                                System.out.println("       (Lagu yang tadi diputar telah dipindahkan ke Riwayat)");
                            }
                        }
                        app.tekanEnterUntukLanjut(scanner);
                        break;
                    case 6:
                        app.animasiLoading("\nMembuka catatan riwayat");
                        app.lihatRiwayat();
                        app.tekanEnterUntukLanjut(scanner);
                        break;
                    case 7:
                        if (app.pemutar.getRiwayatLagu().isEmpty()) {
                            System.out.println("\n[INFO] Riwayat kosong. Tidak bisa kembali (Undo).");
                        } else {
                            app.animasiLoading("\nMengembalikan ke trek sebelumnya");
                            Lagu laguLama = app.pemutar.kembaliKeSebelumnya();
                            System.out.println("<< UNDO BERHASIL. Kembali memutar: " + laguLama);
                        }
                        app.tekanEnterUntukLanjut(scanner);
                        break;
                    case 8:
                        app.animasiLoading("\nMenyimpan data dan mematikan sistem");
                        System.out.println("Terima kasih telah menggunakan MUSIQ Player!");
                        break;
                    default:
                        System.out.println("\n[!] Pilihan tidak ada di menu. Pilih angka 1-8.");
                        app.tekanEnterUntukLanjut(scanner);
                }
            } catch (InputMismatchException e) {
                // EDGE CASE: Penanganan jika user memasukkan huruf alih-alih angka numerik
                System.out.println("\n[ERROR] Masukan tidak valid! Harus berupa angka numerik.");
                scanner.nextLine(); 
                app.tekanEnterUntukLanjut(scanner);
            }
        }
        scanner.close();
    }
}