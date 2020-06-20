package io.evlikat.limopdf.util;

import java.util.ArrayList;
import java.util.List;

public class CompositeBlockElement implements IBlockElement {

    private final List<IBlockElement> innerBlockElements = new ArrayList<>();

    public void addBlockElement(IBlockElement blockElement) {
        innerBlockElements.add(blockElement);
    }

    public void addBlockElement(IBlock block, float topPadding, float topMargin) {
        innerBlockElements.add(SimpleBlockElement.of(block.getHeight(), topPadding, topMargin));
    }

    public float getTopPadding(int lineIndex) {
        return innerBlockElements.get(lineIndex).getTopPadding();
    }

    public float getTopMargin(int lineIndex) {
        return innerBlockElements.get(lineIndex).getTopMargin();
    }

    @Override
    public float getTopPadding() {
        return innerBlockElements.isEmpty() ? 0 : innerBlockElements.get(0).getTopPadding();
    }

    @Override
    public float getTopMargin() {
        return innerBlockElements.isEmpty() ? 0 : innerBlockElements.get(0).getTopMargin();
    }

    @Override
    public float getHeight() {
        return innerBlockElements.isEmpty()
            ? 0
            : innerBlockElements.get(0).getHeight()
            + (float) innerBlockElements.stream().skip(1).mapToDouble(IBlockElement::getHeight).sum();
    }
}
