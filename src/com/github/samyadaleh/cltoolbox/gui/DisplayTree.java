package com.github.samyadaleh.cltoolbox.gui;

import java.awt.Graphics;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.github.samyadaleh.cltoolbox.common.tag.Tree;

public class DisplayTree extends JFrame implements DisplayTreeInterface {

  private static final long serialVersionUID = -9123591819196303915L;
  private Tree tree;
  private final Map<String, Integer[]> nodesDrawn = new HashMap<>();
  private String[] itemForm;
  private Graphics g;

  /** Called with a tree in bracket format as argument, retrieves the depth by
   * brackets to estimate needed windows size. */
  public DisplayTree(String[] args) throws ParseException {
    super();
    this.tree = new Tree(args[0]);
    if (args.length > 1) {
      this.itemForm = args[1].substring(1, args[1].length() - 1).split(",");
    } else {
      itemForm = new String[] {};
    }
    this.setLocation(X, Y);

    this.setSize(80 * tree.getWidth(), 80 * tree.getHeight());
    this.setVisible(true);

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }

  /** Initiates the drawing of the tree. */
  public void paint(Graphics g) {
    this.g = g;
    AbstractDisplayTree.paint(this);
  }
  
  public static void main(String[] args) throws ParseException {
    new DisplayTree(args);
  }

  @Override public Tree getTree() {
    return this.tree;
  }

  @Override public String[] getItemForm() {
    return this.itemForm;
  }

  @Override public void drawText(String label, int x, int y) {
    g.drawString(label, x, y);
  }

  @Override public void drawLine(int x1, int y1, int x2,
    int y2) {
    g.drawLine(x1, y1, x2, y2);
  }

  @Override public Map<String, Integer[]> getNodesDrawn() {
    return this.nodesDrawn;
  }

  @Override public void clearRect(int width, int height) {
    g.clearRect(0, 0, width, height);
  }
  
}
