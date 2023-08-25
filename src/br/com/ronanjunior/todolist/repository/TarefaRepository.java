package br.com.ronanjunior.todolist.repository;

import br.com.ronanjunior.todolist.domain.CategoriaDomain;
import br.com.ronanjunior.todolist.domain.DateManipulacaoDomain;
import br.com.ronanjunior.todolist.domain.StatusDomain;
import br.com.ronanjunior.todolist.domain.TarefaDomain;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TarefaRepository implements TarefaDomain {
    private String nome;
    private String descricao;
    private Date dtTermino;
    private Integer prioridade;
    private List<CategoriaDomain> categorias;
    private StatusDomain status;

    private DateManipulacaoDomain dateManipulacao;

    public TarefaRepository(String nome, StatusDomain status, DateManipulacaoDomain dateManipulacao) {
        this.nome = nome;
        this.status = status;
        this.descricao = null;
        this.dtTermino = null;
        this.prioridade = 1;
        this.categorias = null;
        this.dateManipulacao = dateManipulacao;
    }

    public TarefaRepository(String nome, StatusDomain status, DateManipulacaoDomain dateManipulacao, Date dtTermino) {
        this.nome = nome;
        this.status = status;
        this.descricao = null;
        this.dtTermino = dtTermino;
        this.prioridade = 1;
        this.categorias = null;
        this.dateManipulacao = dateManipulacao;
    }

    public TarefaRepository(
            String nome,
            String descricao,
            Date dtTermino,
            Integer prioridade,
            StatusDomain status,
            DateManipulacaoDomain dateManipulacao
    ) {
        this.nome = nome;
        this.descricao = descricao;
        this.dtTermino = dtTermino;
        this.prioridade = prioridade;
        this.categorias = null;
        this.status = status;
        this.dateManipulacao = dateManipulacao;
    }

    public String getNome() {
        return nome;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TarefaRepository that = (TarefaRepository) o;
        return Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

    @Override
    public String toString() {
        return "TarefaRepository{" +
                "nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dtTermino=" + dateManipulacao.converterDateParaString(dtTermino, "dd/MM/yyyy") +
                ", prioridade=" + prioridade +
                ", categorias=" + categorias +
                ", status=" + status +
                '}';
    }
}
