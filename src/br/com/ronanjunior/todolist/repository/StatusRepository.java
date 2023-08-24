package br.com.ronanjunior.todolist.repository;

import br.com.ronanjunior.todolist.domain.StatusDomain;

public class StatusRepository implements StatusDomain {
    private String nome;

    public StatusRepository(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "StatusRepository{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
