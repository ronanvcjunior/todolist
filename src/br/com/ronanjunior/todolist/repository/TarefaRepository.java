package br.com.ronanjunior.todolist.repository;

import br.com.ronanjunior.todolist.domain.CategoriaDomain;
import br.com.ronanjunior.todolist.domain.StatusDomain;
import br.com.ronanjunior.todolist.domain.TarefaDomain;

import java.util.Date;
import java.util.List;

public class TarefaRepository implements TarefaDomain {
    private String nome;
    private String descricao;
    private Date dtTermino;
    private Integer prioridade;
    private List<CategoriaDomain> categorias;
    private StatusDomain status;

    public TarefaRepository(String nome, StatusDomain status) {
        this.nome = nome;
        this.status = status;
        this.descricao = null;
        this.dtTermino = null;
        this.prioridade = 1;
        this.categorias = null;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDtTermino() {
        return dtTermino;
    }

    public void setDtTermino(Date dtTermino) {
        this.dtTermino = dtTermino;
    }

    public Integer getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Integer prioridade) {
        this.prioridade = prioridade;
    }

    public List<CategoriaDomain> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaDomain> categorias) {
        this.categorias = categorias;
    }

    public StatusDomain getStatus() {
        return status;
    }

    public void setStatus(StatusDomain status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TarefaRepository{" +
                "nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dtTermino=" + dtTermino +
                ", prioridade=" + prioridade +
                ", categorias=" + categorias +
                ", status=" + status +
                '}';
    }
}
