package br.com.ronanjunior.todolist.domain;

import java.text.ParseException;
import java.util.Date;

public interface DateManipulacaoDomain {
    Date converterStringParaDate(String dtString, String formato) throws ParseException;
    String converterDateParaString(Date date, String formato);
}
