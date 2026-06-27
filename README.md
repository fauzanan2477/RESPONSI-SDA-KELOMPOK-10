# Proyek Responsi Akhir SDA 2025B — MUSIQ Player

### Kelompok: [Isi Nomor Kelompok Kalian]
**Anggota Kelompok 10:**
1. FAUZAN AKBAR NAVIS — [L0125041]
2. [ANGELINA JOULY] — [L0125]
3. [NAUFAL HAFIZ RAHMAN] — [L0125]

## 1. Deskripsi Singkat & Fitur Utama
MUSIQ Player adalah aplikasi simulasi manajemen pustaka musik interaktif berbasis Command Line Interface (CLI) yang menerapkan prinsip penyimpanan data hierarkis dan alur data linear seperti platform Spotify.

**Fitur Utama:**
* **Browse Katalog:** Menjelajahi katalog musik secara bertingkat berdasarkan urutan Genre -> Artis -> Album -> Lagu.
* **Pencarian Lagu:** Mencari lagu di seluruh katalog berdasarkan kata kunci judul atau artis secara dinamis.
* **Antrean Putar (Play Queue):** Mengatur daftar putar lagu mendatang secara teratur.
* **Kontrol Pemutar (Play Next & Undo):** Memutar lagu urutan terdepan atau kembali memulihkan lagu yang baru saja diputar.
* **Riwayat Putar (History):** Menyimpan daftar lagu yang telah selesai didengarkan.

## 2. Struktur Data yang Digunakan & Alasan Pemilihan
* **General Tree (N-Ary Tree):** Digunakan untuk membangun katalog musik bertingkat (Genre-Artis-Album). Alasan pemilihan karena struktur data ini sangat ideal untuk memodelkan hubungan data hierarkis di dunia nyata. Dioptimalkan dengan *LinkedHashMap* agar proses pencarian kategori anak bernilai $O(1)$.
* **Queue (FIFO) menggunakan LinkedList:** Digunakan pada fitur Antrean Putar (*Next in Queue*). Alasan pemilihan karena *LinkedList* memfasilitasi operasi penambahan data di akhir (*addLast*) dan penghapusan di awal (*removeFirst*) dengan performa konstan $O(1)$ tanpa perlu proses *resizing* memori.
* **Stack (LIFO) menggunakan ArrayDeque:** Digunakan untuk fitur Riwayat Putar dan fungsi *Undo/Back*. Alasan pemilihan karena *ArrayDeque* tidak memiliki *synchronized overhead* seperti kelas *Stack* peninggalan lama (*legacy class*), sehingga operasi *push* dan *pop* data berjalan jauh lebih cepat pada kompleksitas $O(1)$.

## 3. Panduan Instalasi & Menjalankan Program
Aplikasi ini dibangun menggunakan Java murni tanpa library eksternal tambahan.
1. Pastikan perangkat Anda sudah terinstal Java Development Kit (JDK).
2. Letakkan file `CatalogManager.java`, `PlaybackManager.java`, dan `MainApp.java` dalam satu folder yang sama.
3. Buka Terminal atau PowerShell pada direktori folder tersebut.
4. Lakukan kompilasi program dengan mengetik:
   ```bash
   javac *.java
