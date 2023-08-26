package br.com.ronanjunior.todolist.repository;

import br.com.ronanjunior.todolist.domain.CategoriaDomain;
import br.com.ronanjunior.todolist.domain.DateManipulacaoDomain;
import br.com.ronanjunior.todolist.domain.TarefaDomain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TarefaRepository implements TarefaDomain {
    private String nome;
    private String descricao;
    private Date dtTermino;
    private Integer prioridade;
    private List<CategoriaDomain> categorias;
    private String status;

    private DateManipulacaoDomain dateManipulacao;

    public TarefaRepository(DateManipulacaoDomain dateManipulacao) {
        this.nome = null;
        this.status = null;
        this.descricao = null;
        this.dtTermino = null;
        this.prioridade = null;
        this.categorias = new ArrayList<>();
        this.dateManipulacao = dateManipulacao;
    }

    public TarefaRepository(String nome, String status, DateManipulacaoDomain dateManipulacao) {
        this.nome = nome;
        this.status = status;
        this.descricao = null;
        this.dtTermino = null;
        this.prioridade = 1;
        this.categorias = new ArrayList<>();
        this.dateManipulacao = dateManipulacao;
    }

    public TarefaRepository(String nome, Date dtTermino, DateManipulacaoDomain dateManipulacao) {
        this.nome = nome;
        this.status = "ToDo";
        this.descricao = null;
        this.dtTermino = dtTermino;
        this.prioridade = 1;
        this.categorias = new ArrayList<>();
        this.dateManipulacao = dateManipulacao;
    }

    public TarefaRepository(String nome, String status, DateManipulacaoDomain dateManipulacao, Date dtTermino) {
        this.nome = nome;
        this.status = status;
        this.descricao = null;
        this.dtTermino = dtTermino;
        this.prioridade = 1;
        this.categorias = new ArrayList<>();
        this.dateManipulacao = dateManipulacao;
    }

    public TarefaRepository(
            String nome,
            String descricao,
            Date dtTermino,
            Integer prioridade,
            String status,
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

    public void adicionarCategoria(CategoriaDomain categoria) {
        categorias.add(categoria);
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
