package org.jfree.data;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class RangeTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNewRangeWithLowerBiggerThanUpper(){
        new Range(5, 3);
    }

    @Test
    public void testCombineNullRanges(){
        Range range = new Range(1, 3);
        Range combined = Range.combine(range, null);
        Assert.assertEquals(1, range.getLowerBound(), 0.1d);
        Assert.assertEquals(3, range.getUpperBound(), 0.1d);

        combined = Range.combine(null, range);
        Assert.assertEquals(1, range.getLowerBound(), 0.1d);
        Assert.assertEquals(3, range.getUpperBound(), 0.1d);
    }


    @Test
    public void testCombinePositiveRanges(){
        Range range1 = new Range(3.5d, 5d);
        Range range2 = new Range(4d, 4.5d);

        Range range = Range.combine(range1, range2);

        Assert.assertEquals(3.5, range.getLowerBound(), 0.1d);
        Assert.assertEquals(5, range.getUpperBound(), 0.1d);
    }

    @Test
    public void testCombineNegativeRanges(){
        Range range1 = new Range(-4, -2.3);
        Range range2 = new Range(-6, -5);

        Range range = Range.combine(range1, range2);

        Assert.assertEquals(-6, range.getLowerBound(), 0.1d);
        Assert.assertEquals(-2.3, range.getUpperBound(), 0.1d);
    }

    @Test
    public void testConstrainPositiveValue(){
        Range range = new Range(5, 6);
        Assert.assertEquals(5, range.constrain(3), 0.1d);
        Assert.assertEquals(6, range.constrain(7), 0.1d);
        Assert.assertEquals(5.5, range.constrain(5.5), 0.1d);

    }

    @Test
    public void testConstrainNegativeValue(){
        Range range = new Range(-3, -1);
        Assert.assertEquals(-1, range.constrain(0), 0.1d);
        Assert.assertEquals(-3, range.constrain(-6), 0.1d);
        Assert.assertEquals(-2.5, range.constrain(-2.5), 0.1d);

    }

    @Test
    public void testContainsPostiveValues(){
        Range range = new Range(3, 7);
        assertTrue(range.contains(3.1));
        Assert.assertFalse(range.contains(8));

    }

    @Test
    public void testContainsNegativeValues(){
        Range range = new Range(-7, -3);
        assertTrue(range.contains(-3.1));
        Assert.assertFalse(range.contains(-8));
    }

    @Test
    public void testExpandPositiveRange(){
        Range range = new Range(2, 6);
        Range expandedRange = Range.expand(range, 0.25, 0.5);
        assertEquals(1, expandedRange.getLowerBound(), 0.1d );
        assertEquals(8, expandedRange.getUpperBound(), 0.1d );
    }

    //TODO to cover illegal argument
    @Test(expected = IllegalArgumentException.class)
    public void testExpandNullRange(){
        Range.expand(null, 1, 2);
    }

    //TODO
    @Test
    public void testExpandToIncludeNullRange(){
        Range range = Range.expandToInclude(null, 5);
        assertEquals(5, range.getLowerBound(), 0.1d);
        assertEquals(5, range.getUpperBound(), 0.1d);
    }

    @Test
    public void testExpandToIncludeContainedValued(){
        Range range = new Range(1, 3);
        Range expanded = Range.expandToInclude(range, 2);
        assertEquals(1, range.getLowerBound(), 0.1d);
        assertEquals(3, range.getUpperBound(), 0.1d);
    }

    @Test
    public void testExpandToIncludePositiveRange(){
        Range range = new Range(0, 3);
        Range expanded = Range.expandToInclude(range, -3);
        assertEquals(-3, expanded.getLowerBound(), 0.1d);
        assertEquals(-3, expanded.getUpperBound(), 0.1d);
    }

    @Test
    public void testExpandToIncludeNegativeRange(){
        Range range = new Range(-2, -1);
        Range expanded = Range.expandToInclude(range, 0);
        assertEquals(-2, expanded.getLowerBound() , 0.1d);
        assertEquals(0, expanded.getUpperBound(), 0.1d);
    }

    @Test
    public void testCentralValuePositiveRange(){
        Range range = new Range(3.0, 4.0);
        assertEquals(3.5d, range.getCentralValue(), 0.1d);
    }

    @Test
    public void testCentralValueNegativeRange(){
        Range range = new Range(-3.0, -2.0);
        assertEquals(2.5d, range.getCentralValue(), 0.1d);
    }

    @Test
    public void testLengthPositiveRange(){
        Range range = new Range(2.0,3);
        assertEquals(1,range.getLength(), 0.1d);
    }

    @Test
    public void testLengthNegativeRange(){
        Range range = new Range(-3.0,-1);
        assertEquals(2,range.getLength(), 0.1d);
    }

    @Test
    public void testLowerBoundPositiveRange(){
        Range range = new Range(0,1);
        assertEquals(0,range.getLowerBound(), 0.1d);
    }

    @Test
    public void testLowerBoundNegativeRange(){
        Range range = new Range(-1,1);
        assertEquals(-1,range.getLowerBound(), 0.1d);
    }

    @Test
    public void testUpperBoundPositiveRange(){
        Range range = new Range(0,1);
        assertEquals(1,range.getUpperBound(), 0.1d);
    }

    @Test
    public void testUpperBoundNegativeRange(){
        Range range = new Range(-1,1);
        assertEquals(1,range.getUpperBound(), 0.1d);
    }

    @Test
    public void testIntersectsPositiveRange(){
        Range range1 = new Range(3, 6);
        assertTrue(range1.intersects(-5, 4));
        assertTrue(range1.intersects(4,7));
        assertFalse(range1.intersects(8,10));
    }

    @Test
    public void testIntersectsNegativeRange(){
        Range range1 = new Range(-6, -3);
        assertTrue(range1.intersects(-5, -2));
        assertTrue(range1.intersects(-5, 4));
        assertFalse(range1.intersects(-8, -7));
    }

    @Test
    public void testShiftPositiveRange(){
        Range range = new Range(0.0, 1.0);
        Range shifted = Range.shift(range, -1);
        assertEquals(-1, shifted.getLowerBound(), 0.1d);
        assertEquals(0, shifted.getUpperBound(), 0.1d);
    }

    @Test
    public void testShiftNegativeRange(){
        Range range = new Range(-1, 0);
        Range shifted = Range.shift(range, 1);
        assertEquals(0, shifted.getLowerBound(), 0.1d);
        assertEquals(1, shifted.getUpperBound() ,0.1d);
    }

    @Test
    public void testShiftPositiveRangeWithNoCrossing(){
        Range range = new Range(3, 5);
        Range shifted = Range.shift(range, -4);
        assertEquals(0,shifted.getLowerBound(), 0.1d);
        assertEquals(1,shifted.getUpperBound(), 0.1d);
    }

    @Test
    public void testShiftPositiveRangeWithCrossing(){
        Range range = new Range(3, 5);
        Range shifted = Range.shift(range, -4, true);
        assertEquals(-1, shifted.getLowerBound(), 0.1d);
        assertEquals(-1, shifted.getUpperBound() , 0.1d);
    }

    @Test
    public void testShiftNegativeRangeWithCrossing(){
        Range range = new Range(-3, -2);
        Range shifted = Range.shift(range, 4, true);
        assertEquals(1, shifted.getLowerBound() , 0.1d);
        assertEquals(2, shifted.getUpperBound() , 0.1d);
    }

}