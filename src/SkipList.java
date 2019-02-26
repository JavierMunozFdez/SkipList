import java.util.Random;

class SkipList<Key extends Comparable<Key>, Value> {
    private final static int MAX_HEIGHT = 5;
    private Node head;
    private Node tail;
    private Random RNG = new Random(0);
    SkipList() {
        head = new Node(null, null, 0);
        tail = new Node(null, null, 0);
        head.setRight(tail);
    }

    void put(Key key, Value value) {
        final int height = determineHeight();
        final Node newNode = new Node(key, value, height);
        if (height > head.getHeight()) {
            final Node newHead = new Node(null, null, height);
            newHead.setDown(head);
            final Node newTail = new Node(null, null, height);
            newTail.setDown(tail);
            newHead.setRight(newTail);
            head = newHead;
            tail = newTail;
        }
        Node current = head;
        while (height < current.getHeight()) {
            current = current.getDown();
        }
        current.insert(newNode);
    }

    Value remove(Key key) {
        final Node toBeRemoved = head.get(key);
        if (toBeRemoved == null) {
            System.out.println("Error: Node not Found");
            return null;
        } else {
            toBeRemoved.remove();
            return toBeRemoved.getValue();
        }
    }

    Value get(final Key key) {
        final Node n = head.get(key);
        if (n == null) {
            System.out.println("Error: Node not Found");
            return null;
        } else {
            return n.getValue();
        }
    }

    private int determineHeight() {
        int height = 0;
        while (height <= head.getHeight() && height < MAX_HEIGHT) {
            if (RNG.nextBoolean()) {
                height++;
            } else {
                break;
            }
        }
        return height;
    }

    public String toString() {
        Node current = head;
        String ret = "";
        while (current != null) {
            ret = ret.concat(current.toString());
            current = current.getDown();
            ret = ret.concat(String.format("%n"));
        }
        return ret;
    }

    private class Node {
        private final Key key;
        private final Value value;
        private final int height;
        private Node right;
        private Node left;
        private Node down;

        Node(Key k, Value v, int h) {
            key = k;
            value = v;
            height = h;
        }

        int getHeight() {
            return height;
        }

        void setRight(Node right) {
            this.right = right;
        }

        void setDown(Node down) {
            this.down = down;
        }

        public String toString() {
            return String.format("K: %s, V: %s, R: [%s]", key, value, right);
        }

        void insert(final Node n) {
            Node current = this;
            while (current.right.key != null && current.right.key.compareTo(n.key) < 0) {
                current = current.right;
            }
            if (current.height == n.height) {
                n.right = current.right;
                n.right.left = n;
                current.right = n;
                n.left = current;
                if (current.height > 0) {
                    final Node newNode = new Node(n.key, n.value, n.height - 1);
                    n.setDown(newNode);
                    current.down.insert(newNode);
                }
            }
        }

        Value getValue() {
            return value;
        }
        /*
            Returns null if it couldn't be found
         */
        Node get(Key key) {
            if (this.key != null && this.key == key) {
                return this;
            } else {
                if (right.key != null && right.key.compareTo(key) <= 0) {
                    return right.get(key);
                } else if (down != null){
                    return down.get(key);
                }
            }
            return null;
        }

        Node getDown() {
            return down;
        }

        void remove() {
            this.left.right = this.right;
            this.right.left = this.left;
            if (this.down != null) {
                this.down.remove();
            }
        }
    }
}
