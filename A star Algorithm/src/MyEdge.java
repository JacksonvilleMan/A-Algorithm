public class MyEdge {
    private String name;
    private MyNode node1;
    private MyNode node2;

    public MyEdge(String name, MyNode node1, MyNode node2) {
        this.name = name;
        this.node1 = node1;
        this.node2 = node2;
    }

    public MyNode getNode1() {
        return this.node1;
    }
    public MyNode getNode2() {
        return this.node2;
    }

    public String toString() {
        return this.name + " " + this.node1.getName() + " " + this.node2.getName();
    }
}