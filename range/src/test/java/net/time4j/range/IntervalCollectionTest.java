package net.time4j.range;

import net.time4j.PlainDate;
import net.time4j.PlainTimestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static net.time4j.ClockUnit.NANOS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;


@RunWith(JUnit4.class)
public class IntervalCollectionTest {

    @Test
    public void getIntervals() {
        DateInterval i1 =
            DateInterval.between(
                PlainDate.of(2014, 2, 28),
                PlainDate.of(2014, 5, 31));
        DateInterval i2 =
            DateInterval.between(
                PlainDate.of(2014, 2, 27),
                PlainDate.of(2014, 6, 1));
        List<ChronoInterval<PlainDate>> intervals =
            new ArrayList<ChronoInterval<PlainDate>>();
        intervals.add(i1);
        intervals.add(i2);
        intervals = // sorted!
            IntervalCollection.onDateAxis().plus(intervals).getIntervals();
        assertThat(
            intervals.get(0).equals(i2),
            is(true));
        assertThat(
            intervals.get(1).equals(i1),
            is(true));
        assertThat(
            intervals.size(),
            is(2));
    }

    @Test(expected=NoSuchElementException.class)
    public void getMinimumEmpty() {
        IntervalCollection.onDateAxis().getMinimum();
    }

    @Test(expected=NoSuchElementException.class)
    public void getMaximumEmpty() {
        IntervalCollection.onDateAxis().getMaximum();
    }

    @Test
    public void getMinimum() {
        DateInterval i1 =
            DateInterval.between(
                PlainDate.of(2014, 2, 28),
                PlainDate.of(2014, 5, 31));
        DateInterval i2 =
            DateInterval.between(
                PlainDate.of(2014, 2, 27),
                PlainDate.of(2014, 6, 1));
        List<ChronoInterval<PlainDate>> intervals =
            new ArrayList<ChronoInterval<PlainDate>>();
        intervals.add(i1);
        intervals.add(i2);
        assertThat(
            IntervalCollection.onDateAxis().plus(intervals).getMinimum(),
            is(PlainDate.of(2014, 2, 27)));
    }

    @Test
    public void getMinimumPast() {
        DateInterval i1 =
            DateInterval.until(PlainDate.of(2014, 2, 28));
        DateInterval i2 =
            DateInterval.between(
                PlainDate.of(2014, 2, 27),
                PlainDate.of(2014, 6, 1));
        List<ChronoInterval<PlainDate>> intervals =
            new ArrayList<ChronoInterval<PlainDate>>();
        intervals.add(i1);
        intervals.add(i2);
        assertThat(
            IntervalCollection.onDateAxis().plus(intervals).getMinimum(),
            nullValue());
    }

    @Test
    public void getMaximum1() {
        DateInterval i1 =
            DateInterval.between(
                PlainDate.of(2014, 2, 28),
                PlainDate.of(2014, 5, 31));
        DateInterval i2 =
            DateInterval.between(
                PlainDate.of(2014, 2, 27),
                PlainDate.of(2014, 6, 1));
        List<ChronoInterval<PlainDate>> intervals =
            new ArrayList<ChronoInterval<PlainDate>>();
        intervals.add(i1);
        intervals.add(i2);
        assertThat(
            IntervalCollection.onDateAxis().plus(intervals).getMaximum(),
            is(PlainDate.of(2014, 6, 1)));
    }

    @Test
    public void getMaximum2() {
        DateInterval i1 =
            DateInterval.between(
                PlainDate.of(2014, 2, 28),
                PlainDate.of(2014, 5, 31));
        DateInterval i2 =
            DateInterval.between(
                PlainDate.of(2014, 2, 27),
                PlainDate.of(2014, 6, 4)).withOpenEnd();
        DateInterval i3 =
            DateInterval.between(
                PlainDate.of(2014, 2, 27),
                PlainDate.of(2014, 6, 1));
        List<ChronoInterval<PlainDate>> intervals =
            new ArrayList<ChronoInterval<PlainDate>>();
        intervals.add(i1);
        intervals.add(i2);
        intervals.add(i3);
        assertThat(
            IntervalCollection.onDateAxis().plus(intervals).getMaximum(),
            is(PlainDate.of(2014, 6, 3)));
    }

    @Test
    public void getMaximumTSP1() {
        TimestampInterval i1 =
            TimestampInterval.between(
                PlainDate.of(2014, 2, 28).atStartOfDay(),
                PlainDate.of(2014, 5, 31).atStartOfDay());
        TimestampInterval i2 =
            TimestampInterval.between(
                PlainDate.of(2014, 2, 27).atStartOfDay(),
                PlainDate.of(2014, 6, 4).atStartOfDay());
        TimestampInterval i3 =
            TimestampInterval.between(
                PlainDate.of(2014, 2, 27).atStartOfDay(),
                PlainDate.of(2014, 6, 1).atStartOfDay());
        List<ChronoInterval<PlainTimestamp>> intervals =
            new ArrayList<ChronoInterval<PlainTimestamp>>();
        intervals.add(i1);
        intervals.add(i2);
        intervals.add(i3);
        assertThat(
            IntervalCollection.onTimestampAxis().plus(intervals).getMaximum(),
            is(PlainDate.of(2014, 6, 4).atStartOfDay().minus(1, NANOS)));
    }

    @Test
    public void getMaximumTSP2() {
        TimestampInterval i1 =
            TimestampInterval.between(
                PlainDate.of(2014, 2, 28).atStartOfDay(),
                PlainDate.of(2014, 5, 31).atStartOfDay());
        TimestampInterval i2 =
            TimestampInterval.between(
                PlainDate.of(2014, 2, 27).atStartOfDay(),
                PlainDate.of(2014, 6, 4).atStartOfDay()).withClosedEnd();
        TimestampInterval i3 =
            TimestampInterval.between(
                PlainDate.of(2014, 2, 27).atStartOfDay(),
                PlainDate.of(2014, 6, 1).atStartOfDay());
        List<ChronoInterval<PlainTimestamp>> intervals =
            new ArrayList<ChronoInterval<PlainTimestamp>>();
        intervals.add(i1);
        intervals.add(i2);
        intervals.add(i3);
        assertThat(
            IntervalCollection.onTimestampAxis().plus(intervals).getMaximum(),
            is(PlainDate.of(2014, 6, 4).atStartOfDay()));
    }

    @Test
    public void getMaximumFuture() {
        DateInterval i1 =
            DateInterval.since(PlainDate.of(2014, 2, 28));
        DateInterval i2 =
            DateInterval.between(
                PlainDate.of(2014, 2, 27),
                PlainDate.of(2014, 6, 1));
        List<ChronoInterval<PlainDate>> intervals =
            new ArrayList<ChronoInterval<PlainDate>>();
        intervals.add(i1);
        intervals.add(i2);
        assertThat(
            IntervalCollection.onDateAxis().plus(intervals).getMaximum(),
            nullValue());
    }

    @Test
    public void gapsWithFuture() {
        DateInterval i1 =
            DateInterval.between(
                PlainDate.of(2014, 2, 28),
                PlainDate.of(2014, 3, 31));
        DateInterval i2 =
            DateInterval.since(PlainDate.of(2014, 4, 10));
        DateInterval i3 =
            DateInterval.between(
                PlainDate.of(2014, 4, 11),
                PlainDate.of(2014, 4, 15));
        DateInterval i4 =
            DateInterval.between(
                PlainDate.of(2014, 6, 15),
                PlainDate.of(2014, 6, 30));
        IntervalCollection<PlainDate> windows = IntervalCollection.onDateAxis();
        windows = windows.plus(i1).plus(i2).plus(i3).plus(i4);
        List<ChronoInterval<PlainDate>> gaps = windows.withGaps().getIntervals();
        ChronoInterval<PlainDate> expected =
            DateInterval.between(
                PlainDate.of(2014, 4, 1),
                PlainDate.of(2014, 4, 9));

        assertThat(gaps.size(), is(1));
        assertThat(gaps.get(0), is(expected));
    }

    @Test
    public void gapsNoOverlaps() {
        DateInterval i1 =
            DateInterval.between(
                PlainDate.of(2014, 4, 1),
                PlainDate.of(2014, 4, 5));
        DateInterval i2 =
            DateInterval.between(
                PlainDate.of(2014, 4, 10),
                PlainDate.of(2014, 6, 1));
        DateInterval i3 =
            DateInterval.between(
                PlainDate.of(2014, 6, 15),
                PlainDate.of(2014, 6, 30));
        IntervalCollection<PlainDate> windows = IntervalCollection.onDateAxis();
        windows = windows.plus(i1).plus(i2).plus(i3);
        List<ChronoInterval<PlainDate>> gaps = windows.withGaps().getIntervals();
        ChronoInterval<PlainDate> expected1 =
            DateInterval.between(
                PlainDate.of(2014, 4, 6),
                PlainDate.of(2014, 4, 9));
        ChronoInterval<PlainDate> expected2 =
            DateInterval.between(
                PlainDate.of(2014, 6, 2),
                PlainDate.of(2014, 6, 14));

        assertThat(gaps.size(), is(2));
        assertThat(gaps.get(0), is(expected1));
        assertThat(gaps.get(1), is(expected2));
    }

    @Test
    public void gapsWithOverlaps() {
        DateInterval i1 =
            DateInterval.between(
                PlainDate.of(2014, 2, 28),
                PlainDate.of(2014, 5, 31));
        DateInterval i2 =
            DateInterval.between(
                PlainDate.of(2014, 4, 1),
                PlainDate.of(2014, 4, 5));
        DateInterval i3 =
            DateInterval.between(
                PlainDate.of(2014, 4, 10),
                PlainDate.of(2014, 6, 1));
        DateInterval i4 =
            DateInterval.between(
                PlainDate.of(2014, 6, 15),
                PlainDate.of(2014, 6, 30));
        IntervalCollection<PlainDate> windows = IntervalCollection.onDateAxis();
        windows = windows.plus(i1).plus(i2).plus(i3).plus(i4);
        List<ChronoInterval<PlainDate>> gaps = windows.withGaps().getIntervals();
        ChronoInterval<PlainDate> expected =
            DateInterval.between(
                PlainDate.of(2014, 6, 2),
                PlainDate.of(2014, 6, 14));

        assertThat(gaps.size(), is(1));
        assertThat(gaps.get(0), is(expected));
    }

    @Test
    public void blocksWithOneGap() {
        DateInterval i1 =
            DateInterval.between(
                PlainDate.of(2014, 2, 28),
                PlainDate.of(2014, 5, 31));
        DateInterval i2 =
            DateInterval.between(
                PlainDate.of(2014, 4, 1),
                PlainDate.of(2014, 4, 5));
        DateInterval i3 =
            DateInterval.between(
                PlainDate.of(2014, 4, 10),
                PlainDate.of(2014, 6, 1));
        DateInterval i4 =
            DateInterval.between(
                PlainDate.of(2014, 6, 15),
                PlainDate.of(2014, 6, 30));
        IntervalCollection<PlainDate> windows = IntervalCollection.onDateAxis();
        windows = windows.plus(i1).plus(i2).plus(i3).plus(i4);
        List<ChronoInterval<PlainDate>> blocks =
            windows.withBlocks().getIntervals();
        ChronoInterval<PlainDate> first =
            DateInterval.between(
                PlainDate.of(2014, 2, 28),
                PlainDate.of(2014, 6, 1));
        ChronoInterval<PlainDate> second =
            DateInterval.between(
                PlainDate.of(2014, 6, 15),
                PlainDate.of(2014, 6, 30));

        assertThat(blocks.size(), is(2));
        assertThat(blocks.get(0), is(first));
        assertThat(blocks.get(1), is(second));
    }

    @Test
    public void blocksFuture() {
        DateInterval i1 =
            DateInterval.since(PlainDate.of(2014, 2, 28));
        DateInterval i2 =
            DateInterval.since(PlainDate.of(2014, 4, 1));
        DateInterval i3 =
            DateInterval.since(PlainDate.of(2014, 3, 10));
        IntervalCollection<PlainDate> windows = IntervalCollection.onDateAxis();
        windows = windows.plus(i1).plus(i2).plus(i3);

        List<ChronoInterval<PlainDate>> blocks =
            windows.withBlocks().getIntervals();
        ChronoInterval<PlainDate> expected = i1;

        assertThat(blocks.size(), is(1));
        assertThat(blocks.get(0), is(expected));
    }

    @Test
    public void blocksIfSingleInterval() {
        DateInterval interval =
            DateInterval.since(PlainDate.of(2014, 2, 28));
        IntervalCollection<PlainDate> windows = IntervalCollection.onDateAxis();
        windows = windows.plus(interval);

        List<ChronoInterval<PlainDate>> blocks =
            windows.withBlocks().getIntervals();
        ChronoInterval<PlainDate> expected = interval;

        assertThat(blocks.size(), is(1));
        assertThat(blocks.get(0), is(expected));
    }

    @Test
    public void blocksIfNoInterval() {
        IntervalCollection<PlainDate> windows = IntervalCollection.onDateAxis();
        assertThat(windows.withBlocks().isEmpty(), is(true));
    }

    @Test
    public void noIntersectionIfNoIntervals() {
        IntervalCollection<PlainDate> windows = IntervalCollection.onDateAxis();
        assertThat(windows.withIntersection().isEmpty(), is(true));
    }

    @Test
    public void noIntersectionIfGapExists() {
        DateInterval i1 =
            DateInterval.between(
                PlainDate.of(2014, 2, 28),
                PlainDate.of(2014, 5, 31));
        DateInterval i2 =
            DateInterval.between(
                PlainDate.of(2014, 4, 1),
                PlainDate.of(2014, 4, 5));
        DateInterval i3 =
            DateInterval.between(
                PlainDate.of(2014, 4, 10),
                PlainDate.of(2014, 6, 1));
        DateInterval i4 =
            DateInterval.between(
                PlainDate.of(2014, 6, 15),
                PlainDate.of(2014, 6, 30));
        IntervalCollection<PlainDate> windows = IntervalCollection.onDateAxis();
        windows = windows.plus(i1).plus(i2).plus(i3).plus(i4);

        assertThat(windows.withIntersection().isEmpty(), is(true));
    }

    @Test
    public void intersectionNormal() {
        DateInterval i1 =
            DateInterval.between(
                PlainDate.of(2014, 2, 28),
                PlainDate.of(2014, 5, 31));
        DateInterval i2 =
            DateInterval.between(
                PlainDate.of(2014, 4, 1),
                PlainDate.of(2014, 4, 15));
        DateInterval i3 =
            DateInterval.between(
                PlainDate.of(2014, 4, 10),
                PlainDate.of(2014, 6, 1));
        IntervalCollection<PlainDate> windows = IntervalCollection.onDateAxis();
        windows = windows.plus(i1).plus(i2).plus(i3);

        List<ChronoInterval<PlainDate>> overlap =
            windows.withIntersection().getIntervals();
        ChronoInterval<PlainDate> expected =
            DateInterval.between(
                PlainDate.of(2014, 4, 10),
                PlainDate.of(2014, 4, 15));

        assertThat(overlap.size(), is(1));
        assertThat(overlap.get(0), is(expected));
    }

    @Test
    public void intersectionFuture() {
        DateInterval i1 =
            DateInterval.since(PlainDate.of(2014, 2, 28));
        DateInterval i2 =
            DateInterval.since(PlainDate.of(2014, 4, 1));
        DateInterval i3 =
            DateInterval.since(PlainDate.of(2014, 3, 10));
        IntervalCollection<PlainDate> windows = IntervalCollection.onDateAxis();
        windows = windows.plus(i1).plus(i2).plus(i3);

        List<ChronoInterval<PlainDate>> overlap =
            windows.withIntersection().getIntervals();
        ChronoInterval<PlainDate> expected = i2;

        assertThat(overlap.size(), is(1));
        assertThat(overlap.get(0), is(expected));
    }

}