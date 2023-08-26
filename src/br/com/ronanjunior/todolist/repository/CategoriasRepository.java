package br.com.ronanjunior.todolist.repository;

import br.com.ronanjunior.todolist.domain.CategoriaDomain;

import java.util.Objects;

public class CategoriasRepository implements CategoriaDomain {
    private String nome;

    public CategoriasRepository(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoriasRepository that = (CategoriasRepository) o;
        return Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

    @Override
    public String toString() {
        return "CategoriasRepository{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
