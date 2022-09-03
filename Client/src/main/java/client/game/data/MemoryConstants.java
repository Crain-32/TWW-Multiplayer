package client.game.data;

/**
 * For information that is one-off, or not suitable for Enums
 */
public class MemoryConstants {

    public static final Integer powerBraceletAddress = 0x803C4C18;
    public static final Integer maxMagicAddress = 0x803C4C1B;
    public static final Integer currMagicAddress = 0x803C4C1C;
    public static final Integer bottleArray = 0x803C4C52;
    public static final Integer powerBraceletFlagAddress = 0x803C4CBE;
    public static final Integer currStageAddress = 0x803C53A4;
    // Short that the game uses. Will increment the current
    // Stage's Key amount by whatever number is put in it.
    public static final Integer incrementSmallKey = 0x803CA77C;
    public static final Integer incrementHeartPieces = 0x803CA77F;

}
