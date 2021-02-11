package de.unileipzig.irpact.commons.persistence;

/**
 * @author Daniel Abitz
 */
public abstract class PersistableBase implements Persistable {

    protected boolean hasUID = false;
    protected long uid = -1;
    protected String uidStr = "-1";

    protected void validateUID() {
        if(!hasUID) {
            throw new IllegalStateException("missing UID");
        }
    }

    @Override
    public boolean hasUID() {
        return hasUID;
    }

    @Override
    public synchronized boolean setUID(UIDManager manager) {
        if(hasUID) {
            return false;
        } else {
            setUID(manager.getUID());
            return true;
        }
    }

    public void setUID(long uid) {
        if(hasUID) {
            throw new IllegalStateException("uid already set");
        } else {
            this.hasUID = true;
            this.uid = uid;
            this.uidStr = Long.toString(uid);
        }
    }

    @Override
    public long getUID() {
        validateUID();
        return uid;
    }

    @Override
    public String getUIDString() {
        validateUID();
        return uidStr;
    }
}
