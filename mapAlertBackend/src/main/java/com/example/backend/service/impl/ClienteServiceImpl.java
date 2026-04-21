package com.example.backend.service.impl;

import org.springframework.stereotype.Service;

import com.example.backend.entity.Cliente;
import com.example.backend.entity.Usuario;
import com.example.backend.repository.ClienteRepository;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    public ClienteServiceImpl(
            ClienteRepository clienteRepository,
            UsuarioRepository usuarioRepository) {
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Cliente crearCliente(Long idUsuario) {

        // validar que el usuario exista
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // validar que no sea ya cliente
        if (clienteRepository.existsById(idUsuario)) {
            throw new RuntimeException("El usuario ya es cliente");
        }

        Cliente cliente = new Cliente();
        cliente.setId(usuario.getId()); // PK compartida

        return clienteRepository.save(cliente);
    }
}
