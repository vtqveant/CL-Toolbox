package chartparsing.tag.earleyprefixvalid;

import java.util.List;

import chartparsing.AbstractDynamicDeductionRule;
import chartparsing.DeductionItem;
import chartparsing.Item;
import common.tag.Tag;

public class TagEarleyPrefixValidScanEps extends AbstractDynamicDeductionRule {

  private final Tag tag;

  public TagEarleyPrefixValidScanEps(Tag tag) {
    this.tag = tag;
    this.name = "scan epsilon";
    this.antNeeded = 1;
  }

  @Override public List<Item> getConsequences() {
    if (antecedences.size() == antNeeded) {
      String[] itemForm = antecedences.get(0).getItemform();
      String treeName = itemForm[0];
      String node = itemForm[1];
      String pos = itemForm[2];
      String iGamma = itemForm[3];
      String i = itemForm[4];
      String j = itemForm[5];
      String k = itemForm[6];
      String l = itemForm[7];
      String adj = itemForm[8];
      if (pos.equals("la") && adj.equals("0") && tag.getTree(treeName)
        .getNodeByGornAdress(node).getLabel().equals("")) {
        Item consequence =
          new DeductionItem(treeName, node, "ra", iGamma, i, j, k, l, "0");
        consequence.setTree(antecedences.get(0).getTree());
        consequences.add(consequence);
      }
    }
    return consequences;
  }

  @Override public String toString() {
    return "[ɣ,p,la,i_ɣ,i,j,k,l,0]" + "\n______ l(ɣ,p) = ε\n"
      + "[ɣ,p,ra,i_ɣ,i,j,k,l,0]";
  }

}
