/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.bstguesser;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

public class TreeNode {
    private static final int SIZE = 60;
    private static final int MARGIN = 20;
    private int value, height;
    protected TreeNode left, right;
    private boolean showValue;
    private int x, y;
    private int color = Color.rgb(150, 150, 250);

    public TreeNode(int value) {
        this.value = value;
        this.height = 0;
        showValue = false;
        left = null;
        right = null;
    }
    public TreeNode(TreeNode sour) {
        this.value = sour.value;
        this.height = sour.height;
        showValue = false;
        left = sour.left;
        right = sour.right;
    }

    public void insert(int valueToInsert) {
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        if (valueToInsert <= this.value) {
            if (this.left == null)
                this.left = new TreeNode(valueToInsert);
            else
                this.left.insert(valueToInsert);
        } else {
            if (this.right == null)
                this.right = new TreeNode(valueToInsert);
            else
                this.right.insert(valueToInsert);
        }

        if (this.left != null && this.right != null) {

            if (this.left.height > this.right.height)
                this.height = this.left.height + 1;
            else
                this.height = this.right.height + 1;

        } else if (this.left == null) {

            this.height = this.right.height + 1;

        } else if (this.right == null) {

            this.height = this.left.height + 1;

        }
        int h=hdiff(this);
        balanceTree(h,valueToInsert);

    }
    public void balanceTree(int h,int valueToInsert)
    {
        if(h<-1)
        {
            if(valueToInsert>right.value)
            {leftrot();}
            else
            {
                right.rightrot();
                leftrot();
            }
        }
        else if(h>1)
        {
            if(valueToInsert<left.value)
            {
                rightrot();
            }
            else
            {
                left.leftrot();
                rightrot();
            }
        }
        height=reCalcHeight();

    }
    public int hdiff(TreeNode tn)
    {
        if(tn.left==null&&tn.right==null)
            return 0;
        else if(tn.left==null)
            return 0-tn.right.height;
        else if(tn.right==null)
            return  tn.left.height-0;
        else
            return tn.left.height-tn.right.height;
    }


    public TreeNode search(int gevalue)
    {
        if(this.value==gevalue) return this;
        else if(this.value>gevalue) return this.left.search(gevalue);
        else return this.right.search(gevalue);
    }

    public void positionSelf(int x0, int x1, int y) {
        this.y = y;
        x = (x0 + x1) / 2;

        if(left != null) {
            left.positionSelf(x0, right == null ? x1 - 2 * MARGIN : x, y + SIZE + MARGIN);
        }
        if (right != null) {
            right.positionSelf(left == null ? x0 + 2 * MARGIN : x, x1, y + SIZE + MARGIN);
        }
    }

    public void draw(Canvas c) {
        Paint linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3);
        linePaint.setColor(Color.GRAY);
        if (left != null)
            c.drawLine(x, y + SIZE/2, left.x, left.y + SIZE/2, linePaint);
        if (right != null)
            c.drawLine(x, y + SIZE/2, right.x, right.y + SIZE/2, linePaint);

        Paint fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(color);
        c.drawRect(x-SIZE/2, y, x+SIZE/2, y+SIZE, fillPaint);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(SIZE * 2/3);
        paint.setTextAlign(Paint.Align.CENTER);
        c.drawText(showValue ? String.valueOf(value) : "?", x, y + SIZE * 3/4, paint);

        if (height > 0) {
            Paint heightPaint = new Paint();
            heightPaint.setColor(Color.MAGENTA);
            heightPaint.setTextSize(SIZE * 2 / 3);
            heightPaint.setTextAlign(Paint.Align.LEFT);
            c.drawText(String.valueOf(height), x + SIZE / 2 + 10, y + SIZE * 3 / 4, heightPaint);
        }

        if (left != null)
            left.draw(c);
        if (right != null)
            right.draw(c);
    }

    public int click(float clickX, float clickY, int target) {
        int hit = -1;
        if (Math.abs(x - clickX) <= (SIZE / 2) && y <= clickY && clickY <= y + SIZE) {
            if (!showValue) {
                if (target != value) {
                    color = Color.RED;
                } else {
                    color = Color.GREEN;
                }
            }
            showValue = true;
            hit = value;
        }
        if (left != null && hit == -1)
            hit = left.click(clickX, clickY, target);
        if (right != null && hit == -1)
            hit = right.click(clickX, clickY, target);
        return hit;
    }

    public void invalidate() {
        color = Color.CYAN;
        showValue = true;
    }
    public void leftrot()
    {
       TreeNode oriPa,oriChi,oriGC;
       oriPa=new TreeNode(this);
       oriChi=new TreeNode(this.right);
       if(right.left!=null)
       oriGC=new TreeNode(this.right.left);
       else
       oriGC=null;
       oriChi.left=oriPa;
       oriPa.right=oriGC;
       this.value=oriChi.value;
       this.left=oriChi.left;
       this.right=oriChi.right;
    }

    public void rightrot()
    {
        TreeNode oriPa,oriChi,oriGC;
        oriPa=new TreeNode(this);
        oriChi=new TreeNode(this.left);
        if(left.right!=null)
            oriGC=new TreeNode(this.left.right);
        else
            oriGC=null;
        oriChi.right=oriPa;
        oriPa.left=oriGC;
        this.value=oriChi.value;
        this.right=oriChi.right;
        this.left=oriChi.left;

    }

    public int reCalcHeight()
    {
        if(this.left==null&&this.right==null) {
             this.height=0;
        }
        else if(this.left==null)
        {
            this.height=this.right.reCalcHeight()+1;
        }
        else if(this.right==null)
        {
            this.height=this.left.reCalcHeight()+1;
        }
        else
        {
            int lh,rh;
            lh=this.left.reCalcHeight();
            rh=this.right.reCalcHeight();
            if(lh>rh)
                this.height=lh+1;
            else
                this.height=rh+1;
        }
        return this.height;
    }
}
