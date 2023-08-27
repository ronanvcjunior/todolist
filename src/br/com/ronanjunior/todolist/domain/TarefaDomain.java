package br.com.ronanjunior.todolist.domain;

import java.util.Date;
import java.util.List;

public interface TarefaDomain {
    void adicionarCategoria(CategoriaDomain categoria);
    List<String> converterTarefaParaListaString();
    void setNome(String nome);
    String getNome();
    String getDescricao();
    void setDescricao(String descricao);
    Date getDtTermino();
    void setDtTermino(Date dtTermino);
    Integer getPrioridade();
    void setPrioridade(Integer prioridade);
    List<CategoriaDomain> getCategorias();
    void setCategorias(List<CategoriaDomain> categorias);
    String getStatus();
    void setStatus(String status);
}
