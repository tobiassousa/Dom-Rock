package com.domrock.controller;

import com.domrock.dto.venda.AtualizarVendaRequestDTO;
import com.domrock.dto.venda.VendaResponseDTO;
import com.domrock.model.Venda;
import com.domrock.repository.VendaRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/venda")
public class VendaController {

    @Autowired
    private VendaRepository repository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public List<VendaResponseDTO> getAll() {
        List<VendaResponseDTO> vendaList = repository.findAll().stream().map(VendaResponseDTO::new).toList();
        ;
        return vendaList;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<Venda> criarVenda(@RequestBody Venda venda) {
        try {
            Venda novaVenda = repository.save(venda);
            return new ResponseEntity<>(novaVenda, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/topVendedores")
    public List<Map<String, Object>> getTopVendedores() {
        List<Map<String, Object>> topVendedores = new ArrayList<>();
        String sql = "SELECT u.nome, v.fk_usuario_id, SUM(v.quant_vendida) AS total_vendido \n" +
                "FROM venda v \n" +
                "JOIN usuario u ON v.fk_usuario_id = u.id \n" +
                "GROUP BY u.nome, v.fk_usuario_id \n" +
                "ORDER BY total_vendido DESC \n" +
                "LIMIT 10";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> row : rows) {
            Map<String, Object> vendedor = new HashMap<>();
            vendedor.put("fk_usuario_id", row.get("fk_usuario_id"));
            vendedor.put("nome_usuario", row.get("nome"));
            vendedor.put("total_vendido", row.get("total_vendido"));
            topVendedores.add(vendedor);
        }
        return topVendedores;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/verificar-quantidades/{fk_usuario_id}")
    public boolean verificarQuantidades(@PathVariable Long fk_usuario_id) {
        List<Venda> vendas = repository.findByUsuario(fk_usuario_id);
        int quantVendidaMaior = 0;
        int quantVendidaMenor = 0;

        for (Venda venda : vendas) {
            if (venda.getQuant_vendida() > venda.getQuant_estimada()) {
                quantVendidaMaior++;
            } else if (venda.getQuant_vendida() < venda.getQuant_estimada()) {
                quantVendidaMenor++;
            }
        }

        return quantVendidaMaior > quantVendidaMenor;
    }

    @CrossOrigin(origins = "http://localhost:5500")
    @RequestMapping(method = RequestMethod.OPTIONS)
    public void preflightResponse(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5500");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
    }

}