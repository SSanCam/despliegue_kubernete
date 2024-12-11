package com.es.ApiLol.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "partidas")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private Date fecha;

    @Column
    private String resultado;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "campeon_id", nullable = false)
    private Campeon campeon;

    @Column(length = 3)
    private int duracion;
}
