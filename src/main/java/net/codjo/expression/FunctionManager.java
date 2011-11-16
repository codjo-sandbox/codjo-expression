/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.expression;
import net.codjo.expression.help.FunctionHelp;
import net.codjo.expression.help.FunctionHolderHelp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Classe Gestionnaire des fonctions utilisable dans les expressions du traitement.
 *
 * @author $Author: gonnot $
 * @version $Revision: 1.15 $
 */
public class FunctionManager implements Cloneable {
    private Map<String, FunctionHolderInfo> functionHolderInfo = new HashMap<String, FunctionHolderInfo>();


    /**
     * Constructor for the FunctionManager object
     */
    public FunctionManager() {
        addFunctionHolder(new KernelFunctionHolder());
    }


    /**
     * Retourne une Map des objets contenant des fonctions utilise dans les expressions.
     *
     * @return Map : key = nom de l'objet, value = l'instance.
     */
    public Map<String, FunctionHolderInfo> getFunctionHolderInfo() {
        return functionHolderInfo;
    }


    /**
     * Retourne la liste des fonctions utilisable dans les expressions.
     *
     * @return Liste de String
     *
     * @deprecated utiliser la version {@link #getAllFunctionsHelp()}
     */
    @Deprecated
    public List<String> getAllFunctions() {
        List<String> allFunction = new ArrayList<String>();
        allFunction
              .add("Description : renvoie la valeur absolue du nombre pass� en param�tre\nUsage : Math.abs(colonne)\nN.B. ne s'applique qu'aux colonnes num�riques\nExemple : Math.abs(Montant)");
        allFunction
              .add("Description : renvoie la plus �lev�e des deux donn�es pass�es en param�tres\nUsage : Math.max(donn�e1, donn�e2)\nExemple : Math.max(Cours d'ouverture, Cours de cl�ture)");
        allFunction
              .add("Description : renvoie la plus faible des deux donn�es pass�es en param�tres\nUsage : Math.min(donn�e1, donn�e2)\nExemple : Math.min(Cours d'ouverture, Cours de cl�ture)");
        allFunction
              .add("Description : renvoie vrai si la colonne pass�e en premier param�tre contient une des valeurs pass�es dans les param�tres suivants\nUsage : in(colonne, valeur1, valeur2, ...)\nExemple : in(Pays, \"FRA\", \"DEU\", \"USA\", \"JPN\")");
        allFunction
              .add("Description : renvoie vrai si la colonne pass�e en premier param�tre ne contient aucune des valeurs pass�es dans les param�tres suivants\nUsage : notIn(colonne, valeur1, valeur2, ...)\nExemple : notIn(Pays, \"FRA\", \"DEU\", \"USA\", \"JPN\")");
        allFunction
              .add("Description : renvoie Valeur si vraie si Condition est remplie et Valeur si fausse dans le cas contraire\nUsage : iif(Condition, Valeur si vraie, Valeur si fausse)\nExemple : iif(in(V�hicule, \"Camion\", \"Voiture\", \"Moto\"), \"V�hicule motoris�\", \"V�hicule non motoris�\")");

        for (FunctionHolderInfo fhi : functionHolderInfo.values()) {
            //noinspection deprecation
            allFunction.addAll(fhi.getFunctionHolder().getAllFunctions());
        }

        return allFunction;
    }


    /**
     * Retourne la liste des noms des fonctions utilisables dans les expressions.
     *
     * @return
     */
    public List<String> getAllFunctionsName() {
        List<String> allFunction = new ArrayList<String>();
        allFunction.add("Math.abs(valeur)");
        allFunction.add("Math.max(a, b)");
        allFunction.add("Math.min(a, b)");
        allFunction.add("in(field, value1, value2, ...)");
        allFunction.add("notIn(field, value1, value2, ...)");
        allFunction.add("iif(condition, trueValue, falseValue)");

        for (FunctionHolderInfo fhi : functionHolderInfo.values()) {
            if (fhi.getFunctionHolder() instanceof KernelFunctionHolder) {
                allFunction.addAll(((KernelFunctionHolder)fhi.getFunctionHolder()).getAllFunctionsName());
            }
            else {
                //noinspection deprecation
                allFunction.addAll(fhi.getFunctionHolder().getAllFunctions());
            }
        }

        return allFunction;
    }


    /**
     * Retourne une liste de FunctionHelp.
     *
     * @return L'aide sur toutes les fonctions contenue dans les FunctionsHolder.
     */
    public List<FunctionHelp> getAllFunctionsHelp() {
        List<FunctionHelp> functionsHelp = new ArrayList<FunctionHelp>();
        functionsHelp.add(new FunctionHelp("in", -1,
                                           "Description : renvoie vrai si la colonne pass�e en premier param�tre contient une des valeurs pass�es dans les param�tres suivants\nUsage : in(colonne, valeur1, valeur2, ...)\nExemple : in(Pays, \"FRA\", \"DEU\", \"USA\", \"JPN\")"));
        functionsHelp.add(new FunctionHelp("notIn", -1,
                                           "Description : renvoie vrai si la colonne pass�e en premier param�tre ne contient aucune des valeurs pass�es dans les param�tres suivants\nUsage : notIn(colonne, valeur1, valeur2, ...)\nExemple : notIn(Pays, \"FRA\", \"DEU\", \"USA\", \"JPN\")"));
        functionsHelp.add(new FunctionHelp("Math.abs", 1,
                                           "Description : renvoie la valeur absolue du nombre pass� en param�tre\nUsage : Math.abs(colonne)\nN.B. ne s'applique qu'aux colonnes num�riques\nExemple : Math.abs(Montant)"));
        functionsHelp.add(new FunctionHelp("iif", 3,
                                           "Description : renvoie Valeur si vraie si Condition est remplie et Valeur si fausse dans le cas contraire\nUsage : iif(Condition, Valeur si vraie, Valeur si fausse)\nExemple : iif(in(V�hicule, \"Camion\", \"Voiture\", \"Moto\"), \"V�hicule motoris�\", \"V�hicule non motoris�\")"));
        functionsHelp.add(new FunctionHelp("Math.max", 2,
                                           "Description : renvoie la plus �lev�e des deux donn�es pass�es en param�tres\nUsage : Math.max(donn�e1, donn�e2)\nExemple : Math.max(Cours d'ouverture, Cours de cl�ture)"));
        functionsHelp.add(new FunctionHelp("Math.min", 2,
                                           "Description : renvoie la plus faible des deux donn�es pass�es en param�tres\nUsage : Math.min(donn�e1, donn�e2)\nExemple : Math.min(Cours d'ouverture, Cours de cl�ture)"));

        for (FunctionHolderInfo fhi : functionHolderInfo.values()) {
            FunctionHolderHelp help = fhi.getHelp();

            functionsHelp.addAll(help.getFunctionHelpList());
        }

        return functionsHelp;
    }


    /**
     * Ajoute un nouveau porteur de fonction au manager.
     *
     * @param fh Le functionHolderInfo
     */
    public void addFunctionHolder(FunctionHolder fh) {
        if (fh != null) {
            FunctionHolderInfo info;
            if (fh.getClass() == UserFunctionHolder.class) {
                info = new UserFunctionHolderInfo((UserFunctionHolder)fh);
            }
            else {
                info = new DefaultFunctionHolderInfo(fh);
            }

            this.functionHolderInfo.put(fh.getName(), info);
        }
    }


    /**
     * Clone ce FunctionManager : la Map des FunctionHolder est clon�e (mais les clefs et les valeurs de cette
     * Map ne sont pas clon�es).
     *
     * @return le nouvel objet
     *
     * @throws IllegalStateException Clone en echec.
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException error) {
            // Cas impossible
            throw new IllegalStateException(error.getMessage());
        }
    }
}
