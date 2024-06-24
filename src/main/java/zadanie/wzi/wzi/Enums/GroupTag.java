package zadanie.wzi.wzi.Enums;

public enum GroupTag {
    TAG_ff(0x0ff),
    TAG_0028(0x0028),
    TAG_0018(0x0018),
    TAG_0020(0x0020),
    TAG_7FE0(0x7fe0),
    TAG_0002(0x0002);

    private final int value;

    GroupTag(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
