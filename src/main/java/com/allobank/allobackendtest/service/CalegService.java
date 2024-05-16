package com.allobank.allobackendtest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.JenisKelamin;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.CalegRepo;
import com.allobank.allobackendtest.repository.DapilRepo;
import com.allobank.allobackendtest.repository.PartaiRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class CalegService {
    
    @Autowired
    private CalegRepo calegRepo;
    
    @Autowired
    private PartaiRepo partaiRepo;
    
    @Autowired
    private DapilRepo dapilRepo;

    public List<Caleg> getAllCalegs() {
        return calegRepo.findAll();
    }

    public List<Caleg> getCalegsByDapil(UUID dapilId) {
        return calegRepo.findByDapilId(dapilId);
    }

    public List<Caleg> getCalegsByPartai(UUID partaiId) {
        return calegRepo.findByPartaiId(partaiId);
    }

    public List<Caleg> getCalegsByNamaDapil(String nama_dapil) {
        return calegRepo.findByDapilNamaDapil(nama_dapil);
    }

    public List<Caleg> getCalegsByNamaPartai(String nama_partai) {
        return calegRepo.findByPartaiNamaPartai(nama_partai);
    }

    public Object importDataFromURL(String link) {
        // List<ObjectNode> jsonArray = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        int newDapilCount;
        int newPartaiCount;
        int newCalegCount;
        try {
            // Fetch CSV From URL
            if (!link.startsWith("http://") && !link.startsWith("https://")) {
                link = "https://" + link;
            }

            String fileContent;

            @SuppressWarnings("deprecation")
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(true);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine).append("\n");
                }
                in.close();

                fileContent = response.toString();
            } else {
                throw new Exception("Failed to fetch file: HTTP response code " + responseCode);
            }

            // Process CSV
            String[] lines = fileContent.split("\n");
            newPartaiCount = 0;
            newDapilCount = 0;
            newCalegCount = 0;
            
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                String[] columns = line.split(",");

                if (columns[7].equals("L") || columns[7].equals("P")) {
                    // Create a JSON object for each row
                    // ObjectNode jsonObject = mapper.createObjectNode();
                    // jsonObject.put("DAPIL", columns[0]);
                    // jsonObject.put("PROVINSI", columns[1]);
                    // jsonObject.put("NAMA_DAPIL", columns[2]);
                    // jsonObject.put("NOMOR_PARTAI", columns[3]);
                    // jsonObject.put("NAMA_PARTAI", columns[4]);
                    // jsonObject.put("NOMOR_URUT", columns[5]);
                    // jsonObject.put("NAMA_CALEG", columns[6]);
                    // jsonObject.put("JENIS_KELAMIN", columns[7]);
                    // jsonArray.add(jsonObject);

                    Partai partaidb = partaiRepo.findByNamaPartai(columns[4]);
                    Partai partai_caleg;
                    if (partaidb == null) {
                        Partai newPartai = new Partai();
                        newPartai.setNamaPartai(columns[4]);
                        newPartai.setNomorUrut(Integer.parseInt(columns[3]));
                        partaiRepo.save(newPartai);
                        partai_caleg = newPartai;
                        newPartaiCount++;
                    }else{
                        partai_caleg = partaidb;
                        // System.out.print(partaidb);
                        // System.out.print("Partai already exist");
                    }

                    // List<String> DapilList;
                    List<String> DapilList = Arrays.asList("DKI JAKARTA I", "DKI JAKARTA II", "DKI JAKARTA III");
                    Dapil dapildb = dapilRepo.findByNamaDapil(columns[0]);
                    Dapil dapil_caleg;
                    if (dapildb == null) {  
                        // DapilList = Arrays.asList(columns[2]);
                        Dapil newDapil = new Dapil();
                        newDapil.setNamaDapil(columns[0]);
                        newDapil.setProvinsi(columns[1]);
                        newDapil.setJumlahKursi(50);
                        newDapil.setWilayahDapilList(DapilList);
                        dapilRepo.save(newDapil);
                        dapil_caleg = newDapil;
                        newDapilCount++;
                    }else{
                        dapil_caleg = dapildb;
                        // System.out.print(dapildb);
                        // System.out.print("Dapil already exist");
                    }

                    Caleg calegdb = calegRepo.findByNama(columns[6]);
                    if (calegdb == null) {  
                        Caleg newCaleg = new Caleg();
                        newCaleg.setNama(columns[6]);
                        if (columns[7].equals("L")) {
                            newCaleg.setJenisKelamin(JenisKelamin.LAKILAKI);
                        }else{
                            newCaleg.setJenisKelamin(JenisKelamin.PEREMPUAN);
                        }
                        newCaleg.setNomorUrut(Integer.parseInt(columns[5]));
                        newCaleg.setDapil(dapil_caleg);
                        newCaleg.setPartai(partai_caleg);
                        calegRepo.save(newCaleg);
                        newCalegCount++;
                    }else{
                        // System.out.print(calegdb);
                        // System.out.print("Caleg already exist");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        ObjectNode returnJson = mapper.createObjectNode();
        returnJson.put("new_partai_count",newPartaiCount);
        returnJson.put("new_dapil_count",newDapilCount);
        returnJson.put("new_caleg_count",newCalegCount);
        return returnJson;
    }
}