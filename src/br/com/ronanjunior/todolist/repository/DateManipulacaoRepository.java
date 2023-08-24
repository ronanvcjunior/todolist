package br.com.ronanjunior.todolist.repository;

import br.com.ronanjunior.todolist.domain.DateManipulacaoDomain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateManipulacaoRepository implements DateManipulacaoDomain {
    public Date converterStringParaDate(String dtString, String formato) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
        return dateFormat.parse(dtString);
    }

    public String converterDateParaString(Date date, String formato) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
        return dateFormat.format(date);
    }
}
