// ======================= CONTROLLER =======================
package com.bluit.tourgatronomico.controller;

import com.bluit.tourgatronomico.model.Reporte;
import com.bluit.tourgatronomico.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping
    public List<Reporte> getAll() {
        return reporteService.findAll();
    }

    @GetMapping("/{id}")
    public Reporte getById(@PathVariable Long id) {
        return reporteService.findById(id).orElse(null);
    }

    @PostMapping
    public Reporte create(@RequestBody Reporte reporte) {
        return reporteService.save(reporte);
    }

    @PutMapping("/{id}")
    public Reporte update(@PathVariable Long id, @RequestBody Reporte reporte) {
        reporte.setId(id);
        return reporteService.save(reporte);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reporteService.delete(id);
    }
}
