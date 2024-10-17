package fr.skoupi.extensiveapi.minecraft.smartinventory.content;

public class SlotPos {

    private final int row;
    private final int column;

    public SlotPos(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Create a new SlotPos from a case number
     * What is a case number? It's the number of the bukkit slot in the inventory, starting from 0
     *
     * @param caseNumber The case number
     * @see SlotPos#of(int)
     */

    public SlotPos(int caseNumber) {
        this.row = caseNumber / 9;
        this.column = caseNumber % 9;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        SlotPos slotPos = (SlotPos) obj;

        return row == slotPos.row && column == slotPos.column;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;

        return result;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public static SlotPos of(int row, int column) {
        return new SlotPos(row, column);
    }

    /**
     * Create a new SlotPos from a case number
     * What is a case number? It's the number of the bukkit slot in the inventory, starting from 0
     *
     * @param caseNumber The case number
     * @return The SlotPos
     */
    public static SlotPos of(int caseNumber) {
        return new SlotPos(caseNumber);
    }

}
