import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;

import static edu.princeton.cs.algs4.StdDraw.enableDoubleBuffering;

public class LLVisualizer<Item> {
    public static class Arrow {
        public final double xmin, ymin;   // minimum x- and y-coordinates
        public final double xmax, ymax;   // maximum x- and y-coordinates

        public Arrow(double xmin, double ymin, double xmax, double ymax) {
            this.xmin = xmin;
            this.ymin = ymin;
            this.xmax = xmax;
            this.ymax = ymax;
        }

        public void draw() {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.line(xmin, ymin, xmax, ymax);
        }
    }

    public static class LLBuffer {
        public class Node {
            int item;
            Node next;
            Node prev;

            public void draw(double xmin, double ymin, double xmax, double ymax, Color color) {
                StdDraw.setPenColor(color);
                StdDraw.line(xmin, ymin, xmax, ymin);
                StdDraw.line(xmax, ymin, xmax, ymax);
                StdDraw.line(xmax, ymax, xmin, ymax);
                StdDraw.line(xmin, ymax, xmin, ymin);
                StdDraw.line(xmin, (ymin + ymax) / 2, xmax, (ymin + ymax) / 2);
                StdDraw.text((xmin + xmax) / 2, ymin + (ymax - ymin) * 3 / 4,
                             Integer.toString(this.item));
            }
        }

        public Node front;
        public Node back;
        public int size;

        // Create an empty buffer
        public LLBuffer() {
            front = null;
            back = null;
            size = 0;
        }

        // Convert to string
        // Modify toString() method to show where the cursor and mark are
        public String toString() {
            StringBuilder s = new StringBuilder();
            for (Node curr = front; curr != null; curr = curr.next) {
                s.append(curr.item + " ");
            }
            return s.toString();
        }

        // return number of items currently in this buffer
        public int size() {
            return size;
        }

        // is this buffer empty (size equals zero)?
        public boolean isEmpty() {
            return size == 0;
        }

        // add item x to the end of this buffer
        public void enqueue(int x) {
            Node oldBack = back;
            back = new Node();
            back.item = x;
            if (oldBack == null) {
                front = back;  // The new node has null next and prev
            }
            else {
                oldBack.next = back;
                back.prev = oldBack;
            }
            size++;

        }

        // deletes and returns the item at the front of this buffer
        public int dequeue() {
            if (isEmpty()) {
                throw new RuntimeException("dequeue from empty buffer");
            }
            int ret = front.item;
            front = front.next;
            size--;
            if (front == null) {
                back = null;
            }
            else {
                front.prev = null;
            }
            return ret;
        }

        // add item x to the end of this buffer
        public void push(int x) {
            this.enqueue(x);
        }

        // deletes and returns the item at the back of this buffer
        public int pop() {
            if (isEmpty()) {
                throw new RuntimeException("dequeue from empty buffer");
            }
            int ret = back.item;
            back = back.prev;
            size--;
            if (back == null) {
                front = null;
            }
            else {
                back.next = null;
            }
            return ret;
        }

        // returns the item at the front of this ring buffer
        public int peek() {
            if (isEmpty()) {
                throw new RuntimeException("peek on empty buffer");
            }
            return front.item;
        }

        // Insert a new node after the given index or at the front of the buffer if index==0.
        public LLBuffer insertAfter(int index, int item) {
            if (this != null) {
                Node insertMe = new Node();
                insertMe.item = item;
                Node curr = front;
                if (index != 0) {
                    for (int i = 1; i < index - 1; i++) {
                        if (curr == null) {
                            curr = back;
                            break;
                        }
                        curr = curr.next;
                    }
                    insertMe.next = curr.next;
                    insertMe.prev = curr;
                    curr.next = insertMe;
                    if (insertMe.next != null)
                        insertMe.next.prev = null;
                }
                else {
                    insertMe.next = front;
                    front.prev = insertMe;
                    front = insertMe;
                }

                size++;
            }
            return this;
        }

        // Delete a new node after the given index or at the front of the buffer if index==0.
        public LLBuffer deleteAfter(int index) {
            if (this != null) {
                Node curr = front;
                if (index == 0) {
                    front = front.next;
                    front.prev = null;
                }
                else {
                    for (int i = 0; i < index - 1; i++) {
                        if (curr == null) {
                            curr = back;
                            break;
                        }
                        curr = curr.next;
                    }
                    if (curr != back) {
                        curr.next = curr.next.next;
                        curr.next.next.prev = curr;
                    }
                }
            }
            size--;
            return this;
        }

        // Change a node value at the given index or at the front of the buffer if index==0.
        public void changeValue(int index, int item) {
            Node curr = front;
            if (index != 0) {
                for (int i = 0; i < index; i++) {
                    if (curr == null)
                        throw new RuntimeException("Linked list index out of bound");
                    curr = curr.next;
                }
            }
            curr.item = item;
        }
    }

    public final static int width = 10;       //5
    public final static int xdistance = 15;    //10
    public final static int height = 20;      //15
    public final static int ydistance = 20;

    public static void main(String[] args) {
        enableDoubleBuffering();
        // StdDraw.clear();
        Color color = StdDraw.BLACK;
        StdDraw.setPenColor(color);
        int xCoorMax = 170;
        int yCoorMax = 170;
        StdDraw.setXscale(0, xCoorMax);
        StdDraw.setYscale(0, yCoorMax);

        // Initialize LL
        int n = 10;
        LLBuffer buffer = new LLBuffer();
        for (int i = 1; i <= n; i++) {
            buffer.enqueue(i);
        }

        int index = 0;
        LLBuffer.Node curr = buffer.front;
        for (int i = 0; i < buffer.size; i++) {
            // Node
            curr.draw(i * xdistance,
                      index * ydistance,
                      i * xdistance + width,
                      index * ydistance + height,
                      color);
            // Front pointer
            if (curr == buffer.front) {
                StdDraw.text(Double.valueOf(i * xdistance + width / 2),
                             index * ydistance + height + 2,
                             "head");
            }
            // Arrow
            if (curr.next != null) {
                curr = curr.next;
                Arrow arrow = new Arrow(Double.valueOf(i * xdistance + width / 2),
                                        Double.valueOf(index * ydistance + height / 4),
                                        Double.valueOf((i + 1) * xdistance),
                                        Double.valueOf(index * ydistance + height));
                arrow.draw();
            }
            if (i == buffer.size - 1) {
                // Back pointer
                StdDraw.text(Double.valueOf(i * xdistance + width / 2),
                             index * ydistance + height + 2,
                             "back");
                // Size
                StdDraw.text(i * xdistance + width + 7,
                             index * ydistance + height / 2,
                             "size = " + Integer.toString(buffer.size()));
            }
            StdDraw.show(200);
        }


        int insertMe = 3;
        buffer.insertAfter(1, insertMe);

        index = 1;
        curr = buffer.front;
        for (int i = 0; i < buffer.size; i++) {
            if (curr.item == insertMe) {
                color = StdDraw.RED;
            }
            // Node
            curr.draw(i * xdistance,
                      index * ydistance,
                      i * xdistance + width,
                      index * ydistance + height,
                      color);
            if (curr.item == insertMe) {
                color = StdDraw.BLACK;
            }
            // Front pointer
            if (curr == buffer.front) {
                StdDraw.text(Double.valueOf(i * xdistance + width / 2),
                             index * ydistance + height + 2,
                             "head");
            }
            // Arrow
            if (curr.next != null) {
                curr = curr.next;
                Arrow arrow = new Arrow(Double.valueOf(i * xdistance + width / 2),
                                        Double.valueOf(index * ydistance + height / 4),
                                        Double.valueOf((i + 1) * xdistance),
                                        Double.valueOf(index * ydistance + height));
                arrow.draw();
            }
            if (i == buffer.size - 1) {
                // Back pointer
                StdDraw.text(Double.valueOf(i * xdistance + width / 2),
                             index * ydistance + height + 2,
                             "back");
                // Size
                StdDraw.text(i * xdistance + width + 7,
                             index * ydistance + height / 2,
                             "size = " + Integer.toString(buffer.size()));
            }
            StdDraw.show(200);
        }


        int deleteAfter = 1;
        buffer.deleteAfter(deleteAfter);

        index = 2;
        curr = buffer.front;
        for (int i = 0; i < buffer.size; i++) {
            if (i == deleteAfter) {
                color = StdDraw.BOOK_BLUE;
            }
            // Node
            curr.draw(i * xdistance,
                      index * ydistance,
                      i * xdistance + width,
                      index * ydistance + height,
                      color);
            if (i == deleteAfter) {
                color = StdDraw.BLACK;
            }
            // Front pointer
            if (curr == buffer.front) {
                StdDraw.text(Double.valueOf(i * xdistance + width / 2),
                             index * ydistance + height + 2,
                             "head");
            }
            // Arrow
            if (curr.next != null) {
                curr = curr.next;
                Arrow arrow = new Arrow(Double.valueOf(i * xdistance + width / 2),
                                        Double.valueOf(index * ydistance + height / 4),
                                        Double.valueOf((i + 1) * xdistance),
                                        Double.valueOf(index * ydistance + height));
                arrow.draw();
            }
            if (i == buffer.size - 1) {
                // Back pointer
                StdDraw.text(Double.valueOf(i * xdistance + width / 2),
                             index * ydistance + height + 2,
                             "back");
                // Size
                StdDraw.text(i * xdistance + width + 7,
                             index * ydistance + height / 2,
                             "size = " + Integer.toString(buffer.size()));
            }
            StdDraw.show(200);
        }

        deleteAfter = 5;
        buffer.deleteAfter(deleteAfter);

        index = 3;
        curr = buffer.front;
        for (int i = 0; i < buffer.size; i++) {
            if (i == deleteAfter) {
                color = StdDraw.BOOK_BLUE;
            }
            // Node
            curr.draw(i * xdistance,
                      index * ydistance,
                      i * xdistance + width,
                      index * ydistance + height,
                      color);
            if (i == deleteAfter) {
                color = StdDraw.BLACK;
            }
            // Front pointer
            if (curr == buffer.front) {
                StdDraw.text(Double.valueOf(i * xdistance + width / 2),
                             index * ydistance + height + 2,
                             "head");
            }
            // Arrow
            if (curr.next != null) {
                curr = curr.next;
                Arrow arrow = new Arrow(Double.valueOf(i * xdistance + width / 2),
                                        Double.valueOf(index * ydistance + height / 4),
                                        Double.valueOf((i + 1) * xdistance),
                                        Double.valueOf(index * ydistance + height));
                arrow.draw();
            }
            if (i == buffer.size - 1) {
                // Back pointer
                StdDraw.text(Double.valueOf(i * xdistance + width / 2),
                             index * ydistance + height + 2,
                             "back");
                // Size
                StdDraw.text(i * xdistance + width + 7,
                             index * ydistance + height / 2,
                             "size = " + Integer.toString(buffer.size()));
            }
            StdDraw.show(200);
        }

        insertMe = 1;
        buffer.insertAfter(1, insertMe);

        index = 4;
        curr = buffer.front;
        for (int i = 0; i < buffer.size; i++) {
            if (i == deleteAfter) {
                color = StdDraw.BOOK_BLUE;
            }
            // Node
            curr.draw(i * xdistance,
                      index * ydistance,
                      i * xdistance + width,
                      index * ydistance + height,
                      color);
            if (i == deleteAfter) {
                color = StdDraw.BLACK;
            }
            // Front pointer
            if (curr == buffer.front) {
                StdDraw.text(Double.valueOf(i * xdistance + width / 2),
                             index * ydistance + height + 2,
                             "head");
            }
            // Arrow
            if (curr.next != null) {
                curr = curr.next;
                Arrow arrow = new Arrow(Double.valueOf(i * xdistance + width / 2),
                                        Double.valueOf(index * ydistance + height / 4),
                                        Double.valueOf((i + 1) * xdistance),
                                        Double.valueOf(index * ydistance + height));
                arrow.draw();
            }
            if (i == buffer.size - 1) {
                // Back pointer
                StdDraw.text(Double.valueOf(i * xdistance + width / 2),
                             index * ydistance + height + 2,
                             "back");
                // Size
                StdDraw.text(i * xdistance + width + 7,
                             index * ydistance + height / 2,
                             "size = " + Integer.toString(buffer.size()));
            }
            StdDraw.show(200);
        }

    }
}
