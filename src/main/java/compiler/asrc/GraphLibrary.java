package compiler.asrc;


import alice.tuprolog.*;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import utils.GraphUtils;
import utils.StringUtils;

import java.lang.Double;
import java.util.ArrayList;
import java.util.List;

import static compiler.prolog.TuProlog.*;

public abstract class GraphLibrary extends Library {

    public Graph graph;

    public GraphLibrary(Graph g) {
        this.graph = g;
    }

    @Override
    public abstract String getTheory();

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    //supports backtracking but no generation
    public boolean attribute_3(Term ID, Struct attrname, Term value) {
        try {
            String rname = StringUtils.removeQuotation(attrname.getName());
            String rstringvalue = value instanceof Struct ? ((Struct) value).getName() : null;
            int rintvalue = value instanceof Int ? ((Int) value).intValue() : 0;
            Element rID = GraphUtils.getByID(graph, ((Struct)ID.getTerm()).getName());

            if (!rID.hasAttribute(rname)) {
                return false;
            }

            if (value instanceof Struct) {
                return rID.getAttribute(rname).equals(rstringvalue);
            } else if (value instanceof Var) {
                assert value.unify(getEngine(), term(StringUtils.ObjectToString(rID.getAttribute(rname))));
                return true;

            } else if (value instanceof Int) {
                return rID.getNumber(rname) == (double) rintvalue;
            }
            return false;
        }catch (Exception | AssertionError e) {
            return false;
        }
    }

    //does not support backtracking
    boolean numeric(Struct key, Term value, GetNumber actualnumber, boolean nodes, boolean edges, boolean graphs) {
        if (value instanceof Int) {
            return ((Int) value).intValue() == actualnumber.get(GraphUtils.getByID(graph, key.getName()));
        } else if (value instanceof Var) {
            return value.unify(getEngine(), intVal(actualnumber.get(GraphUtils.getByID(graph, key.getName()))));
        }
        return false;
    }

    //does not support backtracking
    boolean bool(Struct key, GetBool actualbool, boolean nodes, boolean edges, boolean graphs) {
        return actualbool.get(GraphUtils.getByID(graph, key.getName()));
    }


    public interface GetNumber {
        int get(Element n);
    }

    public interface GetBool {
        boolean get(Element n);
    }
}