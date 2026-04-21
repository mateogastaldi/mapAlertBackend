package com.example.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "administrador")
public class Administrador extends Usuario {
    // atributos extra en el futuro
}
