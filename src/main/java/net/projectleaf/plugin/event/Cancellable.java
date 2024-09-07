package net.projectleaf.plugin.event;

public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean cancelled);

}
