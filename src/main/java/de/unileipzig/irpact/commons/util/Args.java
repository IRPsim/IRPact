package de.unileipzig.irpact.commons.util;

/**
 * @author Daniel Abitz
 */
public interface Args {

    static String print(String[] args) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < args.length; i++) {
            if(i > 0) {
                sb.append(" ");
            }
            sb.append(args[i]);
        }
        return sb.toString();
    }

    String[] toArray();
}
