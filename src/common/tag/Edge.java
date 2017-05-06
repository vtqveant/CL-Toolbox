package common.tag;

public class Edge {
  Vertex from;
  Vertex to;
  
  Edge(Vertex from, Vertex to) {
    this.from = from;
    this.to= to;
  }
  
  public Vertex getFrom() {
    return this.from;
  }
  
  public Vertex getTo() {
    return this.to;
  }
}