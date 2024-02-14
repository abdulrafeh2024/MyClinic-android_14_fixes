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
 * --------------------
 * AbstractDataset.java
 * --------------------
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
 * (C) Copyright 2000-2008, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Nicolas Brodu (for Astrium and EADS Corporate Research
 *                   Center);
 *
 * Changes (from 21-Aug-2001)
 * --------------------------
 * 21-Aug-2001 : Added standard header. Fixed DOS encoding problem (DG);
 * 18-Sep-2001 : Updated e-mail address in header (DG);
 * 15-Oct-2001 : Moved to new package (com.jrefinery.data.*) (DG);
 * 22-Oct-2001 : Renamed DataSource.java --> Dataset.java etc. (DG);
 * 17-Nov-2001 : Changed constructor from public to protected, created new
 *               AbstractSeriesDataset class and transferred series-related
 *               methods, updated Javadoc comments (DG);
 * 04-Mar-2002 : Updated import statements (DG);
 * 11-Jun-2002 : Updated for change in the event constructor (DG);
 * 07-Aug-2002 : Changed listener list to use
 *               javax.swing.event.EventListenerList (DG);
 * 04-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 * 27-Mar-2003 : Implemented Serializable (DG);
 * 18-Aug-2003 : Implemented Cloneable (DG);
 * 08-Sep-2003 : Serialization fixes (NB);
 * 11-Sep-2003 : Cloning Fixes (NB);
 * 01-Jun-2005 : Added hasListener() method for unit testing (DG);
 *
 */

package org.afree.data.general;

import java.io.InvalidObjectException;
import java.io.ObjectInputValidation;
import java.io.Serializable;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * An abstract implementation of the {@link Dataset} interface, containing a
 * mechanism for registering change listeners.
 */
public abstract class AbstractDataset implements Dataset, Cloneable,
        Serializable, ObjectInputValidation {

    /** For serialization. */
    private static final long serialVersionUID = 1918768939869230744L;

    /** The group that the dataset belongs to. */
    private DatasetGroup group;

    /** Storage for registered change listeners. */
    private transient List<DatasetChangeListener> listenerList;
    
    /**
     * Constructs a dataset. By default, the dataset is assigned to its own
     * group.
     */
    protected AbstractDataset() {
        this.group = new DatasetGroup();
        this.listenerList = new CopyOnWriteArrayList<DatasetChangeListener>();
    }

    /**
     * Returns the dataset group for the dataset.
     * 
     * @return The group (never <code>null</code>).
     * 
     * @see #setGroup(DatasetGroup)
     */
    public DatasetGroup getGroup() {
        return this.group;
    }

    /**
     * Sets the dataset group for the dataset.
     * 
     * @param group
     *            the group (<code>null</code> not permitted).
     * 
     * @see #getGroup()
     */
    public void setGroup(DatasetGroup group) {
        if (group == null) {
            throw new IllegalArgumentException("Null 'group' argument.");
        }
        this.group = group;
    }
    
    /**
     * Validates the object. We use this opportunity to call listeners who have
     * registered during the deserialization process, as listeners are not
     * serialized. This method is called by the serialization system after the
     * entire graph is read.
     *
     * This object has registered itself to the system with a priority of 10.
     * Other callbacks may register with a higher priority number to be called
     * before this object, or with a lower priority number to be called after
     * the listeners were notified.
     *
     * All listeners are supposed to have register by now, either in their
     * readObject or validateObject methods. Notify them that this dataset has
     * changed.
     *
     * @exception InvalidObjectException If the object cannot validate itself.
     */
    public void validateObject() throws InvalidObjectException {
        fireDatasetChanged();
    }

    /**
     * Registers an object to receive notification of changes to the dataset.
     *
     * @param listener  the object to register.
     *
     * @see #removeChangeListener(DatasetChangeListener)
     */
    public void addChangeListener(DatasetChangeListener listener) {
        this.listenerList.add(listener);
    }
    
    /**
     * Deregisters an object so that it no longer receives notification of
     * changes to the dataset.
     *
     * @param listener  the object to deregister.
     *
     * @see #addChangeListener(DatasetChangeListener)
     */
    public void removeChangeListener(DatasetChangeListener listener) {
        this.listenerList.remove(listener);
    }

    /**
     * Returns <code>true</code> if the specified object is registered with
     * the dataset as a listener.  Most applications won't need to call this
     * method, it exists mainly for use by unit testing code.
     *
     * @param listener  the listener.
     *
     * @return A boolean.
     *
     * @see #addChangeListener(DatasetChangeListener)
     * @see #removeChangeListener(DatasetChangeListener)
     */
    public boolean hasListener(EventListener listener) {
        return listenerList.contains(listener);
    }
    
    /**
     * Notifies all registered listeners that the dataset has changed.
     *
     * @param event  contains information about the event that triggered the
     *               notification.
     *
     * @see #addChangeListener(DatasetChangeListener)
     * @see #removeChangeListener(DatasetChangeListener)
     */
    protected void notifyListeners(DatasetChangeEvent event) {
        if(listenerList.size() == 0) {
            return;
        }
        for (int i = listenerList.size() - 1; i >= 0; i--) {
            listenerList.get(i).datasetChanged(event);
        }
    }

    /**
     * Notifies all registered listeners that the dataset has changed.
     *
     * @see #addChangeListener(DatasetChangeListener)
     */
    protected void fireDatasetChanged() {
        notifyListeners(new DatasetChangeEvent(this, this));
    }

    /**
     * Returns a clone of the dataset. The cloned dataset will NOT include the
     * {@link DatasetChangeListener} references that have been registered with
     * this dataset.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException  if the dataset does not support
     *                                     cloning.
     */
    public Object clone() throws CloneNotSupportedException {
        AbstractDataset clone = (AbstractDataset) super.clone();
        clone.listenerList = new CopyOnWriteArrayList<DatasetChangeListener>();
        return clone;
    }
    
}
