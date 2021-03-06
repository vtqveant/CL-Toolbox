package com.github.samyadaleh.cltoolbox.chartparsing.cfg.topdown;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.github.samyadaleh.cltoolbox.chartparsing.dynamicdeductionrule.AbstractDynamicDeductionRule;
import com.github.samyadaleh.cltoolbox.chartparsing.item.DeductionChartItem;
import com.github.samyadaleh.cltoolbox.chartparsing.item.ChartItemInterface;
import com.github.samyadaleh.cltoolbox.common.ArrayUtils;
import com.github.samyadaleh.cltoolbox.common.TreeUtils;
import com.github.samyadaleh.cltoolbox.common.cfg.CfgProductionRule;
import com.github.samyadaleh.cltoolbox.common.tag.Tree;

/**
 * If a nonterminal is on top of a stack it can be replaced by any rhs where it
 * is the lhs.
 */
public class CfgTopDownPredict extends AbstractDynamicDeductionRule {

  private final CfgProductionRule rule;

  public CfgTopDownPredict(CfgProductionRule rule) {
    this.rule = rule;
    this.name = "predict " + rule.toString();
    this.antNeeded = 1;
  }

  @Override public List<ChartItemInterface> getConsequences()
      throws ParseException {
    if (antecedences.size() == antNeeded) {
      String[] itemForm = antecedences.get(0).getItemForm();
      String stack = itemForm[0];
      String[] stackSplit = stack.split(" ");
      String i = itemForm[1];
      List<Tree> derivedTrees = antecedences.get(0).getTrees();
      if (stackSplit[0].equals(rule.getLhs())) {
        if (stackSplit.length == 1) {
          ChartItemInterface consequence;
          if ("".equals(rule.getRhs()[0])) {
            consequence = new DeductionChartItem("", i);
          } else {
            consequence =
                new DeductionChartItem(String.join(" ", rule.getRhs()), i);
          }
          if (derivedTrees.size() == 0) {
            List<Tree> derivedTreesNew = new ArrayList<>();
            derivedTreesNew.add(new Tree(rule));
            consequence.setTrees(derivedTreesNew);
          } else {
            List<Tree> derivedTreesNew = new ArrayList<>();
            Tree derivedTreeBase = new Tree(rule);
            for (Tree tree : derivedTrees) {
              derivedTreesNew.add(
                  TreeUtils.performLeftmostSubstitution(tree, derivedTreeBase));
            }
            consequence.setTrees(derivedTreesNew);
          }
          logItemGeneration(consequence);
          consequences.add(consequence);
        } else {
          ChartItemInterface consequence;
          if ("".equals(rule.getRhs()[0])) {
            consequence = new DeductionChartItem(ArrayUtils
                .getSubSequenceAsString(stackSplit, 1, stackSplit.length), i);
          } else {
            consequence = new DeductionChartItem(
                String.join(" ", rule.getRhs()) + " " + ArrayUtils
                    .getSubSequenceAsString(stackSplit, 1, stackSplit.length),
                i);
          }
          if (derivedTrees.size() == 0) {
            derivedTrees.add(new Tree(rule));
            consequence.setTrees(derivedTrees);
          } else {
            List<Tree> derivedTreesNew = new ArrayList<>();
            Tree derivedTreeBase = new Tree(rule);
            for (Tree tree : derivedTrees) {
              derivedTreesNew.add(
                  TreeUtils.performLeftmostSubstitution(tree, derivedTreeBase));
            }
            consequence.setTrees(derivedTreesNew);
          }
          logItemGeneration(consequence);
          consequences.add(consequence);
        }
      }
    }
    return consequences;
  }

  @Override public String toString() {
    return "[" + rule.getLhs() + "α,i]" + "\n______ " + rule.toString() + ", |"
        + ArrayUtils.toString(rule.getRhs()) + " α| ≤ n - i\n" + "["
        + ArrayUtils.toString(rule.getRhs()) + " α,i]";
  }

}
