package me.shradinx.ultracooldowns.cooldown;

/**
 * Status of player cooldown
 */
public enum CooldownStatus {
    /**
     * Player is off cooldown
     */
    OFF_COOLDOWN,
    /**
     * Player is on cooldown
     */
    ON_COOLDOWN,
    /**
     * A new cooldown has been created for the player
     */
    NEW_COOLDOWN,
    /**
     * Invalid or improper data was provided
     */
    INVALID
}
