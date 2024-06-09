package fr.skoupi.extensiveapi.minecraft.smartinventory.content;

public class SlotPos {

    private final int row;
    private final int column;

    public SlotPos(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public SlotPos(int caseNumber) {
        this.row = caseNumber / 9;
        this.column = caseNumber % 9;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null || getClass() != obj.getClass())
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

    public int getRow() { return row; }
    public int getColumn() { return column; }

    public static SlotPos of(int row, int column) {
        return new SlotPos(row, column);
    }

}
