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
 * -------------------
 * XYItemRenderer.java
 * -------------------
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
 * (C) Copyright 2001-2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Mark Watson (www.markwatson.com);
 *                   Sylvain Vieujot;
 *                   Focus Computer Services Limited;
 *                   Richard Atkinson;
 *
 * Changes
 * -------
 * 19-Oct-2001 : Version 1, based on code by Mark Watson (DG);
 * 22-Oct-2001 : Renamed DataSource.java --> Dataset.java etc. (DG);
 * 13-Dec-2001 : Changed return type of drawItem from void --> Shape.  The area
 *               returned can be used as the tooltip region.
 * 23-Jan-2002 : Added DrawInfo parameter to drawItem() method (DG);
 * 28-Mar-2002 : Added a property change listener mechanism.  Now renderers do
 *               not have to be immutable (DG);
 * 04-Apr-2002 : Added the initialise() method (DG);
 * 09-Apr-2002 : Removed the translated zero from the drawItem method, it can
 *               be calculated inside the initialise method if it is required.
 *               Added a new getToolTipGenerator() method.  Changed the return
 *               type for drawItem() to void (DG);
 * 24-May-2002 : Added ChartRenderingInfo the initialise method API (DG);
 * 25-Jun-2002 : Removed redundant import (DG);
 * 20-Aug-2002 : Added get/setURLGenerator methods to interface (DG);
 * 02-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 * 18-Nov-2002 : Added methods for drawing grid lines (DG);
 * 17-Jan-2003 : Moved plot classes into a separate package (DG);
 * 27-Jan-2003 : Added shape lookup table (DG);
 * 05-Jun-2003 : Added domain and range grid bands (sponsored by Focus Computer
 *               Services Ltd) (DG);
 * 27-Jul-2003 : Added getRangeType() to support stacked XY area charts (RA);
 * 16-Sep-2003 : Changed ChartRenderingInfo --> PlotRenderingInfo (DG);
 * 25-Feb-2004 : Replaced CrosshairInfo with CrosshairState.  Renamed
 *               XYToolTipGenerator --> XYItemLabelGenerator (DG);
 * 26-Feb-2004 : Added lots of new methods (DG);
 * 30-Apr-2004 : Added getRangeExtent() method (DG);
 * 06-May-2004 : Added methods for controlling item label visibility (DG);
 * 13-May-2004 : Removed property change listener mechanism (DG);
 * 18-May-2004 : Added item label font and paint methods (DG);
 * 10-Sep-2004 : Removed redundant getRangeType() method (DG);
 * 06-Oct-2004 : Replaced getRangeExtent() with findRangeBounds() and added
 *               findDomainBounds (DG);
 * 23-Nov-2004 : Changed drawRangeGridLine() --> drawRangeLine() (DG);
 * 07-Jan-2005 : Removed deprecated method (DG);
 * 24-Feb-2005 : Now extends LegendItemSource (DG);
 * 20-Apr-2005 : Renamed XYLabelGenerator --> XYItemLabelGenerator (DG);
 * ------------- JFREECHART 1.0.x ---------------------------------------------
 * 19-Apr-2007 : Deprecated seriesVisible and seriesVisibleInLegend flags (DG);
 * 20-Apr-2007 : Deprecated paint, fillPaint, outlinePaint, stroke,
 *               outlineStroke, shape, itemLabelsVisible, itemLabelFont,
 *               itemLabelPaint, positiveItemLabelPosition,
 *               negativeItemLabelPosition and createEntities override
 *               fields (DG);
 *
 */

package org.afree.chart.renderer.xy;

import org.afree.ui.Layer;
import org.afree.chart.LegendItem;
import org.afree.chart.LegendItemSource;
import org.afree.chart.annotations.XYAnnotation;
import org.afree.chart.axis.ValueAxis;
import org.afree.data.Range;
import org.afree.data.xy.XYDataset;
import org.afree.chart.event.RendererChangeEvent;
import org.afree.chart.event.RendererChangeListener;
import org.afree.chart.labels.ItemLabelPosition;
import org.afree.chart.labels.XYItemLabelGenerator;
import org.afree.chart.labels.XYSeriesLabelGenerator;
import org.afree.chart.plot.CrosshairState;
import org.afree.chart.plot.Marker;
import org.afree.chart.plot.PlotRenderingInfo;
import org.afree.chart.plot.XYPlot;
import org.afree.graphics.geom.Font;
import org.afree.graphics.geom.RectShape;
import org.afree.graphics.geom.Shape;
import org.afree.graphics.PaintType;
import android.graphics.Canvas;
import android.graphics.PathEffect;



/**
 * Interface for rendering the visual representation of a single (x, y) item on
 * an {@link XYPlot}.
 * <p>
 * To support cloning charts, it is recommended that renderers implement both
 * the {@link Cloneable} and <code>PublicCloneable</code> interfaces.
 */
public interface XYItemRenderer extends LegendItemSource {

    /**
     * Returns the plot that this renderer has been assigned to.
     *
     * @return The plot.
     */
    public XYPlot getPlot();

    /**
     * Sets the plot that this renderer is assigned to.  This method will be
     * called by the plot class...you do not need to call it yourself.
     *
     * @param plot  the plot.
     */
    public void setPlot(XYPlot plot);

    /**
     * Returns the number of passes through the data required by the renderer.
     *
     * @return The pass count.
     */
    public int getPassCount();

    /**
     * Returns the lower and upper bounds (range) of the x-values in the
     * specified dataset.
     *
     * @param dataset  the dataset (<code>null</code> permitted).
     *
     * @return The range.
     */
    public Range findDomainBounds(XYDataset dataset);

    /**
     * Returns the lower and upper bounds (range) of the y-values in the
     * specified dataset.  The implementation of this method will take
     * into account the presentation used by the renderers (for example,
     * a renderer that "stacks" values will return a bigger range than
     * a renderer that doesn't).
     *
     * @param dataset  the dataset (<code>null</code> permitted).
     *
     * @return The range (or <code>null</code> if the dataset is
     *         <code>null</code> or empty).
     */
    public Range findRangeBounds(XYDataset dataset);

    /**
     * Add a renderer change listener.
     *
     * @param listener  the listener.
     *
     * @see #removeChangeListener(RendererChangeListener)
     */
    public void addChangeListener(RendererChangeListener listener);

    /**
     * Removes a change listener.
     *
     * @param listener  the listener.
     *
     * @see #addChangeListener(RendererChangeListener)
     */
    public void removeChangeListener(RendererChangeListener listener);


    //// VISIBLE //////////////////////////////////////////////////////////////

    /**
     * Returns a boolean that indicates whether or not the specified item
     * should be drawn (this is typically used to hide an entire series).
     *
     * @param series  the series index.
     * @param item  the item index.
     *
     * @return A boolean.
     */
    public boolean getItemVisible(int series, int item);

    /**
     * Returns a boolean that indicates whether or not the specified series
     * should be drawn (this is typically used to hide an entire series).
     *
     * @param series  the series index.
     *
     * @return A boolean.
     */
    public boolean isSeriesVisible(int series);

    /**
     * Returns the flag that controls whether a series is visible.
     *
     * @param series  the series index (zero-based).
     *
     * @return The flag (possibly <code>null</code>).
     *
     * @see #setSeriesVisible(int, Boolean)
     */
    public Boolean getSeriesVisible(int series);

    /**
     * Sets the flag that controls whether a series is visible and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero-based).
     * @param visible  the flag (<code>null</code> permitted).
     *
     * @see #getSeriesVisible(int)
     */
    public void setSeriesVisible(int series, Boolean visible);

    /**
     * Sets the flag that controls whether a series is visible and, if
     * requested, sends a {@link RendererChangeEvent} to all registered
     * listeners.
     *
     * @param series  the series index.
     * @param visible  the flag (<code>null</code> permitted).
     * @param notify  notify listeners?
     *
     * @see #getSeriesVisible(int)
     */
    public void setSeriesVisible(int series, Boolean visible, boolean notify);

    /**
     * Returns the base visibility for all series.
     *
     * @return The base visibility.
     *
     * @see #setBaseSeriesVisible(boolean)
     */
    public boolean getBaseSeriesVisible();

    /**
     * Sets the base visibility and sends a {@link RendererChangeEvent} to all
     * registered listeners.
     *
     * @param visible  the flag.
     *
     * @see #getBaseSeriesVisible()
     */
    public void setBaseSeriesVisible(boolean visible);

    /**
     * Sets the base visibility and, if requested, sends
     * a {@link RendererChangeEvent} to all registered listeners.
     *
     * @param visible  the visibility.
     * @param notify  notify listeners?
     *
     * @see #getBaseSeriesVisible()
     */
    public void setBaseSeriesVisible(boolean visible, boolean notify);

    // SERIES VISIBLE IN LEGEND (not yet respected by all renderers)

    /**
     * Returns <code>true</code> if the series should be shown in the legend,
     * and <code>false</code> otherwise.
     *
     * @param series  the series index.
     *
     * @return A boolean.
     */
    public boolean isSeriesVisibleInLegend(int series);

    /**
     * Returns the flag that controls whether a series is visible in the
     * legend.  This method returns only the "per series" settings - to
     * incorporate the override and base settings as well, you need to use the
     * {@link #isSeriesVisibleInLegend(int)} method.
     *
     * @param series  the series index (zero-based).
     *
     * @return The flag (possibly <code>null</code>).
     *
     * @see #setSeriesVisibleInLegend(int, Boolean)
     */
    public Boolean getSeriesVisibleInLegend(int series);

    /**
     * Sets the flag that controls whether a series is visible in the legend
     * and sends a {@link RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero-based).
     * @param visible  the flag (<code>null</code> permitted).
     *
     * @see #getSeriesVisibleInLegend(int)
     */
    public void setSeriesVisibleInLegend(int series, Boolean visible);

    /**
     * Sets the flag that controls whether a series is visible in the legend
     * and, if requested, sends a {@link RendererChangeEvent} to all registered
     * listeners.
     *
     * @param series  the series index.
     * @param visible  the flag (<code>null</code> permitted).
     * @param notify  notify listeners?
     *
     * @see #getSeriesVisibleInLegend(int)
     */
    public void setSeriesVisibleInLegend(int series, Boolean visible,
                                         boolean notify);

    /**
     * Returns the base visibility in the legend for all series.
     *
     * @return The base visibility.
     *
     * @see #setBaseSeriesVisibleInLegend(boolean)
     */
    public boolean getBaseSeriesVisibleInLegend();

    /**
     * Sets the base visibility in the legend and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param visible  the flag.
     *
     * @see #getBaseSeriesVisibleInLegend()
     */
    public void setBaseSeriesVisibleInLegend(boolean visible);

    /**
     * Sets the base visibility in the legend and, if requested, sends
     * a {@link RendererChangeEvent} to all registered listeners.
     *
     * @param visible  the visibility.
     * @param notify  notify listeners?
     *
     * @see #getBaseSeriesVisibleInLegend()
     */
    public void setBaseSeriesVisibleInLegend(boolean visible, boolean notify);


    //// PAINT ////////////////////////////////////////////////////////////////

    /**
     * Returns the paint used to fill data items as they are drawn.
     *
     * @param row  the row (or series) index (zero-based).
     * @param column  the column (or category) index (zero-based).
     *
     * @return The paint type (never <code>null</code>).
     */
    public PaintType getItemPaintType(int row, int column);

    /**
     * Returns the paint used to fill an item drawn by the renderer.
     *
     * @param series  the series index (zero-based).
     *
     * @return The paint type (possibly <code>null</code>).
     *
     * @see #setSeriesPaintType(int, PaintType)
     */
    public PaintType getSeriesPaintType(int series);

    /**
     * Sets the paint used for a series and sends a {@link RendererChangeEvent}
     * to all registered listeners.
     *
     * @param series  the series index (zero-based).
     * @param paintType  the paint (<code>null</code> permitted).
     *
     * @see #getSeriesPaintType(int)
     */
    public void setSeriesPaintType(int series, PaintType paintType);

    // FIXME: add setSeriesPaint(int, Paint, boolean)?

    /**
     * Returns the base paint.
     *
     * @return The base paint (never <code>null</code>).
     *
     * @see #setBasePaintType(PaintType)
     */
    public PaintType getBasePaintType();

    /**
     * Sets the base paint and sends a {@link RendererChangeEvent} to all
     * registered listeners.
     *
     * @param paintType  the paint (<code>null</code> not permitted).
     *
     * @see #getBasePaintType()
     */
    public void setBasePaintType(PaintType paintType);

    // FIXME: add setBasePaint(int, Paint, boolean)?

//    // FILL PAINT
//
//    /**
//     * Returns the paint used to fill data items as they are drawn.
//     *
//     * @param row  the row (or series) index (zero-based).
//     * @param column  the column (or category) index (zero-based).
//     *
//     * @return The paint (never <code>null</code>).
//     */
//    public Paint getItemFillPaint(int row, int column);
//
//    /**
//     * Returns the paint used to fill an item drawn by the renderer.
//     *
//     * @param series  the series index (zero-based).
//     *
//     * @return The paint (possibly <code>null</code>).
//     */
//    public Paint getSeriesFillPaint(int series);
//
//    /**
//     * Sets the paint used for a series and sends a
//     * {@link RendererChangeEvent} to all registered listeners.
//     *
//     * @param series  the series index (zero-based).
//     * @param paint  the paint (<code>null</code> permitted).
//     */
//    public void setSeriesFillPaint(int series, Paint paint);
//
//    // FIXME: add setSeriesFillPaint(int, Paint, boolean)?
//
//    /**
//     * Returns the base paint.
//     *
//     * @return The base paint (never <code>null</code>).
//     */
//    public Paint getBaseFillPaint();
//
//    /**
//     * Sets the base paint and sends a {@link RendererChangeEvent} to all
//     * registered listeners.
//     *
//     * @param paint  the paint (<code>null</code> not permitted).
//     */
//    public void setBaseFillPaint(Paint paint);
//
//    // FIXME: add setBaseFillPaint(int, Paint, boolean)?

    //// OUTLINE PAINT ////////////////////////////////////////////////////////

    /**
     * Returns the paint used to outline data items as they are drawn.
     *
     * @param row  the row (or series) index (zero-based).
     * @param column  the column (or category) index (zero-based).
     *
     * @return The paint type (never <code>null</code>).
     */
    public PaintType getItemOutlinePaintType(int row, int column);

    /**
     * Returns the paint used to outline an item drawn by the renderer.
     *
     * @param series  the series (zero-based index).
     *
     * @return The paint type (possibly <code>null</code>).
     *
     * @see #setSeriesOutlinePaintType(int, PaintType)
     */
    public PaintType getSeriesOutlinePaintType(int series);

    /**
     * Sets the paint used for a series outline and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero-based).
     * @param paintType  the paint (<code>null</code> permitted).
     *
     * @see #getSeriesOutlinePaintType(int)
     */
    public void setSeriesOutlinePaintType(int series, PaintType paintType);

    // FIXME: add setSeriesOutlinePaint(int, Paint, boolean)?

    /**
     * Returns the base outline paint.
     *
     * @return The paint type (never <code>null</code>).
     *
     * @see #setBaseOutlinePaintType(PaintType)
     */
    public PaintType getBaseOutlinePaintType();

    /**
     * Sets the base outline paint and sends a {@link RendererChangeEvent} to
     * all registered listeners.
     *
     * @param paintType  the paint (<code>null</code> not permitted).
     *
     * @see #getBaseOutlinePaintType()
     */
    public void setBaseOutlinePaintType(PaintType paintType);

    // FIXME: add setBaseOutlinePaint(Paint, boolean)?

    //// STROKE ///////////////////////////////////////////////////////////////

    /**
     * Returns the stroke used to draw data items.
     *
     * @param row  the row (or series) index (zero-based).
     * @param column  the column (or category) index (zero-based).
     *
     * @return The stroke (never <code>null</code>).
     */
    public Float getItemStroke(int row, int column);

    /**
     * Returns the stroke used to draw the items in a series.
     *
     * @param series  the series (zero-based index).
     *
     * @return The stroke (possibly <code>null</code>).
     *
     * @see #setSeriesStroke(int, Float)
     */
    public Float getSeriesStroke(int series);

    /**
     * Sets the stroke used for a series and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero-based).
     * @param stroke  the stroke (<code>null</code> permitted).
     *
     * @see #getSeriesStroke(int)
     */
    public void setSeriesStroke(int series, Float stroke);

    // FIXME: add setSeriesStroke(int, Stroke, boolean) ?

    /**
     * Returns the base stroke.
     *
     * @return The base stroke (never <code>null</code>).
     *
     * @see #setBaseStroke(Float stroke)
     */
    public Float getBaseStroke();

    /**
     * Sets the base stroke and sends a {@link RendererChangeEvent} to all
     * registered listeners.
     *
     * @param stroke  the stroke (<code>null</code> not permitted).
     *
     * @see #getBaseStroke()
     */
    public void setBaseStroke(Float stroke);

    // FIXME: add setBaseStroke(Stroke, boolean) ?

    //// OUTLINE STROKE ///////////////////////////////////////////////////////

    /**
     * Returns the stroke used to outline data items.  The default
     * implementation passes control to the lookupSeriesOutlineStroke method.
     * You can override this method if you require different behaviour.
     *
     * @param row  the row (or series) index (zero-based).
     * @param column  the column (or category) index (zero-based).
     *
     * @return The stroke (never <code>null</code>).
     */
    public Float getItemOutlineStroke(int row, int column);

    /**
     * Returns the stroke used to outline the items in a series.
     *
     * @param series  the series (zero-based index).
     *
     * @return The stroke (possibly <code>null</code>).
     *
     * @see #setSeriesOutlineStroke(int, Float)
     */
    public Float getSeriesOutlineStroke(int series);

    /**
     * Sets the outline stroke used for a series and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero-based).
     * @param stroke  the stroke (<code>null</code> permitted).
     *
     * @see #getSeriesOutlineStroke(int)
     */
    public void setSeriesOutlineStroke(int series, Float stroke);

    // FIXME: add setSeriesOutlineStroke(int, Stroke, boolean) ?

    /**
     * Returns the base outline stroke.
     *
     * @return The stroke (never <code>null</code>).
     *
     * @see #setBaseOutlineStroke(Float stroke)
     */
    public Float getBaseOutlineStroke();

    /**
     * Sets the base outline stroke and sends a {@link RendererChangeEvent} to
     * all registered listeners.
     *
     * @param stroke  the stroke (<code>null</code> not permitted).
     *
     * @see #getBaseOutlineStroke()
     */
    public void setBaseOutlineStroke(Float stroke);

    // FIXME: add setBaseOutlineStroke(Stroke, boolean) ?

    //// SHAPE ////////////////////////////////////////////////////////////////

    /**
     * Returns a shape used to represent a data item.
     *
     * @param row  the row (or series) index (zero-based).
     * @param column  the column (or category) index (zero-based).
     *
     * @return The shape (never <code>null</code>).
     */
    public Shape getItemShape(int row, int column);

    /**
     * Returns a shape used to represent the items in a series.
     *
     * @param series  the series (zero-based index).
     *
     * @return The shape (possibly <code>null</code>).
     *
     * @see #setSeriesShape(int, Shape)
     */
    public Shape getSeriesShape(int series);

    /**
     * Sets the shape used for a series and sends a {@link RendererChangeEvent}
     * to all registered listeners.
     *
     * @param series  the series index (zero-based).
     * @param shape  the shape (<code>null</code> permitted).
     *
     * @see #getSeriesShape(int)
     */
    public void setSeriesShape(int series, Shape shape);

    // FIXME: add setSeriesShape(int, Shape, boolean) ?

    /**
     * Returns the base shape.
     *
     * @return The shape (never <code>null</code>).
     *
     * @see #setBaseShape(Shape)
     */
    public Shape getBaseShape();

    /**
     * Sets the base shape and sends a {@link RendererChangeEvent} to all
     * registered listeners.
     *
     * @param shape  the shape (<code>null</code> not permitted).
     *
     * @see #getBaseShape()
     */
    public void setBaseShape(Shape shape);

    // FIXME: add setBaseShape(Shape, boolean) ?


    //// LEGEND ITEMS /////////////////////////////////////////////////////////

    /**
     * Returns a legend item for a series from a dataset.
     *
     * @param datasetIndex  the dataset index.
     * @param series  the series (zero-based index).
     *
     * @return The legend item (possibly <code>null</code>).
     */
    public LegendItem getLegendItem(int datasetIndex, int series);


    //// LEGEND ITEM LABEL GENERATOR //////////////////////////////////////////

    /**
     * Returns the legend item label generator.
     *
     * @return The legend item label generator (never <code>null</code>).
     *
     * @see #setLegendItemLabelGenerator(XYSeriesLabelGenerator)
     */
    public XYSeriesLabelGenerator getLegendItemLabelGenerator();

    /**
     * Sets the legend item label generator and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param generator  the generator (<code>null</code> not permitted).
     */
    public void setLegendItemLabelGenerator(XYSeriesLabelGenerator generator);


    //// TOOL TIP GENERATOR ///////////////////////////////////////////////////

  

    //// ITEM LABELS VISIBLE //////////////////////////////////////////////////

    /**
     * Returns <code>true</code> if an item label is visible, and
     * <code>false</code> otherwise.
     *
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     *
     * @return A boolean.
     */
    public boolean isItemLabelVisible(int row, int column);

    /**
     * Returns <code>true</code> if the item labels for a series are visible,
     * and <code>false</code> otherwise.
     *
     * @param series  the series index (zero-based).
     *
     * @return A boolean.
     */
    public boolean isSeriesItemLabelsVisible(int series);

    /**
     * Sets a flag that controls the visibility of the item labels for a
     * series and sends a {@link RendererChangeEvent} to all registered
     * listeners.
     *
     * @param series  the series index (zero-based).
     * @param visible  the flag.
     *
     * @see #isSeriesItemLabelsVisible(int)
     */
    public void setSeriesItemLabelsVisible(int series, boolean visible);

    /**
     * Sets a flag that controls the visibility of the item labels for a series.
     *
     * @param series  the series index (zero-based).
     * @param visible  the flag (<code>null</code> permitted).
     *
     * @see #isSeriesItemLabelsVisible(int)
     */
    public void setSeriesItemLabelsVisible(int series, Boolean visible);

    /**
     * Sets the visibility of item labels for a series and, if requested,
     * sends a {@link RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero-based).
     * @param visible  the visible flag.
     * @param notify  a flag that controls whether or not listeners are
     *                notified.
     *
     * @see #isSeriesItemLabelsVisible(int)
     */
    public void setSeriesItemLabelsVisible(int series, Boolean visible,
                                           boolean notify);

    /**
     * Returns the base setting for item label visibility.
     *
     * @return A flag (possibly <code>null</code>).
     *
     * @see #setBaseItemLabelsVisible(boolean)
     */
    public Boolean getBaseItemLabelsVisible();

    /**
     * Sets the base flag that controls whether or not item labels are visible.
     *
     * @param visible  the flag.
     *
     * @see #getBaseItemLabelsVisible()
     */
    public void setBaseItemLabelsVisible(boolean visible);

    /**
     * Sets the base setting for item label visibility.
     *
     * @param visible  the flag (<code>null</code> permitted).
     *
     * @see #getBaseItemLabelsVisible()
     */
    public void setBaseItemLabelsVisible(Boolean visible);

    /**
     * Sets the base visibility for item labels and, if requested, sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param visible  the visibility flag.
     * @param notify  a flag that controls whether or not listeners are
     *                notified.
     *
     * @see #getBaseItemLabelsVisible()
     */
    public void setBaseItemLabelsVisible(Boolean visible, boolean notify);


    //// ITEM LABEL GENERATOR /////////////////////////////////////////////////

    /**
     * Returns the item label generator for a data item.
     *
     * @param row  the row index (zero based).
     * @param column  the column index (zero based).
     *
     * @return The generator (possibly <code>null</code>).
     */
    public XYItemLabelGenerator getItemLabelGenerator(int row, int column);

    /**
     * Returns the item label generator for a series.
     *
     * @param series  the series index (zero based).
     *
     * @return The generator (possibly <code>null</code>).
     *
     * @see #setSeriesItemLabelGenerator(int, XYItemLabelGenerator)
     */
    public XYItemLabelGenerator getSeriesItemLabelGenerator(int series);

    /**
     * Sets the item label generator for a series and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero based).
     * @param generator  the generator (<code>null</code> permitted).
     *
     * @see #getSeriesItemLabelGenerator(int)
     */
    public void setSeriesItemLabelGenerator(int series,
                                            XYItemLabelGenerator generator);

    // FIXME:

    /**
     * Returns the base item label generator.
     *
     * @return The generator (possibly <code>null</code>).
     *
     * @see #setBaseItemLabelGenerator(XYItemLabelGenerator)
     */
    public XYItemLabelGenerator getBaseItemLabelGenerator();

    /**
     * Sets the base item label generator and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param generator  the generator (<code>null</code> permitted).
     *
     * @see #getBaseItemLabelGenerator()
     */
    public void setBaseItemLabelGenerator(XYItemLabelGenerator generator);

    //// ITEM LABEL FONT ///////////////////////////////////////////////////////

    /**
     * Returns the font for an item label.
     *
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     *
     * @return The font (never <code>null</code>).
     */
    public Font getItemLabelFont(int row, int column);

    /**
     * Returns the font for all the item labels in a series.
     *
     * @param series  the series index (zero-based).
     *
     * @return The font (possibly <code>null</code>).
     */
    public Font getSeriesItemLabelFont(int series);

    /**
     * Sets the item label font for a series and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero-based).
     * @param font  the font (<code>null</code> permitted).
     *
     * @see #getSeriesItemLabelFont(int)
     */
    public void setSeriesItemLabelFont(int series, Font font);

    /**
     * Returns the base item label font (this is used when no other font
     * setting is available).
     *
     * @return The font (<code>never</code> null).
     *
     * @see #setBaseItemLabelFont(Font)
     */
    public Font getBaseItemLabelFont();

    /**
     * Sets the base item label font and sends a {@link RendererChangeEvent}
     * to all registered listeners.
     *
     * @param font  the font (<code>null</code> not permitted).
     *
     * @see #getBaseItemLabelFont()
     */
    public void setBaseItemLabelFont(Font font);

    //// ITEM LABEL PAINT  /////////////////////////////////////////////////////

    /**
     * Returns the paint used to draw an item label.
     *
     * @param row  the row index (zero based).
     * @param column  the column index (zero based).
     *
     * @return The paint type (never <code>null</code>).
     */
    public PaintType getItemLabelPaintType(int row, int column);

    /**
     * Returns the paint used to draw the item labels for a series.
     *
     * @param series  the series index (zero based).
     *
     * @return The paint type (possibly <code>null<code>).
     *
     * @see #setSeriesItemLabelPaintType(int, PaintType)
     */
    public PaintType getSeriesItemLabelPaintType(int series);

    /**
     * Sets the item label paint for a series and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series (zero based index).
     * @param paintType  the paint (<code>null</code> permitted).
     *
     * @see #getSeriesItemLabelPaintType(int)
     */
    public void setSeriesItemLabelPaintType(int series, PaintType paintType);

    /**
     * Returns the base item label paint.
     *
     * @return The paint type (never <code>null<code>).
     */
    public PaintType getBaseItemLabelPaintType();

    /**
     * Sets the base item label paint and sends a {@link RendererChangeEvent}
     * to all registered listeners.
     *
     * @param paintType  the paint (<code>null</code> not permitted).
     */
    public void setBaseItemLabelPaintType(PaintType paintType);

    // POSITIVE ITEM LABEL POSITION...

    /**
     * Returns the item label position for positive values.
     *
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     *
     * @return The item label position (never <code>null</code>).
     */
    public ItemLabelPosition getPositiveItemLabelPosition(int row, int column);

    /**
     * Returns the item label position for all positive values in a series.
     *
     * @param series  the series index (zero-based).
     *
     * @return The item label position (never <code>null</code>).
     */
    public ItemLabelPosition getSeriesPositiveItemLabelPosition(int series);

    /**
     * Sets the item label position for all positive values in a series and
     * sends a {@link RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero-based).
     * @param position  the position (<code>null</code> permitted).
     */
    public void setSeriesPositiveItemLabelPosition(int series,
                                                   ItemLabelPosition position);

    /**
     * Sets the item label position for all positive values in a series and (if
     * requested) sends a {@link RendererChangeEvent} to all registered
     * listeners.
     *
     * @param series  the series index (zero-based).
     * @param position  the position (<code>null</code> permitted).
     * @param notify  notify registered listeners?
     */
    public void setSeriesPositiveItemLabelPosition(int series,
                                                   ItemLabelPosition position,
                                                   boolean notify);

    /**
     * Returns the base positive item label position.
     *
     * @return The position (never <code>null</code>).
     */
    public ItemLabelPosition getBasePositiveItemLabelPosition();

    /**
     * Sets the base positive item label position.
     *
     * @param position  the position (<code>null</code> not permitted).
     */
    public void setBasePositiveItemLabelPosition(ItemLabelPosition position);

    /**
     * Sets the base positive item label position and, if requested, sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param position  the position (<code>null</code> not permitted).
     * @param notify  notify registered listeners?
     */
    public void setBasePositiveItemLabelPosition(ItemLabelPosition position,
                                                 boolean notify);


    // NEGATIVE ITEM LABEL POSITION...

    /**
     * Returns the item label position for negative values.  This method can be
     * overridden to provide customisation of the item label position for
     * individual data items.
     *
     * @param row  the row index (zero-based).
     * @param column  the column (zero-based).
     *
     * @return The item label position (never <code>null</code>).
     */
    public ItemLabelPosition getNegativeItemLabelPosition(int row, int column);

    /**
     * Returns the item label position for all negative values in a series.
     *
     * @param series  the series index (zero-based).
     *
     * @return The item label position (never <code>null</code>).
     */
    public ItemLabelPosition getSeriesNegativeItemLabelPosition(int series);

    /**
     * Sets the item label position for negative values in a series and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero-based).
     * @param position  the position (<code>null</code> permitted).
     */
    public void setSeriesNegativeItemLabelPosition(int series,
                                                   ItemLabelPosition position);

    /**
     * Sets the item label position for negative values in a series and (if
     * requested) sends a {@link RendererChangeEvent} to all registered
     * listeners.
     *
     * @param series  the series index (zero-based).
     * @param position  the position (<code>null</code> permitted).
     * @param notify  notify registered listeners?
     */
    public void setSeriesNegativeItemLabelPosition(int series,
                                                   ItemLabelPosition position,
                                                   boolean notify);

    /**
     * Returns the base item label position for negative values.
     *
     * @return The position (never <code>null</code>).
     */
    public ItemLabelPosition getBaseNegativeItemLabelPosition();

    /**
     * Sets the base item label position for negative values and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param position  the position (<code>null</code> not permitted).
     */
    public void setBaseNegativeItemLabelPosition(ItemLabelPosition position);

    /**
     * Sets the base negative item label position and, if requested, sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param position  the position (<code>null</code> not permitted).
     * @param notify  notify registered listeners?
     */
    public void setBaseNegativeItemLabelPosition(ItemLabelPosition position,
                                                 boolean notify);


    // CREATE ENTITIES
    // FIXME:  these methods should be defined

//    public boolean getItemCreateEntity(int series, int item);
//
//    public Boolean getSeriesCreateEntities(int series);
//
//    public void setSeriesCreateEntities(int series, Boolean create);
//
//    public void setSeriesCreateEntities(int series, Boolean create,
//            boolean notify);
//
//    public boolean getBaseCreateEntities();
//
//    public void setBaseCreateEntities(boolean create);
//
//    public void setBaseCreateEntities(boolean create, boolean notify);

    //// ANNOTATIONS //////////////////////////////////////////////////////////

    /**
     * Adds an annotation and sends a {@link RendererChangeEvent} to all
     * registered listeners.  The annotation is added to the foreground
     * layer.
     *
     * @param annotation  the annotation (<code>null</code> not permitted).
     */
    public void addAnnotation(XYAnnotation annotation);

    /**
     * Adds an annotation to the specified layer.
     *
     * @param annotation  the annotation (<code>null</code> not permitted).
     * @param layer  the layer (<code>null</code> not permitted).
     */
    public void addAnnotation(XYAnnotation annotation, Layer layer);

    /**
     * Removes the specified annotation and sends a {@link RendererChangeEvent}
     * to all registered listeners.
     *
     * @param annotation  the annotation to remove (<code>null</code> not
     *                    permitted).
     *
     * @return A boolean to indicate whether or not the annotation was
     *         successfully removed.
     */
    public boolean removeAnnotation(XYAnnotation annotation);

    /**
     * Removes all annotations and sends a {@link RendererChangeEvent}
     * to all registered listeners.
     */
    public void removeAnnotations();

    /**
     * Draws all the annotations for the specified layer.
     *
     * @param canvas  the graphics device.
     * @param dataArea  the data area.
     * @param domainAxis  the domain axis.
     * @param rangeAxis  the range axis.
     * @param layer  the layer.
     * @param info  the plot rendering info.
     */
    public void drawAnnotations(Canvas canvas,
                                RectShape dataArea,
                                ValueAxis domainAxis,
                                ValueAxis rangeAxis,
                                Layer layer,
                                PlotRenderingInfo info);

    //// DRAWING //////////////////////////////////////////////////////////////

    /**
     * Initialises the renderer then returns the number of 'passes' through the
     * data that the renderer will require (usually just one).  This method
     * will be called before the first item is rendered, giving the renderer
     * an opportunity to initialise any state information it wants to maintain.
     * The renderer can do nothing if it chooses.
     *
     * @param canvas  the graphics device.
     * @param dataArea  the area inside the axes.
     * @param plot  the plot.
     * @param dataset  the dataset.
     * @param info  an optional info collection object to return data back to
     *              the caller.
     *
     * @return The number of passes the renderer requires.
     */
    public XYItemRendererState initialise(Canvas canvas,
                                          RectShape dataArea,
                                          XYPlot plot,
                                          XYDataset dataset,
                                          PlotRenderingInfo info);

    /**
     * Called for each item to be plotted.
     * <p>
     * The {@link XYPlot} can make multiple passes through the dataset,
     * depending on the value returned by the renderer's initialise() method.
     *
     * @param canvas  the graphics device.
     * @param state  the renderer state.
     * @param dataArea  the area within which the data is being rendered.
     * @param info  collects drawing info.
     * @param plot  the plot (can be used to obtain standard color
     *              information etc).
     * @param domainAxis  the domain axis.
     * @param rangeAxis  the range axis.
     * @param dataset  the dataset.
     * @param series  the series index (zero-based).
     * @param item  the item index (zero-based).
     * @param crosshairState  crosshair information for the plot
     *                        (<code>null</code> permitted).
     * @param pass  the pass index.
     */
    public void drawItem(Canvas canvas,
                         XYItemRendererState state,
                         RectShape dataArea,
                         PlotRenderingInfo info,
                         XYPlot plot,
                         ValueAxis domainAxis,
                         ValueAxis rangeAxis,
                         XYDataset dataset,
                         int series,
                         int item,
                         CrosshairState crosshairState,
                         int pass);

    /**
     * Fills a band between two values on the axis.  This can be used to color
     * bands between the grid lines.
     *
     * @param canvas  the graphics device.
     * @param plot  the plot.
     * @param axis  the domain axis.
     * @param dataArea  the data area.
     * @param start  the start value.
     * @param end  the end value.
     */
    public void fillDomainGridBand(Canvas canvas,
                                   XYPlot plot,
                                   ValueAxis axis,
                                   RectShape dataArea,
                                   double start, double end);

    /**
     * Fills a band between two values on the range axis.  This can be used to
     * color bands between the grid lines.
     *
     * @param canvas  the graphics device.
     * @param plot  the plot.
     * @param axis  the range axis.
     * @param dataArea  the data area.
     * @param start  the start value.
     * @param end  the end value.
     */
    public void fillRangeGridBand(Canvas canvas,
                                  XYPlot plot,
                                  ValueAxis axis,
                                  RectShape dataArea,
                                  double start, double end);

    /**
     * Draws a grid line against the domain axis.
     *
     * @param canvas  the graphics device.
     * @param plot  the plot.
     * @param axis  the value axis.
     * @param dataArea  the area for plotting data (not yet adjusted for any
     *                  3D effect).
     * @param value  the value.
     */
    public void drawDomainGridLine(Canvas canvas,
                                   XYPlot plot,
                                   ValueAxis axis,
                                   RectShape dataArea,
                                   double value);

    /**
     * Draws a line perpendicular to the range axis.
     *
     * @param canvas  the graphics device.
     * @param plot  the plot.
     * @param axis  the value axis.
     * @param dataArea  the area for plotting data.
     * @param value  the data value.
     * @param paint  the paint (<code>null</code> not permitted).
     * @param stroke  the stroke (<code>null</code> not permitted).
     * @param pathEffect  the pathEffect (<code>null</code> not permitted).
     */
    public void drawRangeLine(Canvas canvas, XYPlot plot, ValueAxis axis,
            RectShape dataArea, double value, PaintType paint, Float stroke, PathEffect pathEffect);

    /**
     * Draws the specified <code>marker</code> against the domain axis.
     *
     * @param canvas  the graphics device.
     * @param plot  the plot.
     * @param axis  the value axis.
     * @param marker  the marker.
     * @param dataArea  the axis data area.
     */
    public void drawDomainMarker(Canvas canvas, XYPlot plot, ValueAxis axis,
            Marker marker, RectShape dataArea);

    /**
     * Draws a horizontal line across the chart to represent a 'range marker'.
     *
     * @param canvas  the graphics device.
     * @param plot  the plot.
     * @param axis  the value axis.
     * @param marker  the marker line.
     * @param dataArea  the axis data area.
     */
    public void drawRangeMarker(Canvas canvas, XYPlot plot, ValueAxis axis,
            Marker marker, RectShape dataArea);
}
