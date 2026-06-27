import java.util.*;

public class AplikasiUtama {
    private ManajemenKatalog katalog;
    private ManajemenPemutaran pemutar;

    public AplikasiUtama() {
        katalog = new ManajemenKatalog();
        pemutar = new ManajemenPemutaran();
    }

    // --- FITUR UI/UX PREMIUM ---

    /**
     * Membersihkan layar terminal menggunakan kode ANSI Escape.
     */
    private void bersihkanLayar() {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception e) {
            // Jika terminal tidak mendukung ANSI, cetak baris kosong banyak-banyak
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    /**
     * Memberikan jeda agar user bisa membaca output sebelum kembali ke Menu Utama.
     */
    private void tekanEnterUntukLanjut(Scanner scanner) {
        System.out.println("\n[ Tekan ENTER untuk kembali ke Menu Utama... ]");
        scanner.nextLine();
        bersihkanLayar(); // Bersihkan layar setelah enter ditekan
    }

    /**
     * Membuat animasi titik-titik (loading) agar program terlihat profesional.
     */
    private void animasiLoading(String teks) {
        System.out.print(teks);
        try {
            for (int i = 0; i < 3; i++) {
                Thread.sleep(400); // Jeda 400 milidetik per titik
                System.out.print(".");
            }
            System.out.println();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // --- FITUR TAMPILAN ---

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

    // --- MAIN METHOD ---

    public static void main(String[] args) {
        AplikasiUtama app = new AplikasiUtama();
        Scanner scanner = new Scanner(System.in);
        int pilihan = -1;

        app.bersihkanLayar(); // Bersihkan terminal saat pertama kali jalan

        while (pilihan != 7) {
            System.out.println("=================================================");
            System.out.println("             [#] MUSIQ PLAYER [#]                ");
            System.out.println("=================================================");
            
            Lagu laguAktif = app.pemutar.getLaguYangSedangDiputar();
            if (laguAktif != null) {
                System.out.println(">> NOW PLAYING: " + laguAktif);
            } else {
                System.out.println(">> NOW PLAYING: [Tidak ada lagu]");
            }
            
            // Header Dinamis (menunjukkan jumlah isi struktur data)
            int jumlahAntrean = app.pemutar.getAntreanLagu().size();
            int jumlahRiwayat = app.pemutar.getRiwayatLagu().size();
            System.out.println("   [Antrean: " + jumlahAntrean + " lagu] | [Riwayat: " + jumlahRiwayat + " lagu]");
            
            System.out.println("-------------------------------------------------");
            System.out.println("1. [DIR] Lihat Katalog Musik (Tree)");
            System.out.println("2. [+]   Tambah Lagu ke Antrean (Queue)");
            System.out.println("3. [Q]   Lihat Antrean (FIFO)");
            System.out.println("4. >>    Putar Berikutnya (Play Next)");
            System.out.println("5. [H]   Lihat Riwayat (History Stack)");
            System.out.println("6. <<    Kembali (Undo / Back LIFO)");
            System.out.println("7. [X]   Keluar");
            System.out.print("Pilih menu (1-7): ");

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
                        System.out.print("\nMasukkan judul lagu persis seperti di katalog: ");
                        String inputJudul = scanner.nextLine();
                        app.animasiLoading("Mencari lagu di penyimpanan");
                        
                        Lagu laguDitemukan = app.katalog.cariLaguBerdasarkanJudul(app.katalog.getRootKatalog(), inputJudul);
                        if (laguDitemukan != null) {
                            app.pemutar.tambahKeAntrean(laguDitemukan);
                            System.out.println("[OK] '" + laguDitemukan.getJudul() + "' berhasil ditambahkan ke antrean.");
                        } else {
                            System.out.println("[X] Gagal. Lagu '" + inputJudul + "' tidak ditemukan.");
                        }
                        app.tekanEnterUntukLanjut(scanner);
                        break;
                    case 3:
                        app.animasiLoading("\nMembuka daftar antrean");
                        app.lihatAntrean();
                        app.tekanEnterUntukLanjut(scanner);
                        break;
                    case 4:
                        if (app.pemutar.getLaguYangSedangDiputar() == null && app.pemutar.getAntreanLagu().isEmpty()) {
                            System.out.println("\n[INFO] Tidak ada lagu yang sedang diputar maupun di antrean.");
                        } else {
                            app.animasiLoading("\nMemproses pergantian lagu");
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
                    case 5:
                        app.animasiLoading("\nMembuka catatan riwayat");
                        app.lihatRiwayat();
                        app.tekanEnterUntukLanjut(scanner);
                        break;
                    case 6:
                        if (app.pemutar.getRiwayatLagu().isEmpty()) {
                            System.out.println("\n[INFO] Riwayat kosong. Tidak bisa kembali (Undo).");
                        } else {
                            app.animasiLoading("\nMengembalikan ke trek sebelumnya");
                            Lagu laguLama = app.pemutar.kembaliKeSebelumnya();
                            System.out.println("<< UNDO BERHASIL. Kembali memutar: " + laguLama);
                        }
                        app.tekanEnterUntukLanjut(scanner);
                        break;
                    case 7:
                        app.animasiLoading("\nMenyimpan data dan mematikan sistem");
                        System.out.println("Terima kasih telah menggunakan MUSIQ Player!");
                        break;
                    default:
                        System.out.println("\n[!] Pilihan tidak ada di menu. Pilih angka 1-7.");
                        app.tekanEnterUntukLanjut(scanner);
                }
            } catch (InputMismatchException e) {
                System.out.println("\n[ERROR] Masukan tidak valid! Harus berupa angka.");
                scanner.nextLine(); 
                app.tekanEnterUntukLanjut(scanner);
            }
        }
        scanner.close();
    }
}
