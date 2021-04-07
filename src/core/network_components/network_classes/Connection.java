package core.network_components.network_classes;

import utils.NumUtils;

public class Connection {
    public Node start;
    public Node end;
//    public double weight = NumUtils.getRandomRange(0,1);
    public double weight = NumUtils.getRandomRange(-.05,.05);

    public Connection(Node from, Node to) {
        this.start = from;
        this.end = to;
        //TODO - Remove and make user connect manually? Im not sure..
        from.connectionsOut.add(this);
        to.connectionsIn.add(this);
    }

    public void run() {
        end.intake(start.emit() * weight);
//        if(start instanceof BiasNode) System.out.println(weight);
    }

    public double emit() {
        return start.emit() * weight;
    }

    public void destroy() {
        start.connectionsOut.remove(this);
        end.connectionsIn.remove(this);
    }
}