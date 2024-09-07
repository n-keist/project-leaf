package net.projectleaf.plugin;


public abstract class LeafPlugin {

    protected String name, author;
    protected PluginDescription description;

    public void onEnable() {
        System.out.println("Enabling " + getName() + " by " + getAuthor());
    }

    public void onDisable() {
        System.out.println("Disabling " + getName() + " by " + getAuthor());
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public PluginDescription getPluginDescription() {
        return description;
    }
}
