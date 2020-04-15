package TMDCoolAlgorithms;
import java.util.ArrayList;
import java.util.List;

public class BKTree<E> {
    class Node {
        E object;
        ArrayList<Node> children;
        Node(E object) {
            this.object = object;
        }
        private void add (E item) {
            int d = distance.d(object, item);
            if (d == 0) {
                return;
            }

            if (children == null) {
                children = new ArrayList<>();
            }
            Node child = children.get(d);
            if(child == null) {
                Node node = new Node(item);
                children.set(d, node);
            } else {
                child.add(item);
            }
        }
    }

    private Node root;
    private Distance<E> distance;

    public BKTree(Distance<E> distance) {
        this.distance = distance;
    }

    public void add(E[] items) {
        for (E item : items) {
            add(item);
        }
    }
    
    public void add(E item) {
        if (root == null) {
            root = new Node(item);
        }  else {
            root.add(item);
        }
    }


    public void search(E q, int radius, List<E> results) {
        if(root==null) {
            return;
        }
        search(root, q, radius, results);
    }

    private void search(Node node, E q, int k, List<E> results) {
        int d = distance.d(node.object, q);
        if (d<=k && node.object != q) {
            results.add(node.object);
        }
        if (node.children != null) {
            int start = Math.max(1, d-k);
            int end = Math.min(node.children.size(), d+k+1);
            for (int i = start; i < end; i++) {
                Node child = node.children.get(i);
                if (child!=null) {
                    search(child, q, k, results);
                }
            }
        }
    }
}

 
interface Distance<T>  {
    int d(T x, T y);
}


class EditDistance implements Distance<String> {

    private int min(int x, int y, int z) {
        return  Math.min(x, Math.min(y, z));
    }
    
    @Override
    public int d(String x, String y ) {
        if (x.length() < y.length()) {
            String swap = x;
            x = y;
            y = swap;
        }

        int [][] d= new int[2][y.length() + 1];;
        for (int j = 0; j <= y.length(); j++) {
            d[0][j] = j;
        }
        for (int i= 1; i <= x.length(); i++) {
            d[1][0] = i;
            for (int j=1; j <= y.length(); j++) {
                int cost = x.charAt(i-1) == y.charAt(j-1) ? 0: 1;
                d[1][j] = min(d[0][j] + 1, //deletion
                                d[1][j-1] + 1, // add
                                d[0][j-1] + cost // replace
                                );
            }
            int[] swap = d[0];
            d[0] = d[1];
            d[1] = swap;
        }
        return d[0][y.length()];
    }
}

