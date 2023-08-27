package br.com.ronanjunior.todolist.repository;

import br.com.ronanjunior.todolist.domain.CategoriaDomain;
import br.com.ronanjunior.todolist.domain.DateManipulacaoDomain;
import br.com.ronanjunior.todolist.domain.TarefaDomain;

import java.text.ParseException;
import java.util.*;

public class TarefaRepository implements TarefaDomain {
    private String nome;
    private String descricao;
    private Date dtTermino;
    private Integer prioridade;
    private List<CategoriaDomain> categorias;
    private String status;

    private DateManipulacaoDomain dateManipulacao;

    public TarefaRepository(List<String> sTarefa, DateManipulacaoDomain dateManipulacao) {
        this.nome = sTarefa.get(0);

        if (sTarefa.get(1).equals("null")) {
            this.descricao = null;
        } else {
            this.descricao = sTarefa.get(1);
        }

        try {
            this.dtTermino = dateManipulacao.converterStringParaDate(sTarefa.get(2), "dd/MM/yyyy");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        this.prioridade = Integer.parseInt(sTarefa.get(3));

        if (sTarefa.get(4).equals("null")) {
            this.categorias = new ArrayList<>();
        } else {
            this.categorias = new ArrayList<>();
            String[] sCategorias = sTarefa.get(4).split("-");
            for (String sCategoria : sCategorias) {
                this.categorias.add(new CategoriasRepository(sCategoria));
            }
        }

        this.status = sTarefa.get(5);

        this.dateManipulacao = dateManipulacao;
    }

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

    public List<String> converterTarefaParaListaString() {
        List<String> sTarefas = new ArrayList<>();
        sTarefas.add(this.nome);
        sTarefas.add(this.descricao);
        sTarefas.add(dateManipulacao.converterDateParaString(this.dtTermino, "dd/MM/yyyy"));
        sTarefas.add(this.prioridade.toString());

        int numeroCategorias = categorias.size();
        if (numeroCategorias > 0) {
            String sCategorias = "";
            for (int i = 0; i < numeroCategorias; i++) {
                sCategorias = sCategorias.concat(categorias.get(i).getNome());
                if (i < (numeroCategorias-1)) {
                    sCategorias = sCategorias.concat("-");
                }
            }
            sTarefas.add(sCategorias);
        } else {
            sTarefas.add(null);
        }
        sTarefas.add(this.status);

        return sTarefas;
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
        return "nome=" + nome +
                ", descricao=" + descricao +
                ", dtTermino=" + dateManipulacao.converterDateParaString(dtTermino, "dd/MM/yyyy") +
                ", prioridade=" + prioridade +
                ", categorias=" + categorias +
                ", status=" + status;
    }
}
