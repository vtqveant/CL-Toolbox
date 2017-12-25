package common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.cfg.Cfg;
import common.cfg.Pcfg;
import common.lcfrs.Srcg;
import common.tag.Tag;

/** Parses different grammars from text files. */
public class GrammarParser {
  private static final Pattern p = Pattern.compile("\"(.*?)\"");

  /** Parses a CFG from a file and returns it as Cfg. */
  public static Cfg parseCfgFile(String grammarFile) throws IOException {
    Cfg cfg = new Cfg();
    BufferedReader in = new BufferedReader(new FileReader(grammarFile));
    String line = in.readLine().trim();
    while (line != null) {
      String lineTrim = line.trim();
      switch (lineTrim.charAt(0)) {
      case 'N':
        if (cfg.getNonterminals() != null) {
          System.out.println("Declaring N twice is not allowed");
          in.close();
          return null;
        }
        cfg.setNonterminals(parseNT(lineTrim));
        break;
      case 'T':
        if (cfg.getTerminals() != null) {
          System.out.println("Declaring T twice is not allowed");
          in.close();
          return null;
        }
        cfg.setTerminals(parseNT(lineTrim));
        break;
      case 'S':
        if (cfg.getStartSymbol() != null) {
          System.out.println("Declaring S twice is not allowed");
          in.close();
          return null;
        }
        cfg.setStartSymbol(parseS(lineTrim));
        break;
      case 'P':
        if (cfg.getProductionRules().size() > 0) {
          System.out.println("Declaring P twice is not allowed");
          in.close();
          return null;
        }
        for (String rule : parseNT(lineTrim)) {
          cfg.addProductionRule(rule);
        }
        break;
      case 'G':
        System.out.println("Grammar declaration detected. Nothing to do.");
        break;
      default:
        System.err.println("Unknown declaration symbol: " + lineTrim.charAt(0));
      }
      line = in.readLine();
    }
    in.close();
    return cfg;
  }

  /** Parses a PCFG from a file and returns it as Pcfg. */
  public static Pcfg parsePcfgFile(String grammarFile) throws IOException {
    Pcfg pcfg = new Pcfg();
    BufferedReader in = new BufferedReader(new FileReader(grammarFile));
    String line = in.readLine().trim();
    while (line != null) {
      String lineTrim = line.trim();
      switch (lineTrim.charAt(0)) {
      case 'N':
        if (pcfg.getNonterminals() != null) {
          System.out.println("Declaring N twice is not allowed");
          in.close();
          return null;
        }
        pcfg.setNonterminals(parseNT(lineTrim));
        break;
      case 'T':
        if (pcfg.getTerminals() != null) {
          System.out.println("Declaring T twice is not allowed");
          in.close();
          return null;
        }
        pcfg.setTerminals(parseNT(lineTrim));
        break;
      case 'S':
        if (pcfg.getStartSymbol() != null) {
          System.out.println("Declaring S twice is not allowed");
          in.close();
          return null;
        }
        pcfg.setStartSymbol(parseS(lineTrim));
        break;
      case 'P':
        if (pcfg.getProductionRules().size() > 0) {
          System.out.println("Declaring P twice is not allowed");
          in.close();
          return null;
        }
        for (String rule : parseNT(lineTrim)) {
          pcfg.addProductionRule(rule);
        }
        break;
      case 'G':
        System.out.println("Grammar declaration detected. Nothing to do.");
        break;
      default:
        System.err.println("Unknown declaration symbol: " + lineTrim.charAt(0));
      }
      line = in.readLine();
    }
    in.close();
    return pcfg;
  }

  /** Parses a TAG from a text file and returns it as a Tag object. */
  public static Tag parseTagFile(String grammarFile)
    throws IOException, ParseException {
    Tag tag = new Tag();
    BufferedReader in = new BufferedReader(new FileReader(grammarFile));
    String line = in.readLine().trim();
    while (line != null) {
      String lineTrim = line.trim();
      switch (lineTrim.charAt(0)) {
      case 'N':
        if (tag.getNonterminals() != null) {
          System.out.println("Declaring N twice is not allowed");
          in.close();
          return null;
        }
        tag.setNonterminals(parseNT(lineTrim));
        break;
      case 'T':
        if (tag.getTerminals() != null) {
          System.out.println("Declaring T twice is not allowed");
          in.close();
          return null;
        }
        tag.setTerminals(parseNT(lineTrim));
        break;
      case 'S':
        if (tag.getStartSymbol() != null) {
          System.out.println("Declaring S twice is not allowed");
          in.close();
          return null;
        }
        tag.setStartSymbol(parseS(lineTrim));
        break;
      case 'I':
        if (tag.getInitialTreeNames().size() > 0) {
          System.out.println("Declaring I twice is not allowed");
          in.close();
          return null;
        }
        for (String treeDec : parseNT(lineTrim)) {
          tag.addInitialTree(treeDec);
        }
        break;
      case 'A':
        if (tag.getAuxiliaryTreeNames().size() > 0) {
          System.out.println("Declaring A twice is not allowed");
          in.close();
          return null;
        }
        for (String treeDec : parseNT(lineTrim)) {
          tag.addAuxiliaryTree(treeDec);
        }
        break;
      case 'G':
        System.out.println("Grammar declaration detected. Nothing to do.");
        break;
      default:
        System.err.println("Unknown declaration symbol: " + lineTrim.charAt(0));
      }
      line = in.readLine();
    }
    in.close();
    return tag;
  }

  /** For a line like "A", "S" it gets the content of each quote and makes each
   * an element of the returned array. */
  private static String[] parseNT(String lineTrim) {
    Matcher m = p.matcher(lineTrim);
    ArrayList<String> nList = new ArrayList<String>();
    while (m.find()) {
      String n = m.group();
      nList.add(n.substring(1, n.length() - 1));
    }
    return nList.toArray(new String[nList.size()]);
  }

  /** Takes a line like "S" and returns the string inside the quotes. */
  private static String parseS(String lineTrim) {
    Matcher m = p.matcher(lineTrim);
    if (m.find()) {
      String s = m.group();
      return s.substring(1, s.length() - 1);
    }
    System.err.println("No declaration of start symbol found in line " + lineTrim);
    return null;
  }

  /** Parses a sRCG from a file and returns it as Srcg. */
  public static Srcg parseSrcgFile(String grammarFile)
    throws IOException, ParseException {
    Srcg srcg = new Srcg();
    BufferedReader in = new BufferedReader(new FileReader(grammarFile));
    String line = in.readLine().trim();
    while (line != null) {
      String lineTrim = line.trim();
      switch (lineTrim.charAt(0)) {
      case 'N':
        if (srcg.getNonterminals() != null) {
          System.out.println("Declaring N twice is not allowed");
          in.close();
          return null;
        }
        srcg.setNonterminals(parseNT(lineTrim));
        break;
      case 'V':
        if (srcg.getVariables() != null) {
          System.out.println("Declaring V twice is not allowed");
          in.close();
          return null;
        }
        srcg.setVariables(parseNT(lineTrim));
        break;
      case 'T':
        if (srcg.getTerminals() != null) {
          System.out.println("Declaring T twice is not allowed");
          in.close();
          return null;
        }
        srcg.setTerminals(parseNT(lineTrim));
        break;
      case 'S':
        if (srcg.getStartSymbol() != null) {
          System.out.println("Declaring S twice is not allowed");
          in.close();
          return null;
        }
        srcg.setStartSymbol(parseS(lineTrim));
        break;
      case 'P':
        if (srcg.getClauses().size() > 0) {
          System.out.println("Declaring P twice is not allowed");
          in.close();
          return null;
        }
        for (String clauseDec : parseNT(lineTrim)) {
          srcg.addClause(clauseDec);
        }
        break;
      case 'G':
        System.out.println("Grammar declaration detected. Nothing to do.");
        break;
      default:
        System.err.println("Unknown declaration symbol: " + lineTrim.charAt(0));
      }
      line = in.readLine();
    }
    in.close();
    return srcg;
  }
}
