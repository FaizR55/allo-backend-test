package com.allobank.allobackendtest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.service.CalegService;

@RestController
@RequestMapping("/api/caleg")
public class CalegController {

    @Autowired
    private CalegService calegService;

    // import data caleg dari csv web pemilu
    // opendatadev.kpu.go.id/sites/default/files/files/9487b0c97879e47981bbbc807e467cb5.csv
    // hasil iseng aja
    @GetMapping("import")
    public Object importData(@RequestParam(required = true) String url) {
        return calegService.importDataFromURL(url);
    }

    @GetMapping("all")
    public List<Caleg> getAllCalegs(
            @RequestParam(required = false) UUID dapilId,
            @RequestParam(required = false) UUID partaiId,
            @RequestParam(required = false) String dapilNama,
            @RequestParam(required = false) String partaiNama,
            @RequestParam(required = false) String sortBy
    ) {
        List<Caleg> calegs;

        if (dapilId != null) {
            calegs = calegService.getCalegsByDapil(dapilId);
        } else if (partaiId != null) {
            calegs = calegService.getCalegsByPartai(partaiId);
        } else if (dapilNama != null) {
            calegs = calegService.getCalegsByNamaDapil(dapilNama);
        } else if (partaiNama != null) {
            calegs = calegService.getCalegsByNamaPartai(partaiNama);
        } else {
            calegs = calegService.getAllCalegs();
        }

        if ("nomorUrut".equalsIgnoreCase(sortBy)) {
            calegs.sort((c1, c2) -> Integer.compare(c1.getNomorUrut(), c2.getNomorUrut()));
        }

        return calegs;
    }
}