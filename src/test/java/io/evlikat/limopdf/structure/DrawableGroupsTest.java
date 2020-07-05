package io.evlikat.limopdf.structure;

import io.evlikat.limopdf.Drawer;
import io.evlikat.limopdf.StickyDrawer;
import io.evlikat.limopdf.page.PageSpecification;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class DrawableGroupsTest {

    @Test
    public void shouldBuildAllStickyGroup() {
        Sticky sticky1 = new Sticky();
        Sticky sticky2 = new Sticky();
        Sticky sticky3 = new Sticky();
        List<DrawableGroups.DrawableGroup> result = DrawableGroups.buildDrawableGroup(List.of(
            sticky1,
            sticky2,
            sticky3
        ));

        assertThat(result).containsExactly(
            new DrawableGroups.StickyGroup(sticky1, List.of(sticky2, sticky3))
        );
    }

    @Test
    public void shouldBuildMixedStickyGroups() {
        Sticky sticky1 = new Sticky();
        Single single = new Single();
        Sticky sticky2 = new Sticky();
        List<DrawableGroups.DrawableGroup> result = DrawableGroups.buildDrawableGroup(List.of(
            sticky1,
            single,
            sticky2
        ));

        assertThat(result).containsExactly(
            new DrawableGroups.StickyGroup(sticky1, List.of(single)),
            new DrawableGroups.StickyGroup(sticky2)
        );
    }

    @Test
    public void shouldBuildGroupsForSingle() {
        Single single1 = new Single();
        Single single2 = new Single();
        Single single3 = new Single();
        List<DrawableGroups.DrawableGroup> result = DrawableGroups.buildDrawableGroup(List.of(
            single1,
            single2,
            single3
        ));

        assertThat(result).containsExactly(
            new DrawableGroups.Single(single1),
            new DrawableGroups.Single(single2),
            new DrawableGroups.Single(single3)
        );
    }

    static class Sticky implements StickyDrawable {

        @Override
        public StickyDrawer drawer() {
            return null;
        }

        @Override
        public Optional<PageSpecification> pageBreak() {
            return Optional.empty();
        }

        @Override
        public boolean isKeepWithNext() {
            return true;
        }
    }

    static class Single implements Drawable {

        @Override
        public Drawer drawer() {
            return null;
        }

        @Override
        public Optional<PageSpecification> pageBreak() {
            return Optional.empty();
        }
    }
}