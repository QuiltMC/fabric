package net.fabricmc.fabric.impl.item.group;

public interface ItemGroupExtensions extends org.quiltmc.qsl.item.group.impl.ItemGroupExtensions {
    default void fabric_expandArray() {
        quilt$expandArray();
    }
}