package net.tkg.ModernMayhem.server.util;

public interface ContainerUtil {

    default int getBackpackSize() {
        return getSlotPerLine() * getNumberOfLine();
    }

    public int getSlotPerLine();

    public int getNumberOfLine();
}
