package org.jfree.data;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class DataUtilitiesTest {

    private Values2D value;
    private KeyedValues keyedValues;

    @Before
    public void setUp()  {
        value = mock(Values2D.class);
        when(value.getColumnCount()).thenReturn(3);
        when(value.getRowCount()).thenReturn(3);
        when(value.getValue(0, 2)).thenReturn(5);
        when(value.getValue(1, 2)).thenReturn(7);
        when(value.getValue(2, 2)).thenReturn(1);

        keyedValues = mock(KeyedValues.class);
        when(keyedValues.getKey(0)).thenReturn(0);
        when(keyedValues.getKey(1)).thenReturn(1);
        when(keyedValues.getKey(2)).thenReturn(2);
        when(keyedValues.getIndex(0)).thenReturn(0);
        when(keyedValues.getIndex(1)).thenReturn(1);
        when(keyedValues.getIndex(2)).thenReturn(2);
        when(keyedValues.getValue(0)).thenReturn(5);
        when(keyedValues.getValue(1)).thenReturn(9);
        when(keyedValues.getValue(2)).thenReturn(2);
        when(keyedValues.getKeys()).thenReturn(new ArrayList(){
            {
                add(0);
                add(1);
                add(2);
            }
        });
        when(keyedValues.getItemCount()).thenReturn(3);


    }

    @Test
    public void testCalculateColumnTotal() {
        assertEquals(13, DataUtilities.calculateColumnTotal(value, 2), .01d);
        verify(value, times(3)).getValue(anyInt(), anyInt());
    }

    @Test
    public void testCalculateRawTotal() {
        assertEquals(1, DataUtilities.calculateRowTotal(value, 2), .01d);
        verify(value, times(3)).getValue(anyInt(), anyInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNullNumberArray(){
        DataUtilities.createNumberArray(null);
    }

    @Test
    public void testCreateNumberArray() {
        Number[] array = DataUtilities.createNumberArray(new double[]{3,2,1});
        assertEquals(3, array.length);
        assertNotNull(array[0]);
        assertNotNull(array[1]);
        assertNotNull(array[2]);
        assertEquals(3.0, array[0].doubleValue(), 0.1d);
        assertEquals(2.0, array[1].doubleValue(), 0.1d);
        assertEquals(1.0, array[2].doubleValue(), 0.1d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNullNumberArray2d(){
        DataUtilities.createNumberArray2D(null);
    }

    @Test
    public void testCreateNumberArray2d() {
        Number[][] array = DataUtilities.createNumberArray2D(new double[][]{{1,2},{3,4}});
        assertEquals(2, array.length);
        assertNotNull(array[0]);
        assertNotNull(array[1]);
        assertNotNull(array[0][0]);
        assertNotNull(array[0][1]);
        assertNotNull(array[1][0]);
        assertNotNull(array[1][1]);
        assertEquals(1, array[0][0].doubleValue(), 0.1d);
        assertEquals(2, array[0][1].doubleValue(), 0.1d);
        assertEquals(3, array[1][0].doubleValue(), 0.1d);
        assertEquals(4, array[1][1].doubleValue(), 0.1d);
    }

    @Test
    public void testGetCumulativePercentage(){
        KeyedValues result = DataUtilities.getCumulativePercentages(keyedValues);
        assertEquals( 5d / 16d, result.getValue(0).doubleValue(), 0.1d);
        assertEquals( 14d / 16d, result.getValue(1).doubleValue(), 0.1d);
        assertEquals(  1d, result.getValue(2).doubleValue(), 0.1d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCumulativePercentageWithNullKeyedValues(){
        DataUtilities.getCumulativePercentages(null);
    }

}