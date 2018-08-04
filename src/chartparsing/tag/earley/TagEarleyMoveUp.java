package chartparsing.tag.earley;

import java.util.List;

import chartparsing.AbstractDynamicDeductionRule;
import chartparsing.DeductionItem;
import chartparsing.Item;
import common.tag.Tag;

/** If a node has no right sibling, move to the parent. */
public class TagEarleyMoveUp extends AbstractDynamicDeductionRule {

  private final Tag tag;

  /**
   * Constructor needs the grammar to retrieve information about the
   * antecedence.
   */
  public TagEarleyMoveUp(Tag tag) {
    this.tag = tag;
    this.name = "move up";
    this.antNeeded = 1;
  }

  @Override public List<Item> getConsequences() {
    if (antecedences.size() == antNeeded) {
      String[] itemForm = antecedences.get(0).getItemform();
      String treeName = itemForm[0];
      String node = itemForm[1];
      String pos = itemForm[2];
      String i = itemForm[3];
      String j = itemForm[4];
      String k = itemForm[5];
      String l = itemForm[6];
      String adj = itemForm[7];
      String siblingGorn = tag.getTree(treeName).getNodeByGornAdress(node)
        .getGornAddressOfPotentialRightSibling();
      if (!node.equals("") && pos.equals("ra") && adj.equals("0")
        && tag.getTree(treeName).getNodeByGornAdress(siblingGorn) == null) {
        String parentGorn = tag.getTree(treeName).getNodeByGornAdress(node)
          .getGornAddressOfParent();
        Item consequence =
          new DeductionItem(treeName, parentGorn, "rb", i, j, k, l, "0");
        consequence.setTree(antecedences.get(0).getTree());
        consequences.add(consequence);
      }
    }
    return consequences;
  }

  @Override public String toString() {
    return "[ɣ,p.m,ra,i,j,k,l,0]" + "\n______ ɣ(p.m+1) is not defined\n"
      + "[ɣ,p,rb,i,j,k,l,0]";
  }

}
