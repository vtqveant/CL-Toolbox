package chartparsing.tagrules;

import java.util.List;

import chartparsing.AbstractDynamicDeductionRule;
import common.Item;
import common.tag.Tag;
import common.tag.TagCykItem;

/** Tries to substitute a given initial tree into the node of the tree it
 * remembers. */
public class TagCykSubstitute extends AbstractDynamicDeductionRule {
  
  private Tag tag;
  private String nodegorn;
  private String treename;

  /** Remembers tree and node it can substitute in. */
  public TagCykSubstitute(String treename, String nodegorn, Tag tag) {
    this.tag = tag;
    this.treename = treename;
    this.nodegorn = nodegorn;
    this.name = "substitute in " + treename + "(" + nodegorn + ")";
    this.antneeded = 1;
  }

  @Override public List<Item> getConsequences() {
    if (antecedences.size() == antneeded) {
      String[] itemform = antecedences.get(0).getItemform();
      String treename = itemform[0];
      String node = itemform[1];
      int i = Integer.parseInt(itemform[2]);
      int j = Integer.parseInt(itemform[5]);
      if (tag.getInitialTree(treename) != null && node.equals("⊤")) {
        consequences.add(
          new TagCykItem(this.treename, this.nodegorn + "⊤", i, null, null, j));
      }
    }
    return consequences;
  }

  @Override public String toString() {
    StringBuilder representation = new StringBuilder();
    representation.append("[α,ε⊤,i,-,-,j]");
    representation.append("\n______ l(α,ε) = l(" + treename + "," + nodegorn
      + "), " + treename + "(" + nodegorn + ") a substitution node\n");
    representation.append("[" + treename + "," + nodegorn + "⊤,i,-,-,j]");
    return representation.toString();
  }

}
