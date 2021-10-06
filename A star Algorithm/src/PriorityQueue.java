public class PriorityQueue {
    private int count;
    private MyNode[] nodes;
    private double[] estimatedCost;

    public PriorityQueue(int count) {
        this.count = count;
        nodes = new MyNode[this.count];
        estimatedCost = new double[this.count];
    }

    public void insert(MyNode node, double estimatedCost) {
        if(nodes[0] == null)
            nodes[0] = node;
        else {
            MyNode temp;
            int i = 0;
            while(nodes[i] != null) {
                if(nodes[i].getH() > node.getH()) {
                    temp = nodes[i];
                    nodes[i] = node;
                    node = temp;
                }
                i++;
            }
            nodes[i] = node;
        }
    }

    public MyNode getNode(int index) {
        return nodes[index];
    }

    public void print() {
        for(int i = 0; i < this.count; i++) {
            System.out.println(nodes[i].getH());
        }
    }
}