/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.expression;
import net.codjo.expression.help.FunctionHolderHelp;
import java.util.List;
/**
 * Information sur un FunctionHolder particulier.
 *
 * @version $Revision: 1.4 $
 *
 * @see FunctionHolder
 */
interface FunctionHolderInfo {
    /**
     * Retourne le function Holder associ�. � cette classe d'info.
     *
     * @return Un FunctionHolder.
     */
    FunctionHolder getFunctionHolder();


    /**
     * Retourne le type de retour de la m�thode.
     *
     * @param methodName nom de la m�thode.
     *
     * @return un type SQL.
     */
    public Integer getReturnSqlType(String methodName, List argsTypeList);


    public String buildDefinition();


    boolean shouldDefineVariable();


    FunctionHolderHelp getHelp();
}
