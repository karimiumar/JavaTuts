package com.umar.apps.hibernate.sp.psql;

import com.umar.apps.hibernate.lazy.PostDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.*;

@NamedStoredProcedureQueries(
        value = {
                @NamedStoredProcedureQuery(
                        name = "calculate",
                        procedureName = "calculate",
                        parameters = {
                                @StoredProcedureParameter(mode = ParameterMode.IN, type = Double.class, name = "x"),
                                @StoredProcedureParameter(mode = ParameterMode.IN, type = Double.class, name = "y"),
                                @StoredProcedureParameter(mode = ParameterMode.OUT, type = Double.class, name = "sum")
                        }
                )
    }
)
public class StoredProcedureTest {
    @Test
    public void given_stored_procedure_calulate_when_called_with_params_10_and_20_then_returns_30(){
        PostDao postDao = new PostDao();
        StoredProcedureQuery query = postDao.getEMF().createEntityManager().createStoredProcedureQuery("calculate");
        query.registerStoredProcedureParameter("x", Double.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("y", Double.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("sum", Double.class, ParameterMode.OUT);
        query.setParameter("x", 10.0d);
        query.setParameter("y", 20.0d);
        query.execute();
        Double sum = (Double) query.getOutputParameterValue("sum");
        Assertions.assertEquals(30.0d, sum);
    }
}
