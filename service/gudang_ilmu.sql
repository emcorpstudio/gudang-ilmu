-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 06, 2021 at 04:49 PM
-- Server version: 10.4.8-MariaDB
-- PHP Version: 7.3.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gudang_ilmu`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_donasi`
--

CREATE TABLE `tbl_donasi` (
  `id_donasi` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `judul` varchar(100) NOT NULL,
  `deskripsi` varchar(200) NOT NULL,
  `foto` varchar(100) NOT NULL,
  `status` enum('ACTIVE','INACTIVE') NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_donasi`
--

INSERT INTO `tbl_donasi` (`id_donasi`, `id_user`, `judul`, `deskripsi`, `foto`, `status`, `created_at`) VALUES
(1, 5, 'Pembangkit Listrik Tenaga Angin di Sulawesi Selatan', 'Donasi ini akan di gunakan untuk pembangunan pembangkit listrik tenaga angin di Sulawesi Selatan agar daya listrik yang dihasilkan lebih banyak lagi', 'donasi_1.jpg', 'ACTIVE', '2021-07-05 17:31:02'),
(2, 5, 'Pembangkit Listrik Tenaga Air di Pedalaman Wamena', 'Donasi ini akan digunakan untuk pembangunan pembangkit listrik tenaga air di Pedalaman Wamena', 'donasi_2.jpg', 'ACTIVE', '2021-07-05 17:31:02'),
(3, 5, 'Pengembangan Mobil Listrik Nasional', 'Donasi ini akan digunakan untuk pengembangan mobil listrik nasional', 'donasi_3.jpg', 'ACTIVE', '2021-07-05 17:31:02'),
(4, 5, 'Pengembangan Motor Listrik Nasional', 'Donasi ini akan digunakan untuk pengembangan motor listrik nasional', 'donasi_4.jpg', 'ACTIVE', '2021-07-05 17:31:02'),
(5, 5, 'Pembangkit Listrik Tenaga Surya untuk Warna Desa Jatirembe', 'Donasi ini akan digunakan untuk memasang pembangkit listrik tenaga surya bagi Warga Desa Jatirembe', 'donasi_5.jpg', 'ACTIVE', '2021-07-05 17:31:02'),
(6, 5, 'Pembagian Kompor Listrik Gratis untuk UMKM', 'Donasi ini akan digunakan untuk membagikan kompor listrik di kalangan UMKM', 'donasi_6.jpg', 'ACTIVE', '2021-07-05 17:31:02'),
(7, 5, 'Pemberian Sepeda Listrik untuk Ibu-Ibu Penjual Jamu', 'Donasi ini akan digunakan untuk pemberian sepeda listrik bagi ibu-ibu penjual jamu', 'donasi_7.jpg', 'ACTIVE', '2021-07-05 17:31:02');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_produk`
--

CREATE TABLE `tbl_produk` (
  `id_produk` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `judul` varchar(100) NOT NULL,
  `deskripsi` varchar(200) NOT NULL,
  `foto` varchar(100) NOT NULL,
  `status` enum('ACTIVE','INACTIVE') NOT NULL,
  `harga` double NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_produk`
--

INSERT INTO `tbl_produk` (`id_produk`, `id_user`, `judul`, `deskripsi`, `foto`, `status`, `harga`, `created_at`) VALUES
(1, 5, 'Baterai Lithium', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam efficitur ipsum in placerat molestie.  Fusce quis mauris a enim sollicitudin', 'baterai.jpg', 'ACTIVE', 3800000, '2021-07-05 17:31:02'),
(2, 5, 'Kompor Listrik', 'Kompor listrik murah, awet dan tahan lama. Tidak mudah rusak, dan ramah di kantong', 'komporlistrik.jpg', 'ACTIVE', 475000, '2021-07-05 17:31:02'),
(3, 5, 'Kabel Listrik NYM', 'Ini adalah kabel NYM yang awet dan tahan lama, tidak mudah putus dan harga murah', 'kabel.jpg', 'ACTIVE', 325000, '2021-07-05 17:31:02'),
(4, 5, 'Generator Listrik', 'Ini adalah jenis Generator Listrik Magnetik 10kw', 'generator.jpg', 'ACTIVE', 7500000, '2021-07-05 17:31:02'),
(5, 5, 'Speed Reducer', 'Speed Reducer (perubah kecepatan) adalah alat yang berfungsi untuk merubah (menaikkan atau menurunkan) kecepatan putaran', 'speedreducer.jpg', 'ACTIVE', 1200000, '2021-07-05 17:31:02'),
(6, 5, 'Gear Box', 'Ini adalah Motovario Helical Gearbox Type H083 1 78 87 Tanpa Motor', 'gearbox.jpg', 'ACTIVE', 2700000, '2021-07-05 17:31:02'),
(7, 5, 'Lampu Hemat Energi', 'Ini adalah lampu hemat energi, yang lebih murah biaya listriknya', 'lampugantung.jpg', 'ACTIVE', 24000, '2021-07-05 17:31:02'),
(8, 5, 'Aki GS Astra', 'Ini adalah Aki GS Astra yang bisa digunakan untuk menyimpan listrik tenaga surya', 'aki.jpg', 'ACTIVE', 735000, '2021-07-05 17:31:02'),
(9, 5, 'Power Inverter', 'Ini adalah power inverter 220 V yang bisa digunakan untuk pembangkit listrik', 'inverter.jpg', 'ACTIVE', 1750000, '2021-07-05 17:31:02'),
(14, 5, 'Sarung Tangan Listrik', 'Ini adalah sarug tangan anti lisrik', 'sarungtangan.jpg', 'ACTIVE', 548000, '2021-07-05 17:31:02'),
(16, 5, 'asdasd', 'asdasd', 'img_foto_20210706214244.jpg', 'ACTIVE', 32432423, '2021-07-06 14:42:44');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_sumbang`
--

CREATE TABLE `tbl_sumbang` (
  `id_topup` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `amount` double NOT NULL,
  `id_donasi` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_sumbang`
--

INSERT INTO `tbl_sumbang` (`id_topup`, `id_user`, `amount`, `id_donasi`, `created_at`) VALUES
(4, 5, 1000, 1, '2021-07-06 06:41:17'),
(5, 5, 2000, 2, '2021-07-06 06:48:30'),
(6, 5, 2000, 3, '2021-07-06 06:51:52'),
(7, 5, 1000, 1, '2021-07-06 07:40:57'),
(8, 5, 1000, 9, '2021-07-06 07:41:16'),
(9, 5, 2000, 2, '2021-07-06 08:07:07'),
(10, 5, 2000, 1, '2021-07-06 08:56:15'),
(11, 5, 1000, 1, '2021-07-06 08:59:25');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_sumbang_video`
--

CREATE TABLE `tbl_sumbang_video` (
  `id_topup` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `amount` double NOT NULL,
  `id_donasi` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_sumbang_video`
--

INSERT INTO `tbl_sumbang_video` (`id_topup`, `id_user`, `amount`, `id_donasi`, `created_at`) VALUES
(1, 5, 1000, 1, '2021-07-06 14:28:12'),
(2, 5, 2000, 2, '2021-07-06 14:37:18');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_topup`
--

CREATE TABLE `tbl_topup` (
  `id_topup` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `amount` double NOT NULL,
  `type` varchar(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_topup`
--

INSERT INTO `tbl_topup` (`id_topup`, `id_user`, `amount`, `type`, `created_at`) VALUES
(1, 5, 25000, 'Permata', '2021-07-06 03:39:21'),
(2, 5, 15000, 'BCA', '2021-07-06 03:47:27'),
(3, 5, 25000, 'Mandiri', '2021-07-06 04:24:55'),
(4, 5, 25000, 'BCA', '2021-07-06 08:57:45');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_user`
--

CREATE TABLE `tbl_user` (
  `id_user` int(11) NOT NULL,
  `full_name` varchar(50) NOT NULL,
  `address` varchar(200) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(200) NOT NULL,
  `photo` varchar(200) NOT NULL,
  `identity` varchar(200) NOT NULL,
  `status` enum('PENDING','VERIVIED') NOT NULL,
  `role` enum('USER','SUPERADMIN','ADMIN') NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_user`
--

INSERT INTO `tbl_user` (`id_user`, `full_name`, `address`, `phone`, `email`, `password`, `photo`, `identity`, `status`, `role`, `created_date`) VALUES
(5, 'Misbakhul Munir', 'Gresik', '085655312333', 'misbakhulmunir11@gmail.com', 'ca9d3b236d051267fb2abf03814c311d', '', '', 'PENDING', 'USER', '2021-07-05 17:15:35'),
(6, 'Administrator', 'Gresik', '081232672333', 'superadmin@gmail.com', '17c4520f6cfd1ab53d8745e84681eb49', '', '', 'VERIVIED', 'SUPERADMIN', '2021-07-06 04:04:51');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_video`
--

CREATE TABLE `tbl_video` (
  `id_video` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `judul` varchar(100) NOT NULL,
  `deskripsi` varchar(200) NOT NULL,
  `foto` varchar(100) NOT NULL,
  `video` varchar(200) NOT NULL,
  `status` enum('ACTIVE','INACTIVE') NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_video`
--

INSERT INTO `tbl_video` (`id_video`, `id_user`, `judul`, `deskripsi`, `foto`, `video`, `status`, `created_at`) VALUES
(1, 5, 'Cara buat listrik gratis sendiri dirumah dengan modal Rp70000', 'Ini adalah video cara buat listrik gratis sendiri dirumah dengan modal Rp70000', 'video_1.jpg', 'https://www.youtube.com/watch?v=22hboHRFUK0&t=150s', 'ACTIVE', '2021-07-05 17:31:02'),
(2, 5, 'Energi Listrik dari Kotoran Sapi', 'Ini adalah video cara membuat energi listrik dari kotoran sapi', 'video_2.jpg', 'https://www.youtube.com/watch?v=uGl7nzjAZik', 'ACTIVE', '2021-07-05 17:31:02'),
(3, 5, 'Listrik Gratis modal Rp100rb', 'Ini adalah video tutorial listrik gratis modal Rp100rb', 'video_3.jpg', 'https://www.youtube.com/watch?v=KXzHJLiZIu0', 'ACTIVE', '2021-07-05 17:31:02'),
(4, 5, 'Listrik Gratis 800watt Dari Angin Menggunakan Win Turbin', 'Ini adalah video cara membuat Listrik Gratis 800watt Dari Angin Menggunakan Win Turbin', 'video_4.jpg', 'https://www.youtube.com/watch?v=jiF8LZITvqs', 'ACTIVE', '2021-07-05 17:31:02'),
(5, 5, 'Cara buat MIKROHIDRO Listrik gratis tenaga air', 'Ini adalah video bagaimana Cara buat MIKROHIDRO Listrik gratis tenaga air', 'video_5.jpg', 'https://www.youtube.com/watch?v=uSAvH10Tlvw', 'ACTIVE', '2021-07-05 17:31:02'),
(8, 5, 'Momen gaya (Torsi) / Dinamik Rotasi', 'Ini adalah video tentang Momen gaya (Torsi) / Dinamik Rotasi', 'video_7.jpg', 'https://www.youtube.com/watch?v=D1KBQuWeoRI', 'ACTIVE', '2021-07-05 17:31:02');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_donasi`
--
ALTER TABLE `tbl_donasi`
  ADD PRIMARY KEY (`id_donasi`);

--
-- Indexes for table `tbl_produk`
--
ALTER TABLE `tbl_produk`
  ADD PRIMARY KEY (`id_produk`);

--
-- Indexes for table `tbl_sumbang`
--
ALTER TABLE `tbl_sumbang`
  ADD PRIMARY KEY (`id_topup`);

--
-- Indexes for table `tbl_sumbang_video`
--
ALTER TABLE `tbl_sumbang_video`
  ADD PRIMARY KEY (`id_topup`);

--
-- Indexes for table `tbl_topup`
--
ALTER TABLE `tbl_topup`
  ADD PRIMARY KEY (`id_topup`);

--
-- Indexes for table `tbl_user`
--
ALTER TABLE `tbl_user`
  ADD PRIMARY KEY (`id_user`);

--
-- Indexes for table `tbl_video`
--
ALTER TABLE `tbl_video`
  ADD PRIMARY KEY (`id_video`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_donasi`
--
ALTER TABLE `tbl_donasi`
  MODIFY `id_donasi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `tbl_produk`
--
ALTER TABLE `tbl_produk`
  MODIFY `id_produk` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `tbl_sumbang`
--
ALTER TABLE `tbl_sumbang`
  MODIFY `id_topup` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `tbl_sumbang_video`
--
ALTER TABLE `tbl_sumbang_video`
  MODIFY `id_topup` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tbl_topup`
--
ALTER TABLE `tbl_topup`
  MODIFY `id_topup` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `tbl_user`
--
ALTER TABLE `tbl_user`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `tbl_video`
--
ALTER TABLE `tbl_video`
  MODIFY `id_video` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
