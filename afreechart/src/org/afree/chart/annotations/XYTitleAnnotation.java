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
 * ----------------------
 * XYTitleAnnotation.java
 * ----------------------
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
 * 
 * ------------- JFreeChart ---------------------------------------------
 * (C) Copyright 2007, 2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Andrew Mickish;
 *
 * Changes:
 * --------
 * 02-Feb-2007 : Version 1 (DG);
 * 30-Apr-2007 : Fixed equals() method (DG);
 * 26-Feb-2008 : Fixed NullPointerException when drawing chart with a null
 *               ChartRenderingInfo - see patch 1901599 by Andrew Mickish (DG);
 * 03-Sep-2008 : Moved from experimental to main (DG);
 *
 */

package org.afree.chart.annotations;

import java.io.Serializable;

import android.graphics.Canvas;
import android.graphics.PointF;

import org.afree.util.ObjectUtilities;
import org.afree.util.PublicCloneable;
import org.afree.ui.RectangleAnchor;
import org.afree.ui.RectangleEdge;
import org.afree.ui.Size2D;
import org.afree.chart.axis.AxisLocation;
import org.afree.chart.axis.ValueAxis;
import org.afree.chart.block.BlockParams;
import org.afree.chart.block.EntityBlockResult;
import org.afree.chart.block.RectangleConstraint;
import org.afree.chart.HashUtilities;
import org.afree.data.Range;
import org.afree.chart.plot.Plot;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.plot.PlotRenderingInfo;
import org.afree.chart.plot.XYPlot;
import org.afree.chart.title.Title;
import org.afree.chart.util.XYCoordinateType;
import org.afree.graphics.geom.RectShape;

/**
 * An annotation that allows any {@link Title} to be placed at a location on
 * an {@link XYPlot}.
 *
 * @since JFreeChart 1.0.11
 */
public class XYTitleAnnotation extends AbstractXYAnnotation
        implements Cloneable, PublicCloneable, Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -4364694501921559958L;

    /** The coordinate type. */
    private XYCoordinateType coordinateType;

    /** The x-coordinate (in data space). */
    private double x;

    /** The y-coordinate (in data space). */
    private double y;

    /** The maximum width. */
    private double maxWidth;

    /** The maximum height. */
    private double maxHeight;

    /** The title. */
    private Title title;

    /**
     * The title anchor point.
     */
    private RectangleAnchor anchor;

    /**
     * Creates a new annotation to be displayed at the specified (x, y)
     * location.
     *
     * @param x  the x-coordinate (in data space).
     * @param y  the y-coordinate (in data space).
     * @param title  the title (<code>null</code> not permitted).
     */
    public XYTitleAnnotation(double x, double y, Title title) {
        this(x, y, title, RectangleAnchor.CENTER);
    }

    /**
     * Creates a new annotation to be displayed at the specified (x, y)
     * location.
     *
     * @param x  the x-coordinate (in data space).
     * @param y  the y-coordinate (in data space).
     * @param title  the title (<code>null</code> not permitted).
     * @param anchor  the title anchor (<code>null</code> not permitted).
     */
    public XYTitleAnnotation(double x, double y, Title title,
            RectangleAnchor anchor) {
        if (title == null) {
            throw new IllegalArgumentException("Null 'title' argument.");
        }
        if (anchor == null) {
            throw new IllegalArgumentException("Null 'anchor' argument.");
        }
        this.coordinateType = XYCoordinateType.RELATIVE;
        this.x = x;
        this.y = y;
        this.maxWidth = 0.0;
        this.maxHeight = 0.0;
        this.title = title;
        this.anchor = anchor;
    }

    /**
     * Returns the coordinate type (set in the constructor).
     *
     * @return The coordinate type (never <code>null</code>).
     */
    public XYCoordinateType getCoordinateType() {
        return this.coordinateType;
    }

    /**
     * Returns the x-coordinate for the annotation.
     *
     * @return The x-coordinate.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Returns the y-coordinate for the annotation.
     *
     * @return The y-coordinate.
     */
    public double getY() {
        return this.y;
    }

    /**
     * Returns the title for the annotation.
     *
     * @return The title.
     */
    public Title getTitle() {
        return this.title;
    }

    /**
     * Returns the title anchor for the annotation.
     *
     * @return The title anchor.
     */
    public RectangleAnchor getTitleAnchor() {
        return this.anchor;
    }

    /**
     * Returns the maximum width.
     *
     * @return The maximum width.
     */
    public double getMaxWidth() {
        return this.maxWidth;
    }

    /**
     * Sets the maximum width.
     *
     * @param max  the maximum width (0.0 or less means no maximum).
     */
    public void setMaxWidth(double max) {
        this.maxWidth = max;
    }

    /**
     * Returns the maximum height.
     *
     * @return The maximum height.
     */
    public double getMaxHeight() {
        return this.maxHeight;
    }

    /**
     * Sets the maximum height.
     *
     * @param max  the maximum height.
     */
    public void setMaxHeight(double max) {
        this.maxHeight = max;
    }

    /**
     * Draws the annotation.  This method is called by the drawing code in the
     * {@link XYPlot} class, you don't normally need to call this method
     * directly.
     *
     * @param canvas  the graphics device.
     * @param plot  the plot.
     * @param dataArea  the data area.
     * @param domainAxis  the domain axis.
     * @param rangeAxis  the range axis.
     * @param rendererIndex  the renderer index.
     * @param info  if supplied, this info object will be populated with
     *              entity information.
     */
    public void draw(Canvas canvas, XYPlot plot, RectShape dataArea,
                     ValueAxis domainAxis, ValueAxis rangeAxis,
                     int rendererIndex,
                     PlotRenderingInfo info) {

        PlotOrientation orientation = plot.getOrientation();
        AxisLocation domainAxisLocation = plot.getDomainAxisLocation();
        AxisLocation rangeAxisLocation = plot.getRangeAxisLocation();
        RectangleEdge domainEdge = Plot.resolveDomainAxisLocation(
                domainAxisLocation, orientation);
        RectangleEdge rangeEdge = Plot.resolveRangeAxisLocation(
                rangeAxisLocation, orientation);
        Range xRange = domainAxis.getRange();
        Range yRange = rangeAxis.getRange();
        double anchorX = 0.0;
        double anchorY = 0.0;
        if (this.coordinateType == XYCoordinateType.RELATIVE) {
            anchorX = xRange.getLowerBound() + (this.x * xRange.getLength());
            anchorY = yRange.getLowerBound() + (this.y * yRange.getLength());
        }
        else {
            anchorX = domainAxis.valueToJava2D(this.x, dataArea, domainEdge);
            anchorY = rangeAxis.valueToJava2D(this.y, dataArea, rangeEdge);
        }

        float j2DX = (float) domainAxis.valueToJava2D(anchorX, dataArea,
                domainEdge);
        float j2DY = (float) rangeAxis.valueToJava2D(anchorY, dataArea,
                rangeEdge);
        float xx = 0.0f;
        float yy = 0.0f;
        if (orientation == PlotOrientation.HORIZONTAL) {
            xx = j2DY;
            yy = j2DX;
        }
        else if (orientation == PlotOrientation.VERTICAL) {
            xx = j2DX;
            yy = j2DY;
        }

        double maxW = dataArea.getWidth();
        double maxH = dataArea.getHeight();
        if (this.coordinateType == XYCoordinateType.RELATIVE) {
            if (this.maxWidth > 0.0) {
                maxW = maxW * this.maxWidth;
            }
            if (this.maxHeight > 0.0) {
                maxH = maxH * this.maxHeight;
            }
        }
        if (this.coordinateType == XYCoordinateType.DATA) {
            maxW = this.maxWidth;
            maxH = this.maxHeight;
        }
        RectangleConstraint rc = new RectangleConstraint(
                new Range(0, maxW), new Range(0, maxH));

        Size2D size = this.title.arrange(canvas, rc);
        RectShape titleRect = new RectShape(0, 0, size.width,
                size.height);
        PointF anchorPoint = RectangleAnchor.coordinates(titleRect,
                this.anchor);
        xx = xx - (float) anchorPoint.x;
        yy = yy - (float) anchorPoint.y;
        titleRect.setRect(xx, yy, titleRect.getWidth(), titleRect.getHeight());
        BlockParams p = new BlockParams();
        if (info != null) {
            if (info.getOwner().getEntityCollection() != null) {
                p.setGenerateEntities(true);
            }
        }
        Object result = this.title.draw(canvas, titleRect, p);
        if (info != null) {
            if (result instanceof EntityBlockResult) {
                EntityBlockResult ebr = (EntityBlockResult) result;
                info.getOwner().getEntityCollection().addAll(
                        ebr.getEntityCollection());
            }
            String toolTip = getToolTipText();
            String url = getURL();
            if (toolTip != null || url != null) {
                addEntity(info, new RectShape(xx, yy,
                        (float) size.width, (float) size.height),
                        rendererIndex, toolTip, url);
            }
        }
    }

    /**
     * Tests this object for equality with an arbitrary object.
     *
     * @param obj  the object (<code>null</code> permitted).
     *
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof XYTitleAnnotation)) {
            return false;
        }
        XYTitleAnnotation that = (XYTitleAnnotation) obj;
        if (this.coordinateType != that.coordinateType) {
            return false;
        }
        if (this.x != that.x) {
            return false;
        }
        if (this.y != that.y) {
            return false;
        }
        if (this.maxWidth != that.maxWidth) {
            return false;
        }
        if (this.maxHeight != that.maxHeight) {
            return false;
        }
        if (!ObjectUtilities.equal(this.title, that.title)) {
            return false;
        }
        if (!this.anchor.equals(that.anchor)) {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Returns a hash code for this object.
     *
     * @return A hash code.
     */
    public int hashCode() {
        int result = 193;
        result = HashUtilities.hashCode(result, this.anchor);
        result = HashUtilities.hashCode(result, this.coordinateType);
        result = HashUtilities.hashCode(result, this.x);
        result = HashUtilities.hashCode(result, this.y);
        result = HashUtilities.hashCode(result, this.maxWidth);
        result = HashUtilities.hashCode(result, this.maxHeight);
        result = HashUtilities.hashCode(result, this.title);
        return result;
    }

    /**
     * Returns a clone of the annotation.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException  if the annotation can't be cloned.
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
