import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Assignment1 {
    public static void main(String[] args) throws IOException {
        String graph = "src/graph2.txt";

        Graph g = new DefaultGraph("g");
        FileSource fs = FileSourceFactory.sourceFor(graph);

        fs.addSink(g);

        try {
            fs.readAll(graph);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fs.removeSink(g);
        }

        g.addAttribute("ui.stylesheet", styleSheet);

        Scanner s1 = null;
        Scanner s2 = null;
        File file = null;

        try {
            file = new File(graph);
            s1 = new Scanner(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            file = new File(graph);
            s2 = new Scanner(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int nodeCount = 0;
        int edgeCount = 0;
        int polyCount = 0;

        s1.nextLine();
        s1.nextLine();
        s1.useDelimiter("\\s*xy:\\s*| |,|\n");

        while(s1.hasNext()) {
            String action = s1.next();
            String a = s1.next();
            String b = s1.next();
            String c = s1.next();
            if(action.compareTo("an") ==0 )
                nodeCount++;
            else if(action.compareTo("ae") == 0)
                edgeCount++;
            if(nodeCount == edgeCount)
                polyCount++;
        }

        //System.out.println(nodeCount + " " + edgeCount + " " + polyCount);

        //index nodes.length() - 2 -> S
        //index nodes.length() - 1 -> G
        MyNode[] nodes = new MyNode[nodeCount];
        MyEdge[] edges = new MyEdge[edgeCount];
        MyPolygon[] polygons = new MyPolygon[polyCount];

        nodeCount = 0;
        edgeCount = 0;
        polyCount = 0;

        int countCorrection = 0;

        s2.nextLine();
        s2.nextLine();
        s2.useDelimiter("\\s*xy:\\s*| |,|\n");

        while(s2.hasNext()) {
            String action = s2.next();
            String a = s2.next();
            String b = s2.next();
            String c = s2.next();

            if(action.compareTo("an") == 0) {

                nodes[nodeCount++] = new MyNode(a, Double.parseDouble(b), Double.parseDouble(c));
                //System.out.println("Node added:\n" + nodes[nodeCount - 1].toString());
            }
            else if(action.compareTo("ae") == 0) {
                MyNode temp1 = null;
                MyNode temp2 = null;

                //Gets rid of '\r' in strings (I couldn't get it to work in the .useDelimiter)
                if(a.charAt(a.length() - 1) == '\r')
                    a = a.substring(0, a.length() - 1);
                if(b.charAt(b.length() - 1) == '\r')
                    b = b.substring(0, b.length() - 1);
                if(c.charAt(c.length() - 1) == '\r')
                    c = c.substring(0, c.length() - 1);

                for(int i = 0; i < nodeCount; i++) {
                    if(nodes[i].getName().compareTo(b) == 0)
                        temp1 = nodes[i];
                    if(nodes[i].getName().compareTo(c) == 0)
                        temp2 = nodes[i];
                }
                edges[edgeCount++] = new MyEdge(a, temp1, temp2);
                //System.out.println("Edge added:\n" + edges[edgeCount - 1].toString());
            }
            if(nodeCount == edgeCount) {
                polygons[polyCount] = new MyPolygon(polyCount + 1, edgeCount - countCorrection);
                for(int i = 0 + countCorrection; i < edgeCount; i++)
                    polygons[polyCount++].insert(edges[i]);
                //System.out.println("\nPolygon added:\n" + polygons[polyCount - 1].toString());
            }
            countCorrection = edgeCount;
        }
        //System.out.println(nodeCount + " " + edgeCount + " " + polyCount);

        MyNode current = nodes[nodeCount - 2];
        nodes[nodeCount - 2].setTraveled(true);
        MyNode goal = nodes[nodeCount - 1];

        for(int i = 0; i < nodeCount; i++)
            nodes[i].setH(goal);

        for(int i = 0; i < nodeCount-1; i++) {
            for(int j = 0; j < nodeCount-i-1; j++) {
                if(nodes[j].getH() > nodes[j+1].getH()) {
                    MyNode temp = nodes[j];
                    nodes[j] = nodes[j+1];
                    nodes[j+1] = temp;
                }
            }
        }

        int newNodeCount = 0;
        int newEdgeCount = 0;

        while(current.getxCoord() != goal.getxCoord() && current.getyCoord() != goal.getyCoord()) {
            for(int i = 0; i < nodeCount; i++) {
                if(!nodes[i].getTraveled()) {
                    boolean intersect = false;
                    for(int j = 0; j < edgeCount; j++) {
                        if(checkIntersection(current, nodes[i], edges[j].getNode1(), edges[j].getNode2()) == 0) {
                            intersect = true;
                            break;
                        }
                        else if(checkIntersection(current, nodes[i], edges[j].getNode1(), edges[j].getNode2()) == 1) {
                            for(int k = 0; k < polyCount; k++) {
                                if(polygons[k].checkNodeOnPolygon(nodes[i])) {
                                    intersect = true;
                                    break;
                                }
                            }
                        }
                    }
                    if(!intersect) {
                        Node n1 = g.addNode(String.valueOf(newNodeCount++));
                        n1.setAttribute("x", current.getxCoord());
                        n1.setAttribute("y", current.getyCoord());
                        Node n2 = g.addNode(String.valueOf(newNodeCount++));
                        n2.setAttribute("x", nodes[i].getxCoord());
                        n2.setAttribute("y", nodes[i].getyCoord());
                        Edge e = g.addEdge(String.valueOf(newEdgeCount++), n1, n2);
                        e.setAttribute("ui.class", "marked");

                        nodes[i].setTraveled(true);
                        current = nodes[i];
                        break;
                    }
                }
            }
        }
        g.display(false);
    }

    static boolean onEdge(MyNode p, MyNode q, MyNode r) {
        if(q.getxCoord() < Math.max(p.getxCoord(), r.getxCoord()) &&
                q.getxCoord() > Math.min(p.getxCoord(), r.getxCoord()) &&
                q.getyCoord() < Math.max(p.getyCoord(), r.getyCoord()) &&
                q.getyCoord() > Math.min(p.getyCoord(), r.getyCoord()))
            return true;
        return false;
    }

    static int orientation(MyNode p, MyNode q, MyNode r) {
        double val = (q.getyCoord() - p.getyCoord()) * (r.getxCoord() - q.getxCoord()) - (q.getxCoord() - p.getxCoord()) * (r.getyCoord() - q.getyCoord());
        if(val == 0)
            return 0;
        return (val > 0)? 1:2;
    }

    static boolean checkSameNode(MyNode p, MyNode q) {
        if(p.getxCoord() == q.getxCoord() && p.getyCoord() == q.getyCoord())
            return true;
        return false;
    }

    static int checkIntersection(MyNode p1, MyNode q1, MyNode p2, MyNode q2) {
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if(o1 != o2 && o3 != o4 && checkSameNode(p1, p2))
            return 1;
        if(o1 != o2 && o3 != o4 && checkSameNode(p1, q2))
            return 1;
        if(o1 != o2 && o3 != o4 && checkSameNode(q1, p2))
            return 1;
        if(o1 != o2 && o3 != o4 && checkSameNode(q1, q2))
            return 1;

        if(o1 != o2 && o3 != o4)
            return 0;

        if(o1 == 0 && onEdge(p1, p2, q1) && o2 == 0 && onEdge(p1, p2, q1) && o3 == 0 && onEdge(p2, p1, q2) && o4 == 0 && onEdge(p2, p1, q2))
            return 1;

        if(o1 == 0 && onEdge(p1, p2, q1))
            return 0;
        if(o2 == 0 && onEdge(p1, q2, q1))
            return 0;
        if(o3 == 0 && onEdge(p2, p1, q2))
            return 0;
        if(o4 == 0 && onEdge(p2, q1, q2))
            return 0;

        return 1;
    }

    static String styleSheet = "edge.marked {fill-color: red;}";
}