package com.github.samyadaleh.cltoolbox.chartparsing.cfg.cyk;

import com.github.samyadaleh.cltoolbox.chartparsing.item.AbstractChartItem;
import com.github.samyadaleh.cltoolbox.chartparsing.item.ProbabilisticChartItemInterface;
import com.github.samyadaleh.cltoolbox.common.ArrayUtils;

/**
 * Probabilistic item used for probabilistic CYK parsing.
 */
public class PcfgCykItem extends AbstractChartItem implements ProbabilisticChartItemInterface {
  
  private final double p;
  
  /** Constructor takes the weight, a nonterminal, the beginning and end of
   * its span. */
  public PcfgCykItem(double p, String lhs, int i, int j) {
    this.p = p;
    this.itemForm = new String[] {lhs, String.valueOf(i), String.valueOf(j)};
  }

  @Override public String toString() {
    return String.valueOf(p) + " : "
      + ArrayUtils.toString(this.itemForm);
  }

  @Override public Double getProbability() {
    return this.p;
  }

}
