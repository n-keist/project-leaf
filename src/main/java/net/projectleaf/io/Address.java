package net.projectleaf.io;

public class Address {

    private int port;
    private String hostname;

    public Address(int port, String hostname) {
        this.port = port;
        this.hostname = hostname;
    }

    /**
     *
     * @return {@link Integer} the port provided
     */
    public int getPort() {
        return port;
    }

    /**
     *
     * @return {@link String} the hostname provided
     */
    public String getHostname() {
        return hostname;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return hostname.replace(".", "-") + "_" + port;
    }

}
