package com.example.backend.config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.backend.entity.Usuario;
import com.example.backend.entity.Reporte;
import com.example.backend.enums.Rol;
import com.example.backend.enums.TipoReporte;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.repository.ReporteRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final ReporteRepository reporteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Seed/Update Super Admin User
        if (!usuarioRepository.findByUsuario("superadmin").isPresent()) {
            Usuario superadmin = Usuario.builder()
                    .usuario("superadmin")
                    .contrasena(passwordEncoder.encode("Superadmin1234!"))
                    .nombres("Super")
                    .apellidos("Admin")
                    .email("superadmin@mapalert.com")
                    .rol(Rol.SUPER_ADMIN)
                    .activo(true)
                    .build();
            usuarioRepository.save(superadmin);
        } else {
            usuarioRepository.findByUsuario("superadmin").ifPresent(user -> {
                user.setContrasena("Superadmin1234!");
                user.setRol(Rol.SUPER_ADMIN);
                usuarioRepository.save(user);
            });
        }

        // Seed/Update Admin User
        if (!usuarioRepository.findByUsuario("admin").isPresent()) {
            Usuario admin = Usuario.builder()
                    .usuario("admin")
                    .contrasena(passwordEncoder.encode("Admin1234!"))
                    .nombres("Admin")
                    .apellidos("MapAlert")
                    .email("admin@mapalert.com")
                    .rol(Rol.ADMIN)
                    .activo(true)
                    .build();
            usuarioRepository.save(admin);
        } else {
            usuarioRepository.findByUsuario("admin").ifPresent(user -> {
                user.setContrasena("Admin1234!");
                user.setRol(Rol.ADMIN);
                usuarioRepository.save(user);
            });
        }

        // Seed/Update Regular User (Vecino)
        if (!usuarioRepository.findByUsuario("vecino").isPresent()) {
            Usuario vecino = Usuario.builder()
                    .usuario("vecino")
                    .contrasena(passwordEncoder.encode("Vecino1234!"))
                    .nombres("Vecino")
                    .apellidos("Comun")
                    .email("vecino@mapalert.com")
                    .rol(Rol.USER)
                    .activo(true)
                    .build();
            usuarioRepository.save(vecino);
        } else {
            usuarioRepository.findByUsuario("vecino").ifPresent(user -> {
                user.setContrasena("Vecino1234!");
                user.setRol(Rol.USER);
                usuarioRepository.save(user);
            });
        }

        // Seed Sample Reports in Santa Fe, Argentina (Near -31.6353, -60.7031) if empty
        if (reporteRepository.count() == 0) {
            Usuario adminUser = usuarioRepository.findByUsuario("admin").orElse(null);
            Usuario vecinoUser = usuarioRepository.findByUsuario("vecino").orElse(null);

            if (vecinoUser != null) {
                Reporte r1 = Reporte.builder()
                        .latitud(-31.6320)
                        .longitud(-60.7010)
                        .calle("Bulevar Pellegrini")
                        .numeroCalle(2500)
                        .ciudad("Santa Fe")
                        .provincia("Santa Fe")
                        .pais("Argentina")
                        .tipoReporte(TipoReporte.BACHE)
                        .descripcion("Bache muy profundo en la calzada sobre carril rápido")
                        .fechaCreacion(LocalDateTime.now())
                        .activo(true)
                        .usuario(vecinoUser)
                        .build();
                reporteRepository.save(r1);

                Reporte r2 = Reporte.builder()
                        .latitud(-31.6380)
                        .longitud(-60.7050)
                        .calle("San Jerónimo")
                        .numeroCalle(3200)
                        .ciudad("Santa Fe")
                        .provincia("Santa Fe")
                        .pais("Argentina")
                        .tipoReporte(TipoReporte.ACCIDENTE)
                        .descripcion("Choque menor entre dos autos en la esquina, tránsito demorado")
                        .fechaCreacion(LocalDateTime.now())
                        .activo(true)
                        .usuario(vecinoUser)
                        .build();
                reporteRepository.save(r2);
            }

            if (adminUser != null) {
                Reporte r3 = Reporte.builder()
                        .latitud(-31.6340)
                        .longitud(-60.6980)
                        .calle("Urquiza")
                        .numeroCalle(2100)
                        .ciudad("Santa Fe")
                        .provincia("Santa Fe")
                        .pais("Argentina")
                        .tipoReporte(TipoReporte.CALLE_SIN_LUZ)
                        .descripcion("Toda la cuadra se encuentra sin alumbrado público desde anoche")
                        .fechaCreacion(LocalDateTime.now())
                        .activo(true)
                        .usuario(adminUser)
                        .build();
                reporteRepository.save(r3);
            }

            System.out.println(">>> Database seeded with 3 test reports.");
        }
    }
}
