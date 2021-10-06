public class MyPolygon {
    private int polyNum;
    private int count;
    private MyEdge[] edges;
    private int top;

    public MyPolygon(int polyNum, int count) {
        this.polyNum = polyNum;
        this.count = count;
        this.edges = new MyEdge[this.count];
        this.top = -1;
    }

    public void insert(MyEdge edge) {
        if(isFull())
            System.out.println("This polygon is full...");
        else
            this.edges[++top] = edge;
    }

    public boolean isFull() {
        return (this.top == this.count - 1);
    }

    public boolean checkNodeOnPolygon(MyNode node) {
        for(int i = 0; i < count; i++) {
            if((node.getxCoord() == edges[i].getNode1().getxCoord() && node.getyCoord() == edges[i].getNode1().getyCoord()) ||
                    (node.getxCoord() == edges[i].getNode2().getxCoord() && node.getyCoord() == edges[i].getNode2().getyCoord()))
                return true;
        }
        return false;
    }

    public String toString() {
        String string = "Polygon #" + this.polyNum + "\n";
        for(int i = 0; i < this.count; i++)
            string += edges[i].getNode1().toString() + "\n" + edges[i].getNode2().toString() + "\n";
        for(int i = 0; i < this.count; i++)
            string += edges[i].toString() + "\n";
        return string;
    }
}