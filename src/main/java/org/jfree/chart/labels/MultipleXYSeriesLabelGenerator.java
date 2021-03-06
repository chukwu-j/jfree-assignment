/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation; either version 2.1 of the License, or 
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this library; if not, write to the Free Software Foundation, 
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * -----------------------------------
 * MultipleXYSeriesLabelGenerator.java
 * -----------------------------------
 * (C) Copyright 2004, 2005, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: MultipleXYSeriesLabelGenerator.java,v 1.6 2005/06/28 08:57:06 mungady Exp $
 *
 * Changes
 * -------
 * 19-Nov-2004 : Version 1 (DG);
 * 18-Apr-2005 : Use StringBuffer (DG);
 *
 */

package org.jfree.chart.labels;

import java.awt.Font;
import java.awt.Paint;
import java.awt.font.TextAttribute;
import java.io.Serializable;
import java.text.AttributedString;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.data.xy.XYDataset;
import org.jfree.util.PublicCloneable;

/**
 * A series label generator for plots that use data from 
 * an {@link XYDataset}.
 */
public class MultipleXYSeriesLabelGenerator implements XYSeriesLabelGenerator, 
                                                       Cloneable, 
                                                       PublicCloneable,
                                                       Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 138976236941898560L;
    
    /** The default item label format. */
    public static final String DEFAULT_LABEL_FORMAT = "{0}";
    
    /** The format pattern for the initial part of the label. */
    private String formatPattern;
    
    /** The format pattern for additional labels. */
    private String additionalFormatPattern;
    
    /** Storage for the additional series labels. */
    private Map seriesLabelLists;

    /**
     * Creates an item label generator using default number formatters.
     */
    public MultipleXYSeriesLabelGenerator() {
        this(DEFAULT_LABEL_FORMAT);
    }
    
    /**
     * Creates a new series label generator.
     * 
     * @param format  the format pattern (<code>null</code> not permitted).
     */
    public MultipleXYSeriesLabelGenerator(String format) {
        if (format == null) {
            throw new IllegalArgumentException("Null 'format' argument.");
        }
        this.formatPattern = format;
        this.additionalFormatPattern = "\n{0}";
        this.seriesLabelLists = new HashMap();
    }
    
    /**
     * Adds an extra label for the specified series.
     * 
     * @param series  the series index.
     * @param label  the label.
     */
    public void addSeriesLabel(int series, String label) {
        Integer key = new Integer(series);
        List labelList = (List) this.seriesLabelLists.get(key);
        if (labelList == null) {
            labelList = new java.util.ArrayList();
            this.seriesLabelLists.put(key, labelList);
        }
        labelList.add(label);
    }
    
    /**
     * Clears the extra labels for the specified series.
     * 
     * @param series  the series index.
     */
    public void clearSeriesLabels(int series) {
        Integer key = new Integer(series);
        this.seriesLabelLists.put(key, null);       
    }

    /**
     * Generates a label for the specified series.  This label will be
     * used for the chart legend.
     * 
     * @param dataset  the dataset (<code>null</code> not permitted).
     * @param series  the series.
     * 
     * @return A series label.
     */
    public String generateLabel(XYDataset dataset, int series) {
        if (dataset == null) {
            throw new IllegalArgumentException("Null 'dataset' argument.");
        }
        StringBuffer label = new StringBuffer();
        label.append(
            MessageFormat.format(
                this.formatPattern, createItemArray(dataset, series)
            )
        );
        Integer key = new Integer(series);
        List extraLabels = (List) this.seriesLabelLists.get(key);
        if (extraLabels != null) {
            Object[] temp = new Object[1];
            for (int i = 0; i < extraLabels.size(); i++) {
                temp[0] = extraLabels.get(i);
                String labelAddition = MessageFormat.format(
                    this.additionalFormatPattern, temp
                );
                label.append(labelAddition);
            }
        }
        return label.toString();
    }

    /**
     * Generates an attributed label for the specified series, or 
     * <code>null</code> if no attributed label is available (in which case,
     * the string returned by {@link #generateLabel(XYDataset, int)} will 
     * provide the fallback).  Only certain attributes are recognised by the 
     * code that ultimately displays the labels: 
     * <ul>
     * <li>{@link TextAttribute#FONT}: will set the font;</li>
     * <li>{@link TextAttribute#POSTURE}: a value of 
     *     {@link TextAttribute#POSTURE_OBLIQUE} will add {@link Font#ITALIC} to
     *     the current font;</li>
     * <li>{@link TextAttribute#WEIGHT}: a value of 
     *     {@link TextAttribute#WEIGHT_BOLD} will add {@link Font#BOLD} to the 
     *     current font;</li>
     * <li>{@link TextAttribute#FOREGROUND}: this will set the {@link Paint} 
     *     for the current</li>
     * <li>{@link TextAttribute#SUPERSCRIPT}: the values 
     *     {@link TextAttribute#SUPERSCRIPT_SUB} and 
     *     {@link TextAttribute#SUPERSCRIPT_SUPER} are recognised.</li> 
     * </ul>
     * 
     * @param dataset  the dataset.
     * @param series  the series index.
     * 
     * @return An attributed label (possibly <code>null</code>).
     */
    public AttributedString generateAttributedLabel(XYDataset dataset, 
                                                    int series) {
        return null;   
    }

    /**
     * Creates the array of items that can be passed to the 
     * {@link MessageFormat} class for creating labels.
     *
     * @param dataset  the dataset (<code>null</code> not permitted).
     * @param series  the series (zero-based index).
     *
     * @return The items (never <code>null</code>).
     */
    protected Object[] createItemArray(XYDataset dataset, int series) {
        Object[] result = new Object[1];
        result[0] = dataset.getSeriesKey(series).toString();
        return result;
    }

    /**
     * Returns an independent copy of the generator.
     * 
     * @return A clone.
     * 
     * @throws CloneNotSupportedException if cloning is not supported.
     */
    public Object clone() throws CloneNotSupportedException { 
        return super.clone();
    }
    
    /**
     * Tests this object for equality with an arbitrary object.
     *
     * @param obj  the other object (<code>null</code> permitted).
     *
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StandardXYSeriesLabelGenerator)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        return true;
    }

}