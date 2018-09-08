package com.github.samyadaleh.cltoolbox.common.cfg;

import com.github.samyadaleh.cltoolbox.common.parser.GrammarParserUtils;
import com.github.samyadaleh.cltoolbox.common.parser.Token;
import com.github.samyadaleh.cltoolbox.common.parser.TokenReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.samyadaleh.cltoolbox.common.parser.GrammarParserUtils.addSymbolToCategory;

/** Representation of a context free grammar where the rules have
 * probabilities. */
public class Pcfg extends AbstractCfg{
  private final List<PcfgProductionRule> productionRules = new ArrayList<>();

  public Pcfg() {
    super();
  }

  /** Create a PCFG from a CFG where all rules have the same probability. */
  public Pcfg(Cfg cfg) {
    this.nonterminals = cfg.getNonterminals();
    this.terminals = cfg.getTerminals();
    this.startSymbol = cfg.getStartSymbol();
    for (String nt : nonterminals) {
      int ruleCount = 0;
      for (CfgProductionRule rule : cfg.getProductionRules()) {
        if (rule.getLhs().equals(nt)) {
          ruleCount++;
        }
      }
      for (CfgProductionRule rule : cfg.getProductionRules()) {
        if (rule.getLhs().equals(nt)) {
          this.productionRules.add(new PcfgProductionRule(rule.getLhs(),
            rule.getRhs(), 1.0 / ruleCount));
        }
      }
    }
  }

  public Pcfg(BufferedReader in) throws IOException, ParseException {    Character[] specialChars =
      new Character[] {'-', '>', '{', '}', ',', '|', '=', ':'};
    TokenReader reader = new TokenReader(in, specialChars);
    Set<String> validCategories = new HashSet<>();
    validCategories.add("N");
    validCategories.add("T");
    validCategories.add("S");
    validCategories.add("P");
    validCategories.add("G");
    List<String> category = new ArrayList<>();
    int lineNumber = 0;
    String prob = null;
    String lhs = null;
    StringBuilder rhs = null;
    List<String> symbols = new ArrayList<>();
    Token token;
    while ((token = reader.getNextToken()) != null) {
      String tokenString = token.getString();
      switch (category.size()) {
      case 0:
        GrammarParserUtils
            .handleMainCategory(this, validCategories, category,
                token);
        break;
      case 1:
        addSymbolToCategory(category, token, "=");
        break;
      case 2:
        category = GrammarParserUtils
            .addStartsymbolOrAddCategory(this, category, token);
        break;
      case 3:
        switch (category.get(0)) {
        case "N":
          switch (tokenString) {
          case "}":
            this.nonterminals = symbols.toArray(new String[0]);
            category = new ArrayList<>();
            symbols = new ArrayList<>();
            break;
          case ",":
            break;
          default:
            symbols.add(tokenString);
          }
          break;
        case "T":
          switch (tokenString) {
          case "}":
            this.terminals = symbols.toArray(new String[0]);
            category = new ArrayList<>();
            symbols = new ArrayList<>();
            break;
          case ",":
            break;
          default:
            symbols.add(tokenString);
          }
          break;
        case "P":
          prob = findProbabilityOrAddCategory(category, prob, token);
          break;
        default:
          if (lhs != null) {
            throw new ParseException("Expected - but found " + token,
                lineNumber);
          }
          if (!tokenString.equals(",")) {
            lhs = tokenString;
          }
        }
        break;
      case 4:
        lhs = GrammarParserUtils
            .findLhsOrAddCategory(category, lhs, token);
        break;
      case 5:
        if (tokenString.equals(">")) {
          category.add(tokenString);
          rhs = new StringBuilder();
        } else {
          throw new ParseException("Expected > but found " + token, lineNumber);
        }
        break;
      default:
        switch (tokenString) {
        case "}":
          category = new ArrayList<>();
          this.addProductionRule(prob + " : " + lhs + " -> " + rhs.toString());
          prob = null;
          lhs = null;
          break;
        case "|":
          this.addProductionRule(prob + " : " + lhs + " -> " + rhs.toString());
          rhs = new StringBuilder();
          break;
        case ",":
          this.addProductionRule(prob + " : " + lhs + " -> " + rhs.toString());
          rhs = new StringBuilder();
          lhs = null;
          prob = null;
          category.remove(5);
          category.remove(4);
          category.remove(3);
          break;
        default:
          if (rhs.length() > 0) {
            rhs.append(' ');
          }
          rhs.append(tokenString);
        }
        break;
      }
    }
  }

  private String findProbabilityOrAddCategory(List<String> category,
      String prob, Token token) throws ParseException {
    String tokenString = token.getString();
    if (prob == null || !tokenString.equals(":")) {
      prob = tokenString;
    } else if (tokenString.equals(":")) {
      category.add(tokenString);
    } else {
      throw new ParseException("Unexpected situation with token " + tokenString,
          token.getLineNumber());
    }
    return prob;
  }

  public List<PcfgProductionRule> getProductionRules() {
    return productionRules;
  }

  public void setProductionRules(String[][] rules) {
    for (String[] rule : rules) {
      this.productionRules.add(new PcfgProductionRule(rule));
    }
  }
  
  protected void appendRuleRepresentation(StringBuilder builder) {
    for (int i = 0; i < productionRules.size(); i++) {
      if (i > 0) {
        builder.append(", ");
      }
      builder.append(productionRules.get(i).toString());
    }
  }

  /** Creates a PcfgProductionRule from the string representation and adds it to
   * its set of rules. 
   */
  private void addProductionRule(String rule) throws ParseException {
    this.productionRules.add(new PcfgProductionRule(rule));
  }
}
