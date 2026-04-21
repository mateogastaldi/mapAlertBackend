package com.example.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.entity.Cliente;
import com.example.backend.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<Cliente> crearCliente(
            @PathVariable Long idUsuario) {

        Cliente cliente = clienteService.crearCliente(idUsuario);
        return new ResponseEntity<>(cliente, HttpStatus.CREATED);
    }
}
