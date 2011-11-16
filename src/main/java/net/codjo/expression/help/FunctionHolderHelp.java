/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.expression.help;
import java.util.List;
/**
 * Aide sur un porteur de fonction.
 *
 * @see net.codjo.expression.FunctionHolder
 */
public interface FunctionHolderHelp {
    /**
     * Retourne la liste des aides sur les fonctions d�finit par le {@link net.codjo.expression.FunctionHolder}
     * associ� a ce FunctionHolderHelp.
     *
     * @return Une liste de {@link FunctionHelp}
     */
    List<FunctionHelp> getFunctionHelpList();
}
