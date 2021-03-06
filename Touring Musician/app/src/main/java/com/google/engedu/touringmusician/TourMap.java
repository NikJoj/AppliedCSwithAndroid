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

package com.google.engedu.touringmusician;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Node;

public class TourMap extends View {

    private Bitmap mapImage;
    private CircularLinkedList list = new CircularLinkedList();
    private String insertMode = "Add";

    public TourMap(Context context) {
        super(context);
        mapImage = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.map);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mapImage, 0, 0, null);
        Paint pointPaint = new Paint();
        Paint linepaint = new Paint();
        linepaint.setColor(Color.BLACK);
        linepaint.setStrokeWidth(3);
        pointPaint.setColor(Color.RED);
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        Point lastpoint,firstpoint;
        if(!list.headnull()) {
            firstpoint=list.rethead();
            lastpoint=firstpoint;
            for (Point p : list) {
               // if(p.x<mapImage.getHeight()+0&&p.y<mapImage.getWidth()+0)
                //Log.d("points",p.toString()+" "+lastpoint.toString());
                canvas.drawCircle(p.x, p.y, 20, pointPaint);
                if(lastpoint!=p);
                canvas.drawLine(lastpoint.x,lastpoint.y,p.x,p.y,linepaint);
                lastpoint=p;
            }

            canvas.drawLine(lastpoint.x,lastpoint.y,firstpoint.x,firstpoint.y,linepaint);
        }
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Point p = new Point((int) event.getX(), (int)event.getY());
                if (insertMode.equals("Closest")) {
                    list.insertNearest(p);
                } else if (insertMode.equals("Smallest")) {
                    list.insertSmallest(p);
                } else {
                    list.insertBeginning(p);
                }
                TextView message = (TextView) ((Activity) getContext()).findViewById(R.id.game_status);
                if (message != null) {
                    message.setText(String.format("Tour length is now %.2f", list.totalDistance()));
                }
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void reset() {
        list.reset();
        invalidate();
    }

    public void setInsertMode(String mode) {
        insertMode = mode;
    }
}
