package com.wcl.raidnight.models;

import org.immutables.value.Value;

/**
 * @author dtou
 */

@Value.Immutable
public abstract class GuildReport {
    public abstract String id();
    public abstract String title();
    public abstract String owner();
    public abstract long start();
    public abstract long end();
    public abstract int zone();
}