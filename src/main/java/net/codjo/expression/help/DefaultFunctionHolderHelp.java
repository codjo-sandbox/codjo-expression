/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.expression.help;
import net.codjo.expression.FunctionHolder;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Aide par d�faut sur un {@link net.codjo.expression.FunctionHolder} standard.
 * 
 * <p>
 * Le message d'aide associ� � une fonction  est construit de mani�re automatique (e.g.
 * <code>Usage : utils.bar(cha�ne, entier)</code>).
 * </p>
 *
 * @see FunctionHolderHelp
 * @see UserFunctionHolderHelp
 */
public class DefaultFunctionHolderHelp implements FunctionHolderHelp {
    private List<FunctionHelp> functionList;

    /**
     * Constructeur qui permet de cr�er la liste de function � partir du functionHolder
     * transmis
     *
     * @param functionHolder le FunctionHolder
     */
    public DefaultFunctionHolderHelp(FunctionHolder functionHolder) {
        this.functionList = new ArrayList<FunctionHelp>();

        Method[] methods = functionHolder.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (Modifier.isPublic(method.getModifiers()) && notDefinedInInterface(method)) {
                String name = functionHolder.getName() + "." + method.getName();
                functionList.add(new FunctionHelp(name,
                                                  method.getParameterTypes().length,
                                                  "Usage : " + name + "(" + parameterToString(method) + ")"));
            }
        }
    }

    /**
     * Retourne la liste des param�tres de la m�thode sous forme de chaine de caract�re.
     *
     * @param method la m�thode.
     *
     * @return la liste des param�tres
     */
    private String parameterToString(Method method) {
        StringBuffer parameters = new StringBuffer();
        for (int j = 0; j < method.getParameterTypes().length; j++) {
            if (j > 0) {
                parameters.append(", ");
            }
            parameters.append(translate(method.getParameterTypes()[j]));
        }
        return parameters.toString();
    }


    /**
     * Traduit une classe pour le rendre lisible par un utilisateur.
     *
     * @param clazz la class � traduire
     *
     * @return une chaine de caract�re correspondant � la classe
     */
    private String translate(Class clazz) {
        if (clazz == String.class) {
            return "cha�ne";
        }
        else if (clazz == BigDecimal.class) {
            return "nombre";
        }
        else if (Date.class.isAssignableFrom(clazz)) {
            return "date";
        }
        else if (clazz == int.class) {
            return "entier";
        }
        else if (clazz == boolean.class) {
            return "bool�en";
        }
        return "variable";
    }


    /**
     * Indique si la m�thode fait partie de l'interface FunctionHolder.
     *
     * @param method
     *
     * @return retourne true si la m�thode ne fait pas partie des m�thodes implement�es
     */
    private boolean notDefinedInInterface(Method method) {
        return !"getName".equals(method.getName())
        && !"getAllFunctions".equals(method.getName());
    }


    public List<FunctionHelp> getFunctionHelpList() {
        return functionList;
    }
}
