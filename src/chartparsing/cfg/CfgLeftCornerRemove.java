package chartparsing.cfg;

import java.util.List;

import chartparsing.AbstractDynamicDeductionRule;
import chartparsing.Item;
import common.ArrayUtils;

/** If topmost symbol on stacks completed and predicted are the same, remove
 * both. */
public class CfgLeftCornerRemove extends AbstractDynamicDeductionRule {

  public CfgLeftCornerRemove() {
    this.name = "remove";
    this.antNeeded = 1;
  }

  @Override public List<Item> getConsequences() {
    if (antecedences.size() == antNeeded) {
      String[] itemForm = antecedences.get(0).getItemform();
      String stackCompl = itemForm[0];
      String[] stackComplSplit = stackCompl.split(" ");
      String stackPred = itemForm[1];
      String[] stackPredSplit = stackPred.split(" ");
      String stackLhs = itemForm[2];
      if (stackCompl.length() > 0 && stackPred.length() > 0
        && stackComplSplit[0].equals(stackPredSplit[0])) {
        String newCompl = ArrayUtils.getSubSequenceAsString(stackComplSplit, 1,
          stackComplSplit.length);
        String newPred = ArrayUtils.getSubSequenceAsString(stackPredSplit, 1,
          stackPredSplit.length);
        consequences.add(new CfgDollarItem(newCompl, newPred, stackLhs));
      }
    }
    return consequences;
  }

  @Override public String toString() {
    return "[Xα,Xβ,ɣ]" + "\n______\n" + "[α,β,ɣ]";
  }

}