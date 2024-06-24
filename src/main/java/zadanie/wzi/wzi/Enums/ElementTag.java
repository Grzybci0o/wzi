package zadanie.wzi.wzi.Enums;

public enum ElementTag {
    TAG_0010(0x0010),
    TAG_0011(0x0011),
    TAG_0101(0x0101),
    TAG_0100(0x0100),
    TAG_1053(0x1053),
    TAG_1052(0x1052),
    TAG_0030(0x0030),
    TAG_0050(0x0050),
    TAG_0034(0x0034),
    TAG_0032(0x0032),
    TAG_1050(0x1050),
    TAG_1051(0x1051),
    TAG_0069(0x0069),
    TAG_1041(0x1041),
    TAG_7FE0(0x7fe0),
    TAG_0088(0x0088),
    TAG_0009(0x0009);

    private final int value;

    ElementTag(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

