package io.evlikat.limopdf.structure;

import io.evlikat.limopdf.DrawableSink;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class DrawableGroups {

    /**
     * Build DrawableGroup's making element chain if previous was "sticky" and breaking otherwise
     * iterating over elements.
     * <p>
     * "Sticky" element is StickyDrawable with keepWithNext = true.
     *
     * @param elements elements to iterate
     */
    public static List<DrawableGroup> buildDrawableGroup(List<Drawable> elements) {
        return elements.stream().reduce(
            new ArrayList<>(),
            (acc, drawable) -> {
                if (acc.isEmpty()) {
                    acc.add(buildGroup(drawable));
                } else {
                    DrawableGroup lastGroup = acc.get(acc.size() - 1);
                    if (lastGroup instanceof StickyGroup && ((StickyGroup) lastGroup).isOpen()) {
                        ((StickyGroup) lastGroup).add(drawable);
                    } else {
                        acc.add(buildGroup(drawable));
                    }
                }
                return acc;
            },
            (l1, l2) -> l1
        );
    }

    private static DrawableGroup buildGroup(Drawable drawable) {
        if (drawable instanceof StickyDrawable) {
            StickyDrawable stickyDrawable = (StickyDrawable) drawable;
            if (stickyDrawable.isKeepWithNext()) {
                return new StickyGroup(stickyDrawable);
            }
        }
        return new Single(drawable);
    }

    public interface DrawableGroup {

        void putTo(DrawableSink drawableSink);
    }

    @Data
    public static class StickyGroup implements DrawableGroup {

        private final StickyDrawable stickyDrawable;
        private final List<Drawable> otherElements = new ArrayList<>();

        public StickyGroup(StickyDrawable stickyDrawable) {
            this.stickyDrawable = stickyDrawable;
        }

        public StickyGroup(StickyDrawable stickyDrawable, List<Drawable> otherElements) {
            this(stickyDrawable);
            this.otherElements.addAll(otherElements);
        }

        public boolean isOpen() {
            if (otherElements.isEmpty()) {
                return true;
            }
            Drawable last = otherElements.get(otherElements.size() - 1);
            return last instanceof StickyDrawable && ((StickyDrawable) last).isKeepWithNext();
        }

        public void add(Drawable drawable) {
            otherElements.add(drawable);
        }

        @Override
        public void putTo(DrawableSink drawableSink) {
            drawableSink.add(stickyDrawable, otherElements);
        }
    }

    @Data
    public static class Single implements DrawableGroup {

        private final Drawable drawable;

        @Override
        public void putTo(DrawableSink drawableSink) {
            drawableSink.add(drawable);
        }
    }
}
