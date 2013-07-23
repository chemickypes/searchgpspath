package com.lorenzo.pointlocations;

import java.util.Random;


public class TrapezoidalMaps {
    
    static final String xNode = "x";
    static final String yNode = "y";
    static final String trNode = "t";
    static final double perm = 0.000000000000000000000001;
    private int numTrapezoids;
    private Node D;
    private Trapezoid T;
    private Segment[] S;
    private Point[] corners;
    private int numSegments;
    
    public TrapezoidalMaps(Segment[] segments, Point[] corners){
        for(Segment se : segments){
            se.left.x = se.left.x + (perm*(se.left.y));
            se.right.x = se.right.x + (perm*(se.right.y));
        }
        S = segments;
        this.corners = corners;
        numSegments = segments.length;
        numTrapezoids = 0;
        this.createMap();
    }
    
    public void createMap(){
        if(numSegments > 0){
            Point p1 = new Point(corners[0].x + (perm*(corners[0].y)), corners[0].y, "s");
            Point p2 = new Point(corners[3].x + (perm*(corners[3].y)), corners[3].y, "s");
            Point p3 = new Point(corners[1].x + (perm*(corners[1].y)), corners[1].y, "s");
            Point p4 = new Point(corners[2].x + (perm*(corners[2].y)), corners[2].y, "s");
            Segment segB = new Segment(p1, p2, "b");
            Segment segT = new Segment(p3, p4, "t");
            T = new Trapezoid();
            T.name = "root";
            T.top = segT;
            T.bottom = segB;
            T.left = p3;
            T.right = p2;
            T.lRight = null;
            T.uRight = null;
            T.lLeft = null;
            T.uLeft = null;
            D = new Node();
            D.node = "t";
            D.t = T;
            T.node = D;
            numTrapezoids++;
            
            S = randPermutation(S);

            insertSegments();
            
            System.out.println("Il search-tree D e' stato generato.");
        } else{
            System.out.println("Il search-tree D non e' stato generato a causa dell'assenza di segmenti.");
        }
        
    }
    
    private Segment[] randPermutation(Segment segment[])
    {
        Random random = new Random();
        for(int k = numSegments-1; k >= 2; k--)
        {
            int j = (int)(Math.random()*numSegments);
            Segment lsegment = segment[k];
            segment[k] = segment[j];
            segment[j] = lsegment;
        }
        return segment;
    }
    
    private void insertSegments()
    {
        for(int j = 0; j < numSegments; j++)
        {
            Segment lsegment = S[j];
            Trapezoid trapezoid = followSegment(lsegment);
            Trapezoid trapezoid1 = updateT(trapezoid, lsegment);
            updateD(trapezoid, lsegment, trapezoid1);
        }
        //Computation.showD(D);
    }
    
     private Trapezoid followSegment(Segment seg)
    {
        Point p = seg.getStartingPoint();
        Point q = seg.getEndingPoint();

        Node node = find(D, p, q);

        Trapezoid trapezoid = node.t;
        Trapezoid trapezoid1 = trapezoid;
        trapezoid1.next = null;

        while(q.isRightOf(trapezoid.rightp())) 
        {
            if(Computation.isAbove(trapezoid.rightp(), seg.left, seg.right))
                trapezoid.next = trapezoid.lRight;
            else
                trapezoid.next = trapezoid.uRight;
            trapezoid = trapezoid.next;
            if(trapezoid == null)
                return trapezoid1;
        }
        trapezoid.next = null;
        return trapezoid1;
    }
     
     private Node find(Node node, Point p, Point q)
    {
        Node nodeFound;
        nodeFound = node;

                while(true)
                {
                while("x".equals(nodeFound.node))
                {
                        if(nodeFound.p != null && nodeFound.isLeft(p))
                {
                    nodeFound = nodeFound.left;
                } else {
                    nodeFound = nodeFound.right;
                }
                }
            if( "y".equals(nodeFound.node))
            {
                if(nodeFound.segment.left == p){
                    if(Computation.isAbove(q, nodeFound.segment.left, nodeFound.segment.right)){
                        nodeFound = nodeFound.left;
                    } else{
                        nodeFound = nodeFound.right;
                    }
                } else{
                    if(Computation.isAbove(p, nodeFound.segment.left, nodeFound.segment.right)){
                        nodeFound = nodeFound.left;
                    } else{
                        nodeFound = nodeFound.right;
                    }
                }
            } else {
                return nodeFound;
            }
        }
    }


    private void upDateLeftNeighbors(Trapezoid tr1, Trapezoid tr2)
    {
        if(tr2.uLeft != null)
            tr2.uLeft.uRight = tr1;
        if(tr2.lLeft != null)
            tr2.lLeft.lRight = tr1;
    }

    private void upDateRightNeighbors(Trapezoid tr1, Trapezoid tr2)
    {
        if(tr2.uRight != null)
            tr2.uRight.uLeft = tr1;
        if(tr2.lRight != null)
            tr2.lRight.lLeft = tr1;
    }
    
    private Trapezoid updateT(Trapezoid trapezoid, Segment lsegment)
    {
        int i = 0;
        Trapezoid trapezoid1 = trapezoid;
        if(trapezoid1.next == null)
        {
            Trapezoid trapezoid2 = null;
            Trapezoid trapezoid4 = null;
            Trapezoid trapezoid6 = new Trapezoid();
            Trapezoid trapezoid8 = new Trapezoid();
            i = 2;
            trapezoid6.left = lsegment.left;
            trapezoid6.right = lsegment.right;
            trapezoid6.bottom = lsegment;
            trapezoid6.top = trapezoid1.top;
            trapezoid6.name = "C" + numTrapezoids;
            numTrapezoids++;
            trapezoid8.top = lsegment;
            trapezoid8.bottom = trapezoid1.bottom;
            trapezoid8.right = lsegment.right;
            trapezoid8.left = lsegment.left;
            trapezoid8.name = "D" + numTrapezoids;
            numTrapezoids++;
            if(lsegment.left != trapezoid1.left)
            {
                trapezoid2 = new Trapezoid();
                trapezoid2.top = trapezoid1.top;
                trapezoid2.bottom = trapezoid1.bottom;
                trapezoid2.left = trapezoid1.left;
                trapezoid2.right = lsegment.left;
                trapezoid2.lLeft = trapezoid1.lLeft;
                trapezoid2.uLeft = trapezoid1.uLeft;
                trapezoid2.lRight = trapezoid8;
                trapezoid2.uRight = trapezoid6;
                trapezoid2.node = trapezoid1.node;
                trapezoid2.name = "A" + numTrapezoids;
                numTrapezoids++;
                upDateLeftNeighbors(trapezoid2, trapezoid1);
                trapezoid6.lLeft = null;
                trapezoid6.uLeft = trapezoid2;
                trapezoid8.lLeft = trapezoid2;
                trapezoid8.uLeft = null;
                i++;
            } else
            {
                trapezoid6.uLeft = trapezoid1.uLeft;
                trapezoid6.lLeft = null;
                trapezoid8.lLeft = trapezoid1.lLeft;
                trapezoid8.uLeft = null;
                if(trapezoid1.uLeft != null)
                    trapezoid1.uLeft.uRight = trapezoid6;
                if(trapezoid1.lLeft != null)
                    trapezoid1.lLeft.lRight = trapezoid8;
            }
            if(lsegment.right != trapezoid1.right)
            {
                trapezoid4 = new Trapezoid();
                trapezoid4.top = trapezoid1.top;
                trapezoid4.bottom = trapezoid1.bottom;
                trapezoid4.left = lsegment.right;
                trapezoid4.right = trapezoid1.right;
                trapezoid4.lRight = trapezoid1.lRight;
                trapezoid4.uRight = trapezoid1.uRight;
                trapezoid4.lLeft = trapezoid8;
                trapezoid4.uLeft = trapezoid6;
                trapezoid4.node = trapezoid1.node;
                trapezoid4.name = "B" + numTrapezoids;
                numTrapezoids++;
                upDateRightNeighbors(trapezoid4, trapezoid1);
                trapezoid6.lRight = null;
                trapezoid6.uRight = trapezoid4;
                trapezoid8.lRight = trapezoid4;
                trapezoid8.uRight = null;
                i++;
            } else
            {
                trapezoid6.uRight = trapezoid1.uRight;
                trapezoid6.lRight = null;
                trapezoid8.lRight = trapezoid1.lRight;
                trapezoid8.uRight = null;
                if(trapezoid1.uRight != null)
                    trapezoid1.uRight.uLeft = trapezoid6;
                if(trapezoid1.lRight != null)
                    trapezoid1.lRight.lLeft = trapezoid8;
            }
            trapezoid8.node = trapezoid1.node;
            trapezoid6.node = trapezoid1.node;
            trapezoid6.next = trapezoid8;
            trapezoid8.next = null;
            if(trapezoid2 != null)
            {
                if(trapezoid4 != null)
                {
                    trapezoid2.next = trapezoid4;
                    trapezoid4.next = trapezoid6;
                } else
                {
                    trapezoid2.next = trapezoid6;
                }
                return trapezoid2;
            }
            if(trapezoid4 != null)
            {
                trapezoid4.next = trapezoid6;
                return trapezoid4;
            } else
            {
                return trapezoid6;
            }
        }
        Trapezoid trapezoid3 = null;
        Trapezoid trapezoid5 = null;
        Trapezoid trapezoid7 = null;
        Trapezoid trapezoid9 = null;
        Trapezoid trapezoid10 = null;
        Trapezoid trapezoid11 = null;
        if(lsegment.left != trapezoid1.left)
        {
            trapezoid9 = new Trapezoid();
            trapezoid10 = new Trapezoid();
            trapezoid11 = new Trapezoid();
            i += 3;
            trapezoid9.top = trapezoid1.top;
            trapezoid9.bottom = trapezoid1.bottom;
            trapezoid9.left = trapezoid1.left;
            trapezoid9.right = lsegment.left;
            trapezoid9.lLeft = trapezoid1.lLeft;
            trapezoid9.uLeft = trapezoid1.uLeft;
            trapezoid9.lRight = trapezoid11;
            trapezoid9.uRight = trapezoid10;
            trapezoid9.node = trapezoid1.node;
            trapezoid9.name = "A" + numTrapezoids;

            numTrapezoids++;
            upDateLeftNeighbors(trapezoid9, trapezoid1);
            trapezoid10.top = trapezoid1.top;
            trapezoid10.bottom = lsegment;
            trapezoid10.left = lsegment.left;
            trapezoid10.lLeft = null;
            trapezoid10.uLeft = trapezoid9;
            trapezoid10.name = "B" + numTrapezoids;

            numTrapezoids++;
            trapezoid11.top = lsegment;
            trapezoid11.bottom = trapezoid1.bottom;
            trapezoid11.left = lsegment.left;
            trapezoid11.lLeft = trapezoid9;
            trapezoid11.uLeft = null;
            trapezoid11.name = "C" + numTrapezoids;

            numTrapezoids++;
        } else
        {
            trapezoid10 = new Trapezoid();
            trapezoid11 = new Trapezoid();
            i += 2;
            trapezoid10.top = trapezoid1.top;
            trapezoid10.bottom = lsegment;
            trapezoid10.left = trapezoid1.left;
            trapezoid10.name = "B" + numTrapezoids;

            numTrapezoids++;
            trapezoid10.uLeft = trapezoid1.uLeft;
            trapezoid10.lLeft = null;
            trapezoid11.lLeft = trapezoid1.lLeft;
            trapezoid11.uLeft = null;
            if(trapezoid1.uLeft != null)
                trapezoid1.uLeft.uRight = trapezoid10;
            if(trapezoid1.lLeft != null)
                trapezoid1.lLeft.lRight = trapezoid11;
            trapezoid11.top = lsegment;
            trapezoid11.bottom = trapezoid1.bottom;
            trapezoid11.left = trapezoid1.left;
            trapezoid11.name = "C" + numTrapezoids;

            numTrapezoids++;
        }
        trapezoid10.uRight = trapezoid1.uRight;
        trapezoid10.lRight = null;
        trapezoid11.lRight = trapezoid1.lRight;
        trapezoid11.uRight = null;
        if(trapezoid1.uRight != null)
            trapezoid1.uRight.uLeft = trapezoid10;
        if(trapezoid1.lRight != null)
            trapezoid1.lRight.lLeft = trapezoid11;
        trapezoid10.node = trapezoid1.node;
        trapezoid11.node = trapezoid1.node;
        trapezoid1.end = trapezoid9;
        trapezoid1.up = trapezoid10;
        trapezoid1.down = trapezoid11;
        trapezoid1 = trapezoid1.next;
        trapezoid5 = trapezoid10;
        trapezoid7 = trapezoid11;
        for(; trapezoid1.next != null; trapezoid1 = trapezoid1.next)
        {
            Trapezoid trapezoid12 = new Trapezoid();
            trapezoid12.name = "U" + numTrapezoids;

            numTrapezoids++;
            i++;
            if(Computation.isAbove(lsegment.left, lsegment.right, trapezoid1.left))
            {
                trapezoid5.right = trapezoid1.left;
                trapezoid12.top = trapezoid1.top;
                trapezoid12.bottom = lsegment;
                trapezoid12.left = trapezoid1.left;
                trapezoid12.lLeft = trapezoid5;
                trapezoid12.uLeft = trapezoid1.uLeft;
                trapezoid12.uRight = trapezoid1.uRight;
                trapezoid5.lRight = trapezoid12;
                trapezoid5 = trapezoid12;
                if(trapezoid1.uLeft != null)
                    trapezoid1.uLeft.uRight = trapezoid12;
                if(trapezoid1.uRight != null)
                    trapezoid1.uRight.uLeft = trapezoid12;
                trapezoid7.lRight = trapezoid1.lRight;
                trapezoid7.node = trapezoid1.node;
            } else
            {
                trapezoid7.right = trapezoid1.left;
                trapezoid12.top = lsegment;
                trapezoid12.bottom = trapezoid1.bottom;
                trapezoid12.left = trapezoid1.left;
                trapezoid12.lLeft = trapezoid1.lLeft;
                trapezoid12.uLeft = trapezoid7;
                trapezoid12.lRight = trapezoid1.lRight;
                trapezoid7.uRight = trapezoid12;
                trapezoid7 = trapezoid12;
                if(trapezoid1.lLeft != null)
                    trapezoid1.lLeft.lRight = trapezoid12;
                if(trapezoid1.lRight != null)
                    trapezoid1.lRight.lLeft = trapezoid12;
                trapezoid5.uRight = trapezoid1.uRight;
                trapezoid5.node = trapezoid1.node;
            }
            trapezoid12.node = trapezoid1.node;
            trapezoid1.up = trapezoid5;
            trapezoid1.down = trapezoid7;
        }

        Trapezoid trapezoid13 = null;
        Trapezoid trapezoid14 = null;
        Trapezoid trapezoid15 = null;
        if(Computation.isAbove(lsegment.left, lsegment.right, trapezoid1.left))
        {
            trapezoid15 = trapezoid7;
            trapezoid15.right = lsegment.right;
            trapezoid14 = new Trapezoid();
            trapezoid14.top = trapezoid1.top;
            trapezoid14.bottom = lsegment;
            trapezoid14.left = trapezoid1.left;
            trapezoid14.right = lsegment.right;
            trapezoid14.lLeft = trapezoid5;
            trapezoid14.uLeft = trapezoid1.uLeft;
            if(trapezoid1.uLeft != null)
                trapezoid1.uLeft.uRight = trapezoid14;
            trapezoid14.name = "L" + numTrapezoids;

            numTrapezoids++;
            trapezoid5.lRight = trapezoid14;
            trapezoid5.right = trapezoid1.left;
            trapezoid14.node = trapezoid1.node;
        } else
        {
            trapezoid14 = trapezoid5;
            trapezoid14.right = lsegment.right;
            trapezoid15 = new Trapezoid();
            trapezoid15.top = lsegment;
            trapezoid15.bottom = trapezoid1.bottom;
            trapezoid15.left = trapezoid1.left;
            trapezoid15.right = lsegment.right;
            trapezoid15.lLeft = trapezoid1.lLeft;
            trapezoid15.uLeft = trapezoid7;
            if(trapezoid1.lLeft != null)
                trapezoid1.lLeft.lRight = trapezoid15;
            trapezoid15.name = "M" + numTrapezoids;

            numTrapezoids++;
            trapezoid7.uRight = trapezoid15;
            trapezoid7.right = trapezoid1.left;
            trapezoid15.node = trapezoid1.node;
        }
        if(lsegment.right != trapezoid1.right)
        {
            trapezoid13 = new Trapezoid();
            i++;
            trapezoid13.top = trapezoid1.top;
            trapezoid13.bottom = trapezoid1.bottom;
            trapezoid13.left = lsegment.right;
            trapezoid13.right = trapezoid1.right;
            trapezoid13.lLeft = trapezoid15;
            trapezoid13.uLeft = trapezoid14;
            trapezoid13.lRight = trapezoid1.lRight;
            trapezoid13.uRight = trapezoid1.uRight;
            trapezoid13.name = "N" + numTrapezoids;

            numTrapezoids++;
            trapezoid13.node = trapezoid1.node;
            upDateRightNeighbors(trapezoid13, trapezoid1);
            trapezoid14.lRight = null;
            trapezoid14.uRight = trapezoid13;
            trapezoid15.lRight = trapezoid13;
            trapezoid15.uRight = null;
        } else
        {
            trapezoid14.uRight = trapezoid1.uRight;
            trapezoid14.lRight = null;
            trapezoid15.lRight = trapezoid1.lRight;
            trapezoid15.uRight = null;
            if(trapezoid1.uRight != null)
                trapezoid1.uRight.uLeft = trapezoid14;
            if(trapezoid1.lRight != null)
                trapezoid1.lRight.lLeft = trapezoid15;
        }
        trapezoid1.up = trapezoid14;
        trapezoid1.down = trapezoid15;
        trapezoid1.end = trapezoid13;
        Trapezoid trapezoid16 = null;
        Trapezoid trapezoid17 = null;
        if(trapezoid.left != lsegment.left)
            trapezoid16 = trapezoid;
        if(trapezoid1.right != lsegment.right)
            trapezoid17 = trapezoid1;
        return trapezoid3;
    }

    private void updateD(Trapezoid trapezoid, Segment lsegment, Trapezoid trapezoid1)
    {
        if(trapezoid.next == null)
        {
            Node node = trapezoid1.node;
            Trapezoid trapezoid3 = node.t;
            if(trapezoid3.left != lsegment.left)
            {
                node.node = "x";
                node.p = lsegment.left;
                node.t = null;
                node.left = new Node();
                node.left.node = "t";
                node.left.t = trapezoid1;
                node.left.t.node = node.left;
                node.right = new Node();
                node = node.right;
                trapezoid1 = trapezoid1.next;
            }
            if(trapezoid3.right != lsegment.right)
            {
                node.node = "x";
                node.p = lsegment.right;
                node.t = null;
                node.right = new Node();
                node.right.node = "t";
                node.right.t = trapezoid1;
                node.right.t.node = node.right;
                trapezoid1 = trapezoid1.next;
                node.left = new Node();
                node.left.node = "y";
                node.left.segment = lsegment;
                node.left.t = null;
                node.left.left = new Node();
                node.left.left.node = "t";
                node.left.left.t = trapezoid1;
                node.left.left.t.node = node.left.left;
                trapezoid1 = trapezoid1.next;
                node.left.right = new Node();
                node.left.right.node = "t";
                node.left.right.t = trapezoid1;
                node.left.right.t.node = node.left.right;
                return;
            } else
            {
                node.node = "y";
                node.segment = lsegment;
                node.t = null;
                node.left = new Node();
                node.left.node = "t";
                node.left.t = trapezoid1;
                node.left.t.node = node.left;
                trapezoid1 = trapezoid1.next;
                node.right = new Node();
                node.right.node = "t";
                node.right.t = trapezoid1;
                node.right.t.node = node.right;
                return;
            }
        }
        Trapezoid trapezoid2 = null;
        Node node1 = trapezoid.node;
        if(trapezoid.left != lsegment.left)
        {
            node1.node = "x";
            node1.p = lsegment.left;
            node1.t = null;
            node1.left = new Node();
            node1.left.node = "t";
            node1.left.t = trapezoid.end;
            node1.left.t.node = node1.left;
            node1.right = new Node();
            node1.right.node = "y";
            node1.right.segment = lsegment;
            node1.right.left = new Node();
            node1.right.left.node = "t";
            node1.right.left.t = trapezoid.up;
            node1.right.left.t.node = node1.right.left;
            node1.right.right = new Node();
            node1.right.right.node = "t";
            node1.right.right.t = trapezoid.down;
            node1.right.right.t.node = node1.right.right;
            trapezoid.end.next = null;
            trapezoid2 = trapezoid.end;
            trapezoid.up.next = trapezoid2;
            trapezoid2 = trapezoid.up;
            trapezoid.down.next = trapezoid2;
            trapezoid2 = trapezoid.down;
        } else
        {
            node1.node = "y";
            node1.segment = lsegment;
            node1.t = null;
            node1.left = new Node();
            node1.left.node = "t";
            node1.left.t = trapezoid.up;
            node1.left.t.node = node1.left;
            node1.right = new Node();
            node1.right.node = "t";
            node1.right.t = trapezoid.down;
            node1.right.t.node = node1.right;
            trapezoid.up.next = null;
            trapezoid2 = trapezoid.up;
            trapezoid.down.next = trapezoid2;
            trapezoid2 = trapezoid.down;
        }
        trapezoid = trapezoid.next;
        boolean flag = false;
        boolean flag1 = false;
        while(trapezoid.next != null) 
        {
            Trapezoid trapezoid4 = trapezoid2;
            node1 = trapezoid.node;
            node1.node = "y";
            node1.segment = lsegment;
            node1.t = null;
            for(; trapezoid4 != null; trapezoid4 = trapezoid4.next)
            {
                if(trapezoid4 == trapezoid.up)
                    flag = true;
                if(trapezoid4 == trapezoid.down)
                    flag1 = true;
            }

            if(flag)
            {
                node1.left = trapezoid.up.node;
            } else
            {
                node1.left = new Node();
                node1.left.node = "t";
                node1.left.t = trapezoid.up;
                node1.left.t.node = node1.left;
                trapezoid.up.next = trapezoid2;
                trapezoid2 = trapezoid.up;
            }
            if(flag1)
            {
                node1.right = trapezoid.down.node;
            } else
            {
                node1.right = new Node();
                node1.right.node = "t";
                node1.right.t = trapezoid.down;
                node1.right.t.node = node1.right;
                trapezoid.down.next = trapezoid2;
                trapezoid2 = trapezoid.down;
            }
            trapezoid = trapezoid.next;
            flag1 = false;
            flag = false;
        }
        node1 = trapezoid.node;
        for(Trapezoid trapezoid5 = trapezoid2; trapezoid5 != null; trapezoid5 = trapezoid5.next)
        {
            if(trapezoid5 == trapezoid.up)
                flag = true;
            if(trapezoid5 == trapezoid.down)
                flag1 = true;
        }

        if(trapezoid.right != lsegment.right)
        {
            node1.node = "x";
            node1.p = lsegment.right;
            node1.t = null;
            node1.right = new Node();
            node1.right.node = "t";
            node1.right.t = trapezoid.end;
            node1.right.t.node = node1.right;
            node1.left = new Node();
            node1.left.node = "y";
            node1.left.segment = lsegment;
            if(flag1)
            {
                node1.left.right = trapezoid.down.node;
            } else
            {
                node1.left.right = new Node();
                node1.left.right.node = "t";
                node1.left.right.t = trapezoid.down;
                node1.left.right.t.node = node1.left.right;
            }
            if(flag)
            {
                node1.left.left = trapezoid.up.node;
                return;
            } else
            {
                node1.left.left = new Node();
                node1.left.left.node = "t";
                node1.left.left.t = trapezoid.up;
                node1.left.left.t.node = node1.left.left;
                return;
            }
        }
        node1.node = "y";
        node1.segment = lsegment;
        node1.t = null;
        if(flag1)
        {
            node1.right = trapezoid.down.node;
        } else
        {
            node1.right = new Node();
            node1.right.node = "t";
            node1.right.t = trapezoid.down;
            node1.right.t.node = node1.right;
        }
        if(flag)
        {
            node1.left = trapezoid.up.node;
            return;
        } else
        {
            node1.left = new Node();
            node1.left.node = "t";
            node1.left.t = trapezoid.up;
            node1.left.t.node = node1.left;
            return;
        }
    }
    
    public Trapezoid findPoint(Point point)
    {       
        point.x = point.x + (perm*(point.y));
        Node node = D;
            while(true)
            {
            if("x".equals(node.node))
            {
                if(node.isLeft(point))
                {
                    node = node.left;

                } else
                {
                    node = node.right;
                }
                continue;
            }
            if(!"y".equals(node.node)){
                System.out.println("Il punto cercato giace sulla faccia: " + node.t.name);
                return node.t;
            }
            if(Computation.isAbove(point, node.segment.left, node.segment.right))
            {
                node = node.left;
            } else
            {
                node = node.right;
            }
        }
    }
    
    public static void main(String [ ] args){
        
        Point a = new Point(2,2, "a");
        Point b = new Point(4,2, "b");
        Point c = new Point(9,2, "c");
        Point d = new Point(4,4, "d");
        Point e = new Point(4,6, "e");
        Point f = new Point(1,8, "f");
        Point g = new Point(6,10, "g");
        Point h = new Point(11,10, "h");
        /*
        Point e = new Point(7,4, "e");
        Point f = new Point(12,9, "f");
        Point g = new Point(6,7, "g");
        Point h = new Point(8,12, "h");
        Point i = new Point(10,13, "i");*/
        Segment[] seg = new Segment[4];
        seg[0] = new Segment(a, d, "0");
        seg[1] = new Segment(b, c, "1");
        seg[2]  = new Segment(e, f, "2");
        seg[3]  = new Segment(g, h, "3");/*
        seg[4]  = new Segment(a, e, "4");
        seg[5]  = new Segment(a, c, "5");
        seg[6]  = new Segment(c, d, "6");
        seg[7]  = new Segment(d, f, "7");
        seg[8]  = new Segment(f, g, "8");*/
        
        
        TrapezoidalMaps mappa = new TrapezoidalMaps(seg, null);
        mappa.createMap();
        
        Point mioP1 = new Point(2,2, "mp1");
        Point mioP2 = new Point(2,5, "mp2");
        Point mioP3 = new Point(1,9, "mp3");
        mappa.findPoint(mioP1);
        mappa.findPoint(mioP2);
        mappa.findPoint(mioP3);
        
        /*boolean flag = seg.isAbove(c);
        System.out.println(flag);*/
        
   }
    
}
