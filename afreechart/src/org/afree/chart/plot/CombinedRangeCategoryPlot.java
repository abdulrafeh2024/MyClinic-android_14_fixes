/* ===========================================================
 * AFreeChart : a free chart library for Android(tm) platform.
 *              (based on JFreeChart and JCommon)
 * ===========================================================
 *
 * (C) Copyright 2010, by ICOMSYSTECH Co.,Ltd.
 * (C) Copyright 2000-2008, by Object Refinery Limited and Contributors.
 *
 * Project Info:
 *    AFreeChart: http://code.google.com/p/afreechart/
 *    JFreeChart: http://www.jfree.org/jfreechart/index.html
 *    JCommon   : http://www.jfree.org/jcommon/index.html
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * [Android is a trademark of Google Inc.]
 *
 * ------------------------------
 * CombinedRangeCategoryPlot.java
 * ------------------------------
 * 
 * (C) Copyright 2010, by ICOMSYSTECH Co.,Ltd.
 *
 * Original Author:  shiraki  (for ICOMSYSTECH Co.,Ltd);
 * Contributor(s):   Sato Yoshiaki ;
 *                   Niwano Masayoshi;
 *
 * Changes (from 19-Nov-2010)
 * --------------------------
 * 19-Nov-2010 : port JFreeChart 1.0.13 to Android as "AFreeChart"
 * 14-Jan-2011 : Updated API docs
 * 
 * ------------- JFreeChart ---------------------------------------------
 * (C) Copyright 2003-2008, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Nicolas Brodu;
 *
 * Changes:
 * --------
 * 16-May-2003 : Version 1 (DG);
 * 08-Aug-2003 : Adjusted totalWeight in remove() method (DG);
 * 19-Aug-2003 : Implemented Cloneable (DG);
 * 11-Sep-2003 : Fix cloning support (subplots) (NB);
 * 15-Sep-2003 : Implemented PublicCloneable.  Fixed errors in cloning and
 *               serialization (DG);
 * 16-Sep-2003 : Changed ChartRenderingInfo --> PlotRenderingInfo (DG);
 * 17-Sep-2003 : Updated handling of 'clicks' (DG);
 * 04-May-2004 : Added getter/setter methods for 'gap' attributes (DG);
 * 12-Nov-2004 : Implements the new Zoomable interface (DG);
 * 25-Nov-2004 : Small update to clone() implementation (DG);
 * 21-Feb-2005 : Fixed bug in remove() method (id = 1121172) (DG);
 * 21-Feb-2005 : The getLegendItems() method now returns the fixed legend
 *               items if set (DG);
 * 05-May-2005 : Updated draw() method parameters (DG);
 * 14-Nov-2007 : Updated setFixedDomainAxisSpaceForSubplots() method (DG);
 * 27-Mar-2008 : Add documentation for getDataRange() method (DG);
 * 31-Mar-2008 : Updated getSubplots() to return EMPTY_LIST for null
 *               subplots, as suggested by Richard West (DG);
 * 26-Jun-2008 : Fixed crosshair support (DG);
 * 11-Aug-2008 : Don't store totalWeight of subplots, calculate it as
 *               required (DG);
 *
 */

package org.afree.chart.plot;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.afree.ui.RectangleEdge;
import org.afree.ui.RectangleInsets;
import org.afree.chart.LegendItemCollection;
import org.afree.chart.axis.AxisSpace;
import org.afree.chart.axis.AxisState;
import org.afree.chart.axis.NumberAxis;
import org.afree.chart.axis.ValueAxis;
import org.afree.data.Range;
import org.afree.chart.event.PlotChangeEvent;
import org.afree.chart.event.PlotChangeListener;
import org.afree.graphics.geom.RectShape;
import android.graphics.Canvas;
import android.graphics.PointF;

/**
 * A combined category plot where the range axis is shared.
 */
public class CombinedRangeCategoryPlot extends CategoryPlot
        implements PlotChangeListener {

    /** For serialization. */
    private static final long serialVersionUID = 7260210007554504515L;

    /** Storage for the subplot references. */
    private List subplots;

    /** The gap between subplots. */
    private double gap;

    /** Temporary storage for the subplot areas. */
    private transient RectShape[] subplotArea;  // TODO: move to plot state

    /**
     * Default constructor.
     */
    public CombinedRangeCategoryPlot() {
        this(new NumberAxis());
    }

    /**
     * Creates a new plot.
     *
     * @param rangeAxis  the shared range axis.
     */
    public CombinedRangeCategoryPlot(ValueAxis rangeAxis) {
        super(null, null, rangeAxis, null);
        this.subplots = new java.util.ArrayList();
        this.gap = 5.0;
    }

    /**
     * Returns the space between subplots.
     *
     * @return The gap (in Canvas units).
     */
    public double getGap() {
        return this.gap;
    }

    /**
     * Sets the amount of space between subplots and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param gap  the gap between subplots (in Canvas units).
     */
    public void setGap(double gap) {
        this.gap = gap;
        fireChangeEvent();
    }

    /**
     * Adds a subplot (with a default 'weight' of 1) and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * <br><br>
     * You must ensure that the subplot has a non-null domain axis.  The range
     * axis for the subplot will be set to <code>null</code>.
     *
     * @param subplot  the subplot (<code>null</code> not permitted).
     */
    public void add(CategoryPlot subplot) {
        // defer argument checking
        add(subplot, 1);
    }

    /**
     * Adds a subplot and sends a {@link PlotChangeEvent} to all registered
     * listeners.
     * <br><br>
     * You must ensure that the subplot has a non-null domain axis.  The range
     * axis for the subplot will be set to <code>null</code>.
     *
     * @param subplot  the subplot (<code>null</code> not permitted).
     * @param weight  the weight (must be >= 1).
     */
    public void add(CategoryPlot subplot, int weight) {
        if (subplot == null) {
            throw new IllegalArgumentException("Null 'subplot' argument.");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Require weight >= 1.");
        }
        // store the plot and its weight
        subplot.setParent(this);
        subplot.setWeight(weight);
        subplot.setInsets(new RectangleInsets(0.0, 0.0, 0.0, 0.0));
        subplot.setRangeAxis(null);
        subplot.setOrientation(getOrientation());
        subplot.addChangeListener(this);
        this.subplots.add(subplot);
        // configure the range axis...
        ValueAxis axis = getRangeAxis();
        if (axis != null) {
            axis.configure();
        }
        fireChangeEvent();
    }

    /**
     * Removes a subplot from the combined chart.
     *
     * @param subplot  the subplot (<code>null</code> not permitted).
     */
    public void remove(CategoryPlot subplot) {
        if (subplot == null) {
            throw new IllegalArgumentException(" Null 'subplot' argument.");
        }
        int position = -1;
        int size = this.subplots.size();
        int i = 0;
        while (position == -1 && i < size) {
            if (this.subplots.get(i) == subplot) {
                position = i;
            }
            i++;
        }
        if (position != -1) {
            this.subplots.remove(position);
            subplot.setParent(null);
            subplot.removeChangeListener(this);

            ValueAxis range = getRangeAxis();
            if (range != null) {
                range.configure();
            }

            ValueAxis range2 = getRangeAxis(1);
            if (range2 != null) {
                range2.configure();
            }
            fireChangeEvent();
        }
    }

    /**
     * Returns the list of subplots.  The returned list may be empty, but is
     * never <code>null</code>.
     *
     * @return An unmodifiable list of subplots.
     */
    public List getSubplots() {
        if (this.subplots != null) {
            return Collections.unmodifiableList(this.subplots);
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * Calculates the space required for the axes.
     *
     * @param canvas  the graphics device.
     * @param plotArea  the plot area.
     *
     * @return The space required for the axes.
     */
    protected AxisSpace calculateAxisSpace(Canvas canvas,
                                           RectShape plotArea) {

        AxisSpace space = new AxisSpace();
        PlotOrientation orientation = getOrientation();

        // work out the space required by the domain axis...
        AxisSpace fixed = getFixedRangeAxisSpace();
        if (fixed != null) {
            if (orientation == PlotOrientation.VERTICAL) {
                space.setLeft(fixed.getLeft());
                space.setRight(fixed.getRight());
            }
            else if (orientation == PlotOrientation.HORIZONTAL) {
                space.setTop(fixed.getTop());
                space.setBottom(fixed.getBottom());
            }
        }
        else {
            ValueAxis valueAxis = getRangeAxis();
            RectangleEdge valueEdge = Plot.resolveRangeAxisLocation(
                    getRangeAxisLocation(), orientation);
            if (valueAxis != null) {
                space = valueAxis.reserveSpace(canvas, this, plotArea, valueEdge,
                        space);
            }
        }

        RectShape adjustedPlotArea = space.shrink(plotArea, null);
        // work out the maximum height or width of the non-shared axes...
        int n = this.subplots.size();
        int totalWeight = 0;
        for (int i = 0; i < n; i++) {
            CategoryPlot sub = (CategoryPlot) this.subplots.get(i);
            totalWeight += sub.getWeight();
        }
        // calculate plotAreas of all sub-plots, maximum vertical/horizontal
        // axis width/height
        this.subplotArea = new RectShape[n];
        double x = adjustedPlotArea.getX();
        double y = adjustedPlotArea.getY();
        double usableSize = 0.0;
        if (orientation == PlotOrientation.VERTICAL) {
            usableSize = adjustedPlotArea.getWidth() - this.gap * (n - 1);
        }
        else if (orientation == PlotOrientation.HORIZONTAL) {
            usableSize = adjustedPlotArea.getHeight() - this.gap * (n - 1);
        }

        for (int i = 0; i < n; i++) {
            CategoryPlot plot = (CategoryPlot) this.subplots.get(i);

            // calculate sub-plot area
            if (orientation == PlotOrientation.VERTICAL) {
                double w = usableSize * plot.getWeight() / totalWeight;
                this.subplotArea[i] = new RectShape(x, y, w,
                        adjustedPlotArea.getHeight());
                x = x + w + this.gap;
            }
            else if (orientation == PlotOrientation.HORIZONTAL) {
                double h = usableSize * plot.getWeight() / totalWeight;
                this.subplotArea[i] = new RectShape(x, y,
                        adjustedPlotArea.getWidth(), h);
                y = y + h + this.gap;
            }

            AxisSpace subSpace = plot.calculateDomainAxisSpace(canvas,
                    this.subplotArea[i], null);
            space.ensureAtLeast(subSpace);

        }

        return space;
    }

    /**
     * Draws the plot on a graphics device (such as the screen or a
     * printer).  Will perform all the placement calculations for each
     * sub-plots and then tell these to draw themselves.
     *
     * @param canvas  the graphics device.
     * @param area  the area within which the plot (including axis labels)
     *              should be drawn.
     * @param anchor  the anchor point (<code>null</code> permitted).
     * @param parentState  the parent state.
     * @param info  collects information about the drawing (<code>null</code>
     *              permitted).
     */
    public void draw(Canvas canvas, RectShape area, PointF anchor,
                     PlotState parentState,
                     PlotRenderingInfo info) {

        // set up info collection...
        if (info != null) {
            info.setPlotArea(area);
        }

        // adjust the drawing area for plot insets (if any)...
        RectangleInsets insets = getInsets();
        insets.trim(area);

        // calculate the data area...
        AxisSpace space = calculateAxisSpace(canvas, area);
        RectShape dataArea = space.shrink(area, null);

        // set the width and height of non-shared axis of all sub-plots
        setFixedDomainAxisSpaceForSubplots(space);

        // draw the shared axis
        ValueAxis axis = getRangeAxis();
        RectangleEdge rangeEdge = getRangeAxisEdge();
        double cursor = RectangleEdge.coordinate(dataArea, rangeEdge);
        AxisState state = axis.draw(canvas, cursor, area, dataArea, rangeEdge,
                info);
        if (parentState == null) {
            parentState = new PlotState();
        }
        parentState.getSharedAxisStates().put(axis, state);

        // draw all the charts
        for (int i = 0; i < this.subplots.size(); i++) {
            CategoryPlot plot = (CategoryPlot) this.subplots.get(i);
            PlotRenderingInfo subplotInfo = null;
            if (info != null) {
                subplotInfo = new PlotRenderingInfo(info.getOwner());
                info.addSubplotInfo(subplotInfo);
            }
            PointF subAnchor = null;
            if (anchor != null && this.subplotArea[i].contains(anchor)) {
                subAnchor = anchor;
            }
            plot.draw(canvas, this.subplotArea[i], subAnchor, parentState,
                    subplotInfo);
        }

        if (info != null) {
            info.setDataArea(dataArea);
        }

    }

    /**
     * Sets the orientation for the plot (and all the subplots).
     *
     * @param orientation  the orientation.
     */
    public void setOrientation(PlotOrientation orientation) {

        super.setOrientation(orientation);

        Iterator iterator = this.subplots.iterator();
        while (iterator.hasNext()) {
            CategoryPlot plot = (CategoryPlot) iterator.next();
            plot.setOrientation(orientation);
        }

    }

    /**
     * Returns a range representing the extent of the data values in this plot
     * (obtained from the subplots) that will be rendered against the specified
     * axis.  NOTE: This method is intended for internal AFreeChart use, and
     * is public only so that code in the axis classes can call it.  Since
     * only the range axis is shared between subplots, the AFreeChart code
     * will only call this method for the range values (although this is not
     * checked/enforced).
      *
      * @param axis  the axis.
      *
      * @return The range.
      */
     public Range getDataRange(ValueAxis axis) {
         Range result = null;
         if (this.subplots != null) {
             Iterator iterator = this.subplots.iterator();
             while (iterator.hasNext()) {
                 CategoryPlot subplot = (CategoryPlot) iterator.next();
                 result = Range.combine(result, subplot.getDataRange(axis));
             }
         }
         return result;
     }

    /**
     * Returns a collection of legend items for the plot.
     *
     * @return The legend items.
     */
    public LegendItemCollection getLegendItems() {
        LegendItemCollection result = getFixedLegendItems();
        if (result == null) {
            result = new LegendItemCollection();
            if (this.subplots != null) {
                Iterator iterator = this.subplots.iterator();
                while (iterator.hasNext()) {
                    CategoryPlot plot = (CategoryPlot) iterator.next();
                    LegendItemCollection more = plot.getLegendItems();
                    result.addAll(more);
                }
            }
        }
        return result;
    }

    /**
     * Sets the size (width or height, depending on the orientation of the
     * plot) for the domain axis of each subplot.
     *
     * @param space  the space.
     */
    protected void setFixedDomainAxisSpaceForSubplots(AxisSpace space) {
        Iterator iterator = this.subplots.iterator();
        while (iterator.hasNext()) {
            CategoryPlot plot = (CategoryPlot) iterator.next();
            plot.setFixedDomainAxisSpace(space, false);
        }
    }

    /**
     * Handles a 'click' on the plot by updating the anchor value.
     *
     * @param x  x-coordinate of the click.
     * @param y  y-coordinate of the click.
     * @param info  information about the plot's dimensions.
     *
     */
    public void handleClick(int x, int y, PlotRenderingInfo info) {

        RectShape dataArea = info.getDataArea();
        if (dataArea.contains(x, y)) {
            for (int i = 0; i < this.subplots.size(); i++) {
                CategoryPlot subplot = (CategoryPlot) this.subplots.get(i);
                PlotRenderingInfo subplotInfo = info.getSubplotInfo(i);
                subplot.handleClick(x, y, subplotInfo);
            }
        }

    }

    /**
     * Receives a {@link PlotChangeEvent} and responds by notifying all
     * listeners.
     *
     * @param event  the event.
     */
    public void plotChanged(PlotChangeEvent event) {
        notifyListeners(event);
    }

    /**
     * Tests the plot for equality with an arbitrary object.
     *
     * @param obj  the object (<code>null</code> permitted).
     *
     * @return <code>true</code> or <code>false</code>.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CombinedRangeCategoryPlot)) {
            return false;
        }
        CombinedRangeCategoryPlot that = (CombinedRangeCategoryPlot) obj;
        if (this.gap != that.gap) {
            return false;
        }
        /* TODO: port ObjectUtilities
        if (!ObjectUtilities.equal(this.subplots, that.subplots)) {
            return false;
        }*/
        return super.equals(obj);
    }

    /**
     * Returns a clone of the plot.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException  this class will not throw this
     *         exception, but subclasses (if any) might.
     */
    /* TODO: port ObjectUtilities
    public Object clone() throws CloneNotSupportedException {
        CombinedRangeCategoryPlot result
            = (CombinedRangeCategoryPlot) super.clone();
        result.subplots = (List) ObjectUtilities.deepClone(this.subplots);
        for (Iterator it = result.subplots.iterator(); it.hasNext();) {
            Plot child = (Plot) it.next();
            child.setParent(result);
        }

        // after setting up all the subplots, the shared range axis may need
        // reconfiguring
        ValueAxis rangeAxis = result.getRangeAxis();
        if (rangeAxis != null) {
            rangeAxis.configure();
        }

        return result;
    }*/

    /**
     * Provides serialization support.
     *
     * @param stream  the input stream.
     *
     * @throws IOException  if there is an I/O error.
     * @throws ClassNotFoundException  if there is a classpath problem.
     */
    private void readObject(ObjectInputStream stream)
        throws IOException, ClassNotFoundException {

        stream.defaultReadObject();

        // the range axis is deserialized before the subplots, so its value
        // range is likely to be incorrect...
        ValueAxis rangeAxis = getRangeAxis();
        if (rangeAxis != null) {
            rangeAxis.configure();
        }

    }

}
