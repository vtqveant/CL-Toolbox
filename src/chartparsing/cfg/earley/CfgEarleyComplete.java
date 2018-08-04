package chartparsing.cfg.earley;

import chartparsing.AbstractDynamicDecutionRuleTwoAntecedences;
import chartparsing.DeductionItem;
import chartparsing.Item;
import common.ArrayUtils;

/**
 * If in one item a dot is before a nonterminal and the other item is a rule
 * with that nonterminal as lhs and the dot at the end, move the dot over the
 * nonterminal.
 */
public class CfgEarleyComplete
  extends AbstractDynamicDecutionRuleTwoAntecedences {

  public CfgEarleyComplete() {
    this.name = "complete";
    this.antNeeded = 2;
  }

  protected void calculateConsequences(String[] itemForm1, String[] itemForm2) {
    String stack1 = itemForm1[0];
    String[] stackSplit1 = stack1.split(" ");
    int i1 = Integer.parseInt(itemForm1[1]);
    int j1 = Integer.parseInt(itemForm1[2]);
    String stack2 = itemForm2[0];
    String[] stackSplit2 = stack2.split(" ");
    int j2 = Integer.parseInt(itemForm2[1]);
    int k2 = Integer.parseInt(itemForm2[2]);

    if (j1 == j2 && stack2.endsWith("•")) {
      for (int l = 0; l < stackSplit1.length; l++) {
        if (stackSplit1[l].startsWith("•") && stackSplit1[l]
          .substring(1, stackSplit1[l].length()).equals(stackSplit2[0])) {
          String newStack;
          if (l == stackSplit1.length - 1) {
            newStack = ArrayUtils.getSubSequenceAsString(stackSplit1, 0, l)
              + " " + stackSplit2[0] + " •";
          } else {
            newStack = ArrayUtils.getSubSequenceAsString(stackSplit1, 0, l)
              + " " + stackSplit2[0] + " •" + ArrayUtils
                .getSubSequenceAsString(stackSplit1, l + 1, stackSplit1.length);
          }
          Item consequence =
            new DeductionItem(newStack, String.valueOf(i1), String.valueOf(k2));
          consequence.setTree(antecedences.get(0).getTree());
          consequences.add(consequence);
          break;
        }
      }
    }
  }

  @Override public String toString() {
    return "[A -> α •B β,i,j] [B -> ɣ •,j,k]" + "\n______\n"
      + "[A -> α B •β,i,k]";
  }

}
