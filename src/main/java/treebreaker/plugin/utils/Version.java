/*
 * Copyright (C) 2021 Daniel Allen
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the
 * GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package main.java.treebreaker.plugin.utils;

/**
 *
 * @author Daniel Allen
 */
public class Version {

    private final int[] ids;

    public Version(String version) {
        this(version, ".");
    }

    public Version(String version, String delimiter) {
        String[] split = version.split("\\.");
        //System.out.println("Length: " + split.length);
        ids = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            String cur = split[i].replaceAll("[^0-9]", "");
            int val;
            if (cur.length() == 0) {
                val = 0;
            } else {
                val = Integer.parseInt(cur);
            }
            ids[i] = val;
        }
    }

    public int[] getVersionNumber() {
        return ids;
    }

    public String getVersionString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ids.length - 1; i++) {
            sb.append(ids[i]).append('.');
        }
        sb.append(ids[ids.length - 1]);
        return sb.toString();
    }

    /**
     * <code>a.compareTo(b)</code>
     *
     * @param other
     * @return -1 if <code>a</code> is less than <code>b</code><br>
     * 0 if <code>a</code> is equal to <code>b</code> <br>
     * 1 if <code>a</code> is greater than <code>b</code>
     */
    public int compareTo(Version other) {
        for (int i = 0; i < Math.max(ids.length, other.ids.length); i++) {
            if (i < Math.min(ids.length, other.ids.length)) {
                if (ids[i] < other.ids[i]) {
                    return -1;
                }
                if (ids[i] > other.ids[i]) {
                    return 1;
                }
            } else if (ids.length != other.ids.length) {
                if (i >= ids.length) {
                    if (other.ids[i] > 0) {
                        return -1;
                    }
                    if (other.ids[i] < 0) {
                        return 1;
                    }
                } else {
                    if (ids[i] > 0) {
                        return 1;
                    }
                    if (ids[i] < 0) {
                        return -1;
                    }
                }
            }
        }
        return 0;
    }
}
