public class MyNode {
    private String name;
    private double xCoord;
    private double yCoord;
    private double h;
    private boolean traveled;

    public MyNode(String name, double xCoord, double yCoord) {
        this.name = name;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.h = 0;
        this.traveled = false;
    }

    public void setH(MyNode goalNode) {
        this.h = Math.sqrt(Math.pow(this.xCoord - goalNode.getxCoord(),2) + Math.pow(this.yCoord - goalNode.getyCoord(),2));
        //System.out.println("Node " + this.name + " 'h' value: " + this.h);
    }
    public void setTraveled(boolean traveled) {
        this.traveled = traveled;
    }

    public String getName() {
        return this.name;
    }
    public double getxCoord() {
        return this.xCoord;
    }
    public double getyCoord() {
        return this.yCoord;
    }
    public double getH() {
        return this.h;
    }
    public boolean getTraveled() {
        return this.traveled;
    }

    public String toString() {
        return this.name + " xy: " + this.xCoord + "," + this.yCoord;
    }
}