package graafvis;


import alice.tuprolog.Term;
import graafvis.checkers.CheckerResult;
import graafvis.checkers.GraafvisChecker;
import graafvis.errors.VisError;
import graafvis.generator.RuleGenerator;
import graafvis.grammar.GraafvisLexer;
import graafvis.grammar.GraafvisParser;
import graafvis.warnings.Warning;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;

import java.util.ArrayList;
import java.util.List;

/**
 * Turns a string representation of a Graafvis script into a list of tuProlog terms
 */
public class GraafvisCompiler {

    /** Errors encountered during the compilation phases */
    private final List<VisError> errors;
    /** Warnings encountered during the compilation phases */
    private final List<Warning> warnings;

    /** Create a new GraafvisCompiler */
    public GraafvisCompiler() {
        errors = new ArrayList<>();
        warnings = new ArrayList<>();
    }

    /**
     * Generate a list of tuProlog terms from a Graafvis script. Note: This clears the current errors/warnings captured
     * by the compiler
     *
     * @param script            The Graafvis script
     * @return                  A list of tuProlog terms corresponding to the script
     * @throws SyntaxException  A syntax error has occurred
     * @throws CheckerException An error has occurred during the checking phase
     */
    public List<Term> compile(String script) throws SyntaxException, CheckerException {
        errors.clear();
        warnings.clear();
        /* Create a parser */
        Lexer lexer = new GraafvisLexer(new ANTLRInputStream(script));
        TokenStream tokens = new CommonTokenStream(lexer);
        GraafvisParser parser = new GraafvisParser(tokens);
        /* Add error listener so errors are captured */
        ErrorListener errorListener = new ErrorListener();
        lexer.removeErrorListeners();
        parser.removeErrorListeners();
        lexer.addErrorListener(errorListener);
        parser.addErrorListener(errorListener);
        /* Parse the program */
        GraafvisParser.ProgramContext programContext = parser.program();
        /* Check for syntax errors */
        if (errorListener.hasErrors()) {
            /* Can't compile -- add errors to list */
            errors.addAll(errorListener.getErrors());
            throw new SyntaxException();
        }
        /* Parsing successful, continue to checking phase */
        GraafvisChecker checker = new GraafvisChecker();
        CheckerResult checkerResult = checker.check(programContext);
        if (checkerResult.getErrors().size() > 0) {
            /* Checker found errors */
            errors.addAll(checkerResult.getErrors());
            warnings.addAll(checkerResult.getWarnings());
            throw new CheckerException();
        }
        warnings.addAll(checkerResult.getWarnings());
        /* Passed the checking phase, generate program */
        return new RuleGenerator(programContext).getResult();
    }

    /** Get the errors that have been encountered */
    public List<VisError> getErrors() {
        return errors;
    }

    /** Get the warnings that have been encountered */
    public List<Warning> getWarnings() {
        return warnings;
    }

    /** An exception that is thrown when a syntax error has been found */
    public class SyntaxException extends Exception {
        /* One or more syntax errors occurred while parsing */
    }

    /** An exception that is thrown when a checker has found an error */
    public class CheckerException extends Exception {
        /* One or more checker errors occurred */
    }

}
