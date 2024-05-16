DROP TABLE IF EXISTS wilayah_dapil_list;
DROP TABLE IF EXISTS caleg;
DROP TABLE IF EXISTS dapil;
DROP TABLE IF EXISTS partai;

CREATE TABLE partai (
    id CHAR(36) PRIMARY KEY,
    nama_partai VARCHAR(255) NOT NULL,
    nomor_urut INT
);

CREATE TABLE dapil (
    id CHAR(36) PRIMARY KEY,
    nama_dapil VARCHAR(255) NOT NULL,
    provinsi VARCHAR(255) NOT NULL,
    jumlah_kursi INT NOT NULL
);

CREATE TABLE dapil_wilayah_dapil_list (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dapil_id CHAR(36),
    wilayah VARCHAR(255) NOT NULL,
    FOREIGN KEY (dapil_id) REFERENCES dapil(id)
);

CREATE TABLE caleg (
    id CHAR(36) PRIMARY KEY,
    dapil_id CHAR(36),
    partai_id CHAR(36),
    nomor_urut INT,
    nama VARCHAR(255) NOT NULL,
    jenis_kelamin ENUM('LAKILAKI', 'PEREMPUAN') NOT NULL,
    FOREIGN KEY (dapil_id) REFERENCES dapil(id),
    FOREIGN KEY (partai_id) REFERENCES partai(id)
);