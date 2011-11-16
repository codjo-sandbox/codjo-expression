package net.codjo.expression;
import java.math.BigInteger;
import java.util.Map;
import javax.swing.text.JTextComponent;
/**
 * Classe contenant les m�thodes pour v�rifier la syntaxe d'une expression situ�e dans un JTextArea.
 *
 * @author LEVEQUT
 */
public final class SyntaxControl {
    private static final String EXCLAMATION = "!";
    private static final String EQUAL = "=";
    private static final String GREATER = ">";
    private static final String LESS = "<";


    private SyntaxControl() {
    }


    /**
     * Contr�le la coh�rence des parenth�ses (m�me nombre de parenth�ses ouvrantes et fermantes).
     *
     * @param source Le texte de l'expression
     *
     * @return La diff�rence entre le nombre de parenth�ses ouvrantes et fermantes
     */
    public static int checkComas(String source) {
        int nbOpenComas = findNumberOfOccurence(source, "(");
        int nbCloseComas = findNumberOfOccurence(source, ")");
        return nbOpenComas - nbCloseComas;
    }


    /**
     * Contr�le la coh�rence des doubles quotes (nombre pair).
     *
     * @param source Le texte de l'expression
     *
     * @return Vrai si nb pair, faux sinon
     */
    public static boolean checkQuotes(String source) {
        boolean isCorrect = true;
        BigInteger nb =
              new BigInteger(Integer.toString(findNumberOfOccurence(source, "\"")));
        if ((nb.mod(new BigInteger("2"))).compareTo(new BigInteger("0")) != 0) {
            isCorrect = false;
        }
        return isCorrect;
    }


    /**
     * Contr�le la coh�rence des op�rateurs d'�galit� "==" (pas de caract�re "=" tout seul).
     *
     * @param source Le texte de l'expression
     *
     * @return Le nombre de caract�re "=" tout seul
     */
    public static int checkEqualityOpers(String source) {
        int idx = source.indexOf(EQUAL);
        int nb = 0;
        while (idx >= 0) {
            // le dernier "=" est en bout de chaine
            if (idx == source.length() - 1) {
                nb++;
                break;
            }

            // le premier "=" est en d�but de chaine
            else if (idx == 0 && !source.substring(idx + 1, idx + 2).equals(EQUAL)) {
                nb++;
            }
            else if (!source.substring(idx + 1, idx + 2).equals(EQUAL)
                     && (!source.substring(idx - 1, idx).equals(EXCLAMATION)
                         && !source.substring(idx - 1, idx).equals(GREATER)
                         && !source.substring(idx - 1, idx).equals(LESS))) {
                nb++;
            }
            idx = source.indexOf(EQUAL, idx + 2);
        }
        return nb;
    }


    /**
     * Contr�le la coh�rence des op�rateurs d'in�galit� "!=" (pas de caract�re "!" tout seul).
     *
     * @param source Le texte de l'expression
     *
     * @return Le nombre de caract�re "!" tout seul
     */
    public static int checkNotEqualityOpers(String source) {
        int idx = source.indexOf(EXCLAMATION);
        int nb = 0;
        while (idx >= 0) {
            // le dernier "!" est en bout de chaine
            if (idx == source.length() - 1) {
                nb++;
                break;
            }
            else if (!source.substring(idx + 1, idx + 2).equals(EQUAL)) {
                nb++;
            }
            idx = source.indexOf(EXCLAMATION, idx + 2);
        }
        return nb;
    }


    public static int checkLogicalOpers(String source, String oper) {
        String singleOper = oper.substring(0, 1);
        int idx = source.indexOf(singleOper);
        int nb = 0;
        while (idx >= 0) {
            // le dernier "singleOper" est en bout de chaine
            if (idx == source.length() - 1) {
                nb++;
                break;
            }
            else if (!source.substring(idx + 1, idx + 2).equals(singleOper)) {
                nb++;
            }
            idx = source.indexOf(singleOper, idx + 2);
        }
        return nb;
    }


    /**
     * Contr�le que la formula est correcte.
     *
     * @param source Le texte de l'expression
     *
     * @return true si la formule est correcte, false autrement
     */
    public static boolean isCorrectFormula(String source) {
        int result = checkComas(source);
        result += checkEqualityOpers(source);
        result += checkNotEqualityOpers(source);
        result += checkLogicalOpers(source, "||");
        result += checkLogicalOpers(source, "&&");
        result += !checkQuotes(source) ? 1 : 0;
        return result == 0;
    }


    /**
     * Trouve le nombre d'occurences d'une chaine dans une autre.
     *
     * @param source    La chaine source
     * @param strToFind La chaine � trouver
     *
     * @return Le nombre d'occurences
     */
    private static int findNumberOfOccurence(String source, String strToFind) {
        int idx = source.indexOf(strToFind);
        int nb = 0;
        while (idx >= 0) {
            nb++;
            idx = source.indexOf(strToFind, idx + 1);
        }
        return nb;
    }


    /**
     * Lance la visualisation des op�rateurs d'�galit� incoh�rents.
     *
     * @param expressionTextArea Le JTextArea de l'expression � �tudier
     * @param lastEqualityIdx    L'index o� il faut commencer la recherche
     *
     * @return L'index o� il faudra commencer la prochaine recherche
     */
    public static int viewEqualityOperError(JTextComponent expressionTextArea,
                                            int lastEqualityIdx) {
        expressionTextArea.requestFocus();
        String source = expressionTextArea.getText();
        int idx = source.indexOf(EQUAL, lastEqualityIdx);
        while (idx >= 0) {
            // le dernier "=" est en bout de chaine
            if (idx == source.length() - 1) {
                expressionTextArea.select(idx, idx + 1);
                lastEqualityIdx = 0;
                break;
            }

            // le premier "=" est en d�but de chaine
            else if (idx == 0 && !source.substring(idx + 1, idx + 2).equals(EQUAL)) {
                expressionTextArea.select(idx, idx + 1);
                lastEqualityIdx = findLastIndex(source, EQUAL, idx);
                break;
            }
            else if (!source.substring(idx + 1, idx + 2).equals(EQUAL)
                     && !source.substring(idx - 1, idx).equals(EXCLAMATION)) {
                expressionTextArea.select(idx, idx + 1);
                lastEqualityIdx = findLastIndex(source, EQUAL, idx);
                break;
            }
            idx = determineNextIndex(source, EQUAL, idx);
        }
        return lastEqualityIdx;
    }


    /**
     * Lance la visualisation des op�rateurs d'in�galit� incoh�rents.
     *
     * @param expressionTextArea Le JTextArea de l'expression � �tudier
     * @param lastNotEqualityIdx L'index o� il faut commencer la recherche
     *
     * @return L'index o� il faudra commencer la prochaine recherche
     */
    public static int viewNotEqualityOperError(JTextComponent expressionTextArea,
                                               int lastNotEqualityIdx) {
        expressionTextArea.requestFocus();
        String source = expressionTextArea.getText();
        int idx = source.indexOf(EXCLAMATION, lastNotEqualityIdx);
        while (idx >= 0) {
            // le dernier "!" est en bout de chaine
            if (idx == source.length() - 1) {
                expressionTextArea.select(idx, idx + 1);
                lastNotEqualityIdx = 0;
                break;
            }
            else if (!source.substring(idx + 1, idx + 2).equals(EQUAL)) {
                expressionTextArea.select(idx, idx + 1);
                lastNotEqualityIdx = findLastIndex(source, EXCLAMATION, idx);
                break;
            }
            idx = determineNextIndex(source, EXCLAMATION, idx);
        }
        return lastNotEqualityIdx;
    }


    /**
     * Permet de visualiser les op�rateurs logiques incoh�rents.
     *
     * @param expressionTextArea Le JTextArea de l'expression � �tudier
     * @param oper               L'op�rateur logique ("&&" ou "||")
     * @param lastLogicalIdx     La Map contenant l'index o� il faut commencer la recherche
     *
     * @return La Map contenant l'index o� il faudra commencer la prochaine recherche
     */
    public static Map viewLogicalOperError(JTextComponent expressionTextArea, String oper,
                                           Map lastLogicalIdx) {
        expressionTextArea.requestFocus();
        String source = expressionTextArea.getText();
        String singleOper = oper.substring(0, 1);
        int idx =
              source.indexOf(singleOper, (Integer)lastLogicalIdx.get(oper));
        while (idx >= 0) {
            // le dernier "singleOper" est en bout de chaine
            if (idx == source.length() - 1) {
                expressionTextArea.select(idx, idx + 1);
                lastLogicalIdx.put(oper, 0);
                break;
            }
            else if (!source.substring(idx + 1, idx + 2).equals(singleOper)) {
                expressionTextArea.select(idx, idx + 1);
                lastLogicalIdx.put(oper, findLastIndex(source, singleOper, idx));
                break;
            }
            idx = determineNextIndex(source, singleOper, idx);
        }
        return lastLogicalIdx;
    }


    /**
     * Lance la visualisation des op�rateurs d'in�galit� incoh�rents.
     *
     * @param expressionTextArea Le JTextArea de l'expression � �tudier
     * @param lastQuoteIdx       index de la derni�re virgule
     *
     * @return L'index o� il faudra commencer la prochaine recherche
     */
    public static int viewQuoteError(JTextComponent expressionTextArea, int lastQuoteIdx) {
        expressionTextArea.requestFocus();
        String source = expressionTextArea.getText();
        int idx = source.indexOf("\"", lastQuoteIdx);
        if (idx >= 0) {
            expressionTextArea.select(idx, idx + 1);
            lastQuoteIdx = findLastIndex(source, "\"", idx);
        }
        return lastQuoteIdx;
    }


    /**
     * S�lectionne la partie de texte comprise entre deux caract�res.
     *
     * @param expressionTextArea Le JTextArea de l'expression � �tudier
     * @param charFound          Le premier caract�re
     * @param charToFind         Le deuxi�me caract�re
     * @param idx                La position du 1er caract�re + 1 (position du curseur)
     * @param asc                Le sens de la recherche (vrai si ascendante, faux sinon)
     */
    public static void selectText(JTextComponent expressionTextArea, String charFound,
                                  String charToFind, int idx, boolean asc) {
        expressionTextArea.requestFocus();

        if (asc) {
            int end =
                  findEndOfSelection(expressionTextArea.getText(), charFound, charToFind,
                                     idx);
            expressionTextArea.select(idx, end);
        }
        else {
            int start =
                  findStartOfSelection(expressionTextArea.getText(), charFound, charToFind,
                                       idx);
            expressionTextArea.select(start, idx - 1);
        }
    }


    /**
     * Trouve le d�but de la zone de texte � s�lectionner.
     *
     * @param source     Le texte de l'expression
     * @param charFound  Le premier caract�re
     * @param charToFind Le deuxi�me caract�re
     * @param idx        La position du 1er caract�re + 1 (position du curseur)
     *
     * @return L'index du d�but de la zone de texte � s�lectionner
     */
    private static int findStartOfSelection(String source, String charFound,
                                            String charToFind, int idx) {
        int startIdx = 0;
        int charToFindIdx = 0;
        int charFoundIdx = 0;
        while (idx > 1) {
            if (source.substring(idx - 2, idx - 1).equals(charFound)) {
                charFoundIdx++;
            }
            else if (source.substring(idx - 2, idx - 1).equals(charToFind)) {
                charToFindIdx++;
                if (charToFindIdx > charFoundIdx) {
                    startIdx = idx - 1;
                    break;
                }
                else {
                    startIdx = idx - 1;
                }
            }
            idx--;
        }
        return startIdx;
    }


    /**
     * Trouve la fin de la zone de texte � s�lectionner.
     *
     * @param source     Le texte de l'expression
     * @param charFound  Le premier caract�re
     * @param charToFind Le deuxi�me caract�re
     * @param idx        La position du 1er caract�re + 1 (position du curseur)
     *
     * @return L'index de la fin de la zone de texte � s�lectionner
     */
    private static int findEndOfSelection(String source, String charFound,
                                          String charToFind, int idx) {
        int endIdx = 0;
        int charToFindIdx = 0;
        int charFoundIdx = 0;
        while (idx <= source.length() - 1) {
            if (source.substring(idx, idx + 1).equals(charFound)) {
                charFoundIdx++;
            }
            else if (source.substring(idx, idx + 1).equals(charToFind)) {
                charToFindIdx++;
                if (charToFindIdx > charFoundIdx) {
                    endIdx = idx;
                    break;
                }
                else {
                    endIdx = idx;
                }
            }
            idx++;
        }
        return endIdx;
    }


    /**
     * Recherche l'index o� il faudra commencer la prochaine recherche pour la visualisation des erreurs.
     *
     * @param source    Le texte de l'expression
     * @param strToFind La chaine � trouver
     * @param idx       L'index courant
     *
     * @return L'index o� il faudra commencer la prochaine recherche
     */
    private static int findLastIndex(String source, String strToFind, int idx) {
        int lastIndex = 0;
        if (source.indexOf(strToFind, idx + 1) >= 0) {
            lastIndex = idx + 1;
        }
        return lastIndex;
    }


    /**
     * D�termine le prochain index pour la boucle de visualisation des erreurs.
     *
     * @param source    Le texte de l'expression
     * @param strToFind La chaine � trouver
     * @param idx       L'index courant
     *
     * @return Le prochain index pour la boucle de visualisation des erreurs
     */
    private static int determineNextIndex(String source, String strToFind, int idx) {
        int nextIndex = source.indexOf(strToFind, 0);
        if (source.indexOf(strToFind, idx + 2) >= 0) {
            nextIndex = source.indexOf(strToFind, idx + 2);
        }
        return nextIndex;
    }
}
