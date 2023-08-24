package br.com.ronanjunior.todolist.repository;

import br.com.ronanjunior.todolist.domain.CategoriaDomain;

public class CategoriasRepository implements CategoriaDomain {
    private String nome;

    public CategoriasRepository(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "CategoriasRepository{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
