package Novamonitor.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ch_tutor")

public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id_tutor")
    private Long id;

    @Column(name = "nm_tutor")
    private String nome;

    @Column(name = "ds_email")
    private String email;

    @Column(name = "nr_telefone")
    private String telefone;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}