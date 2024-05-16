package com.allobank.allobackendtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.allobank.allobackendtest.controller.CalegController;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.service.CalegService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class CalegControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CalegService calegService;

    @InjectMocks
    private CalegController calegController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(calegController).build();
    }

    @Test
    public void testGetAllCalegs() throws Exception {
        List<Caleg> calegList = new ArrayList<>();
        Caleg caleg1 = new Caleg();
        caleg1.setNama("Caleg 1");
        caleg1.setNomorUrut(1);
        calegList.add(caleg1);

        Caleg caleg2 = new Caleg();
        caleg2.setNama("Caleg 2");
        caleg2.setNomorUrut(2);
        calegList.add(caleg2);

        when(calegService.getAllCalegs()).thenReturn(calegList);

        mockMvc.perform(get("/api/caleg/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nama").value("Caleg 1"))
                .andExpect(jsonPath("$[1].nama").value("Caleg 2"));
        
        verify(calegService, times(1)).getAllCalegs();
    }

    @Test
    public void testGetCalegsByDapil() throws Exception {
        List<Caleg> calegList = new ArrayList<>();
        List<String> DapilList = Arrays.asList("DKI JAKARTA I", "DKI JAKARTA II", "DKI JAKARTA III");
        
        UUID dapilId = UUID.randomUUID();
        Dapil dapil1 = new Dapil();
        dapil1.setId(dapilId);
        dapil1.setNamaDapil("Dapil 1");
        dapil1.setProvinsi("DKI JAKARTA");
        dapil1.setWilayahDapilList(DapilList);
        dapil1.setJumlahKursi(50);

        Caleg caleg1 = new Caleg();
        caleg1.setNama("Caleg 1");
        caleg1.setNomorUrut(1);
        caleg1.setDapil(dapil1);
        calegList.add(caleg1);

        when(calegService.getCalegsByDapil(dapilId)).thenReturn(calegList);

        mockMvc.perform(get("/api/caleg/all")
                .param("dapilId", dapilId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nama").value("Caleg 1"));

        verify(calegService, times(1)).getCalegsByDapil(dapilId);
    }

    @Test
    public void testGetCalegsByPartai() throws Exception {
        List<Caleg> calegList = new ArrayList<>();
        
        UUID partaiId = UUID.randomUUID();
        Partai partai1 = new Partai();
        partai1.setId(partaiId);
        partai1.setNamaPartai("Partai 1");
        partai1.setNomorUrut(1);

        Caleg caleg1 = new Caleg();
        caleg1.setNama("Caleg 1");
        caleg1.setNomorUrut(1);
        caleg1.setPartai(partai1);
        calegList.add(caleg1);

        when(calegService.getCalegsByPartai(partaiId)).thenReturn(calegList);

        mockMvc.perform(get("/api/caleg/all")
                .param("partaiId", partaiId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nama").value("Caleg 1"));

        verify(calegService, times(1)).getCalegsByPartai(partaiId);
    }

    @Test
    public void testGetAllCalegsSortedByNomorUrut() throws Exception {
        List<Caleg> calegList = new ArrayList<>();
        Caleg caleg1 = new Caleg();
        caleg1.setNama("Caleg 1");
        caleg1.setNomorUrut(1);
        calegList.add(caleg1);

        Caleg caleg2 = new Caleg();
        caleg2.setNama("Caleg 2");
        caleg2.setNomorUrut(2);
        calegList.add(caleg2);

        when(calegService.getAllCalegs()).thenReturn(calegList);

        mockMvc.perform(get("/api/caleg/all")
                .param("sortBy", "nomorUrut")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nama").value("Caleg 1"))
                .andExpect(jsonPath("$[1].nama").value("Caleg 2"));

        verify(calegService, times(1)).getAllCalegs();
    }
}
